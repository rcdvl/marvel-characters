package com.rcdvl.marvel.ui.details

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rcdvl.marvel.R
import com.rcdvl.marvel.model.MarvelResource
import com.rcdvl.marvel.ui.resourcedetails.ResourceDetailsActivity
import com.rcdvl.marvel.ui.resourcedetails.ResourceDetailsFragment
import com.rcdvl.marvel.ui.util.GlideApp
import java.util.*

/**
 * Created by renan on 3/17/16.
 */
class CharacterResourceAdapter(var resources: ArrayList<MarvelResource>) : RecyclerView.Adapter<ResourceViewHolder>() {

    var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ResourceViewHolder? {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_character_resource, parent, false)
        return ResourceViewHolder(v)
    }

    override fun onBindViewHolder(holder: ResourceViewHolder?, position: Int) {
        holder?.resourceText?.text = resources[position].title

        holder?.itemView?.setOnClickListener {
            val intent = Intent(recyclerView?.context, ResourceDetailsActivity::class.java)
            intent.putExtra(ResourceDetailsFragment.EXTRA_RESOURCES, resources)
            intent.putExtra(ResourceDetailsFragment.EXTRA_SELECTED_INDEX, position)

            (recyclerView?.context as AppCompatActivity).startActivity(intent)
        }

        val resource = resources[position]
        resource.thumbnail?.let {
            loadImage(holder, "${resource.thumbnail?.path}.${resource.thumbnail?.extension}")
        }
    }

    private fun loadImage(holder: ResourceViewHolder?, url: String) {
        if ((holder?.itemView?.context as CharacterDetailsActivity).isAvailable) {
            GlideApp.with(holder.itemView?.context as Activity)
                    .load(url)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(holder.resourceImage)
        }
    }

    fun addResources(resources: ArrayList<MarvelResource>) {
        this.resources.addAll(resources)
    }

    override fun getItemCount() = resources.size
}

class ResourceViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val resourceText: TextView = view.findViewById(R.id.resourceText)
    val resourceImage: ImageView = view.findViewById(R.id.resourceImage)
}
