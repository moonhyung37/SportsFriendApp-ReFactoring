package com.example.sportsfriendrefac.presentation.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sportsfriendrefac.App
import com.example.sportsfriendrefac.R
import com.example.sportsfriendrefac.base.BaseViewModel
import com.example.sportsfriendrefac.domain.bulletinUseCase.BulletinAddEditUseCase
import com.example.sportsfriendrefac.domain.bulletinUseCase.BulletinSelectUseCase
import com.example.sportsfriendrefac.domain.model.BulletinEntity
import com.example.sportsfriendrefac.domain.model.UserEntity
import com.example.sportsfriendrefac.domain.userUseCase.SelectUserUseCase
import com.example.sportsfriendrefac.domain.userUseCase.UpdateUserImageUseCase
import com.example.sportsfriendrefac.domain.userUseCase.UpdateUserUseCase
import com.example.sportsfriendrefac.util.PageType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val bulletinSelectUseCase: BulletinSelectUseCase, //모집글 조회
    private val bulletinAddEditUseCase: BulletinAddEditUseCase, //모집글 추가
    private val selectUserUseCase: SelectUserUseCase, //회원정보 조회
    private val updateUserUseCase: UpdateUserUseCase, //회원정보 수정
    private val updateUserImageUseCase: UpdateUserImageUseCase, //회원 이미지 수정
) : BaseViewModel() {

    //BottomNavigation View에 처음 보여줄 프래그먼트 화면 설정 PageType.PAGE1
    private val _Live_pageType = MutableLiveData(PageType.PAGE1)
    val live_pageType: LiveData<PageType> = _Live_pageType

    //회원정보 과련 이벤트 처리(C,R,U,D)
    private val _sharedUser = MutableSharedFlow<EventUserSealed>()
    val sharedUser = _sharedUser.asSharedFlow()

    //모집글 관련 이벤트 처리(C,R,U,D)
    private val _sharedBulletin = MutableSharedFlow<EventBulletinSealed>()
    val sharedBulletin = _sharedBulletin.asSharedFlow()


    /*모집 글 정보 조회 관련 코드*/


    /* 바텀네비게이션 프래그먼트 관련 코드 */
    //처음 화면에 보여줄  프레그먼트 세팅
    fun setCurrentPage(menuItemId: Int): Boolean {
        //선택한 페이지의 타입을 반환
        val pageType = getPageType(menuItemId)
        //페이지 타입을 이용해 화면을 전환
        changePageLiveData(pageType)
        return true
    }

    //선택한 프래그먼트 Id에 맞는 Fragment Type 반환
    private fun getPageType(menuItemId: Int): PageType {
        return when (menuItemId) {
            R.id.bulletinNavMenu -> PageType.PAGE1
            R.id.friendListNavMenu -> PageType.PAGE2
            R.id.chatRoomNavMenu -> PageType.PAGE3
            R.id.mypageNavMenu -> PageType.PAGE4
            else -> throw IllegalArgumentException("not found menu item id")
        }
    }

    //프래그먼트 전환
    private fun changePageLiveData(pageType: PageType) {
        //페이지 타입이 같은 경우 return
        if (live_pageType.value == pageType) return
        //다른 경우 PageType을 LiveData에 입력
        _Live_pageType.value = pageType
    }


    //전체 모집 글 정보 리스트 조회 유스케이스 실행
    fun selectAllBulletin(myUserIdx: String) {
        bulletinSelectUseCase(1, myUserIdx, viewModelScope) {
            emitEventBulletin(EventBulletinSealed.BulletinSelect(it))
        }
    }


    //모집 글 추가  유스케이스 실행
    fun addBulletin(bulletinEntity: BulletinEntity, context: Context) {
        val list_MultiPartImage: ArrayList<MultipartBody.Part> = ArrayList()

        /* uri -> MultiPart.part로 변경 */
        if (bulletinEntity.bltn_img_url != null && bulletinEntity.bltn_img_url.isNotEmpty()) {
            //imageUri를 잘라서 리스트에 넣기
            val list_imageUri = bulletinEntity.bltn_img_url.split("@")
            //이미지의 개수만큼 Multipart.body 형식으로 변환 후에 리스트에 추가
            list_imageUri.mapIndexed { i, it ->
                //절대경로 변환
                val realPathUri = App.instance.getRealpath(context, Uri.parse(it))
                val file = File(realPathUri ?: "")
                val fileBody: RequestBody = RequestBody.create(
                    "multipart/form-data".toMediaTypeOrNull(),
                    file.absoluteFile
                )
                val imageKey = "image$i"
                val filePart: MultipartBody.Part =
                    MultipartBody.Part.createFormData(imageKey, realPathUri, fileBody)
                list_MultiPartImage.add(filePart)
            }
        }

        //flag 1번 모집글 추가 유스케이스 실행
        bulletinAddEditUseCase(
            //요청데이터
            1,
            bulletinEntity.user_idx,
            bulletinEntity.bltn_title,
            bulletinEntity.bltn_content,
            list_MultiPartImage,
            bulletinEntity.bltn_exer,
            bulletinEntity.bltn_addr,
            viewModelScope) {
            //모집글 프래그먼트에 추가한 모집글 정보 전달하기
            emitEventBulletin(EventBulletinSealed.BulletinAdd(it))
        }
    }


    //회원정보 조회 유스케이스
    fun selectUserData(userId: String) {
        //SharedFlow으로 응답 값 엑티비티에 전달
        selectUserUseCase(userId, viewModelScope) {
            emitEventUser(EventUserSealed.UserData(it))
        }
    }

    //회원정보 수정 유스케이스
    fun updateUserData(userEntity: UserEntity) {
        updateUserUseCase(userEntity, viewModelScope) {
            emitEventUser(EventUserSealed.UserUpdate(it))
        }
    }

    //회원 프로필 이미지 수정 유스케이스
    fun updateUserImage(userId: String, imageUri: Uri, context: Context) {
        /* uri -> MultiPart.part로 변경 후 List에 담기  */
        //이미지 절대경로 반환
        val realPathUri = App.instance.getRealpath(context, imageUri)
        //이미지 uri로  이미지 파일 생성
        val file = File(realPathUri.toString())
        val requestFile: RequestBody = RequestBody.create(
            "multipart/form-data".toMediaTypeOrNull(),
            file.absoluteFile
        )
        //multiPart.part로 형식 변경
        val imageBody = MultipartBody.Part.createFormData(
            "upload_image",
            file.getName(),
            requestFile
        )

        //회원 이미지 수정 유스케이스 실행
        updateUserImageUseCase(userId, imageBody, viewModelScope) {
            emitEventUser(EventUserSealed.UserUpdate(it))
        }
    }

    /*회원정보 처리 관련 이벤트 클래스 */
    //회원정보 관련 이벤트 처리 클래스
    sealed class EventUserSealed {
        data class UserData(val userEntity: UserEntity) : EventUserSealed()
        data class UserUpdate(val message: String) : EventUserSealed()
    }


    //서버에서 보낸 이벤트를 SharedFlow에 전달해주는 메서드
    private fun emitEventUser(eventUserSealed: EventUserSealed) {
        viewModelScope.launch {
            _sharedUser.emit(eventUserSealed)
        }
    }

    /* 모집글 처리 관련 이벤트 클래스 */
    sealed class EventBulletinSealed {
        data class BulletinSelect(val List_bulletinEntity: List<BulletinEntity>) :
            EventBulletinSealed()

        data class BulletinAdd(val bulletinEntity: BulletinEntity) : EventBulletinSealed()
    }


    //서버에서 보낸 이벤트를 SharedFlow에 전달해주는 메서드
    private fun emitEventBulletin(eventBulletinSealed: EventBulletinSealed) {
        viewModelScope.launch {
            _sharedBulletin.emit(eventBulletinSealed)
        }
    }

}