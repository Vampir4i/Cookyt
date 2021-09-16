package com.example.cookyt.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import com.example.cookyt.R
import com.example.cookyt.adapter.TabAdapter
import com.example.cookyt.databinding.FragmentMainContentBinding
import com.example.cookyt.view_model.MainActivityViewModel
import com.google.android.material.tabs.TabLayout
import org.w3c.dom.Text

class MainContentFragment : Fragment() {
    lateinit var binding: FragmentMainContentBinding
    lateinit var vm: MainActivityViewModel

    var whichPage = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_content, container, false)
        vm = (activity as MainActivity).vm

        val tabAdapter = TabAdapter(childFragmentManager)
        tabAdapter.addFragment(LastVideosFragment(), "ПОСЛЕДНИЕ ВИДЕО")
        tabAdapter.addFragment(CategoryFragment(), "КАТЕГОРИИ")

        binding.viewPager.adapter = tabAdapter

        if(whichPage == 1)
            binding.viewPager.postDelayed({
                binding.viewPager.setCurrentItem(whichPage, true)
            }, 100)

        binding.tabLayout.setupWithViewPager(binding.viewPager)

        for(i in 0..1) {
            val shape = LayoutInflater.from(this.requireContext())
                .inflate(R.layout.custom_tab_layout, null) as ConstraintLayout
            when(i) {
                0 -> {
                    shape.findViewById<TextView>(R.id.tab_text).setTextColor(resources.getColor(R.color.white))
                    shape.findViewById<TextView>(R.id.tab_text).text = "ПОСЛЕДНИЕ ВИДЕО"
                }
                1 -> shape.findViewById<TextView>(R.id.tab_text).text = "КАТЕГОРИИ"
            }
            binding.tabLayout.getTabAt(i)?.customView = shape
        }

        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val shape = tab?.customView as ConstraintLayout
                shape.findViewById<TextView>(R.id.tab_text).setTextColor(resources.getColor(R.color.white))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val shape = tab?.customView as ConstraintLayout
                shape.findViewById<TextView>(R.id.tab_text).setTextColor(resources.getColor(R.color.black))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

        return binding.root
    }

}