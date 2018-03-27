package com.rcdvl.marvel.ui

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.rcdvl.marvel.model.MarvelResponse
import com.rcdvl.marvel.networking.MarvelService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by renan on 09/03/18.
 */
abstract class MarvelViewModel<T>(val marvelService: MarvelService) : ViewModel() {

    private val currentCalls = CompositeDisposable()
    val liveData = MutableLiveData<ArrayList<T>>()
    var offset = 0
    val count = 20
    var total = Int.MAX_VALUE
    var loading = false
    var isFirstRequest = true

    protected fun fireCall(call: Observable<MarvelResponse<T>>) {
        currentCalls.add(call.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<MarvelResponse<T>>() {
                    override fun onComplete() {
                    }

                    override fun onNext(response: MarvelResponse<T>) {
                        val responseData = response.data

                        total = responseData.total

                        offset += count
                        loading = false
                        isFirstRequest = false

                        if (liveData.value == null) {
                            liveData.value = responseData.results
                        } else {
                            liveData.value!!.addAll(responseData.results)
                            liveData.postValue(liveData.value)
                        }
                    }

                    override fun onError(e: Throwable) {
                        liveData.postValue(null)
                        loading = false
                    }
                })
        )
        loading = true
    }

    fun reset() {
        offset = 0
        total = Int.MAX_VALUE
        liveData.value = arrayListOf()
        currentCalls.clear()
        loading = false
        isFirstRequest = true
    }

    override fun onCleared() {
        super.onCleared()
        currentCalls.dispose()
    }
}