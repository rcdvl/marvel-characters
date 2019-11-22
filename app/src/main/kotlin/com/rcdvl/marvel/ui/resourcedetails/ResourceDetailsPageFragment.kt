package com.rcdvl.marvel.ui.resourcedetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rcdvl.marvel.R
import com.rcdvl.marvel.model.MarvelResource
import com.rcdvl.marvel.ui.util.GlideApp
import kotlinx.android.synthetic.main.fragment_resource_page.*

/**
 * Created by renan on 3/21/16.
 */
class ResourceDetailsPageFragment : Fragment() {

    companion object {
        fun newInstance(resource: MarvelResource): ResourceDetailsPageFragment {
            val fragment = ResourceDetailsPageFragment()
            var args = Bundle().apply {
                putSerializable(EXTRA_RESOURCE, resource)
            }
            fragment.arguments = args
            return fragment
        }

        const val EXTRA_RESOURCE = "extra-resource"
        const val EXTRA_SOURCE = "extra-source"
        const val EXTRA_RESOURCE_TYPE = "extra-resource-type"
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_resource_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var resource = arguments?.getSerializable(EXTRA_RESOURCE)

        if (resource != null) {
            resource = resource as MarvelResource

            context?.let {
                GlideApp.with(it)
                        .load(resource.thumbnail?.path + '.' + resource.thumbnail?.extension)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(resourceImage)
            }

            resourceText.text = resource.title
        }
    }
}