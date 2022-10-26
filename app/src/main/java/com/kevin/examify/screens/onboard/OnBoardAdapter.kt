package com.kevin.examify.screens.onboard

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.kevin.examify.R
import android.widget.TextView
import android.widget.LinearLayout
import java.util.ArrayList

//import android.support.v4.view.PagerAdapter;
/**
 * Created by Koech
 */
class OnBoardAdapter(private val mContext: Context, items: ArrayList<OnBoardItem>) :
    PagerAdapter() {
    private var onBoardItems = ArrayList<OnBoardItem>()

    init {
        onBoardItems = items
    }

    override fun getCount(): Int {
        return onBoardItems.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = LayoutInflater.from(mContext).inflate(R.layout.onboard_item, container, false)
        val item = onBoardItems[position]
        val imageView = itemView.findViewById<View>(R.id.iv_onboard) as ImageView
        imageView.setImageResource(item.imageID)
        val tv_title = itemView.findViewById<View>(R.id.tv_header) as TextView
        tv_title.text = item.title
        val tv_content = itemView.findViewById<View>(R.id.tv_desc) as TextView
        tv_content.text = item.description
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}