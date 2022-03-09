package cookyt.soups.one.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.*
import cookyt.soups.one.App
import cookyt.soups.one.R
import cookyt.soups.one.adapter.RecipesListAdapter
import cookyt.soups.one.databinding.ActivitySearchBinding
import cookyt.soups.one.view_model.MainActivityViewModel

class SearchActivity : AppCompatActivity() {
    lateinit var vm: MainActivityViewModel
    lateinit var binding: ActivitySearchBinding

    var searchText = ""
    private lateinit var adView: AdView

    private val adSize: AdSize
        get() {
            val display = windowManager?.defaultDisplay
            val outMetrics = DisplayMetrics()
            display?.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = binding.adContainer.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = resources.getColor(R.color.main_dark_color)
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        vm = ViewModelProvider(this)[MainActivityViewModel::class.java]

        binding.rcRecipes.layoutManager = LinearLayoutManager(this)
        val adapter = RecipesListAdapter(listOf(), this, "", this)
        binding.rcRecipes.adapter = adapter

        vm.searchRecipes.observe(this, {
            adapter.updateValues(it)
            binding.listRefresh.isRefreshing = false
        })

        binding.etSearch.addTextChangedListener {
            searchText = it?.toString() ?: ""
            checkConnection(searchText)
        }

        binding.btnBack.setOnClickListener { finish() }
        binding.listRefresh.setOnRefreshListener { checkConnection(searchText) }
        findViewById<TextView>(R.id.tv_try_again).setOnClickListener { checkConnection(searchText) }
        loadBanner()
    }

    fun checkConnection(text: String) {
        if(App.isOnline()){
            binding.layoutBadInternet.visibility = View.GONE
            binding.rcRecipes.visibility = View.VISIBLE
            vm.searchRecipe(text, getString(R.string.CATEGORY_ID))
        } else {
            binding.rcRecipes.visibility = View.GONE
            binding.layoutBadInternet.visibility = View.VISIBLE
            binding.listRefresh.isRefreshing = false
        }
    }

    private fun loadBanner() {
        adView = AdView(this)
        binding.adContainer.addView(adView)
        adView.adUnitId = getString(AD_UNIT_ID)
        adView.adSize = adSize
        val adRequest = AdRequest
            .Builder()
            .build()

        adRequest.isTestDevice(this)

        adView.adListener = object: AdListener() {
            override fun onAdLoaded() {
                App.makeLog("Code to be executed when an ad finishes loading")
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                App.makeLog("Code to be executed when an ad request fails ${adError.message}")
            }

            override fun onAdOpened() {
                App.makeLog("Code to be executed when an ad opens an overlay that covers the screen")
            }

            override fun onAdClicked() {
                App.makeLog("Code to be executed when the user clicks on an ad")
            }

            override fun onAdClosed() {
                App.makeLog("Code to be executed when the user is about to return to the app after tapping on an ad")
            }
        }

        adView.loadAd(adRequest)
    }

    companion object {
        private val AD_UNIT_ID = R.string.BANNER_ID
//        private val AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    }
}