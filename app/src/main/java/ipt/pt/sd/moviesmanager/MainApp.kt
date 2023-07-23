package ipt.pt.sd.moviesmanager

import android.app.Application
import com.google.firebase.FirebaseApp
import ipt.pt.sd.moviesmanager.models.Movie

class MainApp : Application() {

    companion object{
        //lateinit var favouritesHelper: FavouritesHelper
        val movieList = mutableListOf<Movie>()
    }

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        //favouritesHelper  = FavoritesHelper()
    }
}