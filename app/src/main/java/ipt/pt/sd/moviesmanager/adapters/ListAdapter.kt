package ipt.pt.sd.moviesmanager.adapters




import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import ipt.pt.sd.moviesmanager.R

import ipt.pt.sd.moviesmanager.models.Movie
import kotlinx.android.synthetic.main.movie_item_view.view.*


class ListAdapter (private val itemsList: MutableList<Movie>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.movie_item_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //Obtém referência para o layout da view do item na posição position
        val layout = holder.layoutView
        //Obtém o objeto media da lista de itens na posição position
        val media = itemsList[position]
        //Define o texto do titulo da view como titulo da media, ou name se o titulo for nulo, ou N/A se ambos forem nulos
        layout.title.text = media.title ?: media.name

        //Define o texto da data de lançamento da View como a data de lançamento da media, ou first_air_date se a data lançamento for nula, ou N/A se ambos forem nulos
        layout.release_date.text = media.release_date ?: media.first_air_date ?: "N/A"

        layout.vote_average.text = media.vote_average
        layout.overview.text = media.overview

        //Verifica se o urlToImage é nulo e executa o código se for verdade
        (layout.urlToImage == null).let {
            val baseURLImage = "w500"

            //Constrói o path da imagem
            media.poster_path = "https://image.tmdb.org/t/p/" +baseURLImage + media.poster_path

            Glide.with(layout.context)
                .load(media.poster_path)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(layout.urlToImage)

        }

    }


    //retorna o número total de items da recyclerView
    override fun getItemCount() = itemsList.size

    //Classe de ViewHolder que representa cada item da RecyclerView
    data class ViewHolder(val layoutView: View) : RecyclerView.ViewHolder(layoutView)
    }


