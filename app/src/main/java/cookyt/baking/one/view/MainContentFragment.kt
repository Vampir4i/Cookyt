package cookyt.baking.one.view

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import cookyt.baking.one.R
import cookyt.baking.one.adapter.TabAdapter
import cookyt.baking.one.databinding.FragmentMainContentBinding
import cookyt.baking.one.view_model.MainActivityViewModel
import com.google.android.gms.ads.*
import com.google.android.material.tabs.TabLayout

class MainContentFragment : Fragment() {
    lateinit var binding: FragmentMainContentBinding
    lateinit var vm: MainActivityViewModel
    private lateinit var adView: AdView

    private val adSize: AdSize
        get() {
            val display = activity?.windowManager?.defaultDisplay
            val outMetrics = DisplayMetrics()
            display?.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = binding.adContainer.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this.requireContext(), adWidth)
        }

    var whichPage = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_content, container, false)
        vm = (activity as MainActivity).vm

        val tabAdapter = TabAdapter(childFragmentManager)
        tabAdapter.addFragment(LastRecipesFragment(), "ПОСЛЕДНИЕ ВИДЕО")
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
                    shape.findViewById<TextView>(R.id.tab_text).setTextColor(resources.getColor(R.color.dark_orange))
                    shape.findViewById<TextView>(R.id.tab_text).text = "НОВЫЕ ВИДЕО"
                }
                1 -> shape.findViewById<TextView>(R.id.tab_text).text = "КАТЕГОРИИ"
            }
            binding.tabLayout.getTabAt(i)?.customView = shape
        }

        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val shape = tab?.customView as ConstraintLayout
                shape.findViewById<TextView>(R.id.tab_text).setTextColor(resources.getColor(R.color.dark_orange))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val shape = tab?.customView as ConstraintLayout
                shape.findViewById<TextView>(R.id.tab_text).setTextColor(resources.getColor(R.color.dark_grey))
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })

//        val adRequest = AdRequest.Builder().build()
//        binding.adView.loadAd(adRequest)
//
//        binding.adView.adListener = object: AdListener() {
//            override fun onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                App.makeLog("onAdLoaded")
//            }
//
//            override fun onAdFailedToLoad(adError : LoadAdError) {
//                // Code to be executed when an ad request fails.
//                App.makeLog("onAdFailedToLoad $adError")
//            }
//
//            override fun onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//                App.makeLog("onAdOpened")
//            }
//
//            override fun onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//                App.makeLog("onAdClicked")
//            }
//
//            override fun onAdClosed() {
//                // Code to be executed when the user is about to return
//                // to the app after tapping on an ad.
//                App.makeLog("onAdClosed")
//            }
//        }

//        loadBanner()

        return binding.root
    }

//    private fun loadBanner() {
//        adView = AdView(this.requireContext())
//        binding.adContainer.addView(adView)
//        adView.adUnitId = AD_UNIT_ID
//        adView.adSize = adSize
//        val adRequest = AdRequest
//            .Builder()
//            .build()
//        adView.loadAd(adRequest)
//    }

    companion object {
        // This is an ad unit ID for a test ad. Replace with your own banner ad unit ID.
        private val AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    }

}