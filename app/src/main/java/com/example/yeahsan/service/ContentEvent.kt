package com.example.yeahsan.service

import android.content.Context
import com.example.yeahsan.data.api.model.DoorListVO

class ContentEvent() {

    companion object {

        private var instance: ContentEvent? = null

        private lateinit var context: Context

        fun getInstance(_context: Context): ContentEvent {
            return instance ?: synchronized(this) {
                instance ?: ContentEvent().also {
                    context = _context
                    instance = it
                }
            }
        }
    }

    fun setContent(item : DoorListVO) {
        synchronized(this) {
            //유니티 처리
        }
    }
}