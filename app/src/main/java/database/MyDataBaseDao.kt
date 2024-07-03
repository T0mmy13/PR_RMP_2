package database

import androidx.room.*
import database.MyData
import kotlinx.coroutines.flow.Flow

@Dao
interface MyDataBaseDao {
    @Insert
    suspend fun insert(myData: MyData)
    @Update
    suspend fun update(myData: MyData)
    //fun getAllMyData(): Flow<MyData>
    @Query("SELECT * FROM my_data_table WHERE id = :userId")
    suspend fun getUserById(userId: Int): MyData?
}