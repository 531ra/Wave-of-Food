package com.example.waveoffood

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import com.example.waveoffood.Model.UserModel
import com.example.waveoffood.databinding.ActivitySignMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.database
import com.razorpay.BuildConfig
import java.util.regex.Pattern


class SignMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database:DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var username:String
    private lateinit var Email:String
    private lateinit var Password:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding=ActivitySignMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textView10.setOnClickListener { startActivity(
            Intent(this,login::class.java)
        )
        finish()}
        //initialize firebase auth and database
        auth=FirebaseAuth.getInstance()
        database=Firebase.database.reference
        //initialise google
        val googleSignInOptions=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.Client_Id)).requestEmail().build()
        googleSignInClient= GoogleSignIn.getClient(this,googleSignInOptions)

        binding.CreateAccountbtn.setOnClickListener {
            username=binding.name.text.toString().trim()
            Email=binding.email.text.toString().trim()
            Password=binding.Password.text.toString().trim()
            createAccount()

        }
        binding.GoogleBtn.setOnClickListener{
            val signIntent=googleSignInClient.signInIntent
            launcher.launch(signIntent)

        }
    }
    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if(result.resultCode==Activity.RESULT_OK){
            val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                   val account:GoogleSignInAccount?=task.result
                val credential=GoogleAuthProvider.getCredential(account?.idToken,null)
                auth.signInWithCredential(credential).addOnCompleteListener { task->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Sign-in Successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this,MainActivity2::class.java))
                        finish()
                    }
                    else{
                        Toast.makeText(this, "Sign-in failed", Toast.LENGTH_SHORT).show()
                    }
                }


            }
        }else{
            Toast.makeText(this, "Sign-in  failed", Toast.LENGTH_SHORT).show()
        }

    }

    private fun createAccount() {
        if(username.isNotEmpty()||Email.isNotEmpty()||Password.isNotEmpty()){
            if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                Toast.makeText(this, "enter valid email pattern", Toast.LENGTH_SHORT).show()

            }else{
                auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener { task->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Successfully created account", Toast.LENGTH_SHORT).show()
                        SaveUserData()

                        startActivity(
                            Intent(this,login::class.java)
                        )
                        finish()


                    }else{
                        Toast.makeText(this, "Failed to create account", Toast.LENGTH_SHORT).show()
                    }

                }

            }

        }else{
            Toast.makeText(this, "please fill all the field", Toast.LENGTH_SHORT).show()
        }




    }

    private fun SaveUserData() {
        val user=UserModel(username,Email,Password)
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        database.child("Users").child(userId).setValue(user)



    }
}
