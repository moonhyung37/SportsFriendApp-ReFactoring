package com.example.sportsfriendrefac.domain.bulletinUseCase

import com.example.sportsfriendrefac.domain.model.BulletinEntity
import com.example.sportsfriendrefac.domain.repository.BulletinRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BulletinSelectUseCase(private val bulletinRepository: BulletinRepository) {

    //invoke fun -> 클래스 객체 생성시 바로 실행? (확실X)
    operator fun invoke(
        flag: Int, //1번 전체 모집글 정보 조회 //2번 내가 작성한 모집글 정보 조회
        myUserIdx: String,
        scope: CoroutineScope,
        onResult: (List<BulletinEntity>) -> Unit = {},
    ) { //코루틴을 사용해 Retrofit API 통신을 진행
        scope.launch(Dispatchers.IO) {
            //1번 전체 모집글 정보 조회
            if (flag == 1) {
                //결과값을 반환하기 위해 Deffered 사용
                val response = async {
                    bulletinRepository.selectBulletin()
                }
                response.await().collectLatest {
                    onResult(it)
                }
            }
            //2번 내가 작성한 모집 글 정보 조회
            else if (flag == 2) {
                val response = async {
                    bulletinRepository.selectMyBulletin(myUserIdx)
                }
                response.await().collectLatest {
                    onResult(it)
                }
            }
        }

    }


}