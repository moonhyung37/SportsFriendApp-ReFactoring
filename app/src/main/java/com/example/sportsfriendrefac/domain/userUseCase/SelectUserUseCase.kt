package com.example.sportsfriendrefac.domain.userUseCase

import com.example.sportsfriendrefac.domain.model.UserEntity
import com.example.sportsfriendrefac.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/* DI로 주입받은 repository 인터페이스 */
class SelectUserUseCase(private val userRepository: UserRepository) {
    //invoke fun -> 클래스 객체 생성시 바로 실행? (확실X)
    operator fun invoke(
        //입력 값(요청)
        userId: String,
        scope: CoroutineScope,
        //결과 값(응답)
        onResult: (UserEntity) -> Unit = {},
    ) {
        //코루틴을 사용해 Retrofit API 통신을 진행
        scope.launch(Dispatchers.IO) {
            //회원정보를 갖고온다.
            val response = async {
                userRepository.selectUserUseCase(userId)
            }
            //결과값 ViewModel에 전송
            onResult(response.await())
        }
    }
}