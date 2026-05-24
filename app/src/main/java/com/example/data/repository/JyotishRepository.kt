package com.example.data.repository

import com.example.data.database.*
import kotlinx.coroutines.flow.Flow

class JyotishRepository(private val dao: JyotishDao) {
    val allReadingsFlow: Flow<List<SavedReading>> = dao.getAllReadingsFlow()
    val allChecklistItemsFlow: Flow<List<PoojaChecklistItem>> = dao.getAllChecklistItemsFlow()
    val allRecipesFlow: Flow<List<SavedRecipe>> = dao.getAllRecipesFlow()

    fun getChecklistForPoojaFlow(poojaName: String): Flow<List<PoojaChecklistItem>> {
        return dao.getChecklistForPoojaFlow(poojaName)
    }

    suspend fun insertReading(reading: SavedReading): Long {
        return dao.insertReading(reading)
    }

    suspend fun deleteReading(id: Int) {
        dao.deleteReading(id)
    }

    suspend fun insertChecklistItem(item: PoojaChecklistItem): Long {
        return dao.insertChecklistItem(item)
    }

    suspend fun updateChecklistItemStatus(id: Int, completed: Boolean) {
        dao.updateChecklistItemStatus(id, completed)
    }

    suspend fun clearChecklistForPooja(poojaName: String) {
        dao.clearChecklistForPooja(poojaName)
    }

    suspend fun deleteChecklistItem(id: Int) {
        dao.deleteChecklistItem(id)
    }

    suspend fun insertRecipe(recipe: SavedRecipe): Long {
        return dao.insertRecipe(recipe)
    }

    suspend fun deleteRecipe(id: Int) {
        dao.deleteRecipe(id)
    }
}
