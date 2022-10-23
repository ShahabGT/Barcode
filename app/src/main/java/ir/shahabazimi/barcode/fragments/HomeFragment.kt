package ir.shahabazimi.barcode.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.notifications.InitialResults
import io.realm.kotlin.notifications.ResultsChange
import io.realm.kotlin.notifications.UpdatedResults
import io.realm.kotlin.query.RealmResults
import ir.shahabazimi.barcode.R
import ir.shahabazimi.barcode.classes.RecyclerItemAdapter
import ir.shahabazimi.barcode.classes.RecyclerItemModel
import ir.shahabazimi.barcode.databinding.FragmentHomeBinding
import ir.shahabazimi.barcode.utils.Consts.REALM_DB_NAME
import ir.shahabazimi.barcode.viewmodels.ResultViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private val resultViewModel: ResultViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var config: RealmConfiguration
    private lateinit var realm: Realm
    private lateinit var job: Job
    private val recyclerItemAdapter by lazy {
        RecyclerItemAdapter { item -> item?.let { deleteItem(it) } }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initViews()
    }

    private fun init() {
        observeResult()
        requireActivity().onBackPressedDispatcher.addCallback {
            handleBackPress()
        }
        config = RealmConfiguration.Builder(setOf(RecyclerItemModel::class))
            .name(REALM_DB_NAME)
            .build()
        realm = Realm.open(config)

    }

    private fun initViews() = with(binding) {
        scanButton.setOnClickListener {
            findNavController().navigate(R.id.action_to_scanBarcodeFragment)
        }
        requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar).apply {
            title = getString(R.string.home_fragment_title)
            navigationIcon = null
            setNavigationOnClickListener {
                handleBackPress()
            }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.appbar_menu_clear -> {
                        clear()
                        true
                    }
                    else -> false
                }
            }
            menu.findItem(R.id.appbar_menu_clear).isVisible = true

        }
        recycler.adapter = recyclerItemAdapter
        readData()
    }

    private fun handleBackPress() = activity?.finishAndRemoveTask()

    private fun observeResult() =
        resultViewModel.result.observe(viewLifecycleOwner) { result ->
            realm.writeBlocking {
                result.forEach { barcode ->
                    this.copyToRealm(
                        RecyclerItemModel().apply {
                            weight = barcode
                        }
                    )
                }
            }
        }

    private fun readData() {
        val characters = realm.query(RecyclerItemModel::class)
        job = lifecycleScope.launch {
            val charactersFlow = characters.asFlow()
            charactersFlow.collect { changes: ResultsChange<RecyclerItemModel> ->
                when (changes) {
                    is UpdatedResults -> {
                        Log.d("SHAHABLU", "readData:UpdatedResults:${changes.list.size} ")
                        handleData(changes.list.toList())
                    }
                    is InitialResults -> {
                        Log.d("SHAHABLU", "readData:InitialResults:${changes.list.size} ")
                        handleData(changes.list.toList())
                    }
                    else -> {}
                }
            }
        }

    }

    private fun handleData(data: List<RecyclerItemModel>) {
        val sum = data.sumOf { it.weight.toLong() }
        recyclerItemAdapter.differ.submitList(data)
        recyclerItemAdapter.notifyDataSetChanged()
        binding.detailsFab.text =
            getString(R.string.details_fab_title, sum.toString())
        setEmptyView(data.size)
    }

    private fun clear() {
        realm.apply {
            writeBlocking {
                val query: RealmResults<RecyclerItemModel> = this.query<RecyclerItemModel>().find()
                delete(query)
            }
        }
    }

    private fun deleteItem(item: RecyclerItemModel) {
        realm.apply {
            writeBlocking {
                val query = this.query<RecyclerItemModel>("id == $0", item.id).find().firstOrNull()
                query?.let { delete(it) }
            }
        }
    }

    private fun setEmptyView(count: Int) = with(binding) {
        emptyView.setVisibility(count == 0)
        detailsFab.setShow(count != 0)
        recycler.setVisibility(count != 0)
    }

    private fun View.setVisibility(show: Boolean) {
        if (show) visibility = View.VISIBLE else View.INVISIBLE
    }

    private fun ExtendedFloatingActionButton.setShow(show: Boolean) {
        if (show) show() else hide()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

}