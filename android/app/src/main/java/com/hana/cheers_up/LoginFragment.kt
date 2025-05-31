package com.hana.cheers_up

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hana.cheers_up.databinding.FragmentLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupKakaoLoginButton()
    }

    private fun setupKakaoLoginButton() {
        binding.btnKakaoLogin.setOnClickListener {
            kakaoLogin()
        }
    }

    private fun kakaoLogin() {
        // 카카오 계정으로 로그인 공통 callback 구성
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("KAKAOLOGIN", "카카오 로그인 실패", error)
                Toast.makeText(requireContext(), "로그인실패", Toast.LENGTH_SHORT).show()
            }
            else if(token != null) {
                // TODO 토큰만료 검증??
                Log.i("KakaoLogin", "카카오 로그인 성공 ${token.accessToken}")

                // 로그인 성공 시 HomeFragment로 이동
                navigateToHome()
            }
        }


        if(UserApiClient.instance.isKakaoTalkLoginAvailable(requireContext())) {
            UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                if (error != null) {
                    Log.e("KAKAOLOGIN", "카카오 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    // ==> 아마 로그인 후 그냥 취소버튼 연타하는 그런경우 말하는듯????
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoTalk(requireContext(), callback = callback)
                }
                else if(token != null) {
                    // TODO 토큰만료 검증??
                    Log.i("KakaoLogin", "카카오 로그인 성공 ${token.accessToken}")
                    navigateToHome()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(requireContext(), callback = callback)
        }
    }

    private fun navigateToHome() {
        (activity as? SplashActivity)?.navigateToHome()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
