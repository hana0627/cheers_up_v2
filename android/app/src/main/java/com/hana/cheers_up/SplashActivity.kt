package com.hana.cheers_up

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hana.cheers_up.databinding.ActivitySplashBinding import com.kakao.sdk.user.UserApiClient

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            checkAuthenticationStatus()
        }, 1000)
    }

    private fun checkAuthenticationStatus() {
        // 토큰 정보 있는지 확인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            // 토큰 유효성 체크
            UserApiClient.instance.accessTokenInfo( { tokenInfo, error ->
                if(error != null) {
                    Log.e("SplashActivity","잘못된 토큰11", error)
                    showLoginFragment()
                }else if(tokenInfo != null) {
                    Log.i("SplashActivity", "인증된 토큰, 토큰정보: ${tokenInfo}")
                    navigateToHome()
                }

            })
        } else {
            // 카카오톡이 설치되지 않은 경우, 토큰 존재 여부만 체크
            try {
                UserApiClient.instance.accessTokenInfo( {tokenInfo, error ->
                    if(error != null || tokenInfo == null) {
                        showLoginFragment()
                    } else {
                        navigateToHome()
                    }
                })
            } catch (e: Exception) {
                Log.e("SplashActivity","잘못된 토큰22", e)
                showLoginFragment()
            }
        }
    }

    private fun showLoginFragment() {
        binding.llSplashContent.visibility = View.GONE
        binding.fragmentContainer.visibility = View.VISIBLE

        val loginFragment = LoginFragment()
        replaceFragment(loginFragment)
    }

    fun navigateToHome() {
        // MainActivity로 이동
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}
