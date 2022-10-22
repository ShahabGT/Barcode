package ir.shahabazimi.barcode.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
    }

    private fun initViews() = with(binding) {

    }

    private fun observeResult() {
        resultViewModel.result.observe(viewLifecycleOwner) { result ->
            //todo handle this
        }
    }

}