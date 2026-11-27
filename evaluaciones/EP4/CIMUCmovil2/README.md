# 🌲 CIM UC Móvil - Documentación Técnica y Manual del Proyecto

Aplicación móvil nativa en Android desarrollada en **Kotlin** y **Jetpack Compose** para la gestión, difusión y vinculación tecnológica del **Centro de Innovación en Madera (CIM UC)**. Permite la consulta y administración de proyectos de edificación sustentable, capacidades de I+D y publicaciones técnicas, integrando consumo de **Microservicios propios en Spring Boot**, **APIs REST externas**, persistencia local en **Room Database (Offline-First)**, recursos nativos del dispositivo y control de accesos por roles (**RBAC**).

---

## 👥 Integrantes del Equipo
* **Cristóbal Oñate**
* **Ricardo Almarza**

**Asignatura:** Desarrollo de Aplicaciones Móviles (`DSY1105`)  
**Institución:** Duoc UC  
**Evaluación:** Evaluación Parcial 4 Encargo (6%)  

---

## 📑 Índice de Documentación Técnica
1. [Resumen y Alcance del Proyecto](#-resumen-y-alcance-del-proyecto)
2. [Arquitectura y Patrón MVVM](#-arquitectura-y-patrón-mvvm)
3. [Funcionalidades Principales y Rúbrica EP4](#-funcionalidades-principales-y-rúbrica-ep4)
   * 3.1. Consumo de API Externa y Microservicio Spring Boot
   * 3.2. Control de Acceso por Roles (RBAC)
   * 3.3. Persistencia Local con Room Database
   * 3.4. Integración de Recursos Nativos
   * 3.5. Pruebas Unitarias (JUnit 4)
   * 3.6. Firma Digital y APK Release
4. [Credenciales de Acceso Preconfiguradas](#-credenciales-de-acceso-preconfiguradas)
5. [Instrucciones de Ejecución y Despliegue](#-instrucciones-de-ejecución-y-despliegue)

---

## 🏛️ Resumen y Alcance del Proyecto
**CIM UC Móvil** es una solución tecnológica diseñada para dar a conocer los avances en construcción en madera masiva (CLT, Timber Frame), laboratorios de ensayos e iniciativas de investigación del Centro de Innovación en Madera. La aplicación implementa una arquitectura robusta y moderna basada en componentes de Jetpack, garantizando alta escalabilidad, mantenimiento limpio y un diseño UI/UX responsivo con Material 3.

---

## 🛠️ Arquitectura y Patrón MVVM
El proyecto sigue rigurosamente el patrón arquitectónico **MVVM (Model-View-ViewModel)** recomendado por Android, asegurando la separación de incumbencias:
* **Capa de Presentación (UI):** Pantallas construidas en **Jetpack Compose** (`CimApp`, pantallas de autenticación, catálogo, fichas de detalle y diálogos modales). El estado reactivo se gestiona mediante `StateFlow` y `Coroutines`.
* **Capa de Dominio / Lógica:** `CimViewModel` centraliza las reglas de negocio, validaciones de formularios, autenticación y peticiones de red.
* **Capa de Datos (Data):** 
  * Repositorios (`CimRepository`, `UserRepository`) como única fuente de verdad.
  * Base de datos local **Room Database (SQLite)** con DAOs normalizados (`CimDao`, `UserDao`).
  * Servicios remotos de red (**Retrofit 2 + Gson**) para comunicación HTTP con el backend.

---

## 🚀 Funcionalidades Principales y Rúbrica EP4

### 1. 🌐 Consumo de Microservicios Propios y APIs Externas
La aplicación integra conectividad HTTP avanzada mediante dos servicios Retrofit:
* **Microservicio Propio (Spring Boot):** Conectado al backend local (`http://10.0.2.2:8080/api/cim/posts`) para obtener post de innovación en madera.
* **API Externa Pública:** Conectada a servicios externos en internet (`https://jsonplaceholder.typicode.com/posts`) para posts de respaldo.
* **Sincronización Offline-First:** Al pulsar el botón de sincronización `🌐` (exclusivo para el rol `ADMIN`), la app consume ambas fuentes, las etiqueta visualmente como `[Microservicio]` o `[API Externa]` y las almacena en la base de datos local Room.

### 2. 🔐 Control de Acceso por Roles (RBAC)
Sistema de seguridad y permisos basado en roles de usuario almacenados en base de datos:
* **Administrador (`ADMIN`):** Control total. Puede sincronizar los servicios remotos (`🌐`), crear (`+`), editar (`✏️`) y eliminar (`🗑️`) iniciativas del catálogo, y acceder al panel de gestión de cuentas (`👥`).
* **Usuario Común (`USER`):** Perfil de consulta. Permite explorar el catálogo, buscar en tiempo real, filtrar por categorías (Chips), visualizar fichas de detalle y compartir información.

### 3. 💾 Persistencia Local con Room Database
* Base de datos local `cim_database` con tablas relacionales `cim_contents` y `users`.
* Precarga automática de contenidos institucionales del CIM UC y de la cuenta de Administrador maestro al iniciar por primera vez.

### 4. 📱 Recursos Nativos del Dispositivo
* **Cámara Nativa:** Captura de fotografías desde la ficha técnica utilizando el contrato `ActivityResultContracts.TakePicturePreview`.
* **Compartir Información (`Intent.ACTION_SEND`):** Difusión de fichas técnicas de proyectos a aplicaciones externas de mensajería y correo.
* **Intents Nativos de Correo (`Intent.ACTION_SENDTO`):** Integración con la aplicación de correo nativa del dispositivo para gestionar solicitudes de contacto y colaboración directamente desde la ficha técnica de cada proyecto.
### 5. 🧪 Pruebas Unitarias (JUnit 4)
El proyecto incluye un conjunto completo de pruebas unitarias (`8 passed`) que validan la lógica de dominio, el comportamiento de los roles de usuario, la conversión de entidades y los DTOs de red:
* `UserDomainTest`: Validación de roles (`isAdmin`) y mapeo de entidades.
* `CimApiTest`: Pruebas de propiedades de DTOs remotos.
* `CimContentTest`: Verificación de modelos de dominio y relaciones de contenido.
* Se pueden ejecutar mediante el script automatizado **`run_tests.bat`**.

### 6. 📦 Firma Digital y Generación de APK Release
* Configuración de un keystore privado `release.jks` en el archivo `app/build.gradle.kts` dentro del bloque `signingConfigs`.
* Generación del ejecutable firmado de producción:
  `app/build/outputs/apk/release/app-release.apk`.

---

## 🔑 Credenciales de Acceso Preconfiguradas

* **Administrador Maestro:**
  * **Usuario:** `admin`
  * **Contraseña:** `admin123`
  * **Rol:** Administrador (`ADMIN`) con permisos de escritura, eliminación, sincronización y gestión de usuarios.

* **Nuevo Usuario Común:**
  * Puede registrarse directamente desde la pantalla de inicio haciendo clic en **"Regístrate aquí"** (asignación automática de rol `USER`).

---

## 💻 Instrucciones de Ejecución y Despliegue

### Requisitos Previos:
* Android Studio Jellyfish / Koala / Ladybug o superior.
* JDK 11 o JDK 17 configurado como Gradle JDK.
* Java instalado para ejecutar el microservicio de Spring Boot.

### Pasos para Ejecutar el Proyecto:

1. **Ejecutar el Microservicio Backend (Spring Boot):**
   * Ve a la carpeta `demo/` y haz doble clic en el archivo **`run_backend.bat`** (o ejecuta `.\gradlew bootRun` en la terminal dentro de `demo/`). Esto levantará el servidor en el puerto `8080`.

2. **Ejecutar la Aplicación en Android Studio:**
   * Abre la carpeta `CIMUCmovil2/` en Android Studio.
   * Sincroniza Gradle (`Sync Project with Gradle Files`).
   * Selecciona un emulador o dispositivo físico y presiona **Run 'app'** (`Shift + F10`).

3. **Ejecutar las Pruebas Unitarias:**
   * Haz doble clic en el archivo **`run_tests.bat`** en la raíz del proyecto para verificar que las 8 pruebas unitarias pasen exitosamente.

4. **Generar APK Firmado (Modo Release):**
   * En la terminal del proyecto, ejecuta:
     ```bash
     .\gradlew app:assembleRelease
     ```
   * El APK firmado quedará listo en `app/build/outputs/apk/release/app-release.apk`.
