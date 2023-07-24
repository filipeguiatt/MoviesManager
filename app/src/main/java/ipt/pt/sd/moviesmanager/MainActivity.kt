package ipt.pt.sd.moviesmanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import ipt.pt.sd.moviesmanager.models.Movie
import ipt.pt.sd.moviesmanager.models.Search
import ipt.pt.sd.moviesmanager.models.User

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.movie_item_view.*
import retrofit2.Response
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity() {

    //Declaração das listas de filmes
    val mList = mutableListOf<Movie>()
    val fList = mutableListOf<Movie>()
    private val rCode = 1
    private val lCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        list.adapter = ListAdapter(fList)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid


        if (!uid.isNullOrEmpty()) {
            // Fetch the user's data from the Realtime Database
            val userPictureRef = FirebaseDatabase.getInstance("https://moviesmanager-99a06-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("/users/$uid/picture")
            Log.d("ola",userPictureRef.toString())
            Handler(Looper.getMainLooper()).postDelayed({
                userPictureRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val pictureUrl = snapshot.getValue(String::class.java)
                        if (!pictureUrl.isNullOrEmpty()) {
                            imgPhoto.visibility = View.VISIBLE
                            // Display da imagem do user
                            Glide.with(this@MainActivity)
                                .load(pictureUrl)
                                .transform(CenterCrop(), RoundedCorners(16))
                                .into(imgPhoto)
                        } else {
                            //se nao tiver imagem mete a imageView invisivel
                            imgPhoto.visibility = View.GONE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle any errors that occurred while fetching the user's data
                    }
                })
            }, 5000)
        }

        val myStrings = arrayOf("Author", "Title", "Clear")
        var call = API.create().getData()
        btn_movies.setBackgroundColor(Color.parseColor("#4D101010"))

        callMovies()

        btnSubmit.setOnClickListener {
            val searchTxt = lblEdit.text.toString()
            //teclado desaparece
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(lblEdit.windowToken, 0)

            fList.clear()
            fList.addAll(
                mList.filter {
                    it.title?.lowercase()?.contains(searchTxt.lowercase()) == true
                }
            )
            list.adapter?.notifyDataSetChanged()
        }
            btn_movies.setOnClickListener{
                callMovies()
                btn_movies.setBackgroundColor(Color.parseColor("#4D101010"))
                btn_tv.setBackgroundColor(Color.parseColor("#4D4D4D4D"))
            }

        btn_tv.setOnClickListener{
            callSeries()
            btn_tv.setBackgroundColor(Color.parseColor("#4D101010"))
            btn_movies.setBackgroundColor(Color.parseColor("#4D4D4D4D"))
        }

        btnLogOut.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        //Insere linha que separa cada item(Filme/Serie)
        val divider = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.recycleview_divider)?.let {
            divider.setDrawable(it)
        }
        list.addItemDecoration(divider)


        btnFavsList.setOnClickListener{
            //val intent = Intent(this, FavoritesList::class.java)
            //startActivity(intent)
        }

    }


        //Função para buscar filmes
        private fun callMovies() {
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


    private fun callSeries (){
        var call =  API.create().getDataTv()

        call.enqueue(object : Callback<Search> {
            override fun onFailure(call: Call<Search>, t: Throwable) {
                Log.e("onFailure error", call.request().url().toString())
                Log.e("onFailure error", t.message!!)
            }

            override fun onResponse(call: Call<Search>, response: Response<Search>) {
                response.body()?.let {
                    //o simbolo de estar a pensar começa a trabalhar e quando chega aqui a visibilidade passa a nula, pois estamos a adicionar o texto
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
