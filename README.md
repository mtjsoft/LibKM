# lib-kotlinmvvm

## 1、介绍
kotlin-mvvm 组件化快速开发框架 [DataBinding 版本](https://github.com/mtjsoft/LibKM/tree/databinding/)

## 2、软件架构

![架构图](https://images.gitee.com/uploads/images/2020/0901/152253_8c9fbf3d_1510987.png "KotlinMVVM组件化.png")

## 3、安装教程

Add it in your root build.gradle at the end of repositories:

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add the dependency：

```
dependencies {
	        implementation 'com.gitee.mtj_java:LibKM:DataBinding_1.0.0'
	}

```

 **_强烈推荐 [配套使用的AS代码生成插件](https://gitee.com/mtj_java/kotlinmvpcode)，可直接生成各个base基类的继承模板代码（下载不同分支中的 ***.jar 文件，直接拖拽到AS中即可，安装成功后，可以在AS的顶部Code菜单下面看到）。_** 

## 4、配置使用说明

### 4.1、添加文件
将根目录的config.gradle、config_build.gradle、component_build.gradle、proguard-rules.pro文件复制到自己的项目根目录，以便于自行修改相关的内容。
在根目录的build.gradle中添加
```
apply from: "config.gradle"
```
### 4.2、模式开关
需要一个全局变量来控制当前运行的工程是隔离状态还是合并状态。在gradle.properties中定义：

```
# isBuildModule 为 true 时可以使每个组件独立运行，false 则可以将所有组件集成到宿主 App 中。
isBuildModule=false
```
### 4.3、debug切换
在组件的build.gradle中动态切换library与application

```
if (isBuildModule.toBoolean()) {
    //作为独立App应用运行
    apply plugin: 'com.android.application'
} else {
    //作为组件运行
    apply plugin: 'com.android.library'
}
```
当　isBuildModule 为 true 时，它是一个application，拥有自己的包名

```
android {
    resourcePrefix "login_" //给 Module 内的资源名增加前缀, 避免资源名冲突。如：登录模块添加 login_
    defaultConfig {
        //如果是独立模块，则使用当前组件的包名
        if (isBuildModule.toBoolean()) {
            applicationId 组件的包名
        }
    }
}
```
### 4.4、manifest配置
组件在自己的AndroidManifest.xml各自配置，application标签无需添加属性，也不需要指定activity的intent-filter。当合并打包时，gradle会将每个组件的AndroidManifest合并到宿主App中。

```
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="包名">
    <application>
    </application>
</manifest>
```
组件独立运行时，就需要单独的一个AndroidManifest.xml作为调试用。可以在src/main文件夹下创建一个debug/AndroidManifest.xml。配置application标签属性，并指定启动的activity。

```
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="包名">
    <application
        ...
        >
        <activity 
            ...
            >
            <intent-filter>
                ...
            </intent-filter>
        </activity>
    </application>
</manifest>
```
并在build.gradle中配置

```
android {
    sourceSets {
        main {
            ...
            if (isBuildModule.toBoolean()) {
                //独立运行
                manifest.srcFile 'src/main/debug/AndroidManifest.xml'
            } else {
                //合并到宿主
                manifest.srcFile 'src/main/AndroidManifest.xml'
                resources {
                    //正式版本时，排除debug文件夹下所有调试文件
                    exclude 'src/main/debug/*'
                }
            }
        }
    }
}
```
### 4.5、配置抽取
已经将每个组件的build.gradle公共部分抽取到了 config_build.gradle、component_build.gradle文件中了，在 4.1中复制后，可直接使用。详细使用可参考本示例的 module-login 组件的配置。

## 5、组件的Application初始化

反射是一种解决组件初始化的方法。
框架中定义了IComponentApplication类，包含一个 init 初始化方法。

```
/**
 * 利用反射初始化 Module Application
 *
 * 不能混淆
 */
interface IComponentApplication {
    fun init(application: Application)
}
```
### 5.1、初始化实现
在各自的组件中创建一个初始化类，实现 IComponentApplication 接口。

```
/**
 * 作为合并Module时，初始化自身的一些库
 */
class ModuleApp : IComponentApplication {

    override fun init(application: Application) {
        // 在这里初始化自身的库
    }
}
```
在宿主的Application中调用初始化方法。继承BaseApplication，已实现modulesApplicationInit反射初始化方法

```
class MyApp : BaseApplication() {

    // 需要初始化的组件 ModuleApp 路径
    private val modulesList = arrayListOf(
        "com.suntront.module_login.ModuleApp"
    )
    
    override fun onCreate() {
        super.onCreate()
        // 反射初始化
        modulesApplicationInit(modulesList)
    }
}
```
 _注意：组件中初始化的Module类不能被混淆_ 

## 6、Base基类快速上手使用

### 6.1、关联ViewModel

以 app 中的 TestDataActivity 、TestDataViewModel 、activity_main.xml为例

在activity_main.xml中关联TestDataViewModel。

variable - type：ViewModel类的全路径
variable - name：变量名

```
<layout>
    <data>
        <variable
            type="cn.mtjsoft.libkotlinmvvm.test.TestDataViewModel"
            name="testViewModel"
        />
    </data>
    .....

</layout>
```
### 6.2、继承 BaseActivity

TestDataActivity 继承 BaseActivity

```
class TestDataActivity : BaseActivity<ActivityMainBinding, TestDataViewModel>() {
    
    // 6.1中配置 variable - name：变量名后，由系统生成的ViewModel的id（使用BR.xxx）。
    override fun initVariableId(): Int = BR.testViewModel
    
    // 传入布局id
    override fun layoutResId(): Int = R.layout.activity_main
    
    // 实例化 ViewModel
    override fun providerVMClass(): Class<TestDataViewModel> = TestDataViewModel::class.java

    override fun initView() {
        // 隐藏toolbar返回按钮
        isShowBackView(false)
        setPageTitle("我是标题")
    }

    override fun initData() {
    }

    override fun onClick(p0: View) {
        super.onClick(p0)
    }

    override fun processHandlerMsg(msg: Message) {
    }

}
```
经过6.1的xml修改保存后databinding会自动生成一个ActivityMainBinding类。（如果没有生成，试着点击Build->Clean Project）。

BaseActivity是一个抽象类，有两个泛型参数，一个是ViewDataBinding，另一个是BaseViewModel，上面的 ActivityMainBinding 则是继承的ViewDataBinding作为第一个泛型约束，TestDataViewModel继承BaseViewModel作为第二个泛型约束。

### 6.3、继承 BaseViewModel

TestDataViewModel 继承 BaseViewModel
```
class TestDataViewModel : BaseViewModel() {

    val liveData = MutableLiveData<String>()
}
```
BaseViewModel与BaseActivity通过LiveData来处理常用UI逻辑。在这个TestDataViewModel 中就可以尽情的写你的逻辑了！
BaseViewModel已经内置了 showLoadingUI、showToastUI、startActivity、onBackPressed等UI方法。

### 6.4、数据绑定

databinding框架自带的双向绑定，也有扩展

如在TestDataViewModel 中定义的 liveData

```
val liveData = MutableLiveData<String>()
```
在 activity_main.xml的 EditText标签中用 ‘@={}’ 进行双向绑定，当输入的内容改变时，对于的liveData会随之改变。

TextView中用 ‘@{}’单项绑定显示的内容也会随之变化。

```
<TextView
            android:id="@+id/show_title"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="显示输入的文字"
            android:padding="10dp"
            android:text="@{testViewModel.liveData}" />

        <EditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:padding="10dp"
            android:text="@={testViewModel.liveData}" />
```

至此，一个完成的使用流程基本完成了。其余的 BaseDataActivity 、BaseFragment等等基类的使用方法都是类似的。

 **_强烈推荐 [配套使用的AS代码生成插件](https://gitee.com/mtj_java/kotlinmvpcode)，可直接生成各个base基类的继承模板代码（下载不同分支中的 ***.jar 文件，直接拖拽到AS中即可，安装成功后，可以在AS的顶部Code菜单下面看到）。_** 

OK，先介绍到这儿，更多使用细节，以后再慢慢补充！
