package com.team.bpm.presentation

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        // 카카오 로그인을 위한 SDK 셋업
        KakaoSdk.init(this, getString(R.string.kakao_app_key))

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

//        SoLoader.init(this, false)

//        if (BuildConfig.DEBUG && FlipperUtils.shouldEnableFlipper(this)) {
//            val client = AndroidFlipperClient.getInstance(this)
//            client.addPlugin(InspectorFlipperPlugin(this, DescriptorMapping.withDefaults()))
//            client.addPlugin(CrashReporterPlugin.getInstance())
//            client.addPlugin(DatabasesFlipperPlugin(this))
//            client.addPlugin(NetworkFlipperPlugin())
//            client.start()
//        }
    }

}