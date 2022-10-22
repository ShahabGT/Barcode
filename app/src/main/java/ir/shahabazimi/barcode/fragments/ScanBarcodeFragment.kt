package ir.shahabazimi.barcode.fragments

import android.Manifest.permission.CAMERA
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import ir.shahabazimi.barcode.R
import ir.shahabazimi.barcode.databinding.FragmentScanBarcodeBinding
import ir.shahabazimi.barcode.utils.BarcodeAnalyzer
import ir.shahabazimi.barcode.utils.Consts
import ir.shahabazimi.barcode.utils.Preferences
import ir.shahabazimi.barcode.viewmodels.ResultViewModel

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

typealias BarcodeListener = (barcode: String) -> Unit

class ScanBarcodeFragment : Fragment() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var b: FragmentScanBarcodeBinding
    private lateinit var cameraPermission: ActivityResultLauncher<String>
    private val resultViewModel: ResultViewModel by activityViewModels()
    private var processingBarcode = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cameraExecutor = Executors.newSingleThreadExecutor()
        cameraPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it)
                startCamera()
            else
                NavHostFragment.findNavController(this).popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        b = FragmentScanBarcodeBinding.inflate(layoutInflater, container, false)
        return b.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupPermissions()
    }

    override fun onResume() {
        super.onResume()
        processingBarcode.set(false)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(b.qrPreview.surfaceProvider)
            }
            val imageAnalysis = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, BarcodeAnalyzer { barcode ->
                        if (processingBarcode.compareAndSet(false, true) &&
                            barcode != Consts.BARCODE_ERROR
                        ) {
                            resultViewModel.setResult(barcode)
                            NavHostFragment.findNavController(this).popBackStack()
                        }
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
            } catch (e: Exception) {
                NavHostFragment.findNavController(this).popBackStack()
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            CAMERA
        )

        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    CAMERA
                )
            ) {
                AlertDialog.Builder(requireContext())
                    .setMessage(getString(R.string.camera_permission_rationale_message))
                    .setTitle(getString(R.string.camera_permission_rationale_title))
                    .setPositiveButton(getString(R.string.camera_permission_rationale_positive)) { _, _ ->
                        cameraPermission.launch(CAMERA)
                    }
                    .setNegativeButton(getString(R.string.camera_permission_rationale_negative)) { dialog, _ ->
                        dialog.dismiss();NavHostFragment.findNavController(
                        this
                    ).popBackStack()
                    }.show()

            } else if (!Preferences(requireContext()).getCameraPermission()) {
                cameraPermission.launch(CAMERA)
                Preferences(requireContext()).setCameraPermission()
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.camera_permission_rationale_settings),
                    Toast.LENGTH_SHORT
                ).show()

                startActivity(Intent().also {
                    it.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    it.data = Uri.fromParts("package", requireActivity().packageName, null)
                }
                )
                NavHostFragment.findNavController(this).popBackStack()
            }
        } else {
            startCamera()
        }
    }

}
