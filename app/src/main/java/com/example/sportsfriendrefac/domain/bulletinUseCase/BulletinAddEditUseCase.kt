package com.example.sportsfriendrefac.domain.bulletinUseCase

import com.example.sportsfriendrefac.domain.model.BulletinEntity
import com.example.sportsfriendrefac.domain.repository.BulletinRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

/*모집 글 추가/수정 유스케이스*/
class BulletinAddEditUseCase(private val bulletinRepository: BulletinRepository) {

    //invoke fun -> 클래스 객체 생성시 바로 실행? (확실X)
    //idx
    //-추가 -> UserIdx
    //-수정 -> bltnIdx
    operator fun invoke(
        flag: Int, //모집 글의  추가, 수정을 구분해준다. 1번 : 추가 2번 : 수정
        idx: String,
        title: String,
        content: String,
        imageList: List<MultipartBody.Part>,
        interestExer: String,
        address: String,
        scope: CoroutineScope,
        onResult: (BulletinEntity) -> Unit = {},
    ) {
        //코루틴을 사용해 Retrofit API 통신을 진행
        scope.launch(Dispatchers.IO) {
            //결과값을 반환하기 위해 Deffered 사용
            val response = async {
                bulletinRepository.addEditBulletin(flag, idx,
                    title,
                    content,
                    imageList,
                    interestExer,
                    address)
            }
            //응답 메세지 
            onResult(response.await())
        }
    }
}
