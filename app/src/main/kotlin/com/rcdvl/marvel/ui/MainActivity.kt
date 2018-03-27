package com.rcdvl.marvel.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.view.Menu
import com.rcdvl.marvel.R
import com.rcdvl.marvel.ui.list.CharacterListFragment
import com.rcdvl.marvel.ui.search.CharacterSearchFragment
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.container, CharacterListFragment())
            transaction.commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main_activity, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))

        return true
    }

    override fun onNewIntent(intent: Intent?) {
        if (intent != null && Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            val currentFragment = supportFragmentManager.findFragmentById(R.id.container)

            if (currentFragment !is CharacterSearchFragment) {
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, CharacterSearchFragment.newInstance(query))
                transaction.addToBackStack("")
                transaction.commit()
            } else {
                currentFragment.arguments.putString(CharacterSearchFragment.EXTRA_QUERY, query)
                currentFragment.refreshWithNewQuery()
            }
        }
    }
}
