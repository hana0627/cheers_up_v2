package com.hana.cheers_up

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.hana.cheers_up.api.ApiService
import com.hana.cheers_up.api.RetrofitClient
import com.hana.cheers_up.api.TokenManager
import com.hana.cheers_up.databinding.ActivitySplashBinding
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var tokenManager: TokenManager
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tokenManager = TokenManager(this)
        apiService = RetrofitClient.getApiService()

        Handler(Looper.getMainLooper()).postDelayed({
            checkAuthenticationStatus()
        }, 1000)
    }

    private fun checkAuthenticationStatus() {
        UserApiClient.instance.accessTokenInfo {tokenInfo, error ->
            if(error != null || tokenInfo == null) {
                // 카카오 토큰없음 -> 로그인화면
                Log.e("SplashActivity", "카카오 토큰 없음 또는 만료")
                showLoginFragment()
            }
            else {
                Log.i("SplashActivity", "카카오 토큰 존재 → JWT 재발급 시작")
                // 토큰 있어도 Splash에서 한번 갱신
                lifecycleScope.launch {
                    refreshJwtAndNavigateHome()
                }

            }
        }
    }

    private fun showLoginFragment() {
        binding.llSplashContent.visibility = View.GONE
        binding.fragmentContainer.visibility = View.VISIBLE

        val loginFragment = LoginFragment()
        replaceFragment(loginFragment)
    }


    private suspend fun refreshJwtAndNavigateHome() {
        try {
            Log.i("SplashActivity", "JWT 재발급 시작")

            val currentKakaoToken = getCurrentKakaoToken()

            if(currentKakaoToken != null) {
                val response = apiService.getJwtToken(currentKakaoToken)

                if(response.isSuccessful) {
                    response.body()?.let { apiResponse ->
                        if(apiResponse.resultCode == "OK" && apiResponse.result != null) {
                            tokenManager.saveJwtToken(apiResponse.result)
                            Log.i("SplashActivity", "JWT 재발급 성공 → 홈으로 이동")
                            navigateToHome()
                            return
                        }
                    }
                }

                Log.e("SplashActivity", "JWT 재발급 실패 → 로그인 화면")
                showLoginFragment()
            } else {
                Log.e("SplashActivity", "카카오 토큰 가져오기 실패 → 로그인 화면")
                showLoginFragment()
            }
        } catch (e: Exception) {
            Log.e("SplashActivity", "JWT 재발급 중 오류", e)
            showLoginFragment()
        }
    }

    private fun getCurrentKakaoToken(): String? {
        return com.kakao.sdk.auth.TokenManagerProvider.instance.manager.getToken()?.accessToken
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

//private fun checkAuthenticationStatus() {
//    if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
//        // 토큰 유효성 체크
//        UserApiClient.instance.accessTokenInfo( { tokenInfo, error ->
//            if(error != null) {
//                Log.e("SplashActivity","잘못된 토큰", error)
//                showLoginFragment()
//            }else if(tokenInfo != null) {
//                Log.i("SplashActivity", "인증된 토큰, 토큰정보: ${tokenInfo}")
//                lifecycleScope.launch {
//                    Log.i("SplashActivity","여기호출")
//                    tokenManager.refreshJwtToken()
//                    navigateToHome()
//                }
//            }
//
//        })
//    } else {
//        // 카카오톡이 설치되지 않은 경우, 토큰 존재 여부만 체크
//        try {
//            UserApiClient.instance.accessTokenInfo( {tokenInfo, error ->
//                if(error != null || tokenInfo == null) {
//                    showLoginFragment()
//                } else {
//                    navigateToHome()
//                }
//            })
//        } catch (e: Exception) {
//            Log.e("SplashActivity","잘못된 토큰22", e)
//            showLoginFragment()
//        }
//    }
//}



/*

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🎯 TokenManager 초기화 추가
        tokenManager = TokenManager(this)

        Handler(Looper.getMainLooper()).postDelayed({
            checkAuthenticationStatus()
        }, 1000)
    }

    private fun checkAuthenticationStatus() {
        // 🎯 카카오 토큰 존재 여부 확인 (카카오톡 설치 여부와 관계없이)
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null || tokenInfo == null) {
                // 🎯 카카오 토큰 없음 → 로그인 화면
                Log.e("SplashActivity", "카카오 토큰 없음 또는 만료")
                showLoginFragment()
            } else {
                // 🎯 카카오 토큰 존재 → JWT 새로 발급받고 홈으로
                Log.i("SplashActivity", "카카오 토큰 존재 → JWT 재발급 시작")
                lifecycleScope.launch {
                    refreshJwtAndNavigateToHome()
                }
            }
        }
    }

    private suspend fun refreshJwtAndNavigateToHome() {
        try {
            Log.i("SplashActivity", "JWT 재발급 시작")

            // 🎯 현재 카카오 토큰을 저장하고 JWT 재발급
            val currentKakaoToken = getCurrentKakaoAccessToken()

            if (currentKakaoToken != null) {
                // 카카오 토큰 저장
                tokenManager.saveKakaoToken(currentKakaoToken)

                // JWT 재발급
                val jwtToken = tokenManager.refreshJwtToken()

                if (jwtToken != null) {
                    Log.i("SplashActivity", "JWT 재발급 성공 → 홈으로 이동")
                    navigateToHome()
                } else {
                    Log.e("SplashActivity", "JWT 재발급 실패 → 로그인 화면")
                    showLoginFragment()
                }
            } else {
                Log.e("SplashActivity", "카카오 토큰 가져오기 실패 → 로그인 화면")
                showLoginFragment()
            }
        } catch (e: Exception) {
            Log.e("SplashActivity", "JWT 재발급 중 오류", e)
            showLoginFragment()
        }
    }

    private suspend fun getCurrentKakaoAccessToken(): String? {
        return withContext(Dispatchers.Main) {
            suspendCoroutine { continuation ->
                try {
                    UserApiClient.instance.me { user, error ->
                        if (error == null && user != null) {
                            // 🎯 현재 유효한 카카오 액세스 토큰 가져오기
                            val token = com.kakao.sdk.auth.TokenManagerProvider.instance.manager.getToken()?.accessToken
                            Log.d("SplashActivity", "카카오 토큰 가져오기 성공: ${token?.take(10)}...")
                            continuation.resume(token)
                        } else {
                            Log.e("SplashActivity", "카카오 사용자 정보 가져오기 실패", error)
                            continuation.resume(null)
                        }
                    }
                } catch (e: Exception) {
                    Log.e("SplashActivity", "카카오 토큰 가져오기 실패", e)
                    continuation.resume(null)
                }
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

 */