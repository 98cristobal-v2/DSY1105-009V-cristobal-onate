package cl.duoc.cimucmovil.ui

import android.content.Intent
import android.provider.MediaStore
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cl.duoc.cimucmovil.model.CimContent
import cl.duoc.cimucmovil.model.ContentType
import cl.duoc.cimucmovil.viewmodel.CimViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CimApp(viewModel: CimViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            // Foto capturada con éxito
        }
    }

    val filteredContents = remember(uiState.searchText, uiState.selectedType, uiState.contents) {
        uiState.contents.filter { content ->
            val matchesType = uiState.selectedType == null || content.type == uiState.selectedType
            val matchesSearch = content.title.contains(uiState.searchText, ignoreCase = true) ||
                    content.shortDescription.contains(uiState.searchText, ignoreCase = true) ||
                    content.areaTematica.contains(uiState.searchText, ignoreCase = true)
            matchesType && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("CIM UC - Innovación en Madera", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (uiState.selectedContentId != null || uiState.isEditingOrCreating) {
                        IconButton(onClick = {
                            if (uiState.isEditingOrCreating) viewModel.cancelEditing()
                            else viewModel.onContentSelect(null)
                        }) {
                            Text("←", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            if (!uiState.isEditingOrCreating && uiState.selectedContentId == null) {
                FloatingActionButton(
                    onClick = { viewModel.startCreating() },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Text("+", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            uiState.operationMessage?.let { msg ->
                Snackbar(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    action = {
                        TextButton(onClick = { viewModel.clearMessage() }) {
                            Text("OK", color = MaterialTheme.colorScheme.inversePrimary)
                        }
                    }
                ) {
                    Text(msg)
                }
            }

            AnimatedContent(
                targetState = Triple(uiState.isEditingOrCreating, uiState.selectedContentId, uiState.editingContent),
                transitionSpec = {
                    fadeIn() + slideInHorizontally { it / 2 } togetherWith fadeOut() + slideOutHorizontally { -it / 2 }
                },
                label = "NavigationTransition"
            ) { (isEditing, selectedId, editingContent) ->
                when {
                    isEditing && editingContent != null -> {
                        BackHandler { viewModel.cancelEditing() }
                        CimFormScreen(
                            content = editingContent,
                            titleError = uiState.titleError,
                            descriptionError = uiState.descriptionError,
                            onSave = { title, desc, area, type, year, status, assoc ->
                                viewModel.saveContent(title, desc, area, type, year, status, assoc)
                            },
                            onCancel = { viewModel.cancelEditing() }
                        )
                    }
                    selectedId != null -> {
                        val selectedItem = uiState.contents.find { it.id == selectedId }
                        if (selectedItem != null) {
                            BackHandler { viewModel.onContentSelect(null) }
                            CimDetailScreen(
                                content = selectedItem,
                                allContents = uiState.contents,
                                onSelectItem = { id -> viewModel.onContentSelect(id) },
                                onEdit = { viewModel.startEditing(selectedItem) },
                                onDelete = { viewModel.deleteContent(selectedItem.id) },
                                onOpenCameraButton = { cameraLauncher.launch(null) },
                                onShareContent = {
                                    val sendIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(Intent.EXTRA_TEXT, "CIM UC - ${selectedItem.title}: ${selectedItem.shortDescription}")
                                        type = "text/plain"
                                    }
                                    val shareIntent = Intent.createChooser(sendIntent, null)
                                    context.startActivity(shareIntent)
                                },
                                onBack = { viewModel.onContentSelect(null) }
                            )
                        }
                    }
                    else -> {
                        Column(modifier = Modifier.fillMaxSize()) {
                            OutlinedTextField(
                                value = uiState.searchText,
                                onValueChange = { viewModel.onSearchTextChange(it) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                placeholder = { Text("Buscar por título, área o descripción...") },
                                leadingIcon = { Text("🔍", modifier = Modifier.padding(start = 8.dp)) },
                                trailingIcon = {
                                    if (uiState.searchText.isNotEmpty()) {
                                        IconButton(onClick = { viewModel.onSearchTextChange("") }) {
                                            Text("✕", fontWeight = FontWeight.Bold)
                                        }
                                    }
                                },
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp)
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .horizontalScroll(rememberScrollState())
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                FilterChip(
                                    selected = uiState.selectedType == null,
                                    onClick = { viewModel.onTypeSelect(null) },
                                    label = { Text("Todos") }
                                )
                                ContentType.entries.forEach { type ->
                                    FilterChip(
                                        selected = uiState.selectedType == type,
                                        onClick = { viewModel.onTypeSelect(type) },
                                        label = { Text(getReadableType(type)) }
                                    )
                                }
                            }

                            if (filteredContents.isEmpty()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .weight(1f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text("No se encontraron resultados", style = MaterialTheme.typography.bodyLarge)
                                }
                            } else {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .weight(1f),
                                    contentPadding = PaddingValues(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    items(filteredContents, key = { it.id }) { content ->
                                        CimContentCard(
                                            content = content,
                                            onClick = { viewModel.onContentSelect(content.id) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CimContentCard(content: CimContent, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = getReadableType(content.type),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                if (!content.year.isNullOrEmpty()) {
                    Text(
                        text = content.year,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = content.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = "Área: ${content.areaTematica}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = content.shortDescription,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CimFormScreen(
    content: CimContent,
    titleError: String?,
    descriptionError: String?,
    onSave: (String, String, String, ContentType, String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    var title by remember { mutableStateOf(content.title) }
    var description by remember { mutableStateOf(content.shortDescription) }
    var area by remember { mutableStateOf(content.areaTematica) }
    var type by remember { mutableStateOf(content.type) }
    var year by remember { mutableStateOf(content.year ?: "2025") }
    var status by remember { mutableStateOf(content.status ?: "En Ejecución") }
    var associated by remember { mutableStateOf(content.associatedName ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = if (content.title.isEmpty()) "Registrar Nueva Iniciativa CIM UC" else "Editar Iniciativa",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Título *") },
            isError = titleError != null,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        if (titleError != null) {
            Text(text = titleError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción Corta *") },
            isError = descriptionError != null,
            modifier = Modifier.fillMaxWidth(),
            maxLines = 4
        )
        if (descriptionError != null) {
            Text(text = descriptionError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        OutlinedTextField(
            value = area,
            onValueChange = { area = it },
            label = { Text("Área Temática") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Año") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = status,
            onValueChange = { status = it },
            label = { Text("Estado / Vigencia") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = associated,
            onValueChange = { associated = it },
            label = { Text("Organización / Asociado") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Text("Tipo de Contenido:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ContentType.entries.forEach { t ->
                FilterChip(
                    selected = type == t,
                    onClick = { type = t },
                    label = { Text(getReadableType(t)) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = onCancel,
                modifier = Modifier.weight(1f)
            ) {
                Text("Cancelar")
            }
            Button(
                onClick = { onSave(title, description, area, type, year, status, associated) },
                modifier = Modifier.weight(1f)
            ) {
                Text("Guardar Registro")
            }
        }
    }
}

@Composable
fun CimDetailScreen(
    content: CimContent,
    allContents: List<CimContent>,
    onSelectItem: (String) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onOpenCameraButton: () -> Unit,
    onShareContent: () -> Unit,
    onBack: () -> Unit
) {
    var simulationMessage by remember { mutableStateOf<String?>(null) }
    var showDeleteConfirm by remember { mutableStateOf(false) }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de eliminar este registro del sistema CIM UC?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirm = false
                    onDelete()
                }) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = getReadableType(content.type).uppercase(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.ExtraBold
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                IconButton(onClick = onEdit) {
                    Text("✏️", style = MaterialTheme.typography.titleMedium)
                }
                IconButton(onClick = { showDeleteConfirm = true }) {
                    Text("🗑️", style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        Text(
            text = content.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(text = "Área Temática: ${content.areaTematica}", style = MaterialTheme.typography.bodyMedium)
                if (!content.year.isNullOrEmpty()) {
                    Text(text = "Año: ${content.year}", style = MaterialTheme.typography.bodyMedium)
                }
                if (!content.status.isNullOrEmpty()) {
                    Text(text = "Estado: ${content.status}", style = MaterialTheme.typography.bodyMedium)
                }
                if (!content.associatedName.isNullOrEmpty()) {
                    Text(text = "Asociado a: ${content.associatedName}", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Descripción General",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = content.shortDescription,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(vertical = 6.dp),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Recursos Nativos del Dispositivo",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = onOpenCameraButton,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("📷 Abrir Cámara", style = MaterialTheme.typography.labelSmall)
                    }
                    Button(
                        onClick = onShareContent,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Text("📤 Compartir", style = MaterialTheme.typography.labelSmall)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val relatedItems = allContents.filter { it.id in content.relatedIds }
        if (relatedItems.isNotEmpty()) {
            Text(
                text = "Vinculado con / Elementos Relacionados",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(6.dp))
            relatedItems.forEach { related ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { onSelectItem(related.id) },
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = related.title,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = getReadableType(related.type),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Oportunidades de Colaboración y Contacto",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = "Para vincularse a esta iniciativa o validar documentos institucionales con firma electrónica avanzada.",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "Contacto: ${content.contactEmail}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            simulationMessage = "✓ Solicitud de contacto registrada para: ${content.title}"
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Contactar", style = MaterialTheme.typography.labelMedium)
                    }

                    Button(
                        onClick = {
                            simulationMessage = "🔒 [SIMULACIÓN] Documento firmado exitosamente mediante Firma Electrónica Avanzada y E-Token institucional."
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Usar E-Token", style = MaterialTheme.typography.labelMedium)
                    }
                }

                simulationMessage?.let { msg ->
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = msg,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al Listado Principal")
        }
    }
}

fun getReadableType(type: ContentType): String {
    return when (type) {
        ContentType.PROYECTO -> "Proyecto"
        ContentType.CAPACIDAD_ID -> "Capacidad I+D"
        ContentType.INTEGRANTE -> "Integrante"
        ContentType.PUBLICACION -> "Publicación"
        ContentType.ACTIVIDAD -> "Actividad"
    }
}
