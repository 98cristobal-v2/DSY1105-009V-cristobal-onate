package cl.duoc.cimucmovil.data

import cl.duoc.cimucmovil.model.CimContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CimRepository(private val cimDao: CimDao) {

    val allContents: Flow<List<CimContent>> = cimDao.getAllContents().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun insert(content: CimContent) {
        cimDao.insertContent(CimEntity.fromDomain(content))
    }

    suspend fun update(content: CimContent) {
        cimDao.updateContent(CimEntity.fromDomain(content))
    }

    suspend fun delete(id: String) {
        cimDao.deleteContentById(id)
    }

    suspend fun getCount(): Int {
        return cimDao.getCount()
    }
}
