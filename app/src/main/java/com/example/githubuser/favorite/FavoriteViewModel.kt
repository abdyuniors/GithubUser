package com.example.githubuser.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githubuser.database.FavoriteUser
import com.example.githubuser.database.FavoriteUserDao
import com.example.githubuser.database.FavoriteUserRoomDatabase

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private var favoriteUserDao: FavoriteUserDao?
    private var favoriteUserRoomDatabase: FavoriteUserRoomDatabase?

    init {
        favoriteUserRoomDatabase = FavoriteUserRoomDatabase.getDatabase(application)
        favoriteUserDao = favoriteUserRoomDatabase?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return favoriteUserDao?.getFavoriteUser()
    }

}