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
import com.hana.cheers_up.databinding.FragmentHomeBinding
import com.kakao.sdk.user.UserApiClient

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
                Toast.makeText(requireContext(), "검색: $searchText", Toast.LENGTH_SHORT).show()
                // TODO: 실제 검색 로직 구현
                // 예: 술집 리스트 Fragment로 이동
            } else {
                Toast.makeText(requireContext(), "검색어를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        binding.btnLogout.setOnClickListener {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    Log.e("HomeFragment", "로그아웃 실패", error)
                    // 토큰이 이미 무효한 경우에도 로컬 정리는 진행
                } else {
                    Log.i("HomeFragment", "로그아웃 성공")
                }
                navigateToSplash()
            }
        }
    }
    private fun navigateToSplash() {
        val intent = Intent(requireContext(), SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
