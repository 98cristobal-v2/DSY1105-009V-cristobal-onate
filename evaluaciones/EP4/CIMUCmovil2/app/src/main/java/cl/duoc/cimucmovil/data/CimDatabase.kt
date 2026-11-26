package cl.duoc.cimucmovil.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import cl.duoc.cimucmovil.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [CimEntity::class, UserEntity::class], version = 2, exportSchema = false)
abstract class CimDatabase : RoomDatabase() {

    abstract fun cimDao(): CimDao
    abstract fun userDao(): UserDao

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
                    .fallbackToDestructiveMigration()
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
                        populateInitialContents(database.cimDao())
                        ensureAdminUser(database.userDao())
                    }
                }
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        if (database.cimDao().getCount() == 0) {
                            populateInitialContents(database.cimDao())
                        }
                        ensureAdminUser(database.userDao())
                    }
                }
            }
        }

        suspend fun ensureAdminUser(userDao: UserDao) {
            val admin = userDao.getUserByUsername("admin")
            if (admin == null) {
                userDao.insertUser(
                    UserEntity(
                        username = "admin",
                        password = "admin123",
                        name = "Administrador CIM",
                        role = User.ROLE_ADMIN
                    )
                )
            }
        }

        suspend fun populateInitialContents(dao: CimDao) {
            val initialList = listOf(
                // PROYECTOS EMBLEMÁTICOS
                CimEntity(
                    id = "P1",
                    type = "PROYECTO",
                    title = "Torre Experimental Peñuelas (CLT)",
                    shortDescription = "Diseño, cálculo y construcción de un edificio prototipo de 6 pisos en madera contralaminada (CLT) con monitoreo sísmico continuo.",
                    areaTematica = "Construcción Sustentable en Altura",
                    year = "2025",
                    status = "En Ejecución",
                    associatedName = "Arauco & CIM UC",
                    relatedIdsString = "I1,C1"
                ),
                CimEntity(
                    id = "P2",
                    type = "PROYECTO",
                    title = "Vivienda Social Industrializada VSI-24",
                    shortDescription = "Solución habitacional económica, de montaje en 48 horas y alta eficiencia térmica mediante paneles prefabricados en madera.",
                    areaTematica = "Vivienda e Infraestructura Social",
                    year = "2024",
                    status = "Completado",
                    associatedName = "MINVU & Serviu Metropolitano",
                    relatedIdsString = "I2,C1,C2"
                ),
                CimEntity(
                    id = "P3",
                    type = "PROYECTO",
                    title = "Pasarela Peatonal Curacaví en Glulam",
                    shortDescription = "Estructura peatonal de 34 metros de luz libre fabricada con vigas curvas de madera laminada encolada (Glulam) tratada contra intemperie.",
                    areaTematica = "Infraestructura Vial y Paisajismo",
                    year = "2025",
                    status = "En Ejecución",
                    associatedName = "MOP - Dirección de Vialidad",
                    relatedIdsString = "I1,C1"
                ),
                CimEntity(
                    id = "P4",
                    type = "PROYECTO",
                    title = "Complejo Educativo Rural Modular",
                    shortDescription = "Módulos escolares de carbono negativo con climatización solar pasiva y envolvente térmica en madera de pino radiata certificada.",
                    areaTematica = "Equipamiento Comunitario",
                    year = "2024",
                    status = "Completado",
                    associatedName = "Municipalidad de Villarrica",
                    relatedIdsString = "I2,C3"
                ),

                // CAPACIDADES DE I+D Y LABORATORIOS
                CimEntity(
                    id = "C1",
                    type = "CAPACIDAD_ID",
                    title = "Laboratorio de Ensayos Estructurales y Sísmicos",
                    shortDescription = "Infraestructura con marco de reacción y actuadores servohidráulicos de 100 toneladas para pruebas de carga cíclica en muros CLT.",
                    areaTematica = "I+D Avanzada y Certificación",
                    status = "Activo y Operativo",
                    associatedName = "Campus San Joaquín UC",
                    relatedIdsString = "P1,P2,P3"
                ),
                CimEntity(
                    id = "C2",
                    type = "CAPACIDAD_ID",
                    title = "Modelación BIM y Gemelos Digitales",
                    shortDescription = "Flujos de trabajo digitalizados para diseño paramétrico, cuantificación de huella de carbono y prefabricación automatizada.",
                    areaTematica = "Transferencia Tecnológica",
                    status = "Activo",
                    associatedName = "Laboratorio Digital CIM",
                    relatedIdsString = "P1,P2"
                ),
                CimEntity(
                    id = "C3",
                    type = "CAPACIDAD_ID",
                    title = "Laboratorio de Física de la Construcción",
                    shortDescription = "Cámara climática para evaluación de confort higrotérmico, acústica edilicia y resistencia al fuego de ensambles de madera.",
                    areaTematica = "Eficiencia Energética",
                    status = "Activo",
                    associatedName = "DICTUC & CIM UC",
                    relatedIdsString = "P4,PUB2"
                ),

                // INVESTIGADORES Y EQUIPO
                CimEntity(
                    id = "I1",
                    type = "INTEGRANTE",
                    title = "Dr. Alejandro Silva",
                    shortDescription = "Investigador Principal CIM UC. PhD en Ingeniería Estructural, referente internacional en diseño sismo-resistente y uniones mecánicas.",
                    areaTematica = "Ingeniería Estructural y Sismo",
                    associatedName = "Pontificia Universidad Católica de Chile",
                    relatedIdsString = "P1,P3,PUB1"
                ),
                CimEntity(
                    id = "I2",
                    type = "INTEGRANTE",
                    title = "Dra. Elena Balmaceda",
                    shortDescription = "Directora de Transferencia Tecnológica. Experta en procesos de industrialización sostenible y desarrollo de políticas de vivienda pública.",
                    areaTematica = "Sustentabilidad y Gestión Pública",
                    associatedName = "CIM UC & CORFO",
                    relatedIdsString = "P2,P4,ACT1"
                ),
                CimEntity(
                    id = "I3",
                    type = "INTEGRANTE",
                    title = "Mg. Roberto Manríquez",
                    shortDescription = "Jefe de Laboratorio de Ensayos Físico-Mecánicos. Más de 15 años de experiencia en control de calidad de madera para la construcción.",
                    areaTematica = "Ensayos Físico-Mecánicos",
                    associatedName = "CIM UC",
                    relatedIdsString = "C1,C3"
                ),

                // PUBLICACIONES TÉCNICAS
                CimEntity(
                    id = "PUB1",
                    type = "PUBLICACION",
                    title = "Manual de Diseño Sísmico para Edificaciones en Madera",
                    shortDescription = "Guía técnica integral y criterios normativos basados en la NCh433 para el diseño seguro de edificaciones medianas y altas en Chile.",
                    areaTematica = "Normativa Técnica y Cálculo",
                    year = "2024",
                    associatedName = "Ediciones UC",
                    relatedIdsString = "C1,I1,P1"
                ),
                CimEntity(
                    id = "PUB2",
                    type = "PUBLICACION",
                    title = "Catálogo de Envolventes y Eficiencia Térmica",
                    shortDescription = "Compendio de detalles constructivos y soluciones térmicas para cumplir con las exigencias de la nueva Reglamentación Térmica.",
                    areaTematica = "Manual Técnico",
                    year = "2025",
                    associatedName = "CIM UC & MINVU",
                    relatedIdsString = "C3,P2,P4"
                ),

                // ACTIVIDADES Y EXTENSIÓN
                CimEntity(
                    id = "ACT1",
                    type = "ACTIVIDAD",
                    title = "Seminario Internacional: Arquitectura en Altura en Madera 2026",
                    shortDescription = "Congreso con expertos de Canadá, Austria y Chile sobre tendencias globales en rascacielos híbridos y huella de carbono.",
                    areaTematica = "Extensión y Difusión Científica",
                    year = "2026",
                    status = "Próximo (Noviembre)",
                    associatedName = "Centro de Extensión UC",
                    relatedIdsString = "I1,I2,P1"
                ),
                CimEntity(
                    id = "ACT2",
                    type = "ACTIVIDAD",
                    title = "Taller Práctico: Montaje Rápido de Paneles CLT",
                    shortDescription = "Capacitación en terreno para técnicos y constructores sobre secuencias de izaje, fijaciones estructurales y sellos herméticos.",
                    areaTematica = "Formación Técnica y Oficios",
                    year = "2026",
                    status = "Próximo (Diciembre)",
                    associatedName = "Duoc UC & CIM UC",
                    relatedIdsString = "I3,P2"
                )
            )
            initialList.forEach { dao.insertContent(it) }
        }
    }
}
