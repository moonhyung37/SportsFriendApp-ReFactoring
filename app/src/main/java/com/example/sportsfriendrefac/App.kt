package com.example.sportsfriendrefac

import android.app.Application
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import androidx.loader.content.CursorLoader
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.*
import timber.log.Timber
import java.io.IOException


@HiltAndroidApp
class App : Application() {
    private val PREF_NAME = "MyDataStore"
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREF_NAME)

    //바텀내비게이션 프래그먼트 밖에서 모집 글의 수정, 삭제가 발생했을 때 모집 글목록 프래그먼트에 오면 모집글을 갱신시키는 flag
    //-바텀내비게이션이 재생성 되지 않기 때문에 만들었음.
    //0번 : 갱신 X
    //1번 : 갱신 O
    var flagBulletinSelect = 0


    /*private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = PREF_NAME,
        produceMigrations = { context ->
            listOf(SharedPreferencesMigration(context,
                PREF_NAME,
                setOf(EXAMPLE_DATA1.name, EXAMPLE_DATA2.name)))
        }
    )*/

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

    }

    //객체 생성
    init {
        instance = this
    }

    fun context(): Context {
        return instance.applicationContext
    }

    /* 내부에 readValue, writeValue가 사용되었음. */
    //읽기
    suspend fun setStringData(key: String, value: String) {
        dataStore.writeValue(stringPreferencesKey(key), value)
        Timber.d("1")
    }

    //쓰기
    suspend fun getStringData(key: String): String? {
        return dataStore.readValue(stringPreferencesKey(key))
    }

    //초기화
    suspend fun clearDataStore() {
        return dataStore.storeClear()
    }


    suspend inline fun <T : Any> DataStore<Preferences>.readValue(key: Preferences.Key<T>): T? {
        //에러가 생길 시 recoverOrThrow() 실행
        //아닌 경우 [key]로 값 갖고오기
        //값이 없는 경우 Null 반환
        return data.catch { recoverOrThrow(it) }.map { it[key] }.firstOrNull()
    }

    suspend inline fun <T : Any> DataStore<Preferences>.writeValue(
        key: Preferences.Key<T>,
        value: T?,
    ) {
        edit { preferences ->
            //저장하려는 값이 null인 경우 쉐어드에 저장된 값 삭제
            if (value == null) {
                preferences.remove(key)
            } else {
                //값 저장
                preferences[key] = value
            }
        }
    }

    //초기화
    suspend inline fun DataStore<Preferences>.storeClear() {
        edit { preferences ->
            preferences.clear()
            Timber.d("데이터스토어 초기화")
        }
    }

    //예외처리
    suspend fun FlowCollector<Preferences>.recoverOrThrow(throwable: Throwable) {
        if (throwable is IOException) {
            emit(emptyPreferences())
        } else {
            throw throwable
        }
    }

    //상대경로 -> 절대경로 변환
    fun getRealpath(context: Context, contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        var result: String? = null

        val cursorLoader = CursorLoader(context, contentUri, proj, null, null, null)
        val cursor = cursorLoader.loadInBackground()

        if (cursor != null) {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            result = cursor.getString(columnIndex)
            cursor.close()
        }
        return result
    }

}
/* //Preference DataStore 읽기/쓰기 메서드 모음
 suspend fun saveInt(key: String, value: Int) {
     val dataStoreKey = intPreferencesKey(key)

     context().dataStore.edit { preference ->
         preference[dataStoreKey] = value
     }
 }

 suspend fun readInt(key: String): Int? {
     val dataStoreKey = intPreferencesKey(key)
     val preferences = context().dataStore.data.first()
     return preferences[dataStoreKey]
 }

 suspend fun saveString(key: String, value: String) {
     val dataStoreKey = stringPreferencesKey(key)
     context().dataStore.edit { preference ->
         preference[dataStoreKey] = value
     }
 }

 suspend fun readString(key: String): String? {
     val dataStoreKey = stringPreferencesKey(key)
     val preferences = context().dataStore.data.first()
     return preferences[dataStoreKey]
 }*/
