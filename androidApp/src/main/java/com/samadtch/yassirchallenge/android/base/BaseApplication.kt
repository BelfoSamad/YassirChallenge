package com.samadtch.yassirchallenge.android.base

import android.app.Application
import com.samadtch.yassirchallenge.di.initKoin
import dagger.hilt.android.HiltAndroidApp
import org.koin.android.ext.koin.androidContext

@HiltAndroidApp
class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        //DI with Koin
        initKoin {
            androidContext(this@BaseApplication)
        }
    }
}