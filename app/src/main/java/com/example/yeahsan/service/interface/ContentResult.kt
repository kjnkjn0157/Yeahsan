package com.example.yeahsan.service.`interface`

import com.example.yeahsan.data.api.model.DoorListVO

interface ContentResult {
    /**
     * 단일 결과 처리
     */
    fun onContentReceived(content: DoorListVO)

    /**
     * 다중 결과 처리
     */
  //  fun onContentsReceived(contents: ArrayList<DoorListVO>)
}