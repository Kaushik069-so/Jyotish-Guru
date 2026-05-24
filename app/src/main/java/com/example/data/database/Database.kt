package com.example.data.database

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

// --- Room Entities ---

@Entity(tableName = "saved_readings")
data class SavedReading(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // "Pooja", "Prasad", "Panchang", "Palmistry"
    val title: String,
    val queryText: String,
    val resultText: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "pooja_checklist_items")
data class PoojaChecklistItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val poojaName: String, // e.g. "Ganesh Chaturthi", "Diwali"
    val itemName: String,
    val quantity: String = "As needed",
    val isCompleted: Boolean = false
)

@Entity(tableName = "saved_recipes")
data class SavedRecipe(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recipeName: String,
    val occasion: String,
    val ingredients: String,
    val instructions: String,
    val category: String = "Prasad", // e.g., "Prasad", "Vrat"
    val timestamp: Long = System.currentTimeMillis()
)

// --- Data Access Object (DAO) ---

@Dao
interface JyotishDao {
    // Saved Readings
    @Query("SELECT * FROM saved_readings ORDER BY timestamp DESC")
    fun getAllReadingsFlow(): Flow<List<SavedReading>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReading(reading: SavedReading): Long

    @Query("DELETE FROM saved_readings WHERE id = :id")
    suspend fun deleteReading(id: Int)

    // Pooja Checklist
    @Query("SELECT * FROM pooja_checklist_items ORDER BY id ASC")
    fun getAllChecklistItemsFlow(): Flow<List<PoojaChecklistItem>>

    @Query("SELECT * FROM pooja_checklist_items WHERE poojaName = :poojaName ORDER BY id ASC")
    fun getChecklistForPoojaFlow(poojaName: String): Flow<List<PoojaChecklistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklistItem(item: PoojaChecklistItem): Long

    @Query("UPDATE pooja_checklist_items SET isCompleted = :completed WHERE id = :id")
    suspend fun updateChecklistItemStatus(id: Int, completed: Boolean)

    @Query("DELETE FROM pooja_checklist_items WHERE poojaName = :poojaName")
    suspend fun clearChecklistForPooja(poojaName: String)

    @Query("DELETE FROM pooja_checklist_items WHERE id = :id")
    suspend fun deleteChecklistItem(id: Int)

    // Saved Recipes / Prasad
    @Query("SELECT * FROM saved_recipes ORDER BY timestamp DESC")
    fun getAllRecipesFlow(): Flow<List<SavedRecipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: SavedRecipe): Long

    @Query("DELETE FROM saved_recipes WHERE id = :id")
    suspend fun deleteRecipe(id: Int)
}

// --- AppDatabase ---

@Database(
    entities = [SavedReading::class, PoojaChecklistItem::class, SavedRecipe::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun jyotishDao(): JyotishDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "jyotish_guru_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
