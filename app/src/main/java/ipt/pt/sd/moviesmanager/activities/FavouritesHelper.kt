package ipt.pt.sd.moviesmanager.activities

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import ipt.pt.sd.moviesmanager.R
import ipt.pt.sd.moviesmanager.models.Movie

class FavouritesHelper (context: Context) {

    private val adapterType = Types.newParameterizedType(List::class.java, Movie::class.java)
    private val jsonAdapter = Moshi.Builder().build().adapter<List<Movie>>(adapterType)
    private val favourites = getFavouritesList()
    private val tempList = favourites?.toMutableList()

    private val mSharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(
            context.getString(R.string.app_name),
            Context.MODE_PRIVATE
        )
    }

    companion object{
        const val FAVOURITES_KEY = "favourites"
    }

    fun getFavouritesList(): List<Movie>? {
        val json = mSharedPreferences.getString(FAVOURITES_KEY, null)
        json?.let {
            return jsonAdapter.fromJson(json)
        }
        return null
    }

    fun addFavourite(Movie: Movie){
        val newList = mutableListOf(Movie)

        if (favourites == null){
            mSharedPreferences.edit()
                .putString(FAVOURITES_KEY, jsonAdapter.toJson(newList))
                .apply()
        }
        else{
            tempList?.add(Movie)
            mSharedPreferences.edit()
                .putString(FAVOURITES_KEY, jsonAdapter.toJson(tempList))
                .apply()

        }
    }

    fun removeFavourite(Movie: Movie){
        if(favourites != null){
            tempList?.remove(Movie)
            mSharedPreferences.edit()
                .putString(FAVOURITES_KEY, jsonAdapter.toJson(tempList))
                .apply()
        }
    }


    fun containsFavourite(Movie: Movie){
        favourites?.contains(Movie).let {
            addFavourite(Movie)
        }
    }

}