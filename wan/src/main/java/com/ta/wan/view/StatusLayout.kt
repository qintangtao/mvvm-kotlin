package com.ta.wan.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.ta.wan.R

class StatusLayout : FrameLayout {

    private lateinit var mLoadingView: View
    private lateinit var mErrorView: View
    private lateinit var mEmptyView: View
    private lateinit var mContentView: View

    constructor(context: Context): super(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(context, attrs);
    }

    fun init(context: Context, attrs: AttributeSet?) {
        var typedArray: TypedArray = context!!.obtainStyledAttributes(attrs, R.styleable.StatusLayout)
        mLoadingView = View.inflate(context, typedArray.getResourceId(R.styleable.StatusLayout_loading_view, R.layout.default_loading_view), this)
        mErrorView = View.inflate(context, typedArray.getResourceId(R.styleable.StatusLayout_error_view, R.layout.default_error_view), this)
        mEmptyView = View.inflate(context, typedArray.getResourceId(R.styleable.StatusLayout_empty_view, R.layout.default_empty_view), this)
        mContentView = findViewById(typedArray.getResourceId(R.styleable.StatusLayout_content_view, 0))
        typedArray.recycle()
    }

    fun showLoadingView() {
        showView(mLoadingView);
    }

    fun showErrorView() {
        showView(mErrorView)
    }

    fun showEmptyView() {
        showView(mEmptyView)
    }

    fun showContentView() {
        showView(mContentView)
    }

    private fun showView(view: View) {
        for (i in 0..childCount) {
            var child: View = getChildAt(i)
            child!!.visibility = if (child == view) View.VISIBLE else View.GONE;
        }
    }
}