package com.example.tea1_blais.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.tea1_blais.model.ProfilListeToDo
import com.google.gson.Gson

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        PREFS_NAME,
        Context.MODE_PRIVATE
    )
    private val gson = Gson()

    companion object {
        private const val PREFS_NAME = "TodoListPrefs"
        private const val KEY_LAST_PSEUDO = "lastPseudo"
        private const val KEY_PSEUDO_HISTORY = "pseudoHistory"
        private const val KEY_PROFIL = "profil"
    }

    fun saveLastPseudo(pseudo: String) {
        sharedPreferences.edit().putString(KEY_LAST_PSEUDO, pseudo).apply()
        savePseudoToHistory(pseudo)
    }

    fun getLastPseudo(): String {
        return sharedPreferences.getString(KEY_LAST_PSEUDO, "") ?: ""
    }

    private fun savePseudoToHistory(pseudo: String) {
        val history = getPseudoHistory().toMutableSet()
        history.add(pseudo)
        sharedPreferences.edit().putStringSet(KEY_PSEUDO_HISTORY, history).apply()
    }

    fun getPseudoHistory(): Set<String> {
        return sharedPreferences.getStringSet(KEY_PSEUDO_HISTORY, setOf()) ?: setOf()
    }

    fun clearPseudoHistory() {
        sharedPreferences.edit().remove(KEY_PSEUDO_HISTORY).apply()
    }

    fun saveProfil(profil: ProfilListeToDo) {
        val json = gson.toJson(profil)
        sharedPreferences.edit().putString("${KEY_PROFIL}_${profil.getLogin()}", json).apply()
    }

    fun getProfil(login: String): ProfilListeToDo? {
        val json = sharedPreferences.getString("${KEY_PROFIL}_$login", null)
        return if (json != null) {
            gson.fromJson(json, ProfilListeToDo::class.java)
        } else {
            null
        }
    }
} 