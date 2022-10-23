package ir.shahabazimi.barcode.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import ir.shahabazimi.barcode.R
import ir.shahabazimi.barcode.databinding.FragmentHomeBinding
import ir.shahabazimi.barcode.viewmodels.ResultViewModel


class HomeFragment : Fragment() {

    private val resultViewModel: ResultViewModel by activityViewModels()
    private lateinit var binding: FragmentHomeBinding


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
    }

    private fun handleBackPress() = activity?.finishAndRemoveTask()

    private fun observeResult() {
        resultViewModel.result.observe(viewLifecycleOwner) { result ->
            Toast.makeText(requireContext(), result.joinToString(), Toast.LENGTH_SHORT).show()
            //todo handle this
        }
    }

}