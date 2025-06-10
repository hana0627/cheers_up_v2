package com.hana.cheers_up

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hana.cheers_up.api.ApiService
import com.hana.cheers_up.api.PubAdapter
import com.hana.cheers_up.api.RetrofitClient
import com.hana.cheers_up.api.TokenManager
import com.hana.cheers_up.api.response.PubResponse
import com.hana.cheers_up.databinding.FragmentPubListBinding
import kotlinx.coroutines.launch

class PubListFragment : Fragment() {

    private var _binding: FragmentPubListBinding? = null
    private val binding get() = _binding!!

    private lateinit var tokenManager: TokenManager
    private lateinit var apiService: ApiService
    private lateinit var pubAdapter: PubAdapter
    private val pubList = mutableListOf<PubResponse>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPubListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tokenManager = TokenManager(requireContext())
        apiService = RetrofitClient.getApiService()

        setupUI()
        setupRecyclerView()


        arguments?.let { bundle ->
            val receivedPubs = if(Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
                // Android13 +
                bundle.getParcelableArrayList("pubs",PubResponse::class.java)
            } else {
                // ~ Android12
                @Suppress("DEPRECATION")
                bundle.getParcelableArrayList<PubResponse>("pubs")
            }
            val receivedAddress = bundle.getString("address", "")

            receivedPubs?.let {
                pubList.clear()
                pubList.addAll(it)
                pubAdapter.notifyDataSetChanged()
                binding.etAddress.setText(receivedAddress)
            }
        }
    }

    private fun setupUI() {
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

    private fun setupRecyclerView() {
        pubAdapter = PubAdapter(pubList) { pub ->
            // 항목 클릭시 동작 이벤트. 필요시 구현

        }

        binding.recyclerViewPubs.apply {
            adapter = pubAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
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

    private suspend fun searchPubs(address: String) {
        try {
            binding.progressBar.visibility = View.VISIBLE
            binding.btnSearch.isEnabled = false
            binding.btnSearch.text = "Searching..."

            val jwtToken = tokenManager.getJwtToken()
            if(jwtToken == null) {
                // TODO loginFragment로 이동
                Toast.makeText(requireContext(), "로그인이 필요합니다", Toast.LENGTH_SHORT).show()
                return
            }

            val response = apiService.searchPubs(
                authorization = jwtToken,
                address = address
            )
            if(response.isSuccessful) {
                response.body()?.let { apiResponse ->
                    if(apiResponse.resultCode == "OK" && apiResponse.result != null) {
                        // 🎯 결과를 Log로 출력
                        apiResponse.result.forEachIndexed { index, pub ->
                            Log.i("PubListFragment", "=== 검색 결과 ${index + 1} ===")
                            Log.i("PubListFragment", "술집명: ${pub.pubName}")
                            Log.i("PubListFragment", "주소: ${pub.pubAddress}")
                            Log.i("PubListFragment", "카테고리: ${pub.categoryName}")
                            Log.i("PubListFragment", "거리: ${pub.distance}")
                            Log.i("PubListFragment", "길찾기 URL: ${pub.directionUrl}")
                            Log.i("PubListFragment", "로드뷰 URL: ${pub.roadViewUrl}")
                            Log.i("PubListFragment", "================================")
                        }


                        val fragment = newInstance(apiResponse.result, address)

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack("HomeFragment")
                            .commit()

//                        Toast.makeText(
//                            requireContext(),
//                            "검색 완료! ${apiResponse.result.size}개의 술집을 찾았습니다",
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }else {
                        Log.e("PubListFragment", "검색 실패: ${apiResponse.resultCode}")
                        Toast.makeText(requireContext(), "검색에 실패했습니다", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Log.e("PubListFragment", "API 호출 실패: ${response.code()}")

                if (response.code() == 401) {
                    Toast.makeText(requireContext(), "인증이 만료되었습니다. 다시 로그인해주세요", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "서버 오류가 발생했습니다", Toast.LENGTH_SHORT).show()
                }
            }
        }catch (e: Exception) {
            Log.e("PubListFragment", "검색 중 오류 발생", e)
            Toast.makeText(requireContext(), "네트워크 오류가 발생했습니다", Toast.LENGTH_SHORT).show()
        } finally {
            binding.progressBar.visibility = View.GONE
            binding.btnSearch.isEnabled = true
            binding.btnSearch.text = "Search"
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    companion object {
        fun newInstance(pubs: List<PubResponse>? = null, address: String? = null): PubListFragment {
            val fragment = PubListFragment()
            var bundle = Bundle().apply() {
                pubs?.let { putParcelableArrayList("pubs", ArrayList(it)) }
                address?.let { putString("address", it) }
            }
            fragment.arguments = bundle
            return fragment
        }
    }

}
