package com.c241.ps341.fomo.data.repository

import android.util.Log
import com.c241.ps341.fomo.data.UserData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserRepository {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun createUser(
        id: String,
        name: String,
        password: String,
        email: String,
        photoUrl: String
    ) {
        val user = UserData(name, password, email, photoUrl)
        database.child("user").child(id).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.i("Firebase", "User created")
            } else {
                Log.e("Firebase", "Error to create user")
            }
        }
    }
}