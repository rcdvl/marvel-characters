package com.rcdvl.marvel.ui.search

import android.support.v4.view.ViewCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rcdvl.marvel.R
import com.rcdvl.marvel.ui.details.CharacterDetailsActivity
import com.rcdvl.marvel.model.MarvelCharacter
import com.rcdvl.marvel.ui.util.GlideApp
import java.util.*

/**
 * Created by renan on 3/17/16.
 */
class CharactersSearchAdapter : RecyclerView.Adapter<CharacterViewHolder>() {

    var characters: MutableList<MarvelCharacter> = ArrayList()
    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CharacterViewHolder? {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_character_search, parent, false)

        view.setOnClickListener {
            recyclerView?.let {
                val pos = it.getChildAdapterPosition(view)
                CharacterDetailsActivity.startActivity(it.context, characters[pos],
                        view.findViewById(R.id.characterImage), view.findViewById(R.id.characterName))
            }
        }

        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder?, position: Int) {
        holder?.characterName?.text = characters[position].name

        ViewCompat.setTransitionName(holder?.characterImage, characters[position].id.toString())
        ViewCompat.setTransitionName(holder?.characterName, "${characters[position].id}-name")

        GlideApp.with(holder?.itemView?.context)
                .load(characters[position].thumbnail.path + '.' + characters[position].thumbnail.extension)
                .transition(DrawableTransitionOptions.withCrossFade())
                .dontTransform()
                .into(holder?.characterImage)
    }

    override fun getItemCount() = characters.size

    fun addChars(marvelCharacters: ArrayList<MarvelCharacter>) {
        characters.addAll(marvelCharacters)
    }
}

class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val characterName: TextView = view.findViewById(R.id.characterName)
    val characterImage: ImageView = view.findViewById(R.id.characterImage)
}
