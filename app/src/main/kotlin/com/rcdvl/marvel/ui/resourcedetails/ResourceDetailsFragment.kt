package com.rcdvl.marvel.ui.resourcedetails

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.*
import com.rcdvl.marvel.R
import com.rcdvl.marvel.model.MarvelResource
import com.rcdvl.marvel.ui.util.AndroidUtils
import com.rcdvl.marvel.ui.util.ZoomOutSlideTransformer
import kotlinx.android.synthetic.main.fragment_resource_details.*
import java.util.*

/**
 * Created by renan on 3/21/16.
 */
class ResourceDetailsFragment : Fragment() {

    var resources: ArrayList<MarvelResource> = ArrayList()

    companion object {
        fun newInstance(resources: ArrayList<MarvelResource>, selectedIndex: Int): ResourceDetailsFragment {
            val args = Bundle().apply {
                putSerializable(EXTRA_RESOURCES, resources)
                putInt(EXTRA_SELECTED_INDEX, selectedIndex)
            }

            return ResourceDetailsFragment().apply {
                arguments = args
            }
        }

        const val EXTRA_RESOURCES = "extra-resources"
        const val EXTRA_SELECTED_INDEX = "extra-selected-index"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater?.inflate(R.layout.fragment_resource_details, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        resources = arguments.getSerializable(EXTRA_RESOURCES) as ArrayList<MarvelResource>

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                pageIndicator.text = "${position + 1}/${resources.size}"
            }

        })

        pager.adapter = ResourceDetailsPagerAdapter(fragmentManager)
        pageIndicator.text = "1/${resources.size}"
        pager.pageMargin = AndroidUtils.dpToPx(8f).toInt();

        pager.setCurrentItem(arguments.getInt(EXTRA_SELECTED_INDEX), false)
        pager.setPageTransformer(true, ZoomOutSlideTransformer())
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_resource_details, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.close) {
            activity?.onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    inner class ResourceDetailsPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        override fun getItem(position: Int): Fragment? {
            return ResourceDetailsPageFragment.newInstance(resources[position])
        }

        override fun getCount(): Int {
            return resources.size
        }
    }
}