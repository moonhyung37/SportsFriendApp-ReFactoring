package com.example.sportsfriendrefac.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.databinding.FragmentDialogAddBulletinBinding
import com.example.sportsfriendrefac.databinding.FragmentDialogWebViewDaumAddrBinding

/* 회원가입 시 주소검색 웹뷰가 보여지는 다이얼로그 */
class DaumAddrFragDialog : DialogFragment() {
    var flagAddr: Int? = null
    private var fragmentInterfacer: OnclickListener? = null

    private var _binding: FragmentDialogWebViewDaumAddrBinding? = null
    private val binding get() = _binding!!

    //다이얼로그에서 선택한 주소값을 받아오기 위해 작성
    interface OnclickListener {
        //주소를 받아오는 추상메서드
        //flagAddr
        //-1번 거주지역 2번 관심지역
        fun onButtonClick(address: String?, flagAddr: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.DaumAddrdialog_fullscreen)

        //false로 설정해 주면 화면밖 혹은 뒤로가기 버튼시 다이얼로그가 dismiss 되지 않는다.
//        isCancelable = true
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDialogWebViewDaumAddrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //거주지역, 관심지역을 구분해주는 번호를 받아온다.


        //DOMStorage 허용
        binding.webViewDaumAddr.settings.javaScriptCanOpenWindowsAutomatically = true;
        binding.webViewDaumAddr.settings.domStorageEnabled = true
        //JavaScript 허용
        binding.webViewDaumAddr.settings.javaScriptEnabled = true


        //웹 뷰에 인터페이스 등록
        binding.webViewDaumAddr.addJavascriptInterface(WebAppInterface(), "Android")
        //웹 뷰가 뛰워 진 후에 동작하는 함수

        //웹 뷰로 뛰울 홈페이지 Url 입력
        binding.webViewDaumAddr.loadUrl("http://3.37.253.243/sports_friend/daum.html")

        binding.webViewDaumAddr.webViewClient = object : WebViewClient() {
            //페이지가 완전히 뛰워지면 실행
            override fun onPageFinished(view: WebView, url: String) {
                binding.webViewDaumAddr.loadUrl("javascript:sample2_execDaumPostcode();")
            }

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    //웹뷰에 등록하는 인터페이스
    internal inner class WebAppInterface() {

        //웹뷰에서 주소 정보를 선택 후에 실행되는 함수
        @JavascriptInterface
        fun processDATA(data: String?) {
            //ChoiceAdrress에 있는 구현체가 실행되어서
            //선택한 주소를 구현체가 있는 Fragment에 전달해줌.
            /* addr_flag
           * 1번: 거주지정보
           * 2번: 관심지역정보
           */
            if (flagAddr == 1) {
                fragmentInterfacer?.onButtonClick(data, 1)
            } else if (flagAddr == 2) {
                fragmentInterfacer?.onButtonClick(data, 2)
            }
            dismiss()
        }
    }


    fun setAddressClickListener(fragmentInterfacer: OnclickListener) {
        this.fragmentInterfacer = fragmentInterfacer
    }

    //다이얼로그를 뛰우고 거주지역, 관심지역을 구분해주는 메서드
    //1번: 거주지역
    //2번: 관심지역
    fun showDialog(manager: FragmentManager, TAG: String, flagAddr: Int) {
        this.flagAddr = flagAddr
        show(manager, TAG)
    }

}






