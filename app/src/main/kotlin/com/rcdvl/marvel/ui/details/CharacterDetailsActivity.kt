package com.rcdvl.marvel.ui.details

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.rcdvl.marvel.R
import com.rcdvl.marvel.model.MarvelCharacter
import com.rcdvl.marvel.ui.list.CharactersAdapter
import kotlinx.android.synthetic.main.toolbar.*

/**
 * Created by renan on 3/18/16.
 */
class CharacterDetailsActivity : AppCompatActivity() {

    private var _isDestroyed = false
    var isAvailable: Boolean
        get() = _isDestroyed
        set(value) {
            _isDestroyed = value
        }

    companion object {
        fun startActivity(context: Context, character: MarvelCharacter,
                          characterImageView: ImageView,
                          characterNameView: TextView) {
            val characterImageTransitionName = ViewCompat.getTransitionName(characterImageView)
            val characterNameTransitionName = ViewCompat.getTransitionName(characterNameView)

            val intent = Intent(context, CharacterDetailsActivity::class.java).apply {
                putExtra(CharactersAdapter.EXTRA_CHARACTER, character)
                putExtra(CharactersAdapter.EXTRA_CHARACTER_IMAGE_TRANSITION_NAME,
                        characterImageTransitionName)
                putExtra(CharactersAdapter.EXTRA_CHARACTER_NAME_TRANSITION_NAME,
                        characterNameTransitionName)
            }

            val sharedElements = arrayOf(
                    Pair<View, String>(characterImageView, characterImageTransitionName)
                    // Pair<View, String>(characterNameView, characterNameTransitionName)
            )
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    context as Activity,
                    *sharedElements)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                context.window.sharedElementsUseOverlay = true
                context.startActivity(intent, options.toBundle())
            } else {
                context.startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isAvailable = true

        setContentView(R.layout.activity_character_details)
        setSupportActionBar(toolbar)
        toggleHomeAsUp(true)
        supportPostponeEnterTransition()

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container, CharacterDetailsFragment.newInstance(
                            intent.extras.get(CharactersAdapter.EXTRA_CHARACTER) as MarvelCharacter,
                            intent.extras.getString(
                                    CharactersAdapter.EXTRA_CHARACTER_IMAGE_TRANSITION_NAME),
                            intent.extras.getString(
                                    CharactersAdapter.EXTRA_CHARACTER_NAME_TRANSITION_NAME)))
                    .commit()
        }

        supportStartPostponedEnterTransition()
    }

    override fun onDestroy() {
        isAvailable = false
        super.onDestroy()
    }

    private fun toggleHomeAsUp(active: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(active)
        supportActionBar?.setDisplayShowHomeEnabled(active)

        if (active) {
            toolbar.navigationIcon = resources.getDrawable(R.drawable.ic_back)
        }
    }
}