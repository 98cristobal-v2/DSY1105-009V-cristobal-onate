package cl.duoc.cimucmovil.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cl.duoc.cimucmovil.data.CimDatabase
import cl.duoc.cimucmovil.data.CimRepository
import cl.duoc.cimucmovil.model.CimContent
import cl.duoc.cimucmovil.model.ContentType
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class CimUiState(
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

    private val _uiState = MutableStateFlow(CimUiState())
    val uiState: StateFlow<CimUiState> = _uiState.asStateFlow()

    init {
        val cimDao = CimDatabase.getDatabase(application).cimDao()
        repository = CimRepository(cimDao)

        viewModelScope.launch {
            repository.allContents.collect { list ->
                _uiState.update { it.copy(contents = list) }
            }
        }
    }

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
        viewModelScope.launch {
            repository.delete(id)
            _uiState.update { it.copy(selectedContentId = null, operationMessage = "🗑 Registro eliminado correctamente") }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(operationMessage = null) }
    }
}
