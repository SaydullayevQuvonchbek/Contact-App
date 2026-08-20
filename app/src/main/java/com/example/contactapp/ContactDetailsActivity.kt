package com.example.contactapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.contactapp.databinding.ActivityContactDetailsBinding
import com.bumptech.glide.Glide
import android.view.View

class ContactDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContactDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("NAME") ?: "Unknown"
        val phone = intent.getStringExtra("PHONE") ?: "Unknown"
        val photoUri = intent.getStringExtra("PHOTO_URI")

        binding.tvDetailName.text = name
        binding.tvDetailPhone.text = phone
        binding.tvMobileValue.text = phone

        binding.ivBack.setOnClickListener { finish() }
        binding.ivBack.setImageResource(R.drawable.ic_back)
        binding.ivEdit.setImageResource(R.drawable.ic_edit)
        
        // Load photo if available
        if (photoUri != null) {
            Glide.with(this).load(photoUri).into(binding.ivDetailPhoto)
        } else {
            binding.ivDetailPhoto.visibility = View.GONE
            // Set some default color based on name length
            val colors = listOf(R.color.colorPrimary, R.color.iconGreen, R.color.iconRed, R.color.iconPurple, R.color.iconCyan)
            val colorRes = colors[name.length % colors.size]
            binding.cvMainPhoto.setCardBackgroundColor(getColor(colorRes))
        }

        // Action buttons
        binding.btnChat.setOnClickListener { sendMessage(phone) }
        binding.btnCall.setOnClickListener { dialNumber(phone) }
    }
    
    fun dialNumber(phone: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phone")
        }
        startActivity(intent)
    }
    
    fun sendMessage(phone: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$phone")
        }
        startActivity(intent)
    }
}
