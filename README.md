# mvvm-kotlin
一个MVVM开发框架，基于Kotlin+Retrofit+Coroutine+Databinding+LiveData+LiveEventBus封装：
项目地址：[mvvm-kotlin](https://github.com/qintangtao/mvvm-kotlin)

### 1，Download
##### 1.1 Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

##### 1.2 Add the dependency
Add it in your app build.gradle at the end of dependencies:
```
dependencies {
	...
	implementation 'com.github.qintangtao:mvvm-kotlin:1.0.1'
}
```

##### 1.3 Enable the databinding
Add it in your app build.gradle at the end of android:
```
android {
	...
	dataBinding {
        enabled = true
    }
}
```

### 2，Usage
##### 2.1 BaseActivity
```
class DetailActivity : BaseActivity<DetailViewModel, ActivityDetailBinding>() {
	
	override fun layoutId() = R.layout.activity_detail
	
	override fun initView(savedInstanceState: Bundle?) {
		mBinding?.viewModel = viewModel
		...
	}
	
	override fun initData() {
		...
	}
}
```

##### 2.2 BaseFragment
```
class ProjectFragment : BaseFragment<ProjectViewModel, FragmentProjectBinding>() {
	
	override fun layoutId() = R.layout.fragment_project
	
	override fun initView(savedInstanceState: Bundle?) {
		mBinding?.viewModel = viewModel
		...
	}
	
	override fun lazyLoadData() {
		...
	}
}
```

##### 2.3 BaseStateFragment
*.xml
```
<com.qin.mvvm.view.StateLayout
	android:id="@+id/stateLayout"
	android:layout_width="match_parent"
	android:layout_height="match_parent">
	
	...
	
</com.qin.mvvm.view.StateLayout>
```

Fragment
```
class PopularFragment : BaseStateFragment<PopularViewModel, FragmentPopularBinding>() {
	
	override fun layoutId() = R.layout.fragment_popular
	override fun stateLayout() = stateLayout
	
	override fun initView(savedInstanceState: Bundle?) {
		mBinding?.viewModel = viewModel
		...
	}
	
	override fun lazyLoadData() {
		...
	}
}
```

##### 2.4 BaseViewModel
fun
```
	launchUI
	launchFlow
	launchGo
	launchOnlyResult
	launchSerialResult
	launchFlowResult
	launchFlowzipResult
	launchFlowCombineResult
	launchFlowCombine3Result
	
```

```
class PlazaViewModel : BaseViewModel() {
	
	private val repository by lazy { HomeRepository.getInstance(ApiRetrofit.getInstance()) }
	 
	private val _items = MutableLiveData<MutableList<Article>>()
	val items: LiveData<MutableList<Article>> = _items
	
	fun refreshUserArticleList(isNotify: Boolean = false) {
        launchOnlyResult({
            repository.getUserArticleList(0)
        }, {
            if (it.datas.isEmpty()) RESULT.EMPTY.code
            else {
                page = it.curPage
                _items.value = it.datas.toMutableList()
                RESULT.SUCCESS.code
            }
        }, isNotify = isNotify)
    }
	
}
```


