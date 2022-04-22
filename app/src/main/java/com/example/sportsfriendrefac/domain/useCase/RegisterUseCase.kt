package com.example.sportsfriendrefac.domain.useCase

import com.example.sportsfriendrefac.data.model.User
import com.example.sportsfriendrefac.domain.repository.UserRepository
import com.example.sportsfriendrefac.util.ApiResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/* DI로 주입받은 repository 인터페이스 */
class RegisterUseCase(private val userRepository: UserRepository) {

    //invoke fun -> 클래스 객체 생성시 바로 실행? (확실X)
    operator fun invoke(
        //입력 값(요청)
        user: User,
        scope: CoroutineScope,
        //결과 값(응답)
        onResult: (ApiResult<User>) -> Unit = {},
    ) {
        //코루틴을 사용해 Retrofit API 통신을 진행
        scope.launch(Dispatchers.Main) {
            //결과값을 반환하기 위해 Deffered 사용
            val deferred = async(Dispatchers.IO) {
                userRepository.registerUser(user)
            }
            //결과값 ViewModel에 전송
            onResult(deferred.await())
        }
    }
}