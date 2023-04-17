package com.example.yeahsan.ui.qr

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ContentInfoCompat.Flags
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.example.yeahsan.data.AppDataManager
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

        //  변경 : onCreate에서 권한을 확인하는 경우 사용자가 임의로 권한을 해제하는 경우에 대한 처리가 불가
        initQRCodeScanner()
    }


    private var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            Toast.makeText(this@QrScannerActivity, "권한 허가", Toast.LENGTH_SHORT).show()

            initQRCodeScanner()
        }

        override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
            Toast.makeText(this@QrScannerActivity, "권한 거부\n$deniedPermissions", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun initQRCodeScanner() {

        binding.qrLayout.setOnQRCodeReadListener(qrCodeReadListener)
//        binding.qrLayout.setQRDecodingEnabled(true)
        binding.qrLayout.setAutofocusInterval(2000L)
//        binding.qrLayout.setTorchEnabled(true)    //  flash
//        binding.qrLayout.setFrontCamera() //  front
        binding.qrLayout.setBackCamera()
//        binding.qrLayout.startCamera()
//        binding.qrLayout.stopCamera()
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
            val vibratorManager =
                getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
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

    private fun treatmentResult(result: String) {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(result))
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()

        if (!isCameraPermissionGranted()) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            binding.qrLayout.startCamera()
        }
    }

    override fun onPause() {
        super.onPause()

        if (isCameraPermissionGranted()) {
            binding.qrLayout.stopCamera()
        }
    }

    //  카메라 권한 획득 여부
    private fun isCameraPermissionGranted(): Boolean {

        return ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    //  Launcher는 전역으로 설정 - LifecycleOwners must call register before they are STARTED
    val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this@QrScannerActivity, "권한 허가", Toast.LENGTH_SHORT).show()
                //  액티비티 재실행으로 권한 획득 이후 카메라가 정상적으로 표시되지 않는 오류 수정
                //  1. noHistory : 다른 액티비티로 전환 시 이전으로 돌아올 때 QR 코드 액티비티로 복귀하지 않아도 되는 경우
                //  2. MainActivity 에서 권한 처리, 상황 : 메인 액티비티 -> QR 액티비티 -> 다른 액티비티로 전환하는 경우 이전으로 돌아올 때 QR 코드 액티비티로 복귀해야 할 경우
                Intent(this@QrScannerActivity, QrScannerActivity::class.java).run {
                    this.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(this)
                }
                overridePendingTransition(0, 0)
            } else {
                if(!AppDataManager(application).isDenyCameraPermission()) { // 최초 카메라 권한 거부 시 중복 묻기 방지
                    AppDataManager(application).setDenyCameraPermission(true)
                    Toast.makeText(this@QrScannerActivity, "권한 거부", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    checkPermission() // 권한 없는 채로 다시 qr activity에 진입할 때 권한 묻기
                    Toast.makeText(this@QrScannerActivity, "권한 거부", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

    private fun checkPermission() {

        TedPermission.create()
            .setPermissionListener(permissionlistener)
            .setRationaleMessage("카메라 권한이 필요한 서비스입니다.")
            .setDeniedMessage("[설정] -> [권한]에서 권한 변경이 가능합니다.")
            .setDeniedCloseButtonText("닫기")
            .setGotoSettingButtonText("설정")
            .setRationaleTitle("알림")
            .setPermissions(
                Manifest.permission.CAMERA
            )
            .check()
    }
}