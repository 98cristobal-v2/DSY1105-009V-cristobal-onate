package cl.duoc.cimucmovil.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.cimucmovil.data.CimDatabase
import cl.duoc.cimucmovil.data.CimRepository
import cl.duoc.cimucmovil.data.UserRepository
import cl.duoc.cimucmovil.model.CimContent
import cl.duoc.cimucmovil.model.ContentType
import cl.duoc.cimucmovil.model.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CimUiState(
    val currentUser: User? = null,
    val isRegisterMode: Boolean = false,
    val loginError: String? = null,
    val registerError: String? = null,
    val isManagingUsers: Boolean = false,
    val allUsers: List<User> = emptyList(),
    val searchText: String = "",
    val selectedType: ContentType? = null,
    val selectedContentId: String? = null,
    val contents: List<CimContent> = emptyList(),
    val isEditingOrCreating: Boolean = false,
    val editingContent: CimContent? = null,
    val titleError: String? = null,
    val descriptionError: String? = null,
    val operationMessage: String? = null
)

class CimViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CimRepository
    private val userRepository: UserRepository

    private val _uiState = MutableStateFlow(CimUiState())
    val uiState: StateFlow<CimUiState> = _uiState.asStateFlow()

    init {
        val db = CimDatabase.getDatabase(application)
        val cimDao = db.cimDao()
        val userDao = db.userDao()
        repository = CimRepository(cimDao)
        userRepository = UserRepository(userDao)

        viewModelScope.launch {
            if (cimDao.getCount() == 0) {
                CimDatabase.populateInitialContents(cimDao)
            }
            CimDatabase.ensureAdminUser(userDao)
        }

        viewModelScope.launch {
            repository.allContents.collect { list ->
                _uiState.update { it.copy(contents = list) }
            }
        }

        viewModelScope.launch {
            userRepository.allUsers.collect { users ->
                _uiState.update { it.copy(allUsers = users) }
            }
        }
    }

    // ================= AUTENTICACIÓN Y USUARIOS =================

    fun login(usernameInput: String, passwordInput: String) {
        val u = usernameInput.trim()
        val p = passwordInput.trim()

        if (u.isBlank() || p.isBlank()) {
            _uiState.update { it.copy(loginError = "Por favor ingrese usuario y contraseña") }
            return
        }

        viewModelScope.launch {
            val user = userRepository.authenticate(u, p)
            if (user != null) {
                _uiState.update {
                    it.copy(
                        currentUser = user,
                        loginError = null,
                        registerError = null,
                        operationMessage = "Bienvenido, ${user.name} (${if (user.isAdmin) "Administrador" else "Usuario"})"
                    )
                }
            } else {
                _uiState.update {
                    it.copy(loginError = "Usuario o contraseña incorrectos")
                }
            }
        }
    }

    fun register(nameInput: String, usernameInput: String, passwordInput: String, confirmPassInput: String) {
        val name = nameInput.trim()
        val u = usernameInput.trim()
        val p = passwordInput.trim()
        val cp = confirmPassInput.trim()

        if (name.isBlank() || u.isBlank() || p.isBlank() || cp.isBlank()) {
            _uiState.update { it.copy(registerError = "Todos los campos son obligatorios") }
            return
        }

        if (u.length < 3) {
            _uiState.update { it.copy(registerError = "El usuario debe tener al menos 3 caracteres") }
            return
        }

        if (p.length < 4) {
            _uiState.update { it.copy(registerError = "La contraseña debe tener al menos 4 caracteres") }
            return
        }

        if (p != cp) {
            _uiState.update { it.copy(registerError = "Las contraseñas no coinciden") }
            return
        }

        viewModelScope.launch {
            val existing = userRepository.getUserByUsername(u)
            if (existing != null) {
                _uiState.update { it.copy(registerError = "El nombre de usuario ya está registrado") }
                return@launch
            }

            val success = userRepository.register(
                username = u,
                password = p,
                name = name,
                role = User.ROLE_USER
            )

            if (success) {
                val newUser = userRepository.authenticate(u, p)
                _uiState.update {
                    it.copy(
                        currentUser = newUser,
                        isRegisterMode = false,
                        registerError = null,
                        loginError = null,
                        operationMessage = "¡Cuenta creada exitosamente! Bienvenido $name"
                    )
                }
            } else {
                _uiState.update { it.copy(registerError = "Error al registrar la cuenta. Intente de nuevo.") }
            }
        }
    }

    fun logout() {
        _uiState.update {
            it.copy(
                currentUser = null,
                isRegisterMode = false,
                isManagingUsers = false,
                selectedContentId = null,
                isEditingOrCreating = false,
                editingContent = null,
                loginError = null,
                registerError = null,
                operationMessage = "Sesión cerrada correctamente"
            )
        }
    }

    fun setRegisterMode(register: Boolean) {
        _uiState.update {
            it.copy(
                isRegisterMode = register,
                loginError = null,
                registerError = null
            )
        }
    }

    fun setManagingUsers(managing: Boolean) {
        if (_uiState.value.currentUser?.isAdmin == true) {
            _uiState.update { it.copy(isManagingUsers = managing) }
        }
    }

    fun deleteUser(userId: Long) {
        val current = _uiState.value.currentUser
        if (current?.isAdmin != true) return

        if (userId == current.id) {
            _uiState.update { it.copy(operationMessage = "No puedes eliminar tu propia cuenta activa de administrador") }
            return
        }

        viewModelScope.launch {
            userRepository.deleteUser(userId)
            _uiState.update { it.copy(operationMessage = "Usuario eliminado del sistema") }
        }
    }

    fun createUserByAdmin(nameInput: String, usernameInput: String, passwordInput: String, role: String, onResult: (Boolean, String?) -> Unit) {
        val current = _uiState.value.currentUser
        if (current?.isAdmin != true) {
            onResult(false, "Acción no autorizada")
            return
        }

        val name = nameInput.trim()
        val u = usernameInput.trim()
        val p = passwordInput.trim()

        if (name.isBlank() || u.isBlank() || p.isBlank()) {
            onResult(false, "Todos los campos son obligatorios")
            return
        }

        if (u.length < 3) {
            onResult(false, "El nombre de usuario debe tener al menos 3 caracteres")
            return
        }

        if (p.length < 4) {
            onResult(false, "La contraseña debe tener al menos 4 caracteres")
            return
        }

        viewModelScope.launch {
            val existing = userRepository.getUserByUsername(u)
            if (existing != null) {
                onResult(false, "El usuario @$u ya existe")
                return@launch
            }

            val success = userRepository.register(u, p, name, role)
            if (success) {
                _uiState.update { it.copy(operationMessage = "✓ Usuario @$u creado correctamente") }
                onResult(true, null)
            } else {
                onResult(false, "Error al guardar el usuario en la base de datos")
            }
        }
    }

    fun updateUserByAdmin(userId: Long, nameInput: String, role: String, newPasswordInput: String?, onResult: (Boolean, String?) -> Unit) {
        val current = _uiState.value.currentUser
        if (current?.isAdmin != true) {
            onResult(false, "Acción no autorizada")
            return
        }

        val name = nameInput.trim()
        if (name.isBlank()) {
            onResult(false, "El nombre no puede estar vacío")
            return
        }

        val newPass = newPasswordInput?.trim()
        if (!newPass.isNullOrEmpty() && newPass.length < 4) {
            onResult(false, "La nueva contraseña debe tener al menos 4 caracteres")
            return
        }

        viewModelScope.launch {
            val success = userRepository.updateUser(userId, name, role, newPass)
            if (success) {
                if (current.id == userId) {
                    _uiState.update {
                        it.copy(
                            currentUser = it.currentUser?.copy(name = name, role = role),
                            operationMessage = "✓ Perfil propio actualizado correctamente"
                        )
                    }
                } else {
                    _uiState.update { it.copy(operationMessage = "✓ Usuario actualizado correctamente") }
                }
                onResult(true, null)
            } else {
                onResult(false, "No se encontró el usuario a actualizar")
            }
        }
    }

    // ================= GESTIÓN DE CONTENIDOS =================

    fun onSearchTextChange(text: String) {
        _uiState.update { it.copy(searchText = text) }
    }

    fun onTypeSelect(type: ContentType?) {
        _uiState.update { it.copy(selectedType = type) }
    }

    fun onContentSelect(id: String?) {
        _uiState.update { it.copy(selectedContentId = id, isEditingOrCreating = false, editingContent = null) }
    }

    fun startCreating() {
        if (_uiState.value.currentUser?.isAdmin != true) {
            _uiState.update { it.copy(operationMessage = "Acción permitida solo para el Administrador") }
            return
        }
        _uiState.update {
            it.copy(
                isEditingOrCreating = true,
                editingContent = CimContent(
                    id = "CIM_${System.currentTimeMillis()}",
                    type = ContentType.PROYECTO,
                    title = "",
                    shortDescription = "",
                    areaTematica = "Innovación en Madera",
                    year = "2025",
                    status = "En Ejecución"
                ),
                titleError = null,
                descriptionError = null
            )
        }
    }

    fun startEditing(content: CimContent) {
        if (_uiState.value.currentUser?.isAdmin != true) {
            _uiState.update { it.copy(operationMessage = "Acción permitida solo para el Administrador") }
            return
        }
        _uiState.update {
            it.copy(
                isEditingOrCreating = true,
                editingContent = content,
                titleError = null,
                descriptionError = null
            )
        }
    }

    fun cancelEditing() {
        _uiState.update { it.copy(isEditingOrCreating = false, editingContent = null) }
    }

    fun saveContent(title: String, description: String, area: String, type: ContentType, year: String, status: String, associated: String) {
        if (_uiState.value.currentUser?.isAdmin != true) {
            _uiState.update { it.copy(operationMessage = "Solo el Administrador puede guardar o editar iniciativas") }
            return
        }

        val titleErr = if (title.isBlank()) "El título no puede estar vacío" else null
        val descErr = if (description.isBlank()) "La descripción no puede estar vacía" else null

        if (titleErr != null || descErr != null) {
            _uiState.update { it.copy(titleError = titleErr, descriptionError = descErr) }
            return
        }

        viewModelScope.launch {
            val current = _uiState.value.editingContent
            if (current != null) {
                val existing = _uiState.value.contents.find { it.id == current.id }
                val updatedContent = current.copy(
                    title = title,
                    shortDescription = description,
                    areaTematica = area,
                    type = type,
                    year = year,
                    status = status,
                    associatedName = associated
                )
                if (existing != null) {
                    repository.update(updatedContent)
                    _uiState.update { it.copy(operationMessage = "✓ Registro actualizado exitosamente") }
                } else {
                    repository.insert(updatedContent)
                    _uiState.update { it.copy(operationMessage = "✓ Nuevo registro guardado exitosamente") }
                }
            }
            _uiState.update { it.copy(isEditingOrCreating = false, editingContent = null) }
        }
    }

    fun deleteContent(id: String) {
        if (_uiState.value.currentUser?.isAdmin != true) {
            _uiState.update { it.copy(operationMessage = "Solo el Administrador puede eliminar iniciativas") }
            return
        }

        viewModelScope.launch {
            repository.delete(id)
            _uiState.update { it.copy(selectedContentId = null, operationMessage = "🗑 Registro eliminado correctamente") }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(operationMessage = null) }
    }
}
