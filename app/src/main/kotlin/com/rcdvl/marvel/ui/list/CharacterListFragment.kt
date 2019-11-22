package com.rcdvl.marvel.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.paginate.Paginate
import com.rcdvl.marvel.MarvelApplication
import com.rcdvl.marvel.R
import com.rcdvl.marvel.model.MarvelCharacter
import com.rcdvl.marvel.ui.GenericPaginationCallback
import com.rcdvl.marvel.ui.MarvelViewModelFactory
import kotlinx.android.synthetic.main.fragment_character_list.*
import java.util.*
import javax.inject.Inject

/**
 * Created by renan on 3/17/16.
 */
class CharacterListFragment : Fragment() {
    private lateinit var viewModel: CharacterListViewModel
    private val adapter = CharactersAdapter()
    @Inject
    lateinit var marvelViewModelFactory: MarvelViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        (activity?.application as MarvelApplication).appComponent.inject(this)

        viewModel = ViewModelProviders.of(this,
                marvelViewModelFactory)[CharacterListViewModel::class.java]
        viewModel.liveData.observe(this,
                Observer<ArrayList<MarvelCharacter>> { characters: ArrayList<MarvelCharacter>? ->
                    if (characters == null) {
                        errorView.visibility = View.VISIBLE
                        return@Observer
                    }

                    val count = adapter.characters.size
                    adapter.characters = characters
                    adapter.notifyItemRangeInserted(count + 1, viewModel.count)
                })
        return inflater.inflate(R.layout.fragment_character_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        charactersList.setHasFixedSize(true)
        charactersList.adapter = adapter
        adapter.characters = viewModel.liveData.value ?: arrayListOf()

        val callback = object : GenericPaginationCallback(viewModel, errorView) {
            override fun onLoadMore() {
                super.onLoadMore()
                viewModel.loadMoreCharacters()
            }
        }

        Paginate.with(charactersList, callback)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .setLoadingListItemCreator(CharacterListLoadingItemCreator())
                .build()

        errorView.setOnClickListener {
            callback.onLoadMore()
        }

        super.onViewCreated(view, savedInstanceState)
    }

}
