package com.example.waveoffood

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.waveoffood.Model.UserModel
import com.example.waveoffood.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.firebase.BuildConfig
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    //auth is object for firebase authentication
    private lateinit var auth: FirebaseAuth
    //database object -> we get the reference to the firebase database
    private lateinit var database:DatabaseReference
    private lateinit var Email:String
    private lateinit var Password:String
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // when we click don't have account-> redirect it to signup activity
        binding.textView10.setOnClickListener {
            startActivity(Intent(this,SignMainActivity::class.java))
        }

        // initialize the database object and get the reference to the database
        database=Firebase.database.reference
        //initialise google signin
        val googleSignInOptions= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.Client_Id)).requestEmail().build()
        googleSignInClient=GoogleSignIn.getClient(this,googleSignInOptions)

            //login through google btn
        binding.Googlebtn.setOnClickListener{
            val signIntent=googleSignInClient.signInIntent
            launcher.launch(signIntent)

        }

        //initialize firebase auth
        auth=FirebaseAuth.getInstance()
        //login when click login btn
        binding.Login.setOnClickListener{
            //get credential
            Email=binding.Email.text.toString().trim()
            Password=binding.password.text.toString().trim()
            //check if any field is empty or not
            if(Email.isNotEmpty()||Password.isNotEmpty()){
                //here we check weather the email is in valid patter or not
                if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    Toast.makeText(this, "Invalid Email pattern", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener

                }else{
                   loginAccount(Email,Password)
                }

            }else{
                Toast.makeText(this, "Fill the field", Toast.LENGTH_SHORT).show()
            }
        }

    }
// login account is the function which do the signin process
    private fun loginAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task->
                if(task.isSuccessful){

                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()


                    startActivity(Intent(this,MainActivity2::class.java))
                    finish()
                }else{
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task->
                        if(task.isSuccessful){
                          saveData()
                            startActivity(Intent(this,MainActivity2::class.java))
                            finish()

                        }else{
                            Toast.makeText(this, "Login-Failed", Toast.LENGTH_SHORT).show()
                        }
                    }

                }
            }


    }
// these function is to save the user credential in the realtime database in User node
    private fun saveData() {
        // the data stored in the format of UserModel dataclass
        val user=UserModel(email=Email, password = Password)
    //here we get the unique uid for every user which login
        val userId=FirebaseAuth.getInstance().currentUser!!.uid
        database.child("Users").child(userId).setValue(user)

    }
    //create launcher for google sigin
    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        if(result.resultCode== Activity.RESULT_OK){
            val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
            if(task.isSuccessful){
                val account: GoogleSignInAccount?=task.result
                val credential= GoogleAuthProvider.getCredential(account?.idToken,null)
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

    //if user is already login then redirect it to the mainActivity

    override fun onStart() {
        super.onStart()
        val user=auth.currentUser
        if(user!=null){
            startActivity(Intent(this,MainActivity2::class.java))
            finish()
        }
    }

}