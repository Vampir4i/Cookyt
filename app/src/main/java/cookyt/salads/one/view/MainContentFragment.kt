package cookyt.salads.one.view

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import cookyt.salads.one.R
import cookyt.salads.one.adapter.TabAdapter
import cookyt.salads.one.databinding.FragmentMainContentBinding
import cookyt.salads.one.view_model.MainActivityViewModel
import com.google.android.gms.ads.*
import com.google.android.material.tabs.TabLayout
import cookyt.salads.one.App

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
        tabAdapter.addFragment(LastRecipesFragment(), getString(R.string.l_last_video).uppercase())
        tabAdapter.addFragment(CategoryFragment(), getString(R.string.l_category).uppercase())

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
                    shape.findViewById<TextView>(R.id.tab_text).setTextColor(resources.getColor(R.color.tab_text_color_selected))
                    shape.findViewById<TextView>(R.id.tab_text).text = getString(R.string.l_last_video).uppercase()
                }
                1 -> shape.findViewById<TextView>(R.id.tab_text).text = getString(R.string.l_category).uppercase()
            }
            binding.tabLayout.getTabAt(i)?.customView = shape
        }

        binding.tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val shape = tab?.customView as ConstraintLayout
                shape.findViewById<TextView>(R.id.tab_text).setTextColor(resources.getColor(R.color.tab_text_color_selected))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val shape = tab?.customView as ConstraintLayout
                shape.findViewById<TextView>(R.id.tab_text).setTextColor(resources.getColor(R.color.tab_text_color_unselected))
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

        loadBanner()

        return binding.root
    }

    private fun loadBanner() {
        adView = AdView(this.requireContext())
        binding.adContainer.addView(adView)
        adView.adUnitId = getString(AD_UNIT_ID)
        adView.adSize = adSize
        val adRequest = AdRequest
            .Builder()
            .build()

        adRequest.isTestDevice(this.requireContext())

        adView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                App.makeLog("Code to be executed when an ad finishes loading")
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                App.makeLog("Code to be executed when an ad request fails ${adError.message}")
                // Code to be executed when an ad request fails.
            }

            override fun onAdOpened() {
                App.makeLog("Code to be executed when an ad opens an overlay that covers the screen")
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                App.makeLog("Code to be executed when the user clicks on an ad")
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                App.makeLog("Code to be executed when the user is about to return to the app after tapping on an ad")
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }

        adView.loadAd(adRequest)
    }

    companion object {
        // This is an ad unit ID for a test ad. Replace with your own banner ad unit ID.
        private val AD_UNIT_ID = R.string.BANNER_ID
//        private val AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    }

}