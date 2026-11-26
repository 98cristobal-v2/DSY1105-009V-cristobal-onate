package cl.duoc.cimucmovil.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CimDao {
    @Query("SELECT * FROM cim_contents")
    fun getAllContents(): Flow<List<CimEntity>>

    @Query("SELECT * FROM cim_contents WHERE id = :id")
    suspend fun getContentById(id: String): CimEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContent(content: CimEntity)

    @Update
    suspend fun updateContent(content: CimEntity)

    @Query("DELETE FROM cim_contents WHERE id = :id")
    suspend fun deleteContentById(id: String)

    @Query("SELECT COUNT(*) FROM cim_contents")
    suspend fun getCount(): Int
}
