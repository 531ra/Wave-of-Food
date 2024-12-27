package com.example.waveoffood.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.waveoffood.Model.UserModel
import com.example.waveoffood.R
import com.example.waveoffood.databinding.FragmentProfileBinding
import com.example.waveoffood.login
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
private lateinit var auth: FirebaseAuth
private  val database=FirebaseDatabase.getInstance()
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding=FragmentProfileBinding.inflate(inflater,container,false)
        auth=FirebaseAuth.getInstance()
        setUserData()
        binding.apply {
            name.isEnabled=false
            email.isEnabled=false
            address.isEnabled=false
            phone.isEnabled=false}
        binding.editbtn.setOnClickListener{
            binding.apply {
                name.isEnabled=!name.isEnabled
                address.isEnabled=!address.isEnabled
                email.isEnabled=!email.isEnabled
                phone.isEnabled=!phone.isEnabled
            }
        }
        binding.SaveInformation.setOnClickListener{
            val name=binding.name.text.toString().trim()
            val address=binding.address.text.toString().trim()
            val email=binding.email.text.toString().trim()
            val phone=binding.phone.text.toString().trim()
            UpdateUserData(name,email,address,phone)
        }

        //logout btn
        binding.logout.setOnClickListener{

                auth.signOut()
                val googleSignInOptions= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.Client_Id)).requestEmail().build()
                googleSignInClient= GoogleSignIn.getClient(requireContext(),googleSignInOptions)
                googleSignInClient.signOut().addOnCompleteListener {
                    // Once sign out is complete, redirect to login activity
                 startActivity(Intent(requireContext(),login::class.java))
                    activity?.finish()


                }
            }



        return binding.root
    }

    private fun UpdateUserData(name: String, email: String, address: String, phone: String) {
        val userId=auth.currentUser?.uid
        if(userId!=null){
            val userReference=database.getReference("Users").child(userId)
            val userData= hashMapOf("name" to name,"email" to email,"address" to address, "phone" to phone)
            userReference.setValue(userData).addOnSuccessListener{ task->
                Toast.makeText(requireContext(), "Profile Update Successfully", Toast.LENGTH_SHORT).show()
        }
                .addOnFailureListener{
                    Toast.makeText(requireContext(), "Profile update failed", Toast.LENGTH_SHORT).show()
                    
                }


    }}

    private fun setUserData() {
        val userId=auth.currentUser?.uid?:""
        if(userId!=null){
            val userReference:DatabaseReference=database.getReference("Users").child(userId)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val userProfile=snapshot.getValue(UserModel::class.java)
                        if(userProfile!=null){
                            binding.name.setText(userProfile.name)
                            binding.address.setText(userProfile.address)
                            binding.email.setText(userProfile.email)
                            binding.phone.setText(userProfile.phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

    }

    companion object {

    }
}