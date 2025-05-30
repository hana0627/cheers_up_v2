package com.hana.cheers_up

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.hana.cheers_up.databinding.FragmentHomeBinding

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
    }

    private fun setupSearch() {
        // 주소 검색 EditText 클릭 이벤트
        binding.etAddress.setOnClickListener {
            Toast.makeText(requireContext(), "주소 검색 기능 구현 예정", Toast.LENGTH_SHORT).show()
            // TODO: 주소 검색 Activity나 Dialog 열기
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

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }
}
