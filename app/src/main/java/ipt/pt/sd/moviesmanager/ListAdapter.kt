package ipt.pt.sd.moviesmanager


import android.content.Intent
import java.util.*

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ipt.pt.sd.moviesmanager.models.Movie



class ListAdapter (private val itemsList: MutableList<Movie>) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.movie_item_view, parent, false))
    }

    override fun onBindViewHolder(holder: ListAdapter.ViewHolder, position: Int) {
        val layout = holder.layoutView
        val article = itemsList[position]


    }

    override fun getItemCount() = itemsList.size

    data class ViewHolder(val layoutView: View) : RecyclerView.ViewHolder(layoutView)
    }


