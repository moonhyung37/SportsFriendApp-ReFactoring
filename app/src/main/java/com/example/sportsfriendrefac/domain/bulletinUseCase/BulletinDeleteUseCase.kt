package com.example.sportsfriendrefac.domain.bulletinUseCase

import com.example.sportsfriendrefac.domain.repository.BulletinRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BulletinDeleteUseCase(private val bulletinRepository: BulletinRepository) {


    //invoke fun -> 클래스 객체 생성시 바로 실행? (확실X)
    operator fun invoke(
        bltn_idx: String,
        scope: CoroutineScope,
        onResult: (String) -> Unit = {},
    ) {
        //코루틴을 사용해 Retrofit API 통신을 진행
        scope.launch(Dispatchers.IO) {
            //결과값을 반환하기 위해 Deffered 사용
            val response = async {
                bulletinRepository.deleteMyBulletin(
                    bltn_idx
                )
            }
            //응답 메세지
            onResult(response.await())
        }
    }
}