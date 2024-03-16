package com.example.updatefirebase

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.updatefirebase.databinding.ActivityMainBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.reflect.jvm.internal.impl.resolve.constants.KClassValue.Value

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var dbRef: DatabaseReference
    var id: String?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnSubmit.setOnClickListener {
            dbRef= FirebaseDatabase.getInstance().getReference("User")
//            id=dbRef.push().key
            val user = User(binding.fname.text.toString(),binding.lname.text.toString(),binding.city.text.toString(),binding.uname.text.toString())
            dbRef.child(binding.uname.text.toString()).setValue(user)
        }

        binding.btnUpdate.setOnClickListener{
            dbRef= FirebaseDatabase.getInstance().getReference("User")
            dbRef.orderByChild("userName").equalTo(binding.uname.text.toString()).addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
//            dbRef.addValueEventListener(object : ValueEventListener{
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if(snapshot.exists()){
//                        for(entrySnap in snapshot.children){
//                            val entry = entrySnap.getValue()
//                        }
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//                    TODO("Not yet implemented")
//                }
//
//            })
            val userRef= dbRef.child(binding.uname.text.toString())
            var user = User(firstName = binding.fname.text.toString(), lastName = binding.lname.text.toString(), city = binding.city.text.toString(), userName = binding.uname.text.toString())
            userRef.child("firstName").setValue(binding.fname.text.toString())
            userRef.child("lastName").setValue(binding.lname.text.toString())
            userRef.child("city").setValue(binding.city.text.toString())
                .addOnSuccessListener {
                    Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "not odne", Toast.LENGTH_SHORT).show()
                }
        }
        binding.btnFetch.setOnClickListener {
            dbRef= FirebaseDatabase.getInstance().reference
            val userRef= dbRef.child("User").child(binding.uname.text.toString())
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val userData = snapshot.value as Map<String, Any>
                        val firstName = userData["firstName"] as String
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}