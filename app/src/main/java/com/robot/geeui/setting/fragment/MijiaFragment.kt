package com.robot.geeui.setting.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.letianpai.robot.components.network.nets.GeeUiNetManager
import com.robot.geeui.setting.R
import com.robot.geeui.setting.databinding.FragmentMijiaBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okio.IOException
import org.json.JSONObject

class MijiaFragment : BaseFragment() {
    private var bindStatus: Boolean = false
    lateinit var binding: FragmentMijiaBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMijiaBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cancel.setOnClickListener {
            binding.content.visibility = View.VISIBLE
            binding.confirmLayout.visibility = View.GONE
        }
        binding.confirmUnbind.setOnClickListener {
            clickBind()
        }
        binding.bind?.setOnClickListener {
            if (bindStatus) {
                binding.content.visibility = View.GONE
                binding.confirmLayout.visibility = View.VISIBLE
            } else {
                clickBind()
            }
        }
        getData()
    }

    private fun clickBind() {
        var url = if (bindStatus) {
            "your interface url"
        } else {
            "your interface url"
        }
        GeeUiNetManager.post(context, true, url, HashMap<String, String>(), object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                val json = response.body.string()
                Log.i("TAG", "onResponse: ${json}")
                if (bindStatus) {
                    activity?.finish()
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    private fun getData() {
        singleExecutor.execute {
            GeeUiNetManager.get(
                context,
                true,
                "your interface url",
                object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        e.printStackTrace()
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val json = response.body.string()
                        Log.i("TAG", "onResponse: $json")
                        val jo = JSONObject(json).getJSONObject("data")
                        val bind_status = jo.optInt("bind_status")
                        bindStatus = bind_status == 1
                        updateUI()
                    }
                })
        }
    }

    private fun updateUI() {
        activity?.runOnUiThread {
            binding.bind?.visibility = View.VISIBLE
            if (bindStatus) {
                binding.bind?.setText("解除绑定")
            } else {
                binding.bind?.setText("开始绑定")
            }
        }
    }
}