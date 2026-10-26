package cl.duoc.cimucmovil.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [CimEntity::class], version = 1, exportSchema = false)
abstract class CimDatabase : RoomDatabase() {

    abstract fun cimDao(): CimDao

    companion object {
        @Volatile
        private var INSTANCE: CimDatabase? = null

        fun getDatabase(context: Context): CimDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CimDatabase::class.java,
                    "cim_database"
                )
                    .addCallback(CimDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private class CimDatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateInitialData(database.cimDao())
                    }
                }
            }

            private suspend fun populateInitialData(dao: CimDao) {
                val initialList = listOf(
                    CimEntity(
                        id = "P1",
                        type = "PROYECTO",
                        title = "Edificio Modular en Altura",
                        shortDescription = "Investigación y desarrollo de un prototipo de edificación modular residencial de 5 pisos usando madera contralaminada (CLT).",
                        areaTematica = "Construcción Sustentable",
                        year = "2025",
                        status = "En Ejecución",
                        associatedName = "Arauco & CIM UC",
                        relatedIdsString = "I1,C1"
                    ),
                    CimEntity(
                        id = "P2",
                        type = "PROYECTO",
                        title = "Vivienda Social Industrializada",
                        shortDescription = "Diseño de soluciones habitacionales económicas de alta eficiencia energética y rápida construcción en paneles de madera.",
                        areaTematica = "Vivienda e Infraestructura",
                        year = "2024",
                        status = "Completado",
                        associatedName = "MINVU",
                        relatedIdsString = "I2,C1,C2"
                    ),
                    CimEntity(
                        id = "C1",
                        type = "CAPACIDAD_ID",
                        title = "Laboratorio de Ensayos Estructurales",
                        shortDescription = "Capacidad tecnológica para realizar pruebas de carga, resistencia sísmica y durabilidad en componentes estructurales de madera.",
                        areaTematica = "I+D Avanzada",
                        status = "Activo",
                        relatedIdsString = "P1,P2"
                    ),
                    CimEntity(
                        id = "C2",
                        type = "CAPACIDAD_ID",
                        title = "Modelación BIM Eficiente",
                        shortDescription = "Optimización de procesos constructivos y gemelos digitales aplicados específicamente al diseño estructural en madera.",
                        areaTematica = "Transferencia Tecnológica",
                        status = "Activo",
                        relatedIdsString = "P2"
                    ),
                    CimEntity(
                        id = "I1",
                        type = "INTEGRANTE",
                        title = "Dr. Alejandro Silva",
                        shortDescription = "Investigador Principal CIM UC. Especialista en ingeniería sísmica, cálculo estructural y comportamiento de uniones mecánicas en CLT.",
                        areaTematica = "Ingeniería Estructural",
                        associatedName = "Pontificia Universidad Católica",
                        relatedIdsString = "P1"
                    ),
                    CimEntity(
                        id = "I2",
                        type = "INTEGRANTE",
                        title = "Mg. Elena Balmaceda",
                        shortDescription = "Directora de Transferencia Tecnológica. Experta en procesos de industrialización de madera y sustentabilidad en edificación pública.",
                        areaTematica = "Sustentabilidad y Gestión",
                        associatedName = "CIM UC",
                        relatedIdsString = "P2"
                    ),
                    CimEntity(
                        id = "PUB1",
                        type = "PUBLICACION",
                        title = "Manual de Diseño Sísmico para Madera",
                        shortDescription = "Documento técnico y normativo de libre acceso para el diseño seguro de edificaciones medianas en Chile.",
                        areaTematica = "Publicación Técnica",
                        year = "2024",
                        associatedName = "Ediciones UC",
                        relatedIdsString = "C1,I1"
                    ),
                    CimEntity(
                        id = "ACT1",
                        type = "ACTIVIDAD",
                        title = "Seminario Internacional: Madera del Futuro",
                        shortDescription = "Encuentro anual con expertos de Canadá y Europa sobre tendencias de edificación en madera masiva y sustentabilidad.",
                        areaTematica = "Extensión y Difusión",
                        year = "2026",
                        status = "Próximo (Octubre)",
                        relatedIdsString = "I2"
                    )
                )
                initialList.forEach { dao.insertContent(it) }
            }
        }
    }
}
