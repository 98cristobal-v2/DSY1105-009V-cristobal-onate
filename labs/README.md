# Laboratorios

Proyectos Android de mayor alcance. Cada laboratorio tiene su propio proyecto independiente.

## Laboratorios

- **lab-01**: Primer laboratorio (descripción)
- **lab-02**: Segundo laboratorio (descripción)
- **...**

## Estructura de un laboratorio

Cada laboratorio es un proyecto Android completo:

```
lab-nombre/
├── README.md
├── settings.gradle.kts
├── build.gradle.kts
├── gradlew
├── gradlew.bat
├── gradle/wrapper/
└── app/
    ├── build.gradle.kts
    ├── src/
    └── ...
```

## Cómo compilar y ejecutar

Cada laboratorio incluye su propio `README.md` con instrucciones específicas.

Generalmente:

```bash
cd lab-nombre
./gradlew build
./gradlew installDebug  # en emulador/dispositivo
```
