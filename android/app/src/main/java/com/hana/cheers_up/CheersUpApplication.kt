
package com.hana.cheers_up

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class CheersUpApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val kakaoAppKey = getString(R.string.kakao_app_key)
        KakaoSdk.init(this, kakaoAppKey)
    }
}
