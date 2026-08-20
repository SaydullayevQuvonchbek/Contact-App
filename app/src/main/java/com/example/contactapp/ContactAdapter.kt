package com.example.contactapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.contactapp.databinding.ItemContactBinding

class ContactAdapter(
    private val contacts: List<Contact>,
    private val onClick: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(private val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact) {
            binding.tvContactName.text = contact.name
            binding.tvContactPhone.text = contact.phone
            
            binding.tvInitials.text = contact.initials
            binding.tvInitials.visibility = View.VISIBLE
            binding.ivContactPhoto.visibility = View.GONE
            
            // Set dynamic background color based on name length for variation
            val colors = listOf(R.color.colorPrimary, R.color.iconGreen, R.color.iconRed, R.color.iconPurple, R.color.iconCyan)
            val colorRes = colors[contact.name.length % colors.size]
            binding.cvContactPhoto.setCardBackgroundColor(binding.root.context.getColor(colorRes))
            
            binding.root.setOnClickListener { onClick(contact) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            ItemContactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    override fun getItemCount(): Int = contacts.size
}
