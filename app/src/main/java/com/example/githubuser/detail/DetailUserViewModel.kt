package com.example.githubuser.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.api.ApiConfig
import com.example.githubuser.database.FavoriteUser
import com.example.githubuser.database.FavoriteUserDao
import com.example.githubuser.database.FavoriteUserRoomDatabase
import com.example.githubuser.model.DetailUserResponse
import com.example.githubuser.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailUserViewModel(application: Application) : AndroidViewModel(application) {
    private val detailUser = MutableLiveData<DetailUserResponse>()

    private var favoriteUserDao: FavoriteUserDao?
    private var favoriteUserRoomDatabase: FavoriteUserRoomDatabase?

    init {
        favoriteUserRoomDatabase = FavoriteUserRoomDatabase.getDatabase(application)
        favoriteUserDao = favoriteUserRoomDatabase?.favoriteUserDao()
    }

    fun setDetailUser(username: String) {
        ApiConfig.apiInstance.getDetailUser(username)
            .enqueue(object : Callback<DetailUserResponse> {
                override fun onResponse(
                    call: Call<DetailUserResponse>,
                    response: Response<DetailUserResponse>
                ) {
                    if (response.isSuccessful) {
                        detailUser.postValue(response.body())
                    } else {
                        Log.e("Failure", response.message())
                    }
                }

                override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                    Log.e("Failure", t.message.toString())
                }
            })
    }

    fun getDetailUser(): LiveData<DetailUserResponse> {
        return detailUser
    }

    fun addFavorite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var favoriteUser = FavoriteUser(
                username,
                id,
                avatarUrl
            )
            favoriteUserDao?.insertFavoriteUser(favoriteUser)
        }
    }

    suspend fun checkUser(id: Int) = favoriteUserDao?.checkUser(id)

    fun deleteFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            favoriteUserDao?.deleteFavoriteUser(id)
        }
    }
}
