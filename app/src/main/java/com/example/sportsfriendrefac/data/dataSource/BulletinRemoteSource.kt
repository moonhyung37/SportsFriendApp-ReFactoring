package com.example.sportsfriendrefac.data.dataSource

import com.example.sportsfriendrefac.base.BaseDataSource
import com.example.sportsfriendrefac.data.model.Bulletin
import com.example.sportsfriendrefac.data.retrofitService.BulletinService
import com.example.sportsfriendrefac.util.ApiResult
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

abstract class BulletinRemoteSource : BaseDataSource() {
    abstract suspend fun requestBulletin(): ApiResult<List<Bulletin>>
}

class BulletinRemoteSourceImpl @Inject constructor(
    private val bulletinService: BulletinService,
) : BulletinRemoteSource() {

    //모집글 정보 서버에 요청하기
    override suspend fun requestBulletin(): ApiResult<List<Bulletin>> =
        getResponse(bulletinService.requestBulletin("select_bulletin.php"))
}