# Proyecto Android CIM UC - Entrega EP2

Aplicación móvil nativa desarrollada para **CIM UC** como parte de la entrega de la Evaluación Práctica 2 (EP2). La aplicación implementa arquitectura moderna de Android con **Kotlin**, **Jetpack Compose**, **Room Database** y el patrón de diseño **MVVM**.

---

## 🚀 Características Principales

* **Arquitectura MVVM & Clean Code:** Separación clara de responsabilidades con ViewModels, Repositorios y Fuentes de Datos Locales.
* **UI Moderna con Jetpack Compose:** Interfaz declarativa, adaptable y configurada con temas y estilos personalizados (`UI/Theme`).
* **Persistencia Local con Room:** Base de datos relacional local con precarga de datos iniciales (*seeded data*).
* **Búsqueda y Filtrado:** Listados interactivos con capacidades de búsqueda en tiempo real y filtros dinámicos.
* **Gestión de Datos (CRUD):** Flujos completos para crear, editar, visualizar en detalle y eliminar registros con validaciones de formulario.
* **Integración con Hardware y Sistema:**
  * **Cámara:** Vista previa e integración con la cámara del dispositivo.
  * **Compartir:** Opción para compartir contenido usando Intent del sistema.
* **Presentación EP2:** Incluye presentación HTML interactiva para el soporte de la entrega.
* **Testing Base:** Configuración inicial de *stubs* para pruebas unitarias e instrumentadas.

---

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** [Kotlin](https://kotlinlang.org/)
* **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose)
* **Persistencia:** [Room Database](https://developer.android.com/training/data-storage/room)
* **Arquitectura:** MVVM (Model-View-ViewModel) + StateFlow / LiveData
* **Componentes de Android Jetpack:** ViewModel, Navigation Compose, Coroutines, Flow.
* **Herramientas de construcción:** Gradle (Kotlin DSL / Groovy)

---

## 📂 Estructura del Proyecto

```text
app/src/main/java/com/cimuc/app/
├── data/
│   ├── local/          # Room DB, DAOs y Entidades
│   └── repository/     # Repositorio de datos
├── ui/
│   ├── components/     # Componentes reutilizables de UI
│   ├── screens/        # Pantallas (List, Detail, Form, Camera)
│   ├── theme/          # Colores, Tipografía y Temas
│   └── viewmodel/      # ViewModels para la gestión de estado
└── util/               # Funciones auxiliares e integraciones
```

---

## 🔧 Requisitos e Instalación

### Requisitos previos
* **Android Studio:** Jellyfish / Koala o superior recomendado.
* **JDK:** Versión 17 o superior.
* **SDK Mínimo:** Android 7.0 (API Nivel 24) o superior.

### Pasos para ejecutar el proyecto

1. **Clonar el repositorio:**
   ```bash
   git clone <URL_DEL_REPOSITORIO>
   cd <NOMBRE_DEL_PROYECTO>
   ```

2. **Abrir en Android Studio:**
   Abre Android Studio y selecciona **Open**, luego navega hasta la carpeta del proyecto.

3. **Sincronizar Gradle:**
   Permite que Android Studio descargue todas las dependencias necesarias (`Sync Project with Gradle Files`).

4. **Ejecutar la aplicación:**
   * Conecta un dispositivo físico con *Depuración USB* activada o inicia un Emulador (AVD).
   * Haz clic en **Run 'app'** (`Shift + F10`).

---

## 📄 Presentación EP2

Dentro del proyecto se incluye una presentación interactiva en formato HTML para la evaluación:
* Archivo: `docs/presentation.html` (o la ruta correspondiente en el proyecto).
* Puedes abrir este archivo en cualquier navegador web para visualizar el soporte visual de la entrega EP2.

---

## 🧪 Pruebas (Tests)

El proyecto incluye *stubs* y estructuras base para la ejecución de pruebas:
* **Pruebas unitarias:** Ubicadas en `app/src/test/`
* **Pruebas de UI / Instrumentadas:** Ubicadas en `app/src/androidTest/`

Para ejecutar las pruebas unitarias desde la terminal:
```bash
./gradlew test
```

---

## ✒️ Autor
* **CIM UC** - Entrega EP2 - 2026