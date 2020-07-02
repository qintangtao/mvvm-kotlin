package com.qin.mvvm.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.InflateException
import android.view.View
import android.widget.FrameLayout
import com.qin.mvvm.R

class StateLayout : FrameLayout {

    private lateinit var mLoadingView: View
    private lateinit var mErrorView: View
    private lateinit var mEmptyView: View
    private lateinit var mContentView: View
    private lateinit var mEmptyRetry: View
    private lateinit var mErrorRetry: View

    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(context, attrs);
    }

    fun init(context: Context, attrs: AttributeSet?) {
        var typedArray: TypedArray = context!!.obtainStyledAttributes(attrs, R.styleable.StateLayout)
        mLoadingView = View.inflate(context, typedArray.getResourceId(R.styleable.StateLayout_loading_view, R.layout.default_loading_view), null)
        addView(mLoadingView)
        mErrorView = View.inflate(context, typedArray.getResourceId(R.styleable.StateLayout_error_view, R.layout.default_error_view), null)
        addView(mErrorView)
        mEmptyView = View.inflate(context, typedArray.getResourceId(R.styleable.StateLayout_empty_view, R.layout.default_empty_view), null)
        addView(mEmptyView)
        mEmptyRetry = findViewById(typedArray.getResourceId(R.styleable.StateLayout_empty_retry, R.id.text_empty))
        mErrorRetry = findViewById(typedArray.getResourceId(R.styleable.StateLayout_error_retry, R.id.text_error))
        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount != 4) {
            throw InflateException("Must only child view");
        }
        for (i in 0..(childCount-1)) {
            var child = getChildAt(i)
            if (child != mLoadingView && child != mErrorView && child != mEmptyView) {
                mContentView = child
                break
            }
        }
        //showLoadingView()
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

    fun setEmptyClickListener(onClick: (View?) -> Unit) {
        mEmptyRetry?.setOnClickListener(OnClickListener {
            onClick(it)
        })
    }

    fun setErrorClickListener(onClick: (View?) -> Unit) {
        mEmptyRetry?.setOnClickListener(OnClickListener {
            onClick(it)
        })
    }

    fun setOnClickListener(id: Int, onClick: (View?) -> Unit) {
        var view: View = findViewById(id)
        view?.setOnClickListener(OnClickListener{
            onClick(it)
        })
    }

    fun setEmptyClickListener(listener: OnClickListener) {
        listener?.let {
            mEmptyRetry?.setOnClickListener(listener)
        }
    }

    fun setErrorClickListener(listener: OnClickListener) {
        listener?.let {
            mErrorRetry?.setOnClickListener(listener)
        }
    }

    fun setOnClickListener(id: Int, listener: OnClickListener) {
        listener?.let {
            var view: View = findViewById(id)
            view?.setOnClickListener(it)
        }
    }

    private fun showView(view: View) {
        for (i in 0..(childCount-1)) {
            var child = getChildAt(i)
            child!!.visibility = if (child == view) View.VISIBLE else View.GONE;
        }
    }
}