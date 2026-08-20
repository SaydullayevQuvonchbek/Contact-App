package com.example.contactapp

data class Contact(
    val id: Int,
    val name: String,
    val phone: String,
    val photoUrl: String? = null,
    val isRecent: Boolean = false,
    val recentTime: String? = null
) {
    val initials: String
        get() = name.split(" ").mapNotNull { it.firstOrNull()?.uppercase() }.take(2).joinToString("")
}
