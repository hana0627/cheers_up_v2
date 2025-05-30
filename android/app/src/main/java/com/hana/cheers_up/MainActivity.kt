package com.hana.cheers_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hana.cheers_up.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupNavigation()

        // 🏠 앱 시작시 홈 Fragment 로드
        if (savedInstanceState == null) {
            loadFragment(HomeFragment.newInstance())
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 툴바 아이콘 클릭 이벤트
        binding.ivMenu.setOnClickListener {
            Toast.makeText(this, "미완성 기능입니다.(메뉴)", Toast.LENGTH_SHORT).show()
            // TODO: Navigation Drawer 열기
        }

        binding.ivSearch.setOnClickListener {
            Toast.makeText(this, "미완성 기능입니다.(검색)", Toast.LENGTH_SHORT).show()
            // TODO: 검색 Activity 열기
        }

        binding.ivChat.setOnClickListener {
            Toast.makeText(this, "미완성 기능입니다.(채팅)", Toast.LENGTH_SHORT).show()
            // TODO: 채팅 Activity 열기
        }

        binding.ivProfile.setOnClickListener {
            Toast.makeText(this, "미완성 기능입니다.(프로필)", Toast.LENGTH_SHORT).show()
            // TODO: 프로필 Activity 열기
        }
    }

    private fun setupNavigation() {
        // 하단 네비게이션 아이템 클릭 이벤트
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment.newInstance())
                    true
                }

                R.id.nav_content2 -> {
                    Toast.makeText(this, "미완성 기능입니다.", Toast.LENGTH_SHORT).show()
//                    loadFragment(createPlaceholderFragment("2번 페이지"))
                    false
                }

                R.id.nav_content3 -> {
                    Toast.makeText(this, "미완성 기능입니다.", Toast.LENGTH_SHORT).show()
//                    loadFragment(createPlaceholderFragment("3번 페이지"))
                    false
                }

                R.id.nav_content4 -> {
                    Toast.makeText(this, "미완성 기능입니다.", Toast.LENGTH_SHORT).show()
//                    loadFragment(createPlaceholderFragment("4번 페이지"))
                    false
                }

                R.id.nav_content5 -> {
                    Toast.makeText(this, "미완성 기능입니다.", Toast.LENGTH_SHORT).show()
//                    loadFragment(createPlaceholderFragment("5번 페이지"))
                    false
                }

                else -> false
            }
        }

        // 기본 선택 항목 설정
        binding.bottomNavigation.selectedItemId = R.id.nav_home
    }

    // 🔄 Fragment 로드 함수
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

}
