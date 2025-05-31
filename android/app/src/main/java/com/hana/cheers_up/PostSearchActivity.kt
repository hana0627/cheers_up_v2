package com.hana.cheers_up

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.hana.cheers_up.databinding.ActivityPostsearchBinding

class PostSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostsearchBinding

    companion object {
        val EXTRA_ADDRESS = "extra_address"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostsearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolBar()
        setupWebView()
        loadDaumPostcode()

    }

    private fun setupToolBar() {
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupWebView() {
        binding.webView.apply {
            settings.apply {
                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                setSupportMultipleWindows(true)
                useWideViewPort = true
                loadWithOverviewMode = true
                builtInZoomControls = false
                displayZoomControls = false
                domStorageEnabled = true
            }

            // JavaScript Interface 추가
            addJavascriptInterface(PostJavaScriptInterface(), "Android")

            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
        }
    }

    private fun loadDaumPostcode() {
        val html = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta charset="utf-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=yes">
            <title>주소검색</title>
            <style>
                * { 
                    margin: 0; 
                    padding: 0; 
                    box-sizing: border-box;
                }
                html, body { 
                    font-family: 'Malgun Gothic', sans-serif; 
                    background-color: #F2F2F2 !important;
                    width: 100%;
                    height: 100%;
                    /* 스크롤 허용 */
                    overflow: auto !important;
                }
                #loading { 
                    position: absolute; 
                    top: 50%; 
                    left: 50%; 
                    transform: translate(-50%, -50%); 
                    font-size: 16px; 
                    color: #666;
                    text-align: center;
                }
                #error {
                    position: absolute; 
                    top: 50%; 
                    left: 50%; 
                    transform: translate(-50%, -50%); 
                    font-size: 14px; 
                    color: #ff0000;
                    text-align: center;
                    display: none;
                }
                #layer {
                    background-color: #F2F2F2 !important;
                    /* 스크롤 허용 */
                    overflow: auto !important;
                    width: 100%;
                    height: 100%;
                }
                #daumPostcode {
                    width: calc(100% - 20px) !important;
                    height: 100%;
                    /* 좌우 동일 마진 */
                    margin: 0 10px !important;
                    background-color: #F2F2F2 !important;
                    /* border-radius: 8px; */
                    /* 스크롤 허용 */
                    overflow: auto !important;
                    box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                    /* 터치 스크롤 활성화 */
                    -webkit-overflow-scrolling: touch;
                    touch-action: auto;
                }
                
                /* Daum Postcode 내부 스타일 오버라이드 */
                #daumPostcode iframe {
                    /* border-radius: 8px !important; */
                    /* iframe도 스크롤 허용 */
                    overflow: auto !important;
                    touch-action: auto;
                }
                
                /* 전체 배경 통일 */
                .wrap_search, .search_area, .list_search {
                    background-color: #F2F2F2 !important;
                }
                
                /* 스크롤 관련 추가 설정 */
                * {
                    -webkit-overflow-scrolling: touch;
                }
            </style>
        </head>
        <body>
            <div id="loading">
                <div>주소 검색을 준비중입니다...</div>
                <div style="margin-top: 10px; font-size: 12px;">잠시만 기다려주세요</div>
            </div>
            
            <div id="error">
                <div>주소 검색을 불러올 수 없습니다</div>
                <div style="margin-top: 10px; font-size: 12px;">네트워크 연결을 확인해주세요</div>
            </div>
            
            <div id="layer" style="display:none; position:absolute; top:0; left:0; width:100%; height:100%; z-index:1;">
                <div id="daumPostcode"></div>
            </div>
            
            <script>
                console.log('HTML 문서 시작');
                
                // 스크립트 동적 로드 및 타이머 설정
                let scriptLoaded = false;
                let loadTimeout;
                
                function showError() {
                    console.log('에러 표시');
                    document.getElementById('loading').style.display = 'none';
                    document.getElementById('error').style.display = 'block';
                }
                
                function loadDaumScript() {
                    console.log('Daum 스크립트 로드 시작');
                    
                    // 10초 타임아웃 설정
                    loadTimeout = setTimeout(function() {
                        if (!scriptLoaded) {
                            console.log('스크립트 로드 타임아웃');
                            showError();
                        }
                    }, 10000);
                    
                    const script = document.createElement('script');
                    script.type = 'text/javascript';
                    script.src = 'https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js';
                    
                    script.onload = function() {
                        console.log('Daum Postcode 스크립트 로드 성공');
                        scriptLoaded = true;
                        clearTimeout(loadTimeout);
                        
                        document.getElementById('loading').style.display = 'none';
                        document.getElementById('layer').style.display = 'block';
                        
                        // 약간의 지연 후 실행 (스크립트 초기화 시간)
                        setTimeout(function() {
                            execDaumPostcode();
                        }, 100);
                    };
                    
                    script.onerror = function() {
                        console.log('Daum Postcode 스크립트 로드 실패');
                        clearTimeout(loadTimeout);
                        showError();
                    };
                    
                    document.head.appendChild(script);
                }
                
                function execDaumPostcode() {
                    try {
                        console.log('Daum Postcode 초기화 시작');
                        
                        if (typeof daum === 'undefined' || typeof daum.Postcode === 'undefined') {
                            console.log('daum.Postcode가 정의되지 않음');
                            showError();
                            return;
                        }
                        
                        new daum.Postcode({
                            oncomplete: function(data) {
                                console.log('주소 선택 완료:', data);
                                
                                let fullAddress = data.address;
                                let extraAddress = '';
                                
                                if (data.addressType === 'R') {
                                    if (data.bname !== '') {
                                        extraAddress += data.bname;
                                    }
                                    if (data.buildingName !== '') {
                                        extraAddress += (extraAddress !== '' ? ', ' + data.buildingName : data.buildingName);
                                    }
                                    fullAddress += (extraAddress !== '' ? ' (' + extraAddress + ')' : '');
                                }
                                
                                console.log('전송할 주소:', fullAddress);
                                
                                // Android Interface 호출
                                try {
                                    if (window.Android && typeof window.Android.setAddress === 'function') {
                                        window.Android.setAddress(fullAddress);
                                    } else {
                                        console.log('Android interface를 찾을 수 없음');
                                    }
                                } catch (e) {
                                    console.log('Android interface 호출 오류:', e);
                                }
                            },
                            onresize: function(size) {
                                console.log('팝업 크기 변경:', size);
                            },
                            onsearch: function(data) {
                                console.log('검색 실행:', data);
                            },
                            onclose: function(state) {
                                console.log('팝업 닫힘:', state);
                                // 뒤로가기 등으로 닫힌 경우 액티비티 종료
                                if (state === 'FORCE_CLOSE' || state === 'COMPLETE_CLOSE') {
                                    if (window.Android && typeof window.Android.setAddress === 'function') {
                                        // 빈 값으로 종료 처리할 수도 있음
                                    }
                                }
                            }
                        }).embed('daumPostcode');
                        
                        console.log('Daum Postcode embed 완료');
                        
                    } catch (e) {
                        console.log('Daum Postcode 초기화 오류:', e);
                        showError();
                    }
                }
                
                // 문서 로드 완료 후 실행
                if (document.readyState === 'loading') {
                    document.addEventListener('DOMContentLoaded', function() {
                        console.log('DOMContentLoaded');
                        loadDaumScript();
                    });
                } else {
                    console.log('문서 이미 로드됨');
                    loadDaumScript();
                }
                
                // 추가 안전장치
                window.onload = function() {
                    console.log('Window onload');
                    if (!scriptLoaded) {
                        loadDaumScript();
                    }
                };
            </script>
        </body>
        </html>
    """.trimIndent()

        // HTTPS baseURL로 설정하여 mixed content 문제 방지
        binding.webView.loadDataWithBaseURL(
            "https://postcode.map.daum.net/",
            html,
            "text/html",
            "UTF-8",
            null
        )
    }


    inner class PostJavaScriptInterface {
        @JavascriptInterface
        fun setAddress(address: String) {
            runOnUiThread{
                val resultIntent = Intent().apply {
                    putExtra(EXTRA_ADDRESS, address)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }

        }
    }

    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }


}