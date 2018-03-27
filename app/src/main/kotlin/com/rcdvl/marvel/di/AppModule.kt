package com.rcdvl.marvel.di

import android.app.Application
import android.content.Context
import com.rcdvl.marvel.R
import com.rcdvl.marvel.networking.MarvelService
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by renan on 12/4/17.
 */
@Module
class AppModule(private val application: Application) {

    companion object {
        const val MARVEL_SERVICE_URL = "marvel_service_url"
        const val CHARACTER_RESOURCES_LINKS = "resources_links"
        const val API_PRIVATE_KEY = "api_private_key"
        const val API_PUBLIC_KEY = "api_public_key"
    }

    @Provides
    @Singleton
    @AppContext
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    @Named(AppModule.MARVEL_SERVICE_URL)
    fun provideMarvelServiceUrl() = "http://gateway.marvel.com/"

    @Provides
    @Singleton
    fun provideConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Named(AppModule.API_PUBLIC_KEY)
    @Singleton
    fun provideApiPublicKey(@AppContext context: Context): String {
        context.getString(R.string.api_public_key).let {
            require(!it.isNullOrBlank()) {
                "Please insert your API public key in strings.xml"
            }
            return it
        }
    }

    @Provides
    @Named(AppModule.API_PRIVATE_KEY)
    @Singleton
    fun provideApiPrivateKey(@AppContext context: Context): String {
        context.getString(R.string.api_private_key).let {
            require(!it.isNullOrBlank()) {
                "Please insert your API private key in strings.xml"
            }
            return it
        }
    }

    @Provides
    fun provideOkHttpClient(@AppContext context: Context,
                            @Named(AppModule.API_PRIVATE_KEY) privateKey: String,
                            @Named(AppModule.API_PUBLIC_KEY) publicKey: String): OkHttpClient {
        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cache = Cache(context.cacheDir, cacheSize.toLong())

        return OkHttpClient.Builder().addInterceptor { chain ->

            val timestamp = System.currentTimeMillis()
            val beforeHash = "$timestamp$privateKey$publicKey"
            val hash = BigInteger(1,
                    MessageDigest.getInstance("MD5").digest(beforeHash.toByteArray())).toString(16)

            val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("ts", "$timestamp")
                    .addQueryParameter("hash", hash)
                    .addQueryParameter("apikey", publicKey)
                    .build()

            val cacheControl = CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()
            val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .cacheControl(cacheControl)
                    .build()

            chain.proceed(request)

        }.cache(cache).build()
    }

    @Provides
    @Singleton
    fun provideMarvelService(@Named(AppModule.MARVEL_SERVICE_URL) marvelServiceUrl: String,
                             okHttpClient: OkHttpClient,
                             converterFactory: GsonConverterFactory): MarvelService {
        return Retrofit.Builder()
                .baseUrl(marvelServiceUrl)
                .client(okHttpClient)
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(MarvelService::class.java)
    }

    @Provides
    @Named(AppModule.CHARACTER_RESOURCES_LINKS)
    fun provideResourceLinksNames(): Array<String> {
        return arrayOf("comics", "series", "stories", "events")
    }
}