package com.example.yeahsan.ui.ar

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.example.yeahsan.AppApplication
import com.example.yeahsan.AppConstants
import com.example.yeahsan.data.AppDataManager
import com.example.yeahsan.data.api.model.ContentVO
import com.example.yeahsan.data.api.model.DoorListVO
import com.example.yeahsan.ui.popup.GameZonePopupActivity
import com.google.gson.Gson
import com.unity3d.player.Android_Plugin
import com.unity3d.player.UnityPlayerActivity

class ArActivity : UnityPlayerActivity() {
	
	private var content: String? = null
	private var tour: DoorListVO? = null
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		tour = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			 intent.getParcelableExtra(AppConstants.EXTRA_ITEM, DoorListVO::class.java)
		} else {
			intent.getParcelableExtra(AppConstants.EXTRA_ITEM)
		}

		tour?.run {
			content = Gson().toJson(ContentVO("1.0.0", "", code))
			Log.e("TAG", content ?: "")
			setOnUnityInterface(unityInterface)
			initUnity()
		}
	}

	private val unityInterface = object : Android_Plugin.OnUnityInterface {
		
		override fun content(): String? {
			Log.e("TAG", "Content :: $content")
			return content
		}
		
		override fun clear() {
			Log.e("TAG", "Clear")
			tour?.let {

				Log.e("TAG", "Clear :: ${it.code}")

				val intent = Intent(this@ArActivity ,GameZonePopupActivity::class.java )
				intent.putExtra(AppConstants.CLEAR_CONTENT,it)
				setResult(AppConstants.MISSION_ITEM_CLEAR, intent)
				finish()
			}
		}
	}
}