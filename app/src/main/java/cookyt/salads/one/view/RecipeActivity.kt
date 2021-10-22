package cookyt.salads.one.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import cookyt.salads.one.App
import cookyt.salads.one.R
import cookyt.salads.one.databinding.ActivityRecipeBinding
import cookyt.salads.one.model.RecipeRoom
import cookyt.salads.one.room.HistoryController
import cookyt.salads.one.room.RecipeController
import cookyt.salads.one.view_model.MainActivityViewModel

import com.google.android.ads.nativetemplates.NativeTemplateStyle
import cookyt.salads.one.adapter.RecipesListAdapter

class RecipeActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecipeBinding
    lateinit var vm: MainActivityViewModel
    private var mInterstitialAd: InterstitialAd? = null
    lateinit var adLoader: AdLoader

    lateinit var recipeId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            statusBarColor = resources.getColor(android.R.color.white)
        }

        binding = DataBindingUtil.setContentView(this, R.layout.activity_recipe)
        vm = ViewModelProvider(this)[MainActivityViewModel::class.java]
        recipeId = intent.getStringExtra("recipe_id") ?: ""

        vm.recipe.observe(this) {
            App.makeLog("TITLE ${it.title}")
            binding.title = it.title
            binding.body =
                HtmlCompat.fromHtml(it.description ?: "", HtmlCompat.FROM_HTML_MODE_COMPACT)
                    .toString()
            binding.category = it.sCategory
            binding.channelName = it.channelName
            App.loadPhoto(it.picture, binding.videoImg)

            val adapter = RecipesListAdapter(it.contiguousRecipes ?: listOf(), this, it.sCategory ?: "", this)
            binding.rvContiguous.layoutManager = LinearLayoutManager(this)
            binding.rvContiguous.adapter = adapter

            binding.tvContiguous.visibility = if(it.contiguousRecipes?.isEmpty() == true) View.GONE else View.VISIBLE

            Thread() {
                val isFav = RecipeController.checkIsFavorite(it.getRecipeRoom())
                HistoryController.changeHistory(it.getRecipeHistory())
                runOnUiThread {
                    binding.isFavorite = isFav
                    if (isFav)
                        binding.btnFavorite.setColorFilter(
                            resources.getColor(
                                R.color.main_light_color
                            ), android.graphics.PorterDuff.Mode.SRC_IN
                        );
                    else
                        binding.btnFavorite.setColorFilter(
                            resources.getColor(
                                R.color.light_grey
                            ), android.graphics.PorterDuff.Mode.SRC_IN
                        );
                }
            }.start()
        }

        binding.btnFavorite.setOnClickListener {
            Thread() {
                val isFav = RecipeController.changeFavorite(
                    vm.recipe.value?.getRecipeRoom()
                        ?: RecipeRoom("", "", "")
                )
                runOnUiThread {
                    binding.isFavorite = isFav
                    if (isFav)
                        binding.btnFavorite.setColorFilter(
                            resources.getColor(
                                R.color.main_light_color
                            ), android.graphics.PorterDuff.Mode.SRC_IN
                        );
                    else
                        binding.btnFavorite.setColorFilter(
                            resources.getColor(
                                R.color.light_grey
                            ), android.graphics.PorterDuff.Mode.SRC_IN
                        );
                }
            }.start()
        }

        binding.btnBack.setOnClickListener { finish() }

        binding.btnShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "${vm.recipe.value?.title}\n\n" +
                            "Я хотел бы поделиться этим с вами. Здесь вы можете загрузить это приложение из PlayStore\n\n" +
                            "{ссылка}"
                )
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        findViewById<TextView>(R.id.tv_try_again).setOnClickListener { checkConnection(recipeId) }
        binding.videoImg.setOnClickListener {
            val intent = Intent(this, VideoActivity::class.java)
            intent.putExtra("video_id", vm.recipe.value?.videoId)
            startActivity(intent)
        }

//        val adRequest = AdRequest.Builder().build()
//        binding.adView.loadAd(adRequest)
//        vm.getRecipe(recipeId)
        loadInterstitial()
        loadNativeAd()
    }

    override fun onResume() {
        super.onResume()
        checkConnection(recipeId)
    }

    fun loadInterstitial() {
        var adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this,getString(R.string.INTERSTITIAL_ID), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                App.makeLog(adError.message)
                mInterstitialAd = null
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                App.makeLog("Ad was loaded.")
                mInterstitialAd = interstitialAd

                mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        App.makeLog("Ad was dismissed.")
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError?) {
                        App.makeLog("Ad failed to show.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        App.makeLog("Ad showed fullscreen content.")
                        mInterstitialAd = null
                    }
                }

                if (mInterstitialAd != null) {
                    mInterstitialAd?.show(this@RecipeActivity)
                } else {
                    App.makeLog("The interstitial ad wasn't ready yet.")
                }
            }
        })
    }

    fun loadNativeAd() {
        MobileAds.initialize(this)
        adLoader = AdLoader.Builder(this, getString(R.string.NATIVE_ID))
            .forNativeAd { ad : NativeAd ->

                val styles = NativeTemplateStyle.Builder()
//                        .withMainBackgroundColor(background)
                        .build()
                binding.myTemplate.setStyles(styles)
                binding.myTemplate.setNativeAd(ad)
                // Show the ad.
                if (adLoader.isLoading) {
                    // The AdLoader is still loading ads.
                    // Expect more adLoaded or onAdFailedToLoad callbacks.
                } else {
                    // The AdLoader has finished loading ads.
                }
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    // Handle the failure by logging, altering the UI, and so on.
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                // Methods in the NativeAdOptions.Builder class can be
                // used here to specify individual options settings.
                .build())
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    fun checkConnection(recipeId: String) {
        if (App.isOnline()) {
            binding.layoutBadInternet.visibility = View.GONE
            binding.recipeScreen.visibility = View.VISIBLE
            vm.getRecipe(recipeId)
        } else {
            binding.recipeScreen.visibility = View.GONE
            binding.layoutBadInternet.visibility = View.VISIBLE
        }
    }

}