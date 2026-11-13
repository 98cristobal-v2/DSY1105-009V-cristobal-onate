# 🎬 Guion de Grabación: Demostración de CIM UC Móvil
**Asignatura:** Desarrollo de Aplicaciones Móviles (DSY1105) - Duoc UC  
**Evaluación:** Evaluación Parcial 2 (8%)  
**Integrantes:** Cristóbal Oñate & Ricardo Almarza  
**Duración Estimada:** 4 a 5 minutos  

---

## 📋 Resumen de Puntos Clave que Evaluará el Docente en el Video
1. **IE 2.1.1 (15%):** Diseño visual limpio, jerarquía visual y navegación con `BackHandler`.
2. **IE 2.1.2 (15%):** Formularios completos, íconos y validaciones visuales por campo.
3. **IE 2.2.1 (10%):** Lógica desacoplada en el ViewModel y estado reactivo con StateFlow.
4. **IE 2.2.2 (10%):** Transiciones y animaciones fluidas (`AnimatedContent`).
5. **IE 2.3.1 (15%):** Arquitectura MVVM y persistencia local con Room Database (SQLite).
6. **IE 2.4.1 (15%):** Uso de al menos 2 recursos nativos reales del dispositivo (Cámara 📷 y Compartir 📤).
7. **Control de Roles:** Demostración clara de la diferencia entre **Usuario Común** (solo consulta/interacción) y **Administrador** (CRUD completo y gestión de usuarios).

---

## ⏱️ Estructura del Video Minuto a Minuto

---

### ESCENA 1: Introducción y Contexto del Proyecto (0:00 - 0:35)
* **Qué mostrar en pantalla:** La pantalla de inicio de sesión de la app (`LoginScreen`) en el emulador o teléfono.
* **Qué decir:**
  > "Hola profesor y compañeros. En este video presentamos la entrega de la Evaluación Parcial 2 de la asignatura Desarrollo de Aplicaciones Móviles. Nuestro equipo está compuesto por Ricardo Almarza y quien les habla, Cristóbal Oñate.  
  > La solución desarrollada es **CIM UC Móvil**, una aplicación Android nativa construida con Kotlin, Jetpack Compose y arquitectura MVVM, diseñada para resolver la difusión, vinculación y gestión de iniciativas de edificación sustentable del Centro de Innovación en Madera de la Pontificia Universidad Católica."

---

### ESCENA 2: Formulario de Registro y Validaciones Visuales (0:35 - 1:15)
* **Qué mostrar en pantalla:**
  1. Tocar el enlace inferior: *"¿No tienes cuenta? Regístrate aquí"*.
  2. Mostrar la pantalla de registro con sus íconos (`🏷️`, `👤`, `🔒`).
  3. Presionar el botón *"Registrarme"* con los campos vacíos (se ve el mensaje de error).
  4. Ingresar un nombre de usuario corto o contraseñas que no coincidan para mostrar las validaciones.
  5. Completar los datos correctamente:
     * Nombre: `Cristóbal Oñate`
     * Usuario: `cristobal`
     * Contraseña: `user123`
     * Confirmar: `user123`
  6. Presionar *"Registrarme"*.
* **Qué decir:**
  > "Cumpliendo con los indicadores de formularios validados y lógica desacoplada, implementamos un sistema completo de registro y autenticación. Como pueden observar, cada campo cuenta con íconos visuales y validaciones en tiempo real gestionadas íntegramente desde el ViewModel mediante StateFlow. Si intentamos enviar campos vacíos o contraseñas que no coincidan, la interfaz responde inmediatamente con retroalimentación clara. Ahora crearemos una cuenta con rol de usuario común."

---

### ESCENA 3: Rol Usuario Común (Consulta, Búsqueda, Filtros y Recursos Nativos) (1:15 - 2:30)
* **Qué mostrar en pantalla:**
  1. La pantalla principal cargada con el catálogo de iniciativas.
  2. Destacar en la barra superior el indicador: `👤 Cristóbal Oñate (Usuario)`.
  3. Resaltar que **NO** existe el botón flotante `+` (el usuario común no puede crear).
  4. Escribir en el buscador: *"CLT"* o *"Madera"* y ver cómo filtra instantáneamente.
  5. Probar los chips de filtro por categoría: *"Proyectos"*, *"Capacidades I+D"*, *"Publicaciones"*, *"Todos"*.
  6. Seleccionar un proyecto (ejemplo: *"Edificio Modular en Altura"*).
  7. Destacar la **animación fluida** al entrar al detalle.
  8. Resaltar que en el detalle **NO aparecen los botones de editar (✏️) ni eliminar (🗑️)**.
  9. **Recurso Nativo 1 (Cámara 📷):** Tocar el botón *"Abrir Cámara"* y mostrar la apertura de la cámara nativa del sistema.
  10. **Recurso Nativo 2 (Compartir 📤):** Tocar el botón *"Compartir"* y mostrar el diálogo del sistema operativo (`Intent.ACTION_SEND`) con aplicaciones como WhatsApp o Correo.
  11. Probar los botones interactivos de *"Contactar"* y *"Usar E-Token"*.
  12. Presionar la flecha `←` o el botón de atrás para regresar al catálogo.
