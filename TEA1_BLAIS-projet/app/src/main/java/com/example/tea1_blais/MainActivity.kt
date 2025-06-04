package com.example.tea1_blais

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tea1_blais.databinding.ActivityMainBinding
import com.example.tea1_blais.utils.PreferencesManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferencesManager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preferencesManager = PreferencesManager(this)

        binding.pseudoEditText.setText(preferencesManager.getLastPseudo())

        val pseudoHistory = preferencesManager.getPseudoHistory().toList()
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, pseudoHistory)
        (binding.pseudoEditText as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.okButton.setOnClickListener {
            val pseudo = binding.pseudoEditText.text.toString()
            if (pseudo.isNotBlank()) {
                preferencesManager.saveLastPseudo(pseudo)
                val intent = Intent(this, ChoixListActivity::class.java)
                intent.putExtra("EXTRA_PSEUDO", pseudo)
                startActivity(intent)
            }
        }
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