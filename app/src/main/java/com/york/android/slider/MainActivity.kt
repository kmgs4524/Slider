package com.york.android.slider

import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.AttrRes
import android.support.annotation.RequiresApi
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val textViews = ArrayList<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addTextView()
        initViewPager()
    }

    fun initViewPager() {
        val layoutInflater = LayoutInflater.from(this)
        val views = ArrayList<View>()   // store page views
        val drawables = ArrayList<Drawable>()

        // add background drawable
        drawables.add(resources.getDrawable(R.drawable.businessman, null))
        drawables.add(resources.getDrawable(R.drawable.meeting, null))
        drawables.add(resources.getDrawable(R.drawable.statistics, null))

        // add view
        for (i in 0..2) {
            val view = layoutInflater.inflate(R.layout.page, null)  // view added to LinearLayout
            val imageViewBackground = view.findViewById<ImageView>(R.id.imageView_page_background)
            val textViewTitle = view.findViewById<TextView>(R.id.textView_page_title)

            imageViewBackground.setImageDrawable(drawables[i])
            textViewTitle.setText("Hello Page ${i}")

            views.add(view)
        }

        viewPager_main.adapter = ImagePageAdapter(views)

        // listen page change event
        viewPager_main.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                Log.d("OnPageChangeListener", "state: ${state}")
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onPageSelected(position: Int) {
                Log.d("OnPageChangeListener", "position: ${position}")
                val currentColor = textViews[position].currentTextColor
                val whiteColor = resources.getColor(R.color.md_white_1000, null)

                if (position > 0 && position < textViews.size - 1) {
                    textViews[position - 1].setTextColor(currentColor)
                    textViews[position + 1].setTextColor(currentColor)
                    textViews[position].setTextColor(whiteColor)
                } else if (position == textViews.size - 1) {
                    textViews[position - 1].setTextColor(currentColor)
                    textViews[position].setTextColor(whiteColor)
                } else {
                    textViews[position].setTextColor(whiteColor)
                    textViews[position + 1].setTextColor(currentColor)
                }
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

        })

    }

    fun addTextView() {
        for (i in 0..2) {
            val childTextView = TextView(this)
            childTextView.setText("\u25CF")
            childTextView.textSize = 18f
            linearLayout_main.addView(childTextView)
            textViews.add(childTextView)
        }

        textViews[0].setTextColor(resources.getColor(R.color.md_white_1000, null))
    }

    class ImagePageAdapter(val pages: List<View>) : PagerAdapter() {
        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view == `object`
        }

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            container?.addView(pages[position])

            return pages[position]
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            container?.removeView(pages[position])
        }

        override fun getCount(): Int {
            return pages.size
        }
    }

}

