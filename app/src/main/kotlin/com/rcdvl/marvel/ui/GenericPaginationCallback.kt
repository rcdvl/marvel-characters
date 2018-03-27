package com.rcdvl.marvel.ui

import android.view.View
import com.paginate.Paginate

/**
 * Created by renan on 15/03/18.
 */
abstract class GenericPaginationCallback(private val viewModel: MarvelViewModel<out Any>,
                                         private val errorView: View?) : Paginate.Callbacks {

    override fun isLoading() = viewModel.loading

    override fun hasLoadedAllItems(): Boolean {
        return if (viewModel.isFirstRequest) {
            false
        } else {
            viewModel.offset >= viewModel.total
        }
    }

    override fun onLoadMore() {
        errorView?.visibility = View.GONE
    }
}