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
在主工程app的build.gradle的android {}中加入：
Add it in your app build.gradle at the end of dependencies:
```
dependencies {
	implementation 'com.github.qintangtao:mvvm-kotlin:1.0.1'
}
```

