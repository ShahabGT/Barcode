package ir.shahabazimi.barcode.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import ir.shahabazimi.barcode.R
import ir.shahabazimi.barcode.classes.AppDatabase
import ir.shahabazimi.barcode.classes.RecyclerItemAdapter
import ir.shahabazimi.barcode.classes.RecyclerItemModel
import ir.shahabazimi.barcode.databinding.FragmentHomeBinding
import ir.shahabazimi.barcode.viewmodels.ResultViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.util.*


class HomeFragment : Fragment() {

    private val resultViewModel: ResultViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding
    private val recyclerItemAdapter by lazy {
        RecyclerItemAdapter { item ->
            item?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    deleteItem(
                        it
                    )
                }
            }
        }
    }

    private lateinit var db: AppDatabase

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
        db = AppDatabase.getInstance(requireContext())
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
                        CoroutineScope(Dispatchers.IO).launch { clear() }
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
            CoroutineScope(Dispatchers.IO).launch {
                result.forEach {
                    db.userDao.insert(
                        RecyclerItemModel(
                            id = UUID.randomUUID().toString(),
                            weight = it
                        )
                    )
                }
            }
        }

    private fun readData() =
        db.userDao.getAll().observe(viewLifecycleOwner) {
            handleData(it)
        }

    private fun handleData(data: List<RecyclerItemModel>) {
        val sum = data.sumOf {
            try {
                it.weight?.toBigDecimal() ?: BigDecimal.ZERO

            } catch (e: NumberFormatException) {
                BigDecimal.ZERO
            }
        }
        recyclerItemAdapter.differ.submitList(data)
        binding.detailsFab.text = getString(R.string.details_fab_title, sum.toString())
        setEmptyView(data.size)
    }

    private fun clear() = db.userDao.clearTable()


    private fun deleteItem(item: RecyclerItemModel) = db.userDao.delete(item)

    private fun setEmptyView(count: Int) = with(binding) {
        emptyView.setVisibility(count == 0)
        detailsFab.setShow(count != 0)
        recycler.setVisibility(count != 0)
    }

    private fun View.setVisibility(show: Boolean) {
        visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

    private fun ExtendedFloatingActionButton.setShow(show: Boolean) {
        if (show) show() else hide()
    }

}