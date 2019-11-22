package com.rcdvl.marvel.ui.list

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.paginate.recycler.LoadingListItemCreator
import com.rcdvl.marvel.R

/**
 * Created by renan on 3/17/16.
 */
class CharacterListLoadingItemCreator : LoadingListItemCreator {
    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder?, position: Int) {

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder? {
        val inflater = LayoutInflater.from(parent?.context)
        val view = inflater.inflate(R.layout.character_loading_list_item, parent, false)
        return object : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {}
    }
}
