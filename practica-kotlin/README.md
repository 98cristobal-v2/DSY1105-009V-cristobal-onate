# Práctica Kotlin

Ejercicios conceptuales de Kotlin organizados por bloques temáticos.

## Estructura

```
practica-kotlin/
└── src/cl/duoc/conate/
    ├── fundamentos/
    │   ├── ejercicio01/Main.kt
    │   ├── ejercicio02/Main.kt
    │   └── ...
    ├── nullsafety/
    ├── funciones/
    ├── colecciones/
    └── ...
```

## Bloques

- **fundamentos**: Variables, tipos, operadores básicos
- **nullsafety**: Null safety y operadores seguros
- **funciones**: Funciones, lambdas, funciones de orden superior
- **colecciones**: Listas, mapas, sets y operaciones funcionales
- **oop**: Clases, herencia, polimorfismo
- **genericos**: Genéricos en Kotlin

## Cómo ejecutar

Cada ejercicio es un archivo `Main.kt` independiente que puede ejecutarse directamente:

```bash
kotlinc src/cl/duoc/conate/fundamentos/ejercicio01/Main.kt -include-runtime -d output.jar
java -jar output.jar
```

O usar IDE (IntelliJ IDEA / Android Studio).
