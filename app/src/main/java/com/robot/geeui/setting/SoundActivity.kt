package com.robot.geeui.setting

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.widget.SeekBar
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.letianpai.robot.components.network.system.SystemUtil
import com.robot.geeui.setting.databinding.ActivitySoundBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import java.io.IOException

class SoundActivity : BaseActivity() {
    lateinit var binding: ActivitySoundBinding

    private var audioManager: AudioManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySoundBinding.inflate(layoutInflater)
        setContentView(binding.root)
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        binding.included.back.setOnClickListener { finish() }
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                audioManager!!.setStreamVolume(
                    AudioManager.STREAM_ACCESSIBILITY, progress, 0
                )
                audioManager!!.setStreamVolume(
                    AudioManager.STREAM_VOICE_CALL, progress, 0
                )
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                uploadVolume()
            }
        })

        val currentAccessibility = audioManager!!.getStreamVolume(AudioManager.STREAM_ACCESSIBILITY)
        Log.i("TAG", "onCreate: $currentAccessibility")
        binding.seekBar.progress = (currentAccessibility).toInt()
    }

    private fun uploadVolume() {
        val hashMap = HashMap<String, Any>()
        val sn = SystemUtil.getLtpSn()
        hashMap["sound_volume"] =
            audioManager?.getStreamVolume(AudioManager.STREAM_ACCESSIBILITY) ?: 0
        hashMap["sn"] = sn

        GeeUiNetManager.uploadStatus(
            this@SoundActivity,
            SystemUtil.isInChinese(),
            hashMap,
            object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.i("TAG", "onFailure: ")
                }

                override fun onResponse(call: Call, response: Response) {
                    Log.i("TAG", "onResponse: ${response.body?.string()}")
                }
            })
    }
}