# lib-kotlinmvvm

## 1、介绍
kotlin-mvvm 组件化快速开发框架 DataBinding 版本

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

[配套使用的AS代码生成插件](https://gitee.com/mtj_java/kotlinmvpcode)，自行下载安装不同的版本。可直接生成模板代码。

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
 * 作为合并Module时，初始化自身的一些库
 */
class ModuleApp : IComponentApplication {

    companion object {
    }

    override fun init(application: Application) {
        // 在这里初始化自身的库
    }
}
```


 _注意：组件中初始化的Module类不能被混淆_ 

