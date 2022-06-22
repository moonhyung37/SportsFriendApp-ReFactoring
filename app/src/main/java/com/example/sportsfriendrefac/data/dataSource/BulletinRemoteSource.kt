package com.example.sportsfriendrefac.data.dataSource

import android.util.Log
import com.example.sportsfriendrefac.base.BaseDataSource
import com.example.sportsfriendrefac.data.model.Bulletin
import com.example.sportsfriendrefac.data.retrofitService.BulletinService
import com.example.sportsfriendrefac.util.ApiResult
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject

abstract class BulletinRemoteSource : BaseDataSource() {
    abstract suspend fun selectBulletin(selectFlag: Int, address: String): ApiResult<List<Bulletin>>

    abstract suspend fun selectMyBulletin(myUserIdx: String): ApiResult<List<Bulletin>>


    abstract suspend fun addEditBulletin(
        flag: Int,
        idx: String,
        title: String,
        content: String,
        imageList: List<MultipartBody.Part>,
        interestExer: String,
        address: String,
    ): ApiResult<Bulletin>

    abstract suspend fun deleteBulletin(
        bltn_idx: String,
    ): ApiResult<String>


}

class BulletinRemoteSourceImpl @Inject constructor(
    private val bulletinService: BulletinService,
) : BulletinRemoteSource() {

    //모집글 조회
    //selectFlag 1번: 전체 조회 2번: 거주지역 3번: 관심지역
    override suspend fun selectBulletin(
        selectFlag: Int,
        address: String,
    ): ApiResult<List<Bulletin>> =
        getResponse(bulletinService.selectBulletin(selectFlag, address, "select_bulletin.php"))

    //내가 작성한 모집 글 조회
    override suspend fun selectMyBulletin(myUserIdx: String): ApiResult<List<Bulletin>> =
        getResponse(bulletinService.selectMyBulletin("select_my_bulletin.php", myUserIdx))


    //모집글 추가, 수정
    //-추가 -> userIdx
    //-수정 -> bltnIdx
    override suspend fun addEditBulletin(
        flag: Int,
        idx: String,
        title: String,
        content: String,
        imageList: List<MultipartBody.Part>,
        interestExer: String,
        address: String,
    ): ApiResult<Bulletin> {
        //1번 모집 글 추가
        if (flag == 1) {
            return getResponse(bulletinService.addBulletin(
                "insert_bulletin.php",
                idx,
                title,
                content,
                interestExer,
                address,
                imageList.size,
                imageList
            ))
        }
        //2번 모집 글 수정
        else {
            return getResponse(bulletinService.updateBulletin(
                "update_bulletin.php",
                idx,
                title,
                content,
                interestExer,
                address,
                imageList.size,
                imageList
            ))
        }
    }

    //모집글 삭제
    override suspend fun deleteBulletin(bltn_idx: String): ApiResult<String> {
        Timber.d("111")
        return getResponse(bulletinService.deleteMyBulletin("delete_bulletin.php", bltn_idx))
    }


}