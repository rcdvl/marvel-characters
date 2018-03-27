package com.rcdvl.marvel.ui.search

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paginate.Paginate
import com.rcdvl.marvel.MarvelApplication
import com.rcdvl.marvel.R
import com.rcdvl.marvel.model.MarvelCharacter
import com.rcdvl.marvel.ui.GenericPaginationCallback
import com.rcdvl.marvel.ui.MarvelViewModelFactory
import com.rcdvl.marvel.ui.list.CharacterListLoadingItemCreator
import com.rcdvl.marvel.ui.list.CharacterListViewModel
import kotlinx.android.synthetic.main.fragment_character_list.*
import java.util.*
import javax.inject.Inject

/**
 * Created by renan on 3/21/16.
 */
class CharacterSearchFragment : Fragment() {

    var query: String = ""
    lateinit var adapter: CharactersSearchAdapter
    private lateinit var viewModel: CharacterListViewModel
    @Inject
    lateinit var marvelViewModelFactory: MarvelViewModelFactory

    companion object {
        fun newInstance(query: String): CharacterSearchFragment {
            val args = Bundle().apply {
                putSerializable(CharacterSearchFragment.EXTRA_QUERY, query)
            }
            return CharacterSearchFragment().apply {
                arguments = args
            }
        }

        const val EXTRA_QUERY = "extra-query"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity.application as MarvelApplication).appComponent.inject(this)
        viewModel = ViewModelProviders.of(this,
                marvelViewModelFactory)[CharacterListViewModel::class.java]
        viewModel.liveData.observe(this,
                Observer<ArrayList<MarvelCharacter>> { characters: ArrayList<MarvelCharacter>? ->
                    if (characters == null) {
                        errorView.visibility = View.VISIBLE
                        return@Observer
                    }

                    if (characters.isEmpty()) {
                        return@Observer
                    }

                    val count = adapter.characters.size
                    adapter.characters = characters
                    adapter.notifyItemRangeInserted(count + 1, viewModel.count)
                })
        return inflater?.inflate(R.layout.fragment_character_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        val layoutManager = LinearLayoutManager(context)
        charactersList.layoutManager = layoutManager
        charactersList.setHasFixedSize(true)

        adapter = CharactersSearchAdapter()
        adapter.characters = viewModel.liveData.value ?: arrayListOf()
        charactersList.adapter = adapter

        if (savedInstanceState == null) {
            refreshWithNewQuery()
        } else {
            query = arguments.getString(EXTRA_QUERY)
        }

        setupPagination()

        super.onViewCreated(view, savedInstanceState)
    }

    fun refreshWithNewQuery() {
        query = arguments.getString(EXTRA_QUERY)
        viewModel.reset()

        adapter.characters = arrayListOf()
        adapter.notifyDataSetChanged()
    }

    private fun setupPagination() {
        val callback = object : GenericPaginationCallback(viewModel, errorView) {
            override fun onLoadMore() {
                super.onLoadMore()
                viewModel.loadMoreCharacters(query)
            }

        }

        Paginate.with(charactersList, callback)
                .setLoadingTriggerThreshold(2)
                .setLoadingListItemCreator(CharacterListLoadingItemCreator())
                .build()

        errorView.setOnClickListener {
            callback.onLoadMore()
        }
    }
}