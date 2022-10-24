package ir.shahabazimi.barcode.utils

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import ir.shahabazimi.barcode.fragments.BarcodeListener
import ir.shahabazimi.barcode.utils.Consts.BARCODE_ERROR

class BarcodeAnalyzer(private val barcodeListener: BarcodeListener) : ImageAnalysis.Analyzer {

    private val scanner = BarcodeScanning.getClient()

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener {
                    if (!it.isNullOrEmpty())
                        barcodeListener(it.firstOrNull()?.rawValue.orEmpty())

                }
                .addOnFailureListener { barcodeListener(BARCODE_ERROR) }
                .addOnCompleteListener { imageProxy.close() }
        }
    }
}
