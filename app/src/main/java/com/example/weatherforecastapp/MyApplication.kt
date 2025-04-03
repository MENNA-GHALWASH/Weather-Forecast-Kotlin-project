//package com.example.weatherforecastapp
//
//import android.app.Application
//import android.content.Context
//import android.content.SharedPreferences
//import com.example.weatherforecastapp.settings.model.SettingsManager
//
//class MyApplication : Application() {
//
//    companion object {
//        lateinit var instance: MyApplication
//            private set
//    }
//
//    fun getSharedPreferences(): SharedPreferences {
//        return getSharedPreferences("settings_pref", Context.MODE_PRIVATE)
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        instance = this
//
//        val language = SettingsManager.getLanguage()
//        SettingsManager.setLocale(applicationContext, language)
//    }
//}
