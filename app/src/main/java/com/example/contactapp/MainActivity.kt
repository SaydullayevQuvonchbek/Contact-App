package com.example.contactapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var allContacts: List<Contact> = emptyList()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            loadContacts()
        } else {
            Toast.makeText(this, "Kontaktlarni o'qish uchun ruxsat kerak", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.etSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0)
        binding.fabAdd.setImageResource(R.drawable.ic_add)

        binding.rvRecent.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvContacts.layoutManager = LinearLayoutManager(this)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
        }
    }

    private fun loadContacts() {
        lifecycleScope.launch {
            allContacts = ContactFetcher.fetchContacts(this@MainActivity)
            val adapter = ContactAdapter(allContacts) { contact ->
                openDetails(contact)
            }
            binding.rvContacts.adapter = adapter
            
            // For UI purposes, take random/first 5 as recent
            val recentContacts = allContacts.take(5).map { 
                it.copy(isRecent = true, recentTime = "Yaqinda") 
            }
            val recentAdapter = RecentAdapter(recentContacts) { contact ->
                openDetails(contact)
            }
            binding.rvRecent.adapter = recentAdapter
        }
    }

    private fun openDetails(contact: Contact) {
        val intent = Intent(this, ContactDetailsActivity::class.java).apply {
            putExtra("NAME", contact.name)
            putExtra("PHONE", contact.phone)
            putExtra("PHOTO_URI", contact.photoUrl)
        }
        startActivity(intent)
    }
}
