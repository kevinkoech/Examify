package com.kevin.examify.screens.onboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.kevin.examify.R
import com.kevin.examify.databinding.OnboardBinding

import java.util.ArrayList


class OnBoard : Fragment() {

    private lateinit var binding: OnboardBinding
    private var dotsCount = 0
    private var dots: Array<ImageView?> = emptyArray()
    private var mAdapter: OnBoardAdapter? = null
    var previousPos = 0
    var onBoardItems = ArrayList<OnBoardItem>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = OnboardBinding.inflate(inflater,container,false)


        setViews()


        return binding.root

    }

    private fun setViews() {

        /** button get started **/
        binding.btnGetStarted.setOnClickListener {
          findNavController().navigate(R.id.action_onBoard_to_home)
        }

        setOnBoardAdapter()

    }


//  fun setAdMob(){
//        // Don't forget to insert your App ID below
//// we have inserted Test ID that you can use while testing your App.
//         MobileAds.initialize(this, "ca-app-pub-8330777012217727~2651703055");
//
//
//        // Don't forget to insert your App ID below
//        //we have inserted Test ID that you can use while testing your App.
//        MobileAds.initialize(this, "ca-app-pub-8330777012217727~2651703055");
//
//
//          mAdView = findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
//
//
//        //Google Admob Interstitial Ad
//        mInterstitialAd=
//        mInterstitialAd.setAdUnitId("ca-app-pub-8330777012217727/1069784806");
//
//        //To Load Gogole Admob Interstitial Ad
//        mInterstitialAd.show(this);
//
//
//
//
//
//       //MobileAds.initialize(this, getString(R.string.ads),
//        MobileAds.initialize(this, initializationStatus -> {});
//        AdRequest adRequest = new AdRequest.Builder().build();
//
//        InterstitialAd.load(this,"ca-app-pub-8581982175086198/4925971818", adRequest,
//                new InterstitialAdLoadCallback() {
//                    @Override
//                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
//                        // The mInterstitialAd reference will be null until
//                        // an ad is loaded.
//                        InterstitialAd mInterstitialAd = interstitialAd;
//                        Log.i(TAG, "onAdLoaded");
//                    }
//
//                    @Override
//                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
//                        // Handle the error
//                        Log.d(TAG, loadAdError.toString());
//                        Object mInterstitialAd = null;
//                    }
//                });
//
//
//    }

    fun setOnBoardAdapter(){

        loadData()
        mAdapter = OnBoardAdapter(requireContext(), onBoardItems)
        binding.pagerIntroduction.adapter = mAdapter
        binding.pagerIntroduction.currentItem = 0
        binding.pagerIntroduction.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {


                // Change the current position intimation
                for (i in 0 until dotsCount) {
                    dots[i]!!.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.non_selected_item_dot))
                }
                dots[position]!!.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.selected_item_dot))
                val pos = position + 1
                if (pos == dotsCount && previousPos == dotsCount - 1) show_animation() else if (pos == dotsCount - 1 && previousPos == dotsCount) hide_animation()
                previousPos = pos


            }

            override fun onPageScrollStateChanged(state: Int) {


            }
        })


        setUiPageViewController()

    }

    // Load data into the viewpager
    fun loadData() {
        val header = intArrayOf(R.string.ob_header1, R.string.ob_header2, R.string.ob_header3)
        val desc = intArrayOf(R.string.ob_desc1, R.string.ob_desc2, R.string.ob_desc3)
        val imageId = intArrayOf(R.drawable.slide1, R.drawable.slide2, R.drawable.slide3)
        for (i in imageId.indices) {
            val item = OnBoardItem()
            item.imageID = imageId[i]
            item.title = resources.getString(header[i])
            item.description = resources.getString(desc[i])
            onBoardItems.add(item)
        }
    }


    // Button bottomUp animation
    fun show_animation() {
        val show = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_up_anim)
        binding.btnGetStarted.startAnimation(show)
        show.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                binding.btnGetStarted.visibility = View.VISIBLE

            }

            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {



                binding.btnGetStarted.clearAnimation()




            }
        })
    }

    // Button Topdown animation
    fun hide_animation() {
        val hide = AnimationUtils.loadAnimation(requireContext(), R.anim.slide_down_anim)
        binding.btnGetStarted.startAnimation(hide)
        hide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationRepeat(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                binding.btnGetStarted.clearAnimation()
                binding.btnGetStarted.visibility = View.GONE
            }
        })
    }


    // setup the
    private fun setUiPageViewController() {
        dotsCount = mAdapter!!.count
        dots = arrayOfNulls(dotsCount)
        for (i in 0 until dotsCount) {
            dots[i] = ImageView(requireContext())
            dots[i]!!.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.non_selected_item_dot))
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(6, 0, 6, 0)
            binding.pagerIntroduction.addView(dots[i], params)
        }
        dots[0]!!.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.selected_item_dot))
    }


}