package com.example.yeahsan.ui.qr

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.example.yeahsan.databinding.ActivityQrScannerBinding
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission


class QrScannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQrScannerBinding
    private var scan = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQrScannerBinding.inflate(layoutInflater)

        initView()

        setContentView(binding.root)
    }

    private fun initView() {

        supportActionBar?.title = "QrCode"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        checkPermission()
    }

//    private fun checkPermission() {
//        // 권한 체크해서 권한이 있을 때
//        if (ContextCompat.checkSelfPermission(this@QrScannerActivity, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
//
//            initQrCodeScanner()
//        }
//
//        // 권한이 없을 때 권한을 요구함
//        else {
//            ActivityCompat.requestPermissions(this@QrScannerActivity, arrayOf(android.Manifest.permission.CAMERA), 1)
//        }
//    }


    private fun checkPermission() {

        TedPermission.create()
            .setPermissionListener(permissionlistener)
            .setRationaleMessage("카메라 권한이 필요한 서비스입니다.")
            .setDeniedMessage("[설정] -> [권한]에서 권한 변경이 가능합니다.")
            .setDeniedCloseButtonText("닫기")
            .setGotoSettingButtonText("설정")
            .setRationaleTitle("HELLO")
            .setPermissions(
                Manifest.permission.CAMERA
            )
            .check()
    }

    private var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            Toast.makeText(this@QrScannerActivity, "권한 허가", Toast.LENGTH_SHORT).show()

            initQrCodeScanner()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            Toast.makeText(this@QrScannerActivity, "권한 거부\n$deniedPermissions", Toast.LENGTH_SHORT).show()
        }
    }


    private fun initQrCodeScanner() {

        binding.qrLayout.startCamera()

        binding.qrLayout.setOnQRCodeReadListener(qrCodeReadListener);

        binding.qrLayout.setAutofocusInterval(2000L)

        //binding.qrLayout.setTorchEnabled(true)

        binding.qrLayout.setBackCamera()
    }

    private val qrCodeReadListener = QRCodeReaderView.OnQRCodeReadListener { result, _ ->
        result?.let {
            if (!scan) {
                setVibrator()
                treatmentResult(result)
                Log.e("TAG", "scan result ::: ${it}")// 들어옴 , 알림창 띄우고 activity 넘어가게 하기
            }
        }
    }


    @SuppressLint("ServiceCast")
    private fun setVibrator() {

        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            getSystemService(VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(100L, 10))
        } else {
            vibrator.vibrate(100L)
        }
    }

    private fun treatmentResult(result : String) {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result))
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        binding.qrLayout.startCamera()
}

    override fun onPause() {
        super.onPause()

        binding.qrLayout.stopCamera()
    }
}