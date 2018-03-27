package com.rcdvl.marvel.ui.resourcedetails

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rcdvl.marvel.R
import com.rcdvl.marvel.model.MarvelResource
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*

/**
 * Created by renan on 3/21/16.
 */
class ResourceDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            val res = intent.getSerializableExtra(ResourceDetailsFragment.EXTRA_RESOURCES) as ArrayList<MarvelResource>
            val index = intent.getIntExtra(ResourceDetailsFragment.EXTRA_SELECTED_INDEX, 0)

            supportFragmentManager.beginTransaction()
                    .replace(R.id.container,
                            ResourceDetailsFragment.newInstance(res, index))
                    .commit()
        }
    }
}