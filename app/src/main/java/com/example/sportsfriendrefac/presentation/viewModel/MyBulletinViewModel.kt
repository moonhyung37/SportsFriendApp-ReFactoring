package com.example.sportsfriendrefac.presentation.viewModel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.example.sportsfriendrefac.App
import com.example.sportsfriendrefac.base.BaseViewModel
import com.example.sportsfriendrefac.domain.bulletinUseCase.BulletinAddEditUseCase
import com.example.sportsfriendrefac.domain.bulletinUseCase.BulletinDeleteUseCase
import com.example.sportsfriendrefac.domain.bulletinUseCase.BulletinSelectUseCase
import com.example.sportsfriendrefac.domain.model.BulletinEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

/* 내가 작성한 모집글 수정, 삭제 작업 진행*/
@HiltViewModel
class MyBulletinViewModel @Inject constructor(
    private val bulletinSelectUseCase: BulletinSelectUseCase, //모집글 조회
    private val bulletinAddEditUseCase: BulletinAddEditUseCase, //모집글 수정
    private val bulletinDeleteUseCase: BulletinDeleteUseCase, //모집글 조회

) : BaseViewModel() {

    //모집글 관련 이벤트 처리(C,R,U,D)ㅁ
    private val _sharedBulletin = MutableSharedFlow<EventMyBulletinSealed>()
    val sharedBulletin = _sharedBulletin.asSharedFlow()


    fun selectMyBulletin(myUserIdx: String) {
        //flag 2번: 내가 작성한 모집글 유스케이스 실행
        //selectFlag 1번: 전체 모집글 조회
        bulletinSelectUseCase(2, 1, "", myUserIdx, viewModelScope) {
            emitEventBulletin(EventMyBulletinSealed.MyBulletinSelect(it))
        }
    }

    //내가 작성한 모집 글 삭제 유스케이스 실행
    fun deleteMyBulletin(bltn_idx: String) {
        bulletinDeleteUseCase(bltn_idx, viewModelScope) {
            emitEventBulletin(EventMyBulletinSealed.MyBulletinDelete(it))
        }
    }


    // TODO: 이미지 경로 문제 해결하기 절대경로로 바뀌기 전에 이미 변환되어있어서 절대경로로 바뀌는 메서드 사용하면 Null반환
    //내가 작성한 모집 글 수정 유스케이스 실행
    fun updateMyBulletin(bulletinEntity: BulletinEntity, context: Context) {
        val list_MultiPartImage: ArrayList<MultipartBody.Part> = ArrayList()

        /* uri -> MultiPart.part로 변경 후 List에 담기  */
        if (bulletinEntity.bltn_img_url != null && bulletinEntity.bltn_img_url.isNotEmpty()) {
            //imageUri를 잘라서 리스트에 넣기
            val list_imageUri = bulletinEntity.bltn_img_url.split("@")
            //이미지의 개수만큼 Multipart.body 형식으로 변환 후에 리스트에 추가
            list_imageUri.mapIndexed { i, it ->
                var realPathUri: String? = null ?: ""

                //이미지가 절대경로가 아닌경우 (이미지 수정됨)
                if (!it.contains("http://")) {
                    realPathUri = App.instance.getRealpath(context, Uri.parse(it))
                    //절대경로 변환
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
        }

        //flag 2번 모집글 수정 유스케이스 실행
        bulletinAddEditUseCase(
            //요청데이터
            2,
            bulletinEntity.idx,
            bulletinEntity.bltn_title,
            bulletinEntity.bltn_content,
            list_MultiPartImage,
            bulletinEntity.bltn_exer,
            bulletinEntity.bltn_addr,
            viewModelScope) {
            //모집글 프래그먼트에 추가한 모집글 정보 전달하기
            emitEventBulletin(EventMyBulletinSealed.MyBulletinUpdate(it))
        }
    }


    /* 모집글 처리 관련 이벤트 클래스 */
    sealed class EventMyBulletinSealed {
        //내가 작성한 모집 글 조회
        data class MyBulletinSelect(val List_bulletinEntity: List<BulletinEntity>) :
            EventMyBulletinSealed()

        //내가 작성한 모집 글 삭제
        data class MyBulletinDelete(val bltn_idx: String) :
            EventMyBulletinSealed()

        //내가 작성한 모집 글 수정
        data class MyBulletinUpdate(val bulletinEntity: BulletinEntity) :
            EventMyBulletinSealed()
    }


    //서버에서 보낸 이벤트를 SharedFlow에 전달해주는 메서드
    private fun emitEventBulletin(eventBulletinSealed: EventMyBulletinSealed) {
        viewModelScope.launch {
            _sharedBulletin.emit(eventBulletinSealed)
        }
    }
}