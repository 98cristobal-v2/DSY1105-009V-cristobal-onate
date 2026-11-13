# CIM UC Móvil - Innovación en Madera

Aplicación móvil nativa Android desarrollada en **Kotlin** y **Jetpack Compose** para la gestión, difusión y vinculación del Centro de Innovación en Madera (CIM UC). Permite la consulta de proyectos de edificación sustentable, capacidades de I+D, investigadores y publicaciones técnicas, con persistencia local y soporte para control de accesos por roles.

---

## 👥 Integrantes del Equipo
* **Cristóbal Oñate**
* **Ricardo Almarza**

**Asignatura:** Desarrollo de Aplicaciones Móviles (`DSY1105`)  
**Institución:** Duoc UC  
**Evaluación:** Evaluación Parcial 2 (8%)  

---

## 🚀 Funcionalidades Principales

1. **Autenticación y Control de Acceso por Roles (RBAC):**
   * **Inicio de Sesión y Registro de Usuarios:** Formularios completos con validación en tiempo real y retroalimentación visual clara.
   * **Rol Administrador (`ADMIN`):** Acceso total para crear (`+`), editar (`✏️`) y eliminar (`🗑️`) iniciativas, además de un panel dedicado para el **CRUD Completo de Gestión de Usuarios** registrados en el sistema (`👥`) donde puede crear nuevos administradores o usuarios, modificar sus datos/roles/contraseñas y eliminarlos.
   * **Rol Usuario Común (`USER`):** Perfil de solo lectura e interacción. Puede explorar el catálogo, buscar, filtrar por áreas temáticas y acceder a los recursos del dispositivo sin alterar la base de datos.
   * **Cerrar Sesión:** Botón accesible en la barra superior (`🚪`) para alternar cuentas en cualquier momento.

2. **Catálogo de Iniciativas CIM UC:**
   * **Buscador en tiempo real:** Filtra simultáneamente por título, área temática y descripción.
   * **Filtros por Categoría (Chips):** Proyectos, Capacidades I+D, Integrantes, Publicaciones y Actividades.
   * **Relaciones entre elementos:** Navegación vinculada hacia otros proyectos o capacidades asociadas.

3. **Recursos Nativos del Dispositivo:**
   * **📷 Cámara Nativa:** Integración mediante el contrato `ActivityResultContracts.TakePicturePreview` para captura rápida desde la ficha técnica.
   * **📤 Compartir Nativo (`Intent.ACTION_SEND`):** Permite enviar la información técnica del proyecto a aplicaciones externas instaladas en el dispositivo (WhatsApp, Correo, etc.).

4. **Persistencia Local con Room Database (SQLite):**
   * Persistencia 100% offline mediante `CimDatabase`.
   * Tablas normalizadas para contenidos (`cim_contents`) y usuarios registrados (`users`).
   * Carga inicial automática de datos de demostración y usuario administrador maestro.

5. **Diseño y Animaciones Fluidas:**
   * Implementado íntegramente con **Material 3**.
   * Transiciones y animaciones fluidas con `AnimatedContent` entre pantallas (Listado, Detalle, Formularios, Login).

---

## 🔑 Credenciales de Acceso

* **Administrador:**
  * **Usuario:** `admin`
  * **Contraseña:** `admin123`
  * **Rol:** Administrador con permisos de escritura y gestión de usuarios.

* **Usuario Común:**
  * Puedes registrar una cuenta nueva tocando **"Regístrate aquí"** en la pantalla de inicio de sesión.
  * Por defecto se asigna el rol `USER` (solo consulta e interacción).

---

## 🛠️ Stack Tecnológico y Arquitectura (MVVM)

* **Lenguaje:** Kotlin
* **UI Toolkit:** Jetpack Compose + Material 3
* **Arquitectura:** Model-View-ViewModel (MVVM) con separación estricta de capas:
  * `model/`: Entidades de dominio (`CimContent`, `User`, `ContentType`).
  * `data/`: Base de datos SQLite Room (`CimDatabase`), DAOs (`CimDao`, `UserDao`), Entidades (`CimEntity`, `UserEntity`) y Repositorios (`CimRepository`, `UserRepository`).
  * `viewmodel/`: `CimViewModel` con `StateFlow` reactivo para desacoplar completamente la lógica de validación del estado visual.
  * `ui/`: Pantallas (`CimApp`, `AuthScreens`, `UserManagerDialog`) y componentes temáticos (`theme/`).
* **Pruebas Unitarias:** JUnit 4 en `app/src/test/` validando lógica de dominio y mapeo de entidades.

---

## 💻 Instrucciones para Ejecutar el Proyecto

1. **Requisitos Previos:**
   * Android Studio Jellyfish / Koala / Ladybug o superior.
   * JDK 11 o JDK 17 (configurado como Gradle JDK).
   * Emulador Android o dispositivo físico con Android 7.0 (API 24) o superior.

2. **Pasos:**
   1. Clonar el repositorio:
      ```bash
      git clone <URL_DEL_REPOSITORIO>
      ```
   2. Abrir la carpeta del proyecto en Android Studio.
   3. Esperar la sincronización de Gradle (`Sync Project with Gradle Files`).
   4. Seleccionar el dispositivo o emulador y presionar **Run 'app'** (`Shift + F10`).
