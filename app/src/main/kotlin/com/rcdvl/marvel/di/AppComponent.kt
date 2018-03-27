package com.rcdvl.marvel.di

import com.rcdvl.marvel.ui.details.CharacterDetailsFragment
import com.rcdvl.marvel.ui.list.CharacterListFragment
import com.rcdvl.marvel.ui.search.CharacterSearchFragment
import dagger.Component
import javax.inject.Singleton

/**
 * Created by renan on 07/02/18.
 */
@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(characterListFragment: CharacterListFragment)
    fun inject(characterListFragment: CharacterSearchFragment)
    fun inject(characterDetailsFragment: CharacterDetailsFragment)
}