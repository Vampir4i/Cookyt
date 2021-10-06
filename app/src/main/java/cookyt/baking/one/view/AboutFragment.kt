package cookyt.baking.one.view

import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.*
import cookyt.baking.one.App
import cookyt.baking.one.R
import cookyt.baking.one.adapter.AboutListAdapter
import cookyt.baking.one.adapter.RecipesListAdapter
import cookyt.baking.one.databinding.FragmentAboutBinding
import cookyt.baking.one.model.AboutModel

class AboutFragment : Fragment() {
    lateinit var binding:FragmentAboutBinding
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_about, container, false)

        binding.rvAbout.adapter = AboutListAdapter(aboutInfo, this.requireActivity())
        val layoutManager = LinearLayoutManager(this.requireContext())
        binding.rvAbout.layoutManager = layoutManager

        loadBanner()
        return binding.root
    }


    val aboutInfo = listOf(
        AboutModel("Имя приложения", "Cookyt Выпечка", R.drawable.baseline_video_library_24, true),
        AboutModel("Текущая версия", "1.0", R.drawable.ic_baseline_info_24, true),
        AboutModel("Email", "cookytads@gmail.com", R.drawable.ic_baseline_mail_24, true),
        AboutModel("Copyright", "Copyright 2021", R.drawable.ic_baseline_copyright_24, false),
//        AboutModel("Оцени нас", "Дайте оценку и отзыв", R.drawable.ic_baseline_rate_review_24, false),
        AboutModel("Больше", "Больше приложений", R.drawable.ic_baseline_more_24, false),
        AboutModel("Terms & Policies", "Terms & Policies of Cookyt", R.drawable.ic_baseline_lock_24, false)
    )

    private fun loadBanner() {
        adView = AdView(this.requireContext())
        binding.adContainer.addView(adView)
        adView.adUnitId = AD_UNIT_ID
        adView.adSize = adSize
        val adRequest = AdRequest
            .Builder()
            .build()

        adRequest.isTestDevice(this.requireContext())

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
        private val AD_UNIT_ID = "ca-app-pub-9006479240979656/2665160049"
//        private val AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"
    }

}