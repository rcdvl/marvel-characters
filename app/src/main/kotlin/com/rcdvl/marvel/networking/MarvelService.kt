package com.rcdvl.marvel.networking

import com.rcdvl.marvel.model.MarvelCharacter
import com.rcdvl.marvel.model.MarvelResource
import com.rcdvl.marvel.model.MarvelResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Created by renan on 3/17/16.
 */
interface MarvelService {

    @GET("/v1/public/characters")
    fun getCharactersList(@Query("offset") offset: Int, @Query("limit") limit: Int)
            : Observable<MarvelResponse<MarvelCharacter>>

    @GET("/v1/public/characters")
    fun getCharactersList(@Query("offset") offset: Int, @Query("limit") limit: Int,
                          @Query("nameStartsWith") name: String)
            : Observable<MarvelResponse<MarvelCharacter>>

    @GET("/v1/public/characters/{id}/{resource}")
    fun getCharacterResourceList(@Path("resource") res: String, @Path("id") id: Long,
                                 @Query("offset") offset: Int, @Query("limit") limit: Int)
            : Observable<MarvelResponse<MarvelResource>>

    @GET("/v1/public/{resource}/{id}")
    fun getResourceDetails(@Path("resource") res: String, @Path("id") id: String)
            : Observable<MarvelResponse<MarvelResource>>
}