package com.rcdvl.marvel.ui.details

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.transition.TransitionSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.paginate.Paginate
import com.rcdvl.marvel.MarvelApplication
import com.rcdvl.marvel.R
import com.rcdvl.marvel.di.AppModule
import com.rcdvl.marvel.model.MarvelCharacter
import com.rcdvl.marvel.model.MarvelListWrapper
import com.rcdvl.marvel.model.MarvelResource
import com.rcdvl.marvel.ui.GenericPaginationCallback
import com.rcdvl.marvel.ui.MarvelViewModelFactory
import com.rcdvl.marvel.ui.list.CharacterListLoadingItemCreator
import com.rcdvl.marvel.ui.list.CharactersAdapter
import com.rcdvl.marvel.ui.util.GlideApp
import com.rcdvl.marvel.ui.util.HorizontalSpaceItemDecoration
import com.rcdvl.marvel.ui.util.TextResize
import com.rcdvl.marvel.ui.util.XpEdgeEffect
import kotlinx.android.synthetic.main.fragment_character_details.*
import java.util.*
import javax.inject.Inject
import javax.inject.Named


/**
 * Created by renan on 3/18/16.
 */
class CharacterDetailsFragment : Fragment() {

    private lateinit var character: MarvelCharacter
    private lateinit var viewModel: CharacterDetailsViewModel
    @Inject
    lateinit var marvelViewModelFactory: MarvelViewModelFactory

    @Inject
    @field:[Named(AppModule.CHARACTER_RESOURCES_LINKS)]
    lateinit var contentTypes: Array<String>

    companion object {
        fun newInstance(character: MarvelCharacter, imageTransitionName: String?,
                        nameTransitionName: String): CharacterDetailsFragment {
            val args = Bundle().apply {
                putSerializable(CharactersAdapter.EXTRA_CHARACTER, character)
                putString(CharactersAdapter.EXTRA_CHARACTER_IMAGE_TRANSITION_NAME,
                        imageTransitionName)
                putString(CharactersAdapter.EXTRA_CHARACTER_NAME_TRANSITION_NAME,
                        nameTransitionName)
            }

            return CharacterDetailsFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        character = arguments.getSerializable(CharactersAdapter.EXTRA_CHARACTER) as MarvelCharacter
        (activity.application as MarvelApplication).appComponent.inject(this)
        viewModel = ViewModelProviders.of(this,
                marvelViewModelFactory)[CharacterDetailsViewModel::class.java]

        return inflater?.inflate(R.layout.fragment_character_details, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        GlideApp.with(view)
                .load(character.thumbnail?.path + '.' + character.thumbnail?.extension)
                .dontTransform()
                .into(characterImage)

        GlideApp.with(view)
                .load(R.drawable.background)
                .into(backgroundImage)

        XpEdgeEffect.setColor(scrollView, Color.RED)

        characterName.text = character.name

        if (TextUtils.isEmpty(character.description)) {
            descriptionContainer.visibility = View.GONE
        } else {
            descriptionText.text = character.description
        }

        setupHorizontalLists()
        setupBottomButtons()
        startSharedElementTransition()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupHorizontalLists() {
        val lists = arrayOf(comicsList, seriesList, storiesList, eventsList)
        val listsContents = arrayOf(character.comics, character.series, character.stories,
                character.events)
        val listsContainers = arrayOf(comicsContainer, seriesContainer, storiesContainer,
                eventsContainer)

        lists.forEachIndexed { index, list ->
            list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            list.addItemDecoration(HorizontalSpaceItemDecoration())
            hideIfEmpty(listsContainers[index], listsContents[index])
            setupPagination(lists[index], contentTypes[index])
        }
    }

    private fun setupBottomButtons() {
        val miscButtonsMap = mapOf("detail" to detailButton, "wiki" to wikiButton,
                "comiclink" to comiclinkButton)
        miscButtonsMap.forEach { entry: Map.Entry<String, AppCompatTextView> ->
            setMiscButtonListener(entry.value, entry.key)
        }
    }

    @SuppressLint("NewApi")
    private fun startSharedElementTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val imageTransitionName = arguments.getString(
                    CharactersAdapter.EXTRA_CHARACTER_IMAGE_TRANSITION_NAME)
            val nameTransitionName = arguments.getString(
                    CharactersAdapter.EXTRA_CHARACTER_NAME_TRANSITION_NAME)
            characterImage.transitionName = imageTransitionName
            characterName.transitionName = nameTransitionName

            val transition = activity.window.sharedElementEnterTransition
            if (transition is TransitionSet) {
                transition.addTransition(TextResize())
            }
        }

        contentLayout.requestFocus()
        activity.supportStartPostponedEnterTransition()
    }

    private fun hideIfEmpty(view: View, list: MarvelListWrapper?) {
        if (list == null || list.items == null || list.items!!.size == 0) view.visibility = View.GONE
    }

    private fun setupPagination(list: RecyclerView, resourceType: String) {
        val adapter = CharacterResourceAdapter(ArrayList())
        list.adapter = adapter

        val viewModel = viewModel.characterResourcesViewModels[resourceType]!!
        val paginationCallback = object : GenericPaginationCallback(viewModel, null) {
            override fun onLoadMore() {
                viewModel.loadMore(character.id)
            }

        }

        viewModel.liveData.observe(this, Observer<ArrayList<MarvelResource>> {
            it?.let {
                adapter.resources = it
                adapter.notifyDataSetChanged()
            }
        })

        Paginate.with(list, paginationCallback)
                .setLoadingListItemCreator(CharacterListLoadingItemCreator())
                .build()
    }

    private fun setMiscButtonListener(button: View, type: String) {
        button.setOnClickListener({
            val intent = Intent(Intent.ACTION_VIEW)
            val detailUrl = character.urls?.first { it.type.equals(type, true) }

            if (detailUrl != null) {
                intent.data = Uri.parse(detailUrl.url)
                startActivity(intent)
            }
        })

    }
}

