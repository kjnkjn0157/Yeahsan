package com.example.yeahsan.service.beacon

import com.example.yeahsan.data.api.model.DoorListVO

interface BeaconScanResult {

    fun onBeaconScanned(item : DoorListVO)
}