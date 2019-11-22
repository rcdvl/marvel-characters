package com.rcdvl.marvel.ui.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rcdvl.marvel.R
import com.rcdvl.marvel.model.MarvelCharacter
import com.rcdvl.marvel.ui.details.CharacterDetailsActivity
import com.rcdvl.marvel.ui.util.GlideApp
import java.util.*

/**
 * Created by renan on 3/17/16.
 */
class CharactersAdapter : RecyclerView.Adapter<CharacterViewHolder>() {

    private var recyclerView: RecyclerView? = null
    var characters: ArrayList<MarvelCharacter> = ArrayList()

    companion object {
        const val EXTRA_CHARACTER = "extra-character"
        const val EXTRA_CHARACTER_IMAGE_TRANSITION_NAME = "extra-character-transition-name"
        const val EXTRA_CHARACTER_NAME_TRANSITION_NAME = "extra-character-name-transition-name"
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_character,
                parent, false)

        view.setOnClickListener {
            recyclerView?.let {
                val pos = it.getChildAdapterPosition(view)
                CharacterDetailsActivity.startActivity(it.context, characters[pos],
                        view.findViewById(R.id.characterImage),
                        view.findViewById(R.id.characterName))
            }
        }

        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.characterName?.text = characters[position].name

        ViewCompat.setTransitionName(holder.characterImage, characters[position].id.toString())
        ViewCompat.setTransitionName(holder.characterName, "${characters[position].id}-name")

        GlideApp.with(holder.itemView?.context)
                .load(characters[position].thumbnail.path + '.' + characters[position].thumbnail.extension)
                .transition(DrawableTransitionOptions.withCrossFade())
                .dontTransform()
                .into(holder.characterImage)
    }

    override fun getItemCount() = characters.size
}

class CharacterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val characterName: TextView = view.findViewById(R.id.characterName)
    val characterImage: ImageView = view.findViewById(R.id.characterImage)
}
