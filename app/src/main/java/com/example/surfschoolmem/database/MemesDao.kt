package com.example.surfschoolmem.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.surfschoolmem.structures.Meme

@Dao
interface MemesDao {
    @Query("SELECT * FROM Meme")
    fun getAll(): LiveData<List<Meme>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(memes: List<Meme>)

    @Update
    fun update(meme: Meme)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(meme: Meme)

    @Query("SELECT * FROM Meme WHERE author=:id")
    fun getByAuthor(id: Long): LiveData<List<Meme>>
}