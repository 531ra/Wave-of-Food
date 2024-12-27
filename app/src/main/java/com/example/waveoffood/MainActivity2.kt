package com.example.waveoffood

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.waveoffood.databinding.ActivityMain2Binding
import com.example.waveoffood.fragments.CartFragment
import com.example.waveoffood.fragments.HistoryFragment
import com.example.waveoffood.fragments.HomeFragment
import com.example.waveoffood.fragments.ProfileFragment
import com.example.waveoffood.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding=ActivityMain2Binding.inflate(layoutInflater)
       setContentView(binding.root)
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,HomeFragment()).commit()
     binding.bottomnevigation.setOnNavigationItemSelectedListener { item->
         when(item.itemId){

             R.id.homeFragment->{   supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,HomeFragment()).commit()


                 true

             }
             R.id.cartFragment->{   supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,CartFragment()).commit()


                 true

             }   R.id.searchFragment->{   supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,SearchFragment()).commit()


             true

         }   R.id.historyFragment->{   supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,HistoryFragment()).commit()


             true

         }   R.id.profileFragment->{   supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,ProfileFragment()).commit()


             true

         }
             else-> false

         }

     }
        binding.notificationButton.setOnClickListener{
            val bottomSheetDialog=Notifiaction_Bottom_Fragment()
            bottomSheetDialog.show(supportFragmentManager,"test")

        }


    }
}