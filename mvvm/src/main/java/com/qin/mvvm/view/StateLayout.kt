package com.qin.mvvm.view

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.InflateException
import android.view.View
import android.widget.FrameLayout
import com.qin.mvvm.R

//https://github.com/LZKDreamer/StateLayout
class StateLayout : FrameLayout {

    // loading page
    private lateinit var mLoadingView: View
    private lateinit var mLoadingTextView: View
    private var mLoadingText: String? = null

    // error page
    private lateinit var mErrorView: View
    private lateinit var mErrorTextView: View
    private var mErrorText: String? = null

    // empty page
    private lateinit var mEmptyView: View
    private lateinit var mEmptyTextView: View
    private var mEmptyText: String? = null

    // content page
    private lateinit var mContentView: View


    constructor(context: Context): this(context, null)
    constructor(context: Context, attrs: AttributeSet?): this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int): super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    fun init(context: Context, attrs: AttributeSet?) {
        var typedArray: TypedArray = context!!.obtainStyledAttributes(attrs, R.styleable.StateLayout)
        mLoadingView = View.inflate(context, typedArray.getResourceId(R.styleable.StateLayout_state_loading_layout, R.layout.default_loading_view), null)
        addView(mLoadingView)
        mErrorView = View.inflate(context, typedArray.getResourceId(R.styleable.StateLayout_state_error_layout, R.layout.default_error_view), null)
        addView(mErrorView)
        mEmptyView = View.inflate(context, typedArray.getResourceId(R.styleable.StateLayout_state_empty_layout, R.layout.default_empty_view), null)
        addView(mEmptyView)

        mLoadingTextView = mLoadingView.findViewById(typedArray.getResourceId(R.styleable.StateLayout_state_empty_layout, R.id.text_loading))
        mErrorTextView = mLoadingView.findViewById(typedArray.getResourceId(R.styleable.StateLayout_state_error_layout, R.id.text_error))
        mEmptyTextView = mLoadingView.findViewById(typedArray.getResourceId(R.styleable.StateLayout_state_empty_layout, R.id.text_empty))

        mLoadingText = typedArray.getString(R.styleable.StateLayout_state_loading_text)
        mErrorText = typedArray.getString(R.styleable.StateLayout_state_error_text)
        mEmptyText = typedArray.getString(R.styleable.StateLayout_state_empty_text)



        typedArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (childCount != 4) {
            throw InflateException("Must only child view")
        }
        for (i in 0..(childCount-1)) {
            var child = getChildAt(i)
            if (child != mLoadingView && child != mErrorView && child != mEmptyView) {
                mContentView = child
                break
            }
        }
    }

    fun showLoadingView() {
        showView(mLoadingView)
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
        mEmptyView?.setOnClickListener(OnClickListener {
            onClick(it)
        })
    }

    fun setErrorClickListener(onClick: (View?) -> Unit) {
        mErrorView?.setOnClickListener(OnClickListener {
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
            mEmptyView?.setOnClickListener(listener)
        }
    }

    fun setErrorClickListener(listener: OnClickListener) {
        listener?.let {
            mErrorView?.setOnClickListener(listener)
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
            child!!.visibility = if (child == view) View.VISIBLE else View.GONE
        }
    }
}