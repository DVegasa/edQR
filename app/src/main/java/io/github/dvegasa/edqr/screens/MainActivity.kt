package io.github.dvegasa.edqr.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result
import io.github.dvegasa.edqr.InMemoryStorage
import io.github.dvegasa.edqr.R
import kotlinx.android.synthetic.main.activity_main.*
import me.dm7.barcodescanner.zxing.ZXingScannerView

class MainActivity : AppCompatActivity(), ZXingScannerView.ResultHandler {

    private val qrScanner by lazy {
        ZXingScannerView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (isCameraPermissionGranted()) {
            initQrScanner()
        } else {
            askCameraPermission()
        }
    }

    private fun isCameraPermissionGranted() =
        ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    private fun askCameraPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 42)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            42 -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    initQrScanner()
                } else {
                    askCameraPermission()
                }
                return
            }
        }
    }

    private fun initQrScanner() {
        rootView.addView(qrScanner)
        qrScanner.setResultHandler(this)
    }

    override fun onResume() {
        qrScanner.startCamera()
        super.onResume()
    }

    override fun onPause() {
        qrScanner.stopCamera()
        super.onPause()
    }

    override fun handleResult(rawResult: Result?) {
        qrScanner.stopCamera()
        saveResult(rawResult)
        showResult(rawResult)
    }

    private fun saveResult(rawResult: Result?) {
        InMemoryStorage.qrResult = rawResult
    }

    private fun showResult(rawResult: Result?) {
        if (rawResult == null) {
            Toast.makeText(this, "Scan failed", Toast.LENGTH_SHORT).show()
            return
        }

        val intent = ResultActivity.getIntent(this)
        startActivity(intent)
    }
}
