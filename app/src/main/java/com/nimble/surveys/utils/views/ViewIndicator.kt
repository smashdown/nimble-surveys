package com.nimble.surveys.utils.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nimble.surveys.R
import timber.log.Timber


/**
 * Created by kingfisher on 3/5/18.
 * https://github.com/kingfisherphuoc/EasyRecyclerViewIndicator
 */
open class ViewIndicator : LinearLayout {
    private var mIndicatorMargin = -1
    private var mIndicatorWidth = -1
    private var mIndicatorHeight = -1
    private var mIndicatorBackgroundResId = R.drawable.white_radius
    private var mIndicatorUnselectedBackgroundResId = R.drawable.white_radius

    protected var mLastPosition = -1

    var recyclerView: RecyclerView? = null
        set(value) {
            field = value
            field?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    updateIndicator()
                }
            })
        }

    fun updateIndicator() {
        recyclerView?.let {
            currentPosition = if (it.canScrollVertically(1)) {
                val position = (it.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()
                position
            } else {
                val position = (it.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                position
            }
        }
    }

    open var currentPosition: Int = 0
        set(currentPosition) {
            field = currentPosition
            onCurrentLocationChange()
        }

    open var itemCount: Int = 0
        set(itemCount) {
            field = itemCount
            updateCircleIndicator()
        }

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
            context,
            attrs,
            defStyleAttr,
            defStyleRes
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        handleTypedArray(context, attrs)
        checkIndicatorConfig(context)
    }

    private fun handleTypedArray(context: Context, attrs: AttributeSet?) {
        if (attrs == null) {
            return
        }

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnyViewIndicator)
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.AnyViewIndicator_avi_width, -1)
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.AnyViewIndicator_avi_height, -1)
        mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.AnyViewIndicator_avi_margin, -1)

        mIndicatorBackgroundResId = typedArray.getResourceId(
                R.styleable.AnyViewIndicator_avi_drawable,
                R.drawable.white_radius
        )
        mIndicatorUnselectedBackgroundResId = typedArray.getResourceId(
                R.styleable.AnyViewIndicator_avi_drawable_unselected,
                mIndicatorBackgroundResId
        )

        val orientation = typedArray.getInt(R.styleable.AnyViewIndicator_avi_orientation, -1)
        setOrientation(if (orientation == LinearLayout.VERTICAL) LinearLayout.VERTICAL else LinearLayout.HORIZONTAL)

        val gravity = typedArray.getInt(R.styleable.AnyViewIndicator_avi_gravity, -1)
        setGravity(if (gravity >= 0) gravity else Gravity.CENTER)

        typedArray.recycle()
    }

    @JvmOverloads
    fun configureIndicator(
            indicatorWidth: Int, indicatorHeight: Int, indicatorMargin: Int,
            @DrawableRes indicatorBackgroundId: Int = R.drawable.white_radius,
            @DrawableRes indicatorUnselectedBackgroundId: Int = R.drawable.white_radius
    ) {

        mIndicatorWidth = indicatorWidth
        mIndicatorHeight = indicatorHeight
        mIndicatorMargin = indicatorMargin

        mIndicatorBackgroundResId = indicatorBackgroundId
        mIndicatorUnselectedBackgroundResId = indicatorUnselectedBackgroundId

        checkIndicatorConfig(context)
    }

    private fun checkIndicatorConfig(context: Context) {
        mIndicatorWidth = if (mIndicatorWidth < 0) dip2px(DEFAULT_INDICATOR_WIDTH.toFloat()) else mIndicatorWidth
        mIndicatorHeight = if (mIndicatorHeight < 0) dip2px(DEFAULT_INDICATOR_WIDTH.toFloat()) else mIndicatorHeight
        mIndicatorMargin = if (mIndicatorMargin < 0) dip2px(DEFAULT_INDICATOR_WIDTH.toFloat()) else mIndicatorMargin

        mIndicatorBackgroundResId =
                if (mIndicatorBackgroundResId == 0) R.drawable.white_radius
                else mIndicatorBackgroundResId

        mIndicatorUnselectedBackgroundResId =
                if (mIndicatorUnselectedBackgroundResId == 0) mIndicatorBackgroundResId
                else mIndicatorUnselectedBackgroundResId
    }

    /**
     * Force the circle indicator upgrade
     */
    protected fun updateCircleIndicator() {
        val newCount = itemCount
        val currentCount = childCount

        if (newCount == currentCount) {  // No change
            return
        } else if (mLastPosition < newCount) {
            mLastPosition = currentPosition
        } else {
            mLastPosition = -1
        }

        // show the first
        if (mLastPosition == -1 && newCount > 0) {
            mLastPosition = 0
        }

        createIndicators()
    }

    protected fun onCurrentLocationChange() {
        var currentIndicator: View
        if (mLastPosition >= 0 && getChildAt(mLastPosition) != null) {
            currentIndicator = getChildAt(mLastPosition)
            currentIndicator.setBackgroundResource(mIndicatorUnselectedBackgroundResId)
        }
        val position = currentPosition
        val selectedIndicator = getChildAt(position)
        if (selectedIndicator != null) {
            selectedIndicator.setBackgroundResource(mIndicatorBackgroundResId)
        }
        mLastPosition = position
    }

    private fun createIndicators() {
        removeAllViews()
        val count = itemCount
        if (count <= 0) {
            return
        }
        var currentItem = currentPosition
        val orientation = orientation

        if (currentItem < 0) currentItem = 0
        for (i in 0 until count) {
            if (currentItem == i) {
                addIndicator(i, orientation, mIndicatorBackgroundResId)
            } else {
                addIndicator(i, orientation, mIndicatorUnselectedBackgroundResId)
            }
        }
    }

    private fun addIndicator(pos: Int, orientation: Int, @DrawableRes backgroundDrawableId: Int) {
        val indicator = View(context)
        indicator.setBackgroundResource(backgroundDrawableId)
        indicator.setOnClickListener { view ->
            recyclerView?.let {
                Timber.d("pos=%d", pos)
                it.smoothScrollToPosition(pos)
                currentPosition = pos
            }
            Timber.d("pos=%d", pos)
        }

        addView(indicator, mIndicatorWidth, mIndicatorHeight)
        val lp = indicator.layoutParams as LinearLayout.LayoutParams

        if (orientation == LinearLayout.HORIZONTAL) {
            lp.leftMargin = mIndicatorMargin
            lp.rightMargin = mIndicatorMargin
        } else {
            lp.topMargin = mIndicatorMargin
            lp.bottomMargin = mIndicatorMargin
        }

        indicator.setLayoutParams(lp)
    }

    fun dip2px(dpValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    companion object {
        private val DEFAULT_INDICATOR_WIDTH = 5
    }
}