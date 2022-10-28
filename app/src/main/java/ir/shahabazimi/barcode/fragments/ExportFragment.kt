package ir.shahabazimi.barcode.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.appbar.MaterialToolbar
import ir.shahabazimi.barcode.R
import ir.shahabazimi.barcode.databinding.FragmentExportBinding
import ir.shahabazimi.barcode.viewmodels.DataViewModel


class ExportFragment : Fragment() {

    private lateinit var binding: FragmentExportBinding
    private lateinit var dataViewModel: DataViewModel
    private val args: ExportFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentExportBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        initViews()
    }

    private fun init() {
        dataViewModel = ViewModelProvider(this)[DataViewModel::class.java]
        dataViewModel.data = args.data.data
    }

    private fun initViews() {
        requireActivity().findViewById<MaterialToolbar>(R.id.topAppBar).apply {
            title = getString(R.string.export_fragment_title)
            navigationIcon =
                ResourcesCompat.getDrawable(resources, R.drawable.icon_back, null)
            setNavigationOnClickListener {
                handleBackPress()
            }
            menu.findItem(R.id.appbar_menu_clear).isVisible = false
        }
    }

    private fun handleBackPress() = findNavController().popBackStack()

}