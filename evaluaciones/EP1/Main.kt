import java.util.Scanner

// funcion auxiliar para el cobro
fun calcularTotal(eq: Equipo, mins: Int): Double {
    val base = eq.calcularMontoBruto(mins)
    if (base == 0.0) return 0.0

    val conIva = base * 1.19
    if (eq.tipoUsuario == TipoUsuario.TERCERA_EDAD) {
        return conIva * 0.5
    } else {
        return conIva
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    val puestos = Array(10) { i -> Puesto(i + 1) }
    var caja = 0.0
    var contEquipos = 0

    while (true) {
        println("\n--- MENU GYM ---")
        println("1. Registrar Entrada")
        println("2. Registrar Salida")
        println("3. Ver Puestos")
        println("4. Salir / Cierre")
        print("Opcion: ")

        val op = scanner.nextInt()

        if (op == 1) {
            print("Codigo (6 letras/numeros): ")
            val cod = scanner.next()

            // validar largo
            if (cod.length != 6) {
                println("Error: El codigo debe tener 6 caracteres")
                continue
            }

            print("Nombre equipo: ")
            val nom = scanner.next()

            println("Tipo usuario (1:Visitante, 2:Socio, 3:Tercera edad):")
            val tUser = scanner.nextInt()
            var tipoU = TipoUsuario.VISITANTE
            if (tUser == 2) tipoU = TipoUsuario.SOCIO
            if (tUser == 3) tipoU = TipoUsuario.TERCERA_EDAD

            println("Tipo equipo (1:Cardio, 2:Fuerza, 3:Funcional):")
            val tEq = scanner.nextInt()

            var eq: Equipo? = null
            if (tEq == 1) eq = EquipoCardio(cod, nom, tipoU)
            if (tEq == 2) eq = EquipoFuerza(cod, nom, tipoU)
            if (tEq == 3) {
                print("Lleva entrenador (true/false)?: ")
                val profe = scanner.nextBoolean()
                eq = EquipoFuncional(cod, nom, tipoU, profe)
            }

            var puestoAsignado = false
            for (p in puestos) {
                if (p.estado == EstadoPuesto.LIBRE) {
                    p.equipo = eq
                    p.estado = EstadoPuesto.EN_USO
                    println("OK: Agregado al puesto " + p.numero)
                    puestoAsignado = true
                    break
                }
            }
            if (!puestoAsignado) println("No hay puestos libres")

        } else if (op == 2) {
            print("Codigo de equipo a retirar: ")
            val codBuscar = scanner.next()
            print("Minutos de uso: ")
            val mins = scanner.nextInt()

            var enc = false
            for (p in puestos) {
                if (p.estado == EstadoPuesto.EN_USO && p.equipo?.codigo == codBuscar) {
                    val pagar = calcularTotal(p.equipo!!, mins)
                    caja += pagar
                    contEquipos++

                    println("Total a cobrar: $" + pagar)
                    p.equipo = null
                    p.estado = EstadoPuesto.LIBRE
                    enc = true
                    break
                }
            }
            if (!enc) println("No se encontro el equipo")

        } else if (op == 3) {
            println("\n-- Lista de puestos --")
            for (p in puestos) {
                println("Puesto " + p.numero + ": " + p.estado)
            }

        } else if (op == 4) {
            println("\n--- CIERRE DE TURNO ---")
            println("Equipos atendidos: " + contEquipos)
            println("Total recaudado: $" + caja)
            
            var prom = 0.0
            if (contEquipos > 0) {
                prom = caja / contEquipos
            }
            println("Promedio por equipo: $" + prom)
            break
        }
    }
}
