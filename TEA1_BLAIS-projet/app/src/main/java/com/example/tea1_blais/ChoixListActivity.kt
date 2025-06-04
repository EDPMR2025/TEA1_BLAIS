package com.example.tea1_blais

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.tea1_blais.databinding.ActivityChoixListBinding
import com.example.tea1_blais.model.ListeToDo
import com.example.tea1_blais.model.ProfilListeToDo
import com.example.tea1_blais.utils.PreferencesManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class ChoixListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChoixListBinding
    private lateinit var preferencesManager: PreferencesManager
    private lateinit var profil: ProfilListeToDo
    private lateinit var adapter: ArrayAdapter<ListeToDo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoixListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pseudo = intent.getStringExtra("EXTRA_PSEUDO") ?: return finish()
        
        preferencesManager = PreferencesManager(this)
        profil = preferencesManager.getProfil(pseudo) ?: ProfilListeToDo(pseudo)

        adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            profil.getMesListeToDo()
        )
        binding.listView.adapter = adapter

        binding.listView.setOnItemClickListener { _, _, position, _ ->
            val liste = adapter.getItem(position) ?: return@setOnItemClickListener
            val intent = Intent(this, ShowListActivity::class.java)
            intent.putExtra("EXTRA_PSEUDO", pseudo)
            intent.putExtra("EXTRA_LISTE_INDEX", position)
            startActivity(intent)
        }

        binding.fab.setOnClickListener {
            showAddListDialog()
        }
    }

    private fun showAddListDialog() {
        val editText = TextInputEditText(this).apply {
            hint = getString(R.string.new_list)
        }

        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.new_list)
            .setView(editText)
            .setPositiveButton(R.string.ok) { _, _ ->
                val titre = editText.text?.toString()
                if (!titre.isNullOrBlank()) {
                    val nouvelleListe = ListeToDo(titre)
                    profil.ajouteListe(nouvelleListe)
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
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
} 