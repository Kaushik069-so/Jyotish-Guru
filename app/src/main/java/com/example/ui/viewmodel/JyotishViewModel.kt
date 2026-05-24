package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.BuildConfig
import com.example.data.api.*
import com.example.data.database.*
import com.example.data.repository.JyotishRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface LlmState {
    object Idle : LlmState
    object Loading : LlmState
    data class Success(val text: String) : LlmState
    data class Error(val message: String) : LlmState
}

class JyotishViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val repository = JyotishRepository(database.jyotishDao())

    // UI Navigation Tab
    private val _currentTab = MutableStateFlow("pooja")
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    fun selectTab(tab: String) {
        _currentTab.value = tab
    }

    // --- Database flows ---
    val savedReadings: StateFlow<List<SavedReading>> = repository.allReadingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val savedRecipes: StateFlow<List<SavedRecipe>> = repository.allRecipesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val poojaChecklist: StateFlow<List<PoojaChecklistItem>> = repository.allChecklistItemsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- State for active generation per tab ---
    private val _poojaState = MutableStateFlow<LlmState>(LlmState.Idle)
    val poojaState: StateFlow<LlmState> = _poojaState.asStateFlow()

    private val _recipeState = MutableStateFlow<LlmState>(LlmState.Idle)
    val recipeState: StateFlow<LlmState> = _recipeState.asStateFlow()

    private val _panchangState = MutableStateFlow<LlmState>(LlmState.Idle)
    val panchangState: StateFlow<LlmState> = _panchangState.asStateFlow()

    private val _palmistryState = MutableStateFlow<LlmState>(LlmState.Idle)
    val palmistryState: StateFlow<LlmState> = _palmistryState.asStateFlow()

    private val _chatState = MutableStateFlow<LlmState>(LlmState.Idle)
    val chatState: StateFlow<LlmState> = _chatState.asStateFlow()

    private val _appLanguage = MutableStateFlow("en")
    val appLanguage: StateFlow<String> = _appLanguage.asStateFlow()

    fun updateLanguage(lang: String) {
        _appLanguage.value = lang
    }

    // Key warning trigger
    val isApiKeyMissing: Boolean = BuildConfig.GEMINI_API_KEY.isEmpty() || BuildConfig.GEMINI_API_KEY == "MY_GEMINI_API_KEY"

    // System instructions for JyotishGuru
    private val baseSystemInstruction = """
        You are "JyotishGuru" — an expert AI assistant specializing in Hindu astrology, spirituality, rituals, and Vedic traditions. You are highly knowledgeable, deeply respectful, and rooted in Hindu culture and scriptures.
        
        Key formatting rules:
        - Response style: Warm, respectful, use "Jai Shri Ram" or "Jai Mata Di" where appropriate.
        - Structure responses with clear headings, clean spacing, and bullet points.
        - Add matching emojis (🪔🌸🙏✋📿🍛📅) throughout.
        - Never disrespect any deity, tradition, or regional custom.
        - Always append a subtle disclaimer: "Note: Astrological readings and rituals are for spiritual guidance only."
    """.trimIndent()

    private fun getLanguageInstruction(lang: String): String {
        return when (lang) {
            "hi" -> "REQUIRED: You MUST write your entire response (including all headings, list items, description, procedures and disclaimers) inside the specified markdown sections in Hindi only (हिंदी में जवाब दें)."
            "te" -> "REQUIRED: You MUST write your entire response (including all headings, list items, description, procedures and disclaimers) inside the specified markdown sections in Telugu only (తెలుగులో సమాధానం ఇవ్వండి)."
            "ta" -> "REQUIRED: You MUST write your entire response (including all headings, list items, description, procedures and disclaimers) inside the specified markdown sections in Tamil only (தமிழில் பதில் சொல்லுங்கள்)."
            "kn" -> "REQUIRED: You MUST write your entire response (including all headings, list items, description, procedures and disclaimers) inside the specified markdown sections in Kannada only (ಕನ್ನಡದಲ್ಲಿ ಉತ್ತರ ನೀಡಿ)."
            "mr" -> "REQUIRED: You MUST write your entire response (including all headings, list items, description, procedures and disclaimers) inside the specified markdown sections in Marathi only (मराठीत उत्तर द्या)."
            "bn" -> "REQUIRED: You MUST write your entire response (including all headings, list items, description, procedures and disclaimers) inside the specified markdown sections in Bengali only (বাংলায় উত্তর দিন)।"
            "gu" -> "REQUIRED: You MUST write your entire response (including all headings, list items, description, procedures and disclaimers) inside the specified markdown sections in Gujarati only (ગુજરાતીમાં જવાબ આપો)."
            else -> "REQUIRED: You MUST respond in English."
        }
    }

    // Call Gemini API
    private suspend fun callGemini(prompt: String, systemPrompt: String, inlineData: InlineData? = null): String {
        if (isApiKeyMissing) {
            return "apiKeyMissing"
        }
        val lang = _appLanguage.value
        val langInstruction = getLanguageInstruction(lang)
        val fullSystemInstruction = "$baseSystemInstruction\nLanguage Requirement: $langInstruction\n$systemPrompt"

        val parts = mutableListOf<Part>()
        parts.add(Part(text = prompt))
        if (inlineData != null) {
            parts.add(Part(inlineData = inlineData))
        }

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = parts)),
            systemInstruction = Content(parts = listOf(Part(text = fullSystemInstruction))),
            generationConfig = GenerationConfig(temperature = 0.7f)
        )
        return try {
            val response = RetrofitClient.service.generateContent(BuildConfig.GEMINI_API_KEY, request)
            response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No guidance generated by JyotishGuru."
        } catch (e: Exception) {
            "Ganesha of technical hurdles was encountered: ${e.message ?: "Unknown Connection Issue"}"
        }
    }

    // --- Action Methods ---

    fun generatePoojaGuide(poojaName: String) {
        _poojaState.value = LlmState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val system = """
                You are giving a Hindu Pooja Guide. Provide:
                1. Spiritual significance of the occasion
                2. Exact list of pooja samagri (materials needed) with quantities under a clear heading "### Pooja Samagri List" using list bullets like "- 10g Camphor" or "- Haldi (Turmeric) (1 pack)".
                3. Step-by-step pooja vidhi (procedure)
                4. Key Mantras to chant (Sanskrit + transliteration + meaning)
                5. Do's and Don'ts (niyam and parhej)
                Ensure Samagri is listed clearly with quantities as bullet points so the user can parse them.
            """.trimIndent()
            val result = callGemini("Auspicious guide and steps for $poojaName pooja rituals", system)
            if (result == "apiKeyMissing") {
                _poojaState.value = LlmState.Error("Gemini API Key is missing. Please configure it in the AI Studio Secrets panel.")
            } else if (result.startsWith("Ganesha of technical hurdles")) {
                _poojaState.value = LlmState.Error(result)
            } else {
                _poojaState.value = LlmState.Success(result)
            }
        }
    }

    fun generatePrasadRecipe(recipeOccasion: String) {
        _recipeState.value = LlmState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val system = """
                Provide Prasad suggestions and sacred cooking details for: $recipeOccasion
                Format:
                1. Name of Prasad/Dish and deity it is offered to (bhog)
                2. Full visual ingredients list
                3. Satvik preparation method (no onion, no garlic)
                4. Special vrat (fasting) rules or alternative recipes if applicable
                5. Bhog thali recommendations
            """.trimIndent()
            val result = callGemini("Explain cooking, prasad, and bhog for: $recipeOccasion", system)
            if (result == "apiKeyMissing") {
                _recipeState.value = LlmState.Error("Gemini API Key is missing. Please configure it in the AI Studio Secrets panel.")
            } else if (result.startsWith("Ganesha of technical hurdles")) {
                _recipeState.value = LlmState.Error(result)
            } else {
                _recipeState.value = LlmState.Success(result)
            }
        }
    }

    fun generatePanchangInfo(query: String) {
        _panchangState.value = LlmState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val system = """
                Provide calendar or Panchang insight.
                Explain tithi, var, nakshatra, yoga, karan, or shubh muhurat based on the query.
                Help the user plan dates, understand upcoming vrats/festivals of the current calendar year.
                Provide clear, warm explanations.
            """.trimIndent()
            val result = callGemini(query, system)
            if (result == "apiKeyMissing") {
                _panchangState.value = LlmState.Error("Gemini API Key is missing. Please configure it in the AI Studio Secrets panel.")
            } else if (result.startsWith("Ganesha of technical hurdles")) {
                _panchangState.value = LlmState.Error(result)
            } else {
                _panchangState.value = LlmState.Success(result)
            }
        }
    }

    fun generatePalmistryReading(palmDetails: String, base64Image: String? = null) {
        _palmistryState.value = LlmState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val system = """
                You are an expert, empathetic, and culturally insightful Palmistry (Chiromancy) and Vedic Hasta Samudrika Shastra AI. Your purpose is to analyze images or descriptions of a user's palm and provide a detailed, engaging reading.

                When a user submits an image of their palm or a description, analyze it systematically based on traditional palmistry principles:
                1. Major Lines: Analyze the Heart Line (emotions, relationships, devotion), Head Line (intellect, focus, mental willpower), and Life Line (vitality, life path, strength).
                2. Minor Lines (if visible): Look for the Fate Line (career, destiny, karmic path) and Apollo/Sun Line (success, fame, artistic spirit).
                3. Hand Topography: Assess the mounts (such as Mount of Venus, Jupiter, Saturn, Mercury, Sun/Apollo) and overall hand shape if visible in the image or description.

                Response Guidelines:
                - Structure the analysis with clear markdown headings (e.g. "### ❤️ Heart Line", "### 🧠 Head Line", "### 🌱 Life Line", "### 🌀 Minor Lines", "### ⛰️ Hand Topography") for each segment.
                - Provide a summary of overall personality traits and potential life paths.
                - Maintain an encouraging, balanced, and insightful tone. Avoid dark, definitive, or fatalistic predictions. Always emphasizes that karma and positive actions can redesign these lines over time.
                - You MUST include this standard disclaimer at the very end of your response exactly as written:
                  "Palmistry readings are for entertainment and self-reflection purposes only."
            """.trimIndent()

            val inlineData = if (!base64Image.isNullOrBlank()) {
                InlineData(mimeType = "image/jpeg", data = base64Image)
            } else {
                null
            }

            val result = callGemini(palmDetails, system, inlineData)
            if (result == "apiKeyMissing") {
                _palmistryState.value = LlmState.Error("Gemini API Key is missing. Please configure it in the AI Studio Secrets panel.")
            } else if (result.startsWith("Ganesha of technical hurdles")) {
                _palmistryState.value = LlmState.Error(result)
            } else {
                _palmistryState.value = LlmState.Success(result)
                saveCurrentReading(
                    category = "Palmistry",
                    title = "Palmistry Deconstruction",
                    query = palmDetails,
                    result = result
                )
            }
        }
    }

    fun generateChatResponse(query: String) {
        _chatState.value = LlmState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val lang = _appLanguage.value
            val langInstructions = when (lang) {
                "hi" -> "हिंदी में जवाब दें।"
                "te" -> "తెలుగులో సమాధానం ఇవ్వండి."
                "ta" -> "தமிழில் பதில் சொல்லுங்கள்."
                "kn" -> "ಕನ್ನಡದಲ್ಲಿ ಉತ್ತರ ನೀಡಿ."
                "mr" -> "मराठीत उत्तर द्या."
                "bn" -> "বাংলায় ಉತ್ತರ দিন।"
                "gu" -> "ગુજરાતીમાં જવાબ આપો."
                else -> "Respond in English."
            }
            val system = """
                You are JyotishGuru (ज्योतिषगुरु), an expert AI assistant specializing in Hindu astrology, spirituality, Vedic wisdom, panchang, festivals, pooja vidhi, and palmistry.

                Your personality:
                - Warm, respectful, and spiritually uplifting
                - Begin responses with "🙏" or a relevant Sanskrit shloka when appropriate
                - Use terms like "Shubh", "Mangal", "Jai Shri Ram" naturally
                - Provide accurate information about Hindu traditions, rituals, and festivals
                - Give practical, actionable guidance for poojas and vrats
                - When discussing astrology, be encouraging but honest
                - Always end with a blessing or positive affirmation

                Topics you excel at:
                - Daily panchang (tithi, nakshatra, yoga, karan, rahu kaal, muhurat)
                - Hindu festivals and their significance
                - Pooja vidhi (step-by-step rituals)
                - Mantras and their meanings
                - Palmistry (Hasta Rekha Shastra)
                - Vedic astrology and horoscopes
                - Fasting (vrat) rules and recipes
                - Sacred recipes and prasad preparation
                - Hindu mythology and stories

                Language instruction: $langInstructions

                Keep responses concise but complete. Use emojis for spiritual context. Format with line breaks and markdown headings/bullet points (### for sections, - for list elements) for readability.
            """.trimIndent()
            val result = callGemini(query, system)
            if (result == "apiKeyMissing") {
                _chatState.value = LlmState.Error("Gemini API Key is missing. Please configure it in the AI Studio Secrets panel.")
            } else if (result.startsWith("Ganesha of technical hurdles")) {
                _chatState.value = LlmState.Error(result)
            } else {
                _chatState.value = LlmState.Success(result)
            }
        }
    }

    fun resetState(tab: String) {
        when (tab) {
            "pooja" -> _poojaState.value = LlmState.Idle
            "prasad" -> _recipeState.value = LlmState.Idle
            "panchang" -> _panchangState.value = LlmState.Idle
            "palmistry" -> _palmistryState.value = LlmState.Idle
            "chat" -> _chatState.value = LlmState.Idle
        }
    }

    // --- Saved Readings (Astrology Logs) ---

    fun saveCurrentReading(category: String, title: String, query: String, result: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val reading = SavedReading(
                category = category,
                title = title,
                queryText = query,
                resultText = result
            )
            repository.insertReading(reading)
        }
    }

    fun deleteSavedReading(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteReading(id)
        }
    }

    // --- Prasad Recipes Storage ---

    fun saveCustomRecipe(recipeName: String, occasion: String, ingredients: String, instructions: String, category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipe = SavedRecipe(
                recipeName = recipeName,
                occasion = occasion,
                ingredients = ingredients,
                instructions = instructions,
                category = category
            )
            repository.insertRecipe(recipe)
        }
    }

    fun deleteSavedRecipe(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteRecipe(id)
        }
    }

    // --- Smart Pooja Checklist Conversion ---

    fun parseAndLoadPoojaChecklist(poojaName: String, text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // First, clear existing checklist for this pooja to make it clean
            repository.clearChecklistForPooja(poojaName)

            // Look for bullet lists under "samagri" or simply scan lines
            val lines = text.lines()
            var isSamagriSection = false
            var addedCount = 0

            for (line in lines) {
                val cleanLine = line.trim()
                if (cleanLine.contains("samagri", ignoreCase = true) || cleanLine.contains("material", ignoreCase = true)) {
                    isSamagriSection = true
                    continue
                }

                // If we see another heading or empty lines after some items, we keep scanning but let's be flexible
                if (isSamagriSection && cleanLine.startsWith("###")) {
                    // Ended section if it is another main heading, but let's double check if we added enough
                    if (addedCount > 2) {
                        isSamagriSection = false
                    }
                }

                if (cleanLine.startsWith("-") || cleanLine.startsWith("*") || (cleanLine.getOrNull(0)?.isDigit() == true && cleanLine.contains("."))) {
                    val itemText = cleanLine.substring(1).trim().removePrefix(".").trim()
                    if (itemText.isNotEmpty() && itemText.length < 100) {
                        // Split quantity if it has something like "(50g)" or "50 units" or "-" / ":"
                        var name = itemText
                        var qty = "As needed"
                        if (itemText.contains("(")) {
                            val parts = itemText.split("(")
                            name = parts[0].trim()
                            qty = parts.getOrNull(1)?.replace(")", "")?.trim() ?: "As needed"
                        } else if (itemText.contains("-")) {
                            val parts = itemText.split("-")
                            name = parts[0].trim()
                            qty = parts.getOrNull(1)?.trim() ?: "As needed"
                        } else if (itemText.contains(":")) {
                            val parts = itemText.split(":")
                            name = parts[0].trim()
                            qty = parts.getOrNull(1)?.trim() ?: "As needed"
                        }

                        repository.insertChecklistItem(
                            PoojaChecklistItem(
                                poojaName = poojaName,
                                itemName = name,
                                quantity = qty
                            )
                        )
                        addedCount++
                    }
                }
            }

            // Fallback: If parsing found nothing (due to dynamic format), add default traditional ones
            if (addedCount == 0) {
                 val traditionalItems = listOf(
                     Pair("Akshat (Sacred Rice)", "1 small cup"),
                     Pair("Kumkum & Roli", "1 packet each"),
                     Pair("Diya & Ghee", "1 set"),
                     Pair("Incense Sticks (Agarbatti)", "1 packet"),
                     Pair("Fresh Flowers / Garlands", "As needed"),
                     Pair("Coconut (Nariyal)", "1 unit"),
                     Pair("Sweets for Bhog (Prasad)", "As needed"),
                     Pair("Holy Water (Ganga Jal)", "1 bottle"),
                     Pair("Pooja Thali & Bell", "1 set")
                 )
                for (item in traditionalItems) {
                    repository.insertChecklistItem(
                        PoojaChecklistItem(
                            poojaName = poojaName,
                            itemName = item.first,
                            quantity = item.second
                        )
                    )
                }
            }
        }
    }

    fun addCustomChecklistItem(poojaName: String, itemName: String, quantity: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertChecklistItem(
                PoojaChecklistItem(
                    poojaName = poojaName,
                    itemName = itemName,
                    quantity = quantity
                )
            )
        }
    }

    fun toggleChecklistItem(id: Int, isCompleted: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateChecklistItemStatus(id, isCompleted)
        }
    }

    fun deleteChecklistItem(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteChecklistItem(id)
        }
    }

    fun clearPoojaChecklist(poojaName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.clearChecklistForPooja(poojaName)
        }
    }
}
