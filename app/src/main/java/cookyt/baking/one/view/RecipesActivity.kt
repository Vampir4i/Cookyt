package cookyt.baking.one.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.*
import cookyt.baking.one.App
import cookyt.baking.one.R
import cookyt.baking.one.adapter.RecipesListAdapter
import cookyt.baking.one.databinding.ActivityRecipesBinding
import cookyt.baking.one.view_model.MainActivityViewModel

class RecipesActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecipesBinding
    lateinit var vm: MainActivityViewModel
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
            statusBarColor = resources.getColor(R.color.dark_orange)
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipes)
        vm = ViewModelProvider(this)[MainActivityViewModel::class.java]

        val categoryId = intent.getStringExtra("category_id") ?: "0"
        val category = intent.getStringExtra("category") ?: ""

        binding.category = category
        binding.rcRecipes.layoutManager = LinearLayoutManager(this)
        val adapter = RecipesListAdapter(listOf(), this, category, this)
        binding.rcRecipes.adapter = adapter

        adapter.loadNextPage { checkConnection(categoryId, it.toString()) }

        vm.recipes.observe(this, {
            adapter.updateValues(it)
            binding.listRefresh.isRefreshing = false
        })

        binding.btnBack.setOnClickListener { finish() }
        binding.btnSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        binding.listRefresh.setOnRefreshListener {
            adapter.CURRENT_PAGE = 1
            vm.recipes.value = mutableListOf()
            checkConnection(categoryId)
        }
        findViewById<TextView>(R.id.tv_try_again).setOnClickListener { checkConnection(categoryId) }

        checkConnection(categoryId)
        loadBanner()
    }

    fun checkConnection(categoryId: String, page: String = "1") {
        if(App.isOnline()){
            binding.layoutBadInternet.visibility = View.GONE
            binding.rcRecipes.visibility = View.VISIBLE
            vm.getRecipes(categoryId, page)
        } else {
            binding.rcRecipes.visibility = View.GONE
            binding.layoutBadInternet.visibility = View.VISIBLE
            binding.listRefresh.isRefreshing = false
        }
    }

    private fun loadBanner() {
        adView = AdView(this)
        binding.adContainer.addView(adView)
        adView.adUnitId = AD_UNIT_ID
        adView.adSize = adSize
        val adRequest = AdRequest
            .Builder()
            .build()

        adRequest.isTestDevice(this)

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
        private val AD_UNIT_ID = "ca-app-pub-9006479240979656/2665160049"
//        private val AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    }
}