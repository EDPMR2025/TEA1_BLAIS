package com.example.tea1_blais

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.tea1_blais.databinding.ActivityShowListBinding
import com.example.tea1_blais.model.ItemToDo
import com.example.tea1_blais.utils.PreferencesManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class ShowListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowListBinding
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var adapter: ArrayAdapter<ItemToDo>
    private var listeIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pseudo = intent.getStringExtra("EXTRA_PSEUDO") ?: return finish()
        listeIndex = intent.getIntExtra("EXTRA_LISTE_INDEX", -1)
        if (listeIndex == -1) return finish()

        preferencesManager = PreferencesManager(this)
        val profil = preferencesManager.getProfil(pseudo) ?: return finish()
        val liste = profil.getMesListeToDo().getOrNull(listeIndex) ?: return finish()

        title = liste.getTitreListe()

        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_multiple_choice,
            liste.getLesItems()
        )
        binding.listView.adapter = adapter
        binding.listView.choiceMode = android.widget.ListView.CHOICE_MODE_MULTIPLE

        liste.getLesItems().forEachIndexed { index, item ->
            binding.listView.setItemChecked(index, item.getFait())
        }

        binding.listView.setOnItemClickListener { _, _, position, _ ->
            val item = adapter.getItem(position) ?: return@setOnItemClickListener
            item.setFait(binding.listView.isItemChecked(position))
            preferencesManager.saveProfil(profil)
        }

        binding.fab.setOnClickListener {
            showAddItemDialog(liste, profil)
        }
    }

    private fun showAddItemDialog(liste: com.example.tea1_blais.model.ListeToDo, profil: com.example.tea1_blais.model.ProfilListeToDo) {
        val editText = TextInputEditText(this).apply {
            hint = getString(R.string.new_item)
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.new_item)
            .setView(editText)
            .setPositiveButton(R.string.ok) { _, _ ->
                val description = editText.text?.toString()
                if (!description.isNullOrBlank()) {
                    val nouvelItem = ItemToDo(description)
                    liste.ajouterItem(nouvelItem)
                    preferencesManager.saveProfil(profil)
                    adapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
} 