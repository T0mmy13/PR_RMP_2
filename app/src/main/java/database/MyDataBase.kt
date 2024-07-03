package database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MyData::class], version = 1)
abstract class MyDataBase : RoomDatabase() {
    abstract fun getDbDao() : MyDataBaseDao
    companion object Factory{
        fun getInstance(context: Context): MyDataBase{
            return Room.databaseBuilder(
                context.applicationContext,
                MyDataBase::class.java,
                "MyDataBase.db"
            ).build()
        }
    }
}