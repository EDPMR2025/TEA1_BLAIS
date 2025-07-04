package com.example.tea1_blais.model

import java.io.Serializable

data class ListeToDo(
    private var titreListe: String,
    private val lesItems: MutableList<ItemToDo> = mutableListOf()
) : Serializable {

    fun getTitreListe(): String = titreListe

    fun setTitreListe(titre: String) {
        titreListe = titre
    }

    fun getLesItems(): List<ItemToDo> = lesItems

    fun setLesItems(items: List<ItemToDo>) {
        lesItems.clear()
        lesItems.addAll(items)
    }

    fun ajouterItem(item: ItemToDo) {
        lesItems.add(item)
    }

    fun rechercherItem(descriptionItem: String): ItemToDo? {
        return lesItems.find { it.getDescription() == descriptionItem }
    }

    override fun toString(): String {
        return titreListe
    }
} 