package ipt.pt.sd.moviesmanager


import android.os.Bundle
import android.telecom.Call
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import ipt.pt.sd.moviesmanager.models.Movie
import ipt.pt.sd.moviesmanager.models.Search
import javax.security.auth.callback.Callback
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    //Declaração das listas de filmes
    val mList = mutableListOf<Movie>()
    val fList = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callMovies()

        //Função para buscar filmes
        fun callMovies() {
            var call = API.create().getData()

            call.enqueue(object : Callback<Search> {
                override fun onFailure(call: Call<Search>, t: Throwable) {
                    Log.e("onFailure error", call.request().url().toString())
                    Log.e("onFailure error", t.message!!)
                }

                override fun onResponse(call: Call<Search>, response: Response<Search>) {
                    response.body()?.let {
                        // Ocultar o símbolo de carregamento
                        homeprogress1.visibility = ViewPager.GONE

                        mList.clear()
                        mList.addAll(it.results)

                        fList.clear()
                        fList.addAll(it.results)

                        list.adapter?.notifyDataSetChanged()
                    }
                }
            })
        }

    }
}