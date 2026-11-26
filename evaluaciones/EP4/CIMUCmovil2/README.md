# CIM UC Móvil - Innovación en Madera

Aplicación móvil nativa Android desarrollada en **Kotlin** y **Jetpack Compose** para la gestión, difusión y vinculación del Centro de Innovación en Madera (CIM UC). Permite la consulta y administración de proyectos de edificación sustentable, capacidades de I+D, investigadores y publicaciones técnicas, integrando consumo de **API REST remota (Retrofit)**, persistencia local en **Room Database**, recursos nativos del dispositivo y control de accesos por roles (RBAC).

---

## 👥 Integrantes del Equipo
* **Cristóbal Oñate**
* **Ricardo Almarza**

**Asignatura:** Desarrollo de Aplicaciones Móviles (`DSY1105`)  
**Institución:** Duoc UC  
**Evaluación:** Evaluación Parcial 4 Encargo (6%)  

---

## 🚀 Funcionalidades Principales

1. **🌐 Consumo e Integración de API REST (Retrofit 2 + Gson):**
   * **Sincronización de Datos Remotos:** Integración mediante `Retrofit` y `GsonConverterFactory` para consumir servicios web externos (`CimApiService`).
   * **Mapeo DTO a Dominio:** Conversión dinámica de objetos de transferencia de datos (`CimRemoteDto`) hacia entidades de dominio local (`CimContent`).
   * **Persistencia Directa:** Al presionar el botón de sincronización remota (`🌐`) en la barra superior, los datos obtenidos de la API se insertan automáticamente en la base de datos local Room.
   * **Feedback Visual e Indicadores:** Incluye un diálogo modal informativo con los elementos importados, indicador de carga (`CircularProgressIndicator`) y captura de errores de conexión de red (`remoteError`).

2. **🔐 Autenticación y Control de Acceso por Roles (RBAC):**
   * **Inicio de Sesión y Registro de Usuarios:** Validaciones en tiempo real (campos requeridos, longitud mínima de usuario/contraseña y coincidencia de confirmación).
   * **Rol Administrador (`ADMIN`):** Permisos completos para crear (`+`), editar (`✏️`) y eliminar (`🗑️`) contenidos del catálogo. Acceso al panel de **Gestión de Usuarios** (`👥`) para crear, modificar y eliminar cuentas del sistema.
   * **Rol Usuario Común (`USER`):** Perfil de consulta e interacción. Permite buscar, filtrar por áreas temáticas, visualizar detalles y compartir iniciativas.
   * **Cerrar Sesión:** Acción accesible desde el menú superior (`🚪`) para alternar entre cuentas de manera rápida.

3. **📚 Catálogo Interactivo de Iniciativas CIM UC:**
   * **Buscador en Tiempo Real:** Filtrado simultáneo por título, descripción y área temática.
   * **Filtros por Categoría (Chips):** Selección por Proyectos, Capacidades I+D, Integrantes, Publicaciones y Actividades.
   * **Navegación Dinámica:** Transición fluida entre el catálogo general y la vista en detalle.

4. **📱 Recursos Nativos e Interoperabilidad del Dispositivo:**
   * **📷 Cámara Nativa:** Captura fotográfica desde la ficha técnica utilizando el contrato `ActivityResultContracts.TakePicturePreview`.
   * **📤 Compartir Nativo (`Intent.ACTION_SEND`):** Difusión de la información técnica del proyecto a aplicaciones de terceros (WhatsApp, Correo, etc.).

5. **💾 Persistencia Local con Room Database (SQLite):**
   * Arquitectura offline-first mediante `CimDatabase`.
   * Tablas normalizadas: `cim_contents` (iniciativas/publicaciones) and `users` (usuarios registrados).
   * Precarga automática de contenidos iniciales y del usuario Administrador maestro al iniciar por primera vez.

6. **🎨 Interfaz Moderna y Animaciones Fluidas:**
   * Diseño 100% responsivo con **Material 3**.
   * Animaciones con `AnimatedContent` entre login, catálogo, formularios y detalles.

---

## 🔑 Credenciales de Acceso Preconfiguradas

* **Administrador Maestro:**
  * **Usuario:** `admin`
  * **Contraseña:** `admin123`
  * **Rol:** Administrador (`ADMIN`) con permisos de escritura, eliminación y gestión de usuarios.

* **Usuario Común:**
  * Se puede crear un nuevo usuario desde la pantalla de inicio de sesión haciendo clic en **"Regístrate aquí"**.
  * Asignación automática del rol `USER` (solo lectura/interacción).

---

## 🛠️ Stack Tecnológico y Arquitectura (MVVM)

El proyecto sigue una arquitectura **MVVM (Model-View-ViewModel)** reactiva con separación clara de capas:

* **Lenguaje:** Kotlin
* **UI Toolkit:** Jetpack Compose + Material 3
* **Conectividad REST:** Retrofit 2.x + Gson Converter
* **Persistencia:** Room Database (SQLite) + KSP (Kotlin Symbol Processing)
* **Gestión de Estado:** `CimViewModel` con `StateFlow` y `Coroutines`
* **Capa de Datos:**
  * `data/remote/`: `CimApiService`, `CimRemoteDto` (Consumo HTTP REST).
  * `data/`: `CimDatabase`, DAOs (`CimDao`, `UserDao`), Entidades (`CimEntity`, `UserEntity`), Repositorios (`CimRepository`, `UserRepository`).
* **Capa de Dominio / Modelos:** `CimContent`, `User`, `ContentType`.
* **Capa de Presentación (UI):** `CimApp`, `AuthScreens`, `UserManagerDialog`, `theme/`.
* **Pruebas Unitarias (JUnit 4):**
  * `UserDomainTest`: Pruebas de modelos de dominio y mapeo de entidades de usuario.
  * `CimApiTest`: Pruebas unitarias de consumo e instanciación de DTOs de API REST.

---

## 🌐 Configuración de la API REST

La aplicación está configurada para conectarse a un endpoint API REST a través de `CimApiService`:
* **Base URL:** `http://10.0.2.2:8080/api/cim/` (IP de loopback del emulador de Android hacia la máquina anfitriona).
* **Endpoint:** `GET posts` (`List<CimRemoteDto>`).
* **Manejo de Errores:** En caso de falta de conectividad o fallo en el servidor, la aplicación captura la excepción y notifica mediante la interfaz sin interrumpir la experiencia del usuario ni afectar los datos locales en Room.

---

## 💻 Instrucciones para Ejecutar el Proyecto

1. **Requisitos Previos:**
   * Android Studio Jellyfish / Koala / Ladybug o superior.
   * JDK 11 o JDK 17 (configurado como Gradle JDK).
   * Emulador de Android Studio o dispositivo físico con Android 7.0 (API 24) o superior.

2. **Pasos para Ejecución:**
   1. Clonar el repositorio:
      ```bash
      git clone <URL_DEL_REPOSITORIO>
      ```
   2. Abrir la carpeta `CIMUCmovil2` en Android Studio.
   3. Sincronizar Gradle (`Sync Project with Gradle Files`).
   4. Ejecutar las pruebas unitarias (opcional):
      * Clic derecho en `app/src/test/java` -> **Run 'Tests in 'cl.duoc.cimucmovil''**.
   5. Seleccionar un emulador o dispositivo físico y presionar **Run 'app'** (`Shift + F10`).
