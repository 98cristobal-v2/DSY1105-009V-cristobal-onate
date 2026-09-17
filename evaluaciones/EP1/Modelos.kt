// tipos de usuario
enum class TipoUsuario { VISITANTE, SOCIO, TERCERA_EDAD }

// estados posibles de los puestos
enum class EstadoPuesto { LIBRE, EN_USO, EN_PROCESO, EN_MANTENCION }

// clase base para los equipos
open class Equipo(
    val codigo: String,
    val nombre: String,
    val tipoUsuario: TipoUsuario
) {
    open fun calcularMontoBruto(mins: Int): Double {
        return 0.0
    }
}

// equipo cardio: 1000/hr y 20% dscto socio
class EquipoCardio(cod: String, nom: String, tipo: TipoUsuario) : Equipo(cod, nom, tipo) {
    override fun calcularMontoBruto(mins: Int): Double {
        var hrs = mins / 60.0
        var total = hrs * 1000.0
        
        // aplica descuento si es socio
        if (tipoUsuario == TipoUsuario.SOCIO) {
            total = total * 0.80
        }
        return total
    }
}

// equipo fuerza: 1500/hr, menos de 15 min es gratis
class EquipoFuerza(cod: String, nom: String, tipo: TipoUsuario) : Equipo(cod, nom, tipo) {
    override fun calcularMontoBruto(mins: Int): Double {
        if (mins < 15) {
            return 0.0
        }
        var hrs = mins / 60.0
        return hrs * 1500.0
    }
}

// equipo funcional: 2500/hr y recargo del 30% si va con profe
class EquipoFuncional(
    cod: String,
    nom: String,
    tipo: TipoUsuario,
    val conProfe: Boolean
) : Equipo(cod, nom, tipo) {
    
    override fun calcularMontoBruto(mins: Int): Double {
        var hrs = mins / 60.0
        var total = hrs * 2500.0
        
        if (conProfe) {
            total = total * 1.30
        }
        return total
    }
}

// clase para el puesto
class Puesto(
    val numero: Int,
    var estado: EstadoPuesto = EstadoPuesto.LIBRE,
    var equipo: Equipo? = null
)
