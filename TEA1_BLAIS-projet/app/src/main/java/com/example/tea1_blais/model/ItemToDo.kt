package com.example.tea1_blais.model

import java.io.Serializable

data class ItemToDo(
    private var description: String,
    private var fait: Boolean = false
) : Serializable {
    
    fun getDescription(): String = description
    
    fun setDescription(uneDescription: String) {
        description = uneDescription
    }
    
    fun getFait(): Boolean = fait
    
    fun setFait(fait: Boolean) {
        this.fait = fait
    }
    
    override fun toString(): String {
        return description
    }
} 