* **Qué decir:**
  > "Al iniciar sesión como usuario común, la aplicación aplica el principio de menor privilegio: el usuario tiene acceso de consulta e interacción, pero no puede alterar la base de datos. Vemos un catálogo fluido con buscador en tiempo real y chips de categorías temáticas.  
  > Al ingresar a un registro, apreciamos la transición animada mediante AnimatedContent. Aquí se aprecia que los botones de edición y borrado están ocultos para este perfil.  
  > Para cumplir con el acceso a recursos nativos del dispositivo, integramos dos funcionalidades reales: primero, la cámara nativa del sistema mediante ActivityResultContracts; y segundo, el sistema de compartir nativo mediante un Intent implícito con Action Send, permitiendo difundir la ficha técnica a cualquier aplicación externa."

---

### ESCENA 4: Cierre de Sesión y Acceso como Administrador (2:30 - 3:20)
* **Qué mostrar en pantalla:**
  1. En la barra superior, tocar el ícono de cerrar sesión (`🚪`).
  2. La app vuelve al `LoginScreen`.
  3. Ingresar las credenciales de administrador:
     * Usuario: `admin`
     * Contraseña: `admin123`
  4. Presionar *"Iniciar Sesión"*.
  5. Mostrar que ahora en la barra superior dice: `👤 Administrador CIM (Admin)`.
  6. Destacar que **SÍ aparece el botón flotante `+`** en la esquina inferior derecha.
* **Qué decir:**
  > "Ahora cerramos sesión y nos autenticaremos con el perfil de Administrador, cuyas credenciales maestras pre-cargadas son 'admin' y clave 'admin123'. Al acceder, la interfaz actualiza los permisos: ahora contamos con el botón flotante para registrar nuevas iniciativas y tenemos acceso a herramientas de gestión."

---

### ESCENA 5: Operaciones del Administrador (CRUD y Gestión de Usuarios en Room) (3:20 - 4:15)
* **Qué mostrar en pantalla:**
  1. **Crear Iniciativa:** Tocar el botón flotante `+`. Llenar el formulario con una prueba rápida (ej: *"Torre Experimental CLT"*, *"Prototipo sustentable"*). Presionar *"Guardar Registro"*. Mostrar el aviso `Snackbar` confirmando el guardado.
  2. **Editar Iniciativa:** Entrar a la iniciativa recién creada, tocar el botón de editar (`✏️`), modificar algún texto y guardar.
  3. **Eliminar Iniciativa:** Tocar el botón de eliminar (`🗑️`), confirmar en el diálogo de alerta y ver que desaparece del listado.
  4. **Gestión de Usuarios:** Tocar el ícono de usuarios (`👥`) en la barra superior.
  5. Mostrar el diálogo con la lista de usuarios guardados en SQLite Room (aparecerá `admin` y el usuario `cristobal` que creamos antes).
  6. Demostrar que el admin puede eliminar al usuario común tocando el ícono de papelera junto a su nombre.
* **Qué decir:**
  > "Como Administrador, tenemos control total del ciclo de vida de los datos: podemos añadir nuevas iniciativas, editarlas y eliminarlas con confirmación previa. Todo esto impacta directamente en nuestra base de datos local SQLite gestionada con Room.  
  > Adicionalmente, el administrador cuenta con un panel de gestión de usuarios donde se listan todas las cuentas registradas localmente en la tabla 'users', pudiendo dar de baja cuentas de usuarios comunes."

---

### ESCENA 6: Conclusión y Arquitectura Técnica (4:15 - 4:45)
* **Qué mostrar en pantalla:** Volver a la pantalla principal o mostrar brevemente la estructura de carpetas en Android Studio (`model`, `data`, `viewmodel`, `ui`).
* **Qué decir:**
  > "En resumen, la aplicación cumple con todos los indicadores de la rúbrica de la Evaluación Parcial 2: arquitectura modular MVVM, validaciones desacopladas con StateFlow, persistencia local con Room Database, transiciones animadas fluidas, formularios con retroalimentación visual, separación clara de roles y uso seguro de dos recursos nativos del dispositivo.  
  > Muchas gracias por su atención."

---

## 💡 Consejos Prácticos para la Grabación
1. **Resolución y Audio:** Graba en 1080p y asegúrate de que el micrófono se escuche nítido.
2. **Puntero del mouse / Toques en pantalla:** Si grabas en emulador, mantén los movimientos del cursor pausados para que el docente pueda apreciar cada cambio visual.
3. **Flujo sin interrupciones:** Practica una vez antes de grabar para que el video no supere los 5 minutos.
