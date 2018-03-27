package com.rcdvl.marvel.model

import java.util.ArrayList

/**
 * Created by renan on 12/5/17.
 */

data class MarvelData<T>(var offset: Int, var limit: Int, var total: Int, var count: Int,
                         var results: ArrayList<T>)