package ipt.pt.sd.moviesmanager

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import ipt.pt.sd.moviesmanager.models.Movie

class FavouritesHelper (context: Context) {

    private val adapterType = Types.newParameterizedType(List::class.java, Movie::class.java)
    private val jsonAdapter = Moshi.Builder().build().adapter<List<Movie>>(adapterType)

    companion object{
        const val FAVOURITES_KEY = "favourites"
    }

}