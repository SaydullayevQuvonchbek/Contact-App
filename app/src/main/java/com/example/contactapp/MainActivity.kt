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
import com.example.contactapp.data.AppDatabase
import com.example.contactapp.data.ContactFetcher
import com.example.contactapp.data.RecentContactEntity
import com.example.contactapp.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase

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

        db = AppDatabase.getDatabase(this)

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

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            loadRecentContacts()
        }
    }

    private fun loadContacts() {
        lifecycleScope.launch {
            val contacts = ContactFetcher.fetchContacts(this@MainActivity)
            val adapter = ContactAdapter(contacts) { contact ->
                openDetails(contact)
            }
            binding.rvContacts.adapter = adapter
            
            // Also load recents
            loadRecentContacts()
        }
    }

    private fun loadRecentContacts() {
        lifecycleScope.launch {
            val recentEntities = db.recentContactDao().getRecentContacts()
            val recentContacts = recentEntities.map { 
                Contact(it.id, it.name, it.phone, it.photoUrl, isRecent = true, recentTime = "Yaqinda") 
            }
            val adapter = RecentAdapter(recentContacts) { contact ->
                openDetails(contact)
            }
            binding.rvRecent.adapter = adapter
        }
    }

    private fun openDetails(contact: Contact) {
        // Save to recent
        lifecycleScope.launch {
            db.recentContactDao().insertRecentContact(
                RecentContactEntity(contact.id, contact.name, contact.phone, contact.photoUrl, System.currentTimeMillis())
            )
        }

        val intent = Intent(this, ContactDetailsActivity::class.java).apply {
            putExtra("NAME", contact.name)
            putExtra("PHONE", contact.phone)
            putExtra("PHOTO_URI", contact.photoUrl)
        }
        startActivity(intent)
    }
}
