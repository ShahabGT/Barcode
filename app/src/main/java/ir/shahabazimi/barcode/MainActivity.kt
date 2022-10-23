package ir.shahabazimi.barcode

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import ir.shahabazimi.barcode.classes.RecyclerItemModel
import ir.shahabazimi.barcode.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() = with(binding) {
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.appbar_menu_clear -> {
                    lifecycleScope.launch { clear() }
                    true
                }
                else -> false
            }
        }

    }

    private suspend fun clear() {
        val config = RealmConfiguration.Builder(setOf(RecyclerItemModel::class))
            .build()
        Realm.open(config).apply {
            write {
                val items: RealmResults<RecyclerItemModel> = this.query<RecyclerItemModel>().find()
                delete(items)
            }
        }

    }
}