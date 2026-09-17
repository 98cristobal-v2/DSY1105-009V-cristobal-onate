import java.util.Scanner

// Función auxiliar para calcular el precio final
fun calcularTotalConIvaYBeneficio(equipo: Equipo, minutos: Int): Double {
    val bruto = equipo.calcularMontoBruto(minutos)
    if (bruto == 0.0) return 0.0

    val conIva = bruto * 1.19 // IVA 19%
    return if (equipo.tipoUsuario == TipoUsuario.TERCERA_EDAD) {
        conIva * 0.50 // Descuento 50% en elmonto con IVA
    } else {
        conIva
    }
}

// Validacion simple de formato 
fun esCodigoValido(codigo: String): Boolean {
    return codigo.length == 6
}

fun main() {
    val scanner = Scanner(System.`in`)
    val puestos = Array(10) { i -> Puesto(i + 1) }
    var totalRecaudado = 0.0
    var totalEquiposAtendidos = 0

    while (true) {
        println("\n=== GYMSMART - CONTROL DE TURNO ===")
        println("1. Registrar Entrada de Equipo")
        println("2. Registrar Salida y Cobro")
        println("3. Consultar Estado de Puestos")
        println("4. Finalizar Turno y Generar Reporte")
        print("Seleccione opción: ")

        when (scanner.nextInt()) {
            1 -> {
                print("Ingrese Código de equipo (6 caracteres, ej: CA12CD): ")
                val codigo = scanner.next()

                // Validacion simple
                if (!esCodigoValido(codigo)) {
                    println("[ERROR] Código inválido. Debe tener exactamente 6 caracteres.")
                    continue
                }

                print("Ingrese Nombre del equipo: ")
                val nombre = scanner.next()

                println("Tipo de usuario (1: Visitante, 2: Socio, 3: Tercera Edad): ")
                val tipoUser = when (scanner.nextInt()) {
                    2 -> TipoUsuario.SOCIO
                    3 -> TipoUsuario.TERCERA_EDAD
                    else -> TipoUsuario.VISITANTE
                }

                println("Tipo de equipo (1: Cardio, 2: Fuerza, 3: Funcional): ")
                val tipoEq = scanner.nextInt()

                var nuevoEquipo: Equipo? = null
                if (tipoEq == 1) {
                    nuevoEquipo = EquipoCardio(codigo, nombre, tipoUser)
                } else if (tipoEq == 2) {
                    nuevoEquipo = EquipoFuerza(codigo, nombre, tipoUser)
                } else if (tipoEq == 3) {
                    print("¿Incluye entrenador personal? (true/false): ")
                    val conEnt = scanner.nextBoolean()
                    nuevoEquipo = EquipoFuncional(codigo, nombre, tipoUser, conEnt)
                }

                var asignado = false
                for (p in puestos) {
                    if (p.estado == EstadoPuesto.LIBRE) {
                        p.equipo = nuevoEquipo
                        p.estado = EstadoPuesto.EN_USO
                        println("-> Equipo $codigo registrado exitosamente en Puesto #${p.numero}")
                        asignado = true
                        break
                    }
                }
                if (!asignado) println("[ERROR] Sistema sin capacidad. No hay puestos libres.")
            }

            2 -> {
                print("Ingrese Código del equipo a retirar: ")
                val codBusqueda = scanner.next()
                print("Ingrese Minutos de uso: ")
                val minutos = scanner.nextInt()

                var encontrado = false
                for (p in puestos) {
                    if (p.estado == EstadoPuesto.EN_USO && p.equipo?.codigo.equals(codBusqueda, ignoreCase = true)) {
                        val equipo = p.equipo!!
                        val cobro = calcularTotalConIvaYBeneficio(equipo, minutos)
                        
                        totalRecaudado += cobro
                        totalEquiposAtendidos++

                        println("-> Ticket Emitido para ${equipo.codigo}: Total a pagar:$$cobro")

                        p.equipo = null
                        p.estado = EstadoPuesto.LIBRE
                        encontrado = true
                        break
                    }
                }
                if (!encontrado) println("[ERROR] Equipo no encontrado o no está en uso.")
            }

            3 -> {
                val libres = puestos.count { it.estado == EstadoPuesto.LIBRE }
                println("\n--- ESTADO ACTUAL ---")
                println("Puestos disponibles: $libres / 10")
                for (p in puestos) {
                    println("Puesto #${p.numero}: ${p.estado} \vert{} Equipo:${p.equipo?.codigo ?: "Vacío"}")
                }
            }

            4 -> {
                // Reporte de cierre de turno 
                println("\n========================================")
                println("      REPORTE DE CIERRE DE TURNO        ")
                println("========================================")
                println("Total Equipos Atendidos : $totalEquiposAtendidos")
                println("Total Recaudado en Turno: $$totalRecaudado")                 val promedio = if (totalEquiposAtendidos > 0) totalRecaudado / totalEquiposAtendidos else 0.0                 println("Ingreso Promedio/Equipo : $$promedio")
                println("Puestos Libres al Cierre: ${puestos.count { it.estado == EstadoPuesto.LIBRE }}")
                println("========================================")
                return
            }
        }
    }
}
