package ir.shahabazimi.barcode

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import io.realm.kotlin.Realm
import ir.shahabazimi.barcode.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init()= with(binding){
        topAppBar.setOnMenuItemClickListener { menuItem->
            when(menuItem.itemId){
                R.id.appbar_menu_clear->{
                    //todo Implement This
                    true
                }
                else->false
            }
        }

    }
}