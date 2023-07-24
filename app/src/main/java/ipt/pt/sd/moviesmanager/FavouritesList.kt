package ipt.pt.sd.moviesmanager

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import ipt.pt.sd.moviesmanager.models.Movie
import kotlinx.android.synthetic.main.activity_favourites_list.btnGoBackMain
import kotlinx.android.synthetic.main.activity_favourites_list.listFav

class FavoritesList : AppCompatActivity() {
    val mList = mutableListOf<Movie>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites_list)

        mList.addAll(MainApp.favouritesHelper.getFavouritesList() ?: listOf())

        listFav.adapter = ListAdapterFavourites(mList)
        

        listFav.layoutManager = GridLayoutManager(this, 2)

        btnGoBackMain?.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent())
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }
}