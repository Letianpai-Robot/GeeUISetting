package com.robot.geeui.setting

import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.renhejia.robot.letianpaiservice.ILetianpaiService

class SettingService : Service() {
    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        connectService()
        return super.onStartCommand(intent, flags, startId)
    }

    private var iLetianpaiService: ILetianpaiService? = null
    private var isConnectService = false
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.i("TAG", "SettingService 乐天派 完成AIDLService服务")
            isConnectService = true
            iLetianpaiService = ILetianpaiService.Stub.asInterface(service)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.i("TAG", "SettingService 乐天派 解除绑定aidlserver的AIDLService服务")
            isConnectService = false
        }
    }

    private fun connectService() {
        val intent = Intent()
        intent.setPackage("com.renhejia.robot.letianpaiservice")
        intent.action = "android.intent.action.LETIANPAI"
        bindService(intent, serviceConnection, BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(serviceConnection)
    }

    private val binder: IBinder = MyBinder()
    override fun onBind(intent: Intent): IBinder? {
        return binder
    }

    inner class MyBinder : Binder() {
        val service: SettingService
            get() = this@SettingService
    }
}