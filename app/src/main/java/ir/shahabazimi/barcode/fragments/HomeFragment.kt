package ir.shahabazimi.barcode.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.RealmResults
import ir.shahabazimi.barcode.R
import ir.shahabazimi.barcode.classes.RecyclerItemAdapter
import ir.shahabazimi.barcode.classes.RecyclerItemModel
import ir.shahabazimi.barcode.databinding.FragmentHomeBinding
import ir.shahabazimi.barcode.viewmodels.ResultViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private val resultViewModel: ResultViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private lateinit var config: RealmConfiguration
    private lateinit var realm: Realm
    private val recyclerItemAdapter by lazy{ RecyclerItemAdapter() }


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
        }
        recycler.adapter = recyclerItemAdapter

        recyclerItemAdapter.differ.submitList(
            realm.query<RecyclerItemModel>().find()
        )
    }

    private fun handleBackPress() = activity?.finishAndRemoveTask()

    private fun observeResult() {
        resultViewModel.result.observe(viewLifecycleOwner) { result ->
            lifecycleScope.launch {
                realm.write {
                    result.forEach { barcode ->
                        this.copyToRealm(
                            RecyclerItemModel().apply {
                                weight = barcode
                            }
                        )
                    }
                }
            }
        }
    }

}