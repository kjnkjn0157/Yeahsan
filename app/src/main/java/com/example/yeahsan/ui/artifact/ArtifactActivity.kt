package com.example.yeahsan.ui.artifact

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.yeahsan.R
import com.example.yeahsan.data.AppDataManager

class ArtifactActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_artifact)

        AppDataManager(application).getSampleData {
            Log.e("TAG","activity in sample data ::: "+ it?.body?.aContent)

        }
    }
}