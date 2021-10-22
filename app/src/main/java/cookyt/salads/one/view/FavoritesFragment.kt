package cookyt.salads.one.view

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.*
import cookyt.salads.one.App
import cookyt.salads.one.R
import cookyt.salads.one.adapter.RecipesListAdapter
import cookyt.salads.one.databinding.FragmentFavoritesBinding
import cookyt.salads.one.model.Recipe
import cookyt.salads.one.room.RecipeController
import cookyt.salads.one.view_model.MainActivityViewModel

class FavoritesFragment : Fragment() {
    lateinit var binding: FragmentFavoritesBinding
    lateinit var vm: MainActivityViewModel
    lateinit var adapter: RecipesListAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        vm = (activity as MainActivity).vm

        adapter = RecipesListAdapter(listOf(), this.requireContext(), "", activity as MainActivity)
        adapter.updateFavoriteSet { getFavorites() }
        val layoutManager = LinearLayoutManager(this.requireContext())
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.rvFavorite.layoutManager = layoutManager
        binding.rvFavorite.adapter = adapter

        return binding.root
    }

    fun getFavorites() {
        Thread {
            val listRoom = RecipeController.getFavorites()
            val list = mutableListOf<Recipe>()
            listRoom.forEach { list.add(it.getRecipe()) }
            (activity as MainActivity).runOnUiThread {
                adapter.updateValues(list)
            }
        }.start()

        loadBanner()
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

    override fun onResume() {
        super.onResume()
        getFavorites()
    }

    companion object {
        // This is an ad unit ID for a test ad. Replace with your own banner ad unit ID.
        private val AD_UNIT_ID = R.string.BANNER_ID
//        private val AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    }
}