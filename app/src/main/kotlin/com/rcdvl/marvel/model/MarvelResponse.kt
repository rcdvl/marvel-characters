package com.rcdvl.marvel.model

/**
 * Created by renan on 12/5/17.
 */
data class MarvelResponse<T>(var code: String, var status: String, var copyright: String,
                             var attributionText: String, var attributionHTML: String,
                             var etag: String, var data: MarvelData<T>)