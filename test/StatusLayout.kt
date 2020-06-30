
class StatusLayout : FrameLayout {

	private lateinit var mLoadingView: View
	private lateinit var mErrorView: View
	private lateinit var mEmptyView: View
	private lateinit var mContentView: View
	private lateinit var mEmptyClickId: Int
	private lateinit var mErrorClickId: Int

	constructor(context: Context) : this(context, null)
	constructor(context: Context, attrs: AttrbuteSet?) : this(context, attrs, 0)
	constructor(context: Context, attrs: AttrbuteSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
		init(context, attrs)
	}
	
	fun init(context: Context, attrs: AttrbuteSet?) {
		var typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.StatusLayout)
		mLoadingView = View.inflate(context, typedArray.getResourceId(R.styleable.StatusLayout_loadingView, R.layout.loading_view), this)
		mErrorView = View.inflate(context, typedArray.getResourceId(R.styleable.StatusLayout_errorView, R.layout.error_view), this)
		mEmptyView = View.inflate(context, typedArray.getResourceId(R.styleable.StatusLayout_emptyView, R.layout.empty_view), this)
		mContentView = findByViewById(typedArray.getResourceId(R.styleable.StatusLayout_contentView, R.layout.content_view))
		mEmptyClickId = typedArray.getResourceId(R.styleable.StatusLayout_emptyClick, R.layout.empty_click)
		mErrorClickId = typedArray.getResourceId(R.styleable.StatusLayout_errorClick, R.layout.error_click)
		typedArray.recycle()
		showLoadingView();
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
		
	fun setEmptyClickListener(listener: OnClickListener) {
		listener?.let {
			setOnClickListener(mEmptyClickId) { v ->
				showLoadingView()
				listener.onClick(v)
			}
		}
	}
	
	fun setErrorClickListener(listener: OnClickListener) {
		listener?.let {
			setOnClickListener(mErrorClickId) { v ->
				showLoadingView()
				listener.onClick(v)
			}
		}
	}
	
	fun setOnClickListener(id: Int, listener: OnClickListener) {
		listener?.let {
			findViewById(id)?.setOnClickListener(lt)
		}
	}
	
	private fun showView(view: View) {
		for (i in getChildCount()) {
			child: View = getChildAt(i)
			child.setVisibility(child == view ? View.VISIBLE : View.GONE)
		}
	}
}