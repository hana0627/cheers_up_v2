package com.hana.cheers_up

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.hana.cheers_up.api.ApiService
import com.hana.cheers_up.api.RetrofitClient
import com.hana.cheers_up.api.TokenManager
import com.hana.cheers_up.databinding.FragmentHomeBinding
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private lateinit var tokenManager: TokenManager
    private lateinit var apiService: ApiService


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tokenManager = TokenManager(requireContext())
        apiService = RetrofitClient.getApiService()

        setupSearch()
        logoutEvent()
    }

    private fun setupSearch() {
        // 주소 검색 EditText 클릭 이벤트
        binding.etAddress.setOnClickListener {
            val intent = Intent(requireContext(), PostSearchActivity::class.java)
            postSearchLauncher.launch(intent)
        }

        // 검색 버튼 클릭 이벤트
        binding.btnSearch.setOnClickListener {
            val searchText = binding.etAddress.text.toString()
            if (searchText.isNotEmpty()) {
//                Toast.makeText(requireContext(), "검색: $searchText", Toast.LENGTH_SHORT).show()
                // 코루틴으로 API 호출
                lifecycleScope.launch {
                    searchPubs(searchText)
                }
            } else {
                Toast.makeText(requireContext(), "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 주소검색 API 호출함수
    private suspend fun searchPubs(address: String) {
        try {

            val jwtToken = tokenManager.getJwtToken()

            val response = apiService.searchPubs(
                authorization = "$jwtToken",
                address = address
            )
            if(response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if(apiResponse.resultCode == "OK" && apiResponse.result != null) {
                        // 🎯 결과를 Log로 출력
                        apiResponse.result.forEachIndexed { index, pub ->
                            Log.i("HomeFragment", "=== 검색 결과 ${index + 1} ===")
                            Log.i("HomeFragment", "술집명: ${pub.pubName}")
                            Log.i("HomeFragment", "주소: ${pub.pubAddress}")
                            Log.i("HomeFragment", "카테고리: ${pub.categoryName}")
                            Log.i("HomeFragment", "거리: ${pub.distance}")
                            Log.i("HomeFragment", "길찾기 URL: ${pub.directionUrl}")
                            Log.i("HomeFragment", "로드뷰 URL: ${pub.roadViewUrl}")
                            Log.i("HomeFragment", "================================")
                        }

//                        Toast.makeText(
//                            requireContext(),
//                            "검색 완료! ${apiResponse.result.size}개의 술집을 찾았습니다",
//                            Toast.LENGTH_SHORT
//                        ).show()

                        val fragment = PubListFragment.newInstance(apiResponse.result, address)

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack("HomeFragment")
                            .commit()
                    }else {
                        Log.e("HomeFragment", "검색 실패: ${apiResponse.resultCode}")
                        Toast.makeText(requireContext(), "검색에 실패했습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Log.e("HomeFragment", "API 호출 실패: ${response.code()}")

                if (response.code() == 401) {
                    Toast.makeText(requireContext(), "인증이 만료되었습니다. 다시 로그인해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "서버 오류가 발생했습니다", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.e("HomeFragment", "검색 중 오류 발생", e)
        }

    }


    // 🏠 주소 검색 결과를 받기 위한 ActivityResultLauncher
    private val postSearchLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val address = result.data?.getStringExtra(PostSearchActivity.EXTRA_ADDRESS)
            address?.let {
                binding.etAddress.setText(it)
                binding.etAddress.hint = it
                handlePostSelect(it)
            }
        }
    }

    private fun handlePostSelect(address: String) {
        // 🎯 선택된 주소로 추가 작업 수행
        // 예: 좌표 변환, 저장, API 호출 등
        // 아직은 미구현. 혹시 사용이 필요할 수도 있을거 같아서 남겨둠
    }

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }


    private fun logoutEvent() {
        // 테스트용 임시기능이였어서 주석
        // 근데 혹시 필요할수도 있으니까 함수는 남겨둠

//        binding.btnLogout.setOnClickListener {
//            UserApiClient.instance.logout { error ->
//                if (error != null) {
//                    Log.e("HomeFragment", "로그아웃 실패", error)
//                    // 토큰이 이미 무효한 경우에도 로컬 정리는 진행
//                } else {
//                    Log.i("HomeFragment", "로그아웃 성공")
//                }
//                navigateToSplash()
//            }
//        }
    }
    private fun navigateToSplash() {
        val intent = Intent(requireContext(), SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
