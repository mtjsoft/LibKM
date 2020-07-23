#
#-------------------------------------------基本不用动区域开始----------------------------------------------
#
#
# -----------------------------基本 -----------------------------
#
# 指定代码的压缩级别 0 - 7(指定代码进行迭代优化的次数，在Android里面默认是5，这条指令也只有在可以优化时起作用。)
-optimizationpasses 5
# 混淆时不会产生形形色色的类名(混淆时不使用大小写混合类名)
-dontusemixedcaseclassnames
# 指定不去忽略非公共的库类(不跳过library中的非public的类)
-dontskipnonpubliclibraryclasses
# 指定不去忽略包可见的库类的成员
-dontskipnonpubliclibraryclassmembers
#不进行优化，建议使用此选项，
-dontoptimize
 # 不进行预校验,Android不需要,可加快混淆速度。
-dontpreverify
# 屏蔽警告
#-ignorewarnings

# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
# 保护代码中的Annotation不被混淆
-keepattributes *Annotation*
# 避免混淆泛型, 这在JSON实体映射时非常重要
-keepattributes Signature
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
 #优化时允许访问并修改有修饰符的类和类的成员，这可以提高优化步骤的结果。
# 比如，当内联一个公共的getter方法时，这也可能需要外地公共访问。
# 虽然java二进制规范不需要这个，要不然有的虚拟机处理这些代码会有问题。当有优化和使用-repackageclasses时才适用。
#指示语：不能用这个指令处理库中的代码，因为有的类和类成员没有设计成public ,而在api中可能变成public
-allowaccessmodification
#当有优化和使用-repackageclasses时才适用。
-repackageclasses ''
 # 混淆时记录日志(打印混淆的详细信息)
 # 这句话能够使我们的项目混淆后产生映射文件
 # 包含有类名->混淆后类名的映射关系
-verbose

# 保持哪些类不被混淆
#继承activity,application,service,broadcastReceiver,contentprovider....不进行混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.support.multidex.MultiDexApplication
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep class android.support.** {*;}## 保留support下的所有类及其内部类

-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
#表示不混淆上面声明的类，最后这两个类我们基本也用不上，是接入Google原生的一些服务时使用的。
#----------------------------------------------------

# 保留继承的
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**


#表示不混淆任何包含native方法的类的类名以及native方法名，这个和我们刚才验证的结果是一致
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembernames class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#这个主要是在layout 中写的onclick方法android:onclick="onClick"，不进行混淆
#表示不混淆Activity中参数是View的方法，因为有这样一种用法，在XML中配置android:onClick=”buttonClick”属性，
#当用户点击该按钮时就会调用Activity中的buttonClick(View view)方法，如果这个方法被混淆的话就找不到了
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}

#表示不混淆枚举中的values()和valueOf()方法，枚举我用的非常少，这个就不评论了
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#表示不混淆任何一个View中的setXxx()和getXxx()方法，
#因为属性动画需要有相应的setter和getter的方法实现，混淆了就无法工作了。
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#表示不混淆Parcelable实现类中的CREATOR字段，
#毫无疑问，CREATOR字段是绝对不能改变的，包括大小写都不能变，不然整个Parcelable工作机制都会失败。
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
# 这指定了继承Serizalizable的类的如下成员不被移除混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepattributes InnerClasses

# 保留R下面的资源
-keep class **.R$* {
 *;
}
#不混淆资源类下static的
-keepclassmembers class **.R$* {
    public static <fields>;
}

# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

# 保留我们自定义控件（继承自View）不被混淆
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#
#----------------------------- WebView(项目中没有可以忽略) -----------------------------
#
#webView需要进行特殊处理
-keepclassmembers class fqcn.of.javascript.interface.for.Webview {
   public *;
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
    public boolean *(android.webkit.WebView, java.lang.String);
}
-keepclassmembers class * extends android.webkit.WebViewClient {
    public void *(android.webkit.WebView, jav.lang.String);
}
#在app中与HTML5的JavaScript的交互进行特殊处理
#我们需要确保这些js要调用的原生方法不能够被混淆，于是我们需要做如下处理：
-keepclassmembers class com.ljd.example.JSInterface {
    <methods>;
}


#
# ----------------------------- 实体Model不能混淆，否则找不到对应的属性获取不到值 -----------------------------
#
# 实体类不混淆
-keep class **.model.**{*;}
-keep class **.bean.**{*;}
-keep class **.entity.**{*;}
# 保留 继承自 IComponentApplication不被混淆，否则无法通过反射初始化Module的Application
-keep class * implements com.mtjsoft.www.kotlinmvputils.imp.IComponentApplication
# ----------------------------- 其他的 -----------------------------
# 删除代码中Log相关的代码
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}
# 保持测试相关的代码
-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**
#
#-------------------------------------------第三方SDK混淆区域开始----------------------------------------------
# 简单粗暴
# -dontwarn 结合 -keep 设置第三方SDK不混淆
#

# 沉浸式状态栏
-dontwarn com.gyf.immersionbar.**
-keep class com.gyf.immersionbar.* {*;}

# glide
-dontwarn com.github.bumptech.glide.**
-keep class com.github.bumptech.glide.**{*;}

# banner
-dontwarn com.youth.banner.**
-keep class com.youth.banner.**{*;}

# bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-dontwarn com.tencent.tinker.**
-keep public class com.tencent.tinker.**{*;}
-keep class android.support.**{*;}

# EventBus
-keepattributes *Annotation*
-keepclassmembers class ** {@org.greenrobot.eventbus.Subscribe <methods>;}
-keep enum org.greenrobot.eventbus.ThreadMode {*;}
# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {<init>(java.lang.Throwable);}

# Retrofit2
-dontwarn retrofit2.**
-keep class retrofit2.**{*;}

# RxJava2 & RxAndroid
-dontwarn io.reactivex.rxjava2.**
-keep class io.reactivex.rxjava2.**{*;}

# rxlifecycle2
-dontwarn com.trello.rxlifecycle2.**
-keep class com.trello.rxlifecycle2.**{*;}

# OkHttp3
-dontwarn okhttp3.**
-dontwarn okio.**
-keep class okhttp3.**{*;}
-keep class okio.**{*;}

# Gson
-dontwarn com.google.gson.**
-keep class com.google.gson.**{*;}
-keep class com.google.protobuf.** {*;}
-keep interface com.google.gson.**{*;}

# --------------------微信相关SDK混淆开始----------------------
-dontwarn com.tencent.mm.**
-dontwarn com.tencent.wxop.**
-keep class com.tencent.mm.**{*;}
-keep class com.tencent.wxop.**{*;}
-keep class **.pay.**{*;}
-keep class **.wxapi.**{*;}
# --------------------微信相关SDK混淆结束----------------------

# 支付宝钱包
-dontwarn com.alipay.**
-dontwarn HttpUtils.HttpFetcher
-dontwarn com.ta.utdid2.**
-dontwarn com.ut.device.**
-keep class com.alipay.**{*;}
-keep class com.ta.utdid2.**{*;}
-keep class com.ut.device.**{*;}

# 百度地图
-keep class com.baidu.** {*;}
-keep class mapsdkvi.com.** {*;}
-dontwarn com.baidu.**

# 极光推送
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
-keep class android.support.** { *; }
-keep class androidx.** { *; }
-keep class com.google.android.** { *; }

#
-dontwarn com.wang.avi.**
-keep class com.wang.avi.**{*;}
#
-dontwarn me.jessyan.**
-keep class me.jessyan.**{*;}
#
-dontwarn com.contrarywind.**
-keep class com.contrarywind.**{*;}
#
-dontwarn com.yuyh.imgsel.**
-keep class com.yuyh.imgsel.**{*;}
#
-dontwarn com.hyman.**
-keep class com.hyman.**{*;}
#
-dontwarn com.bm.photoview.**
-keep class com.bm.photoview.**{*;}
#
-dontwarn com.gyf.immersionbar.**
-keep class com.gyf.immersionbar.**{*;}
#
-dontwarn com.github.mcxtzhang.**
-keep class com.github.mcxtzhang.**{*;}
#
-dontwarn de.hdodenhof.**
-keep class de.hdodenhof.**{*;}
#
-dontwarn com.github.mcxtzhang.**
-keep class de.hdodenhof.**{*;}
#
-dontwarn com.makeramen.**
-keep class com.makeramen.**{*;}
#
-dontwarn com.github.razerdp.**
-keep class com.github.razerdp.**{*;}
#
-dontwarn com.alibaba.android.**
-keep class com.alibaba.android.**{*;}
#
-dontwarn cn.yc.**
-keep class cn.yc.**{*;}
#
-dontwarn com.scwang.smartrefresh.**
-keep class com.scwang.smartrefresh.**{*;}
#
-dontwarn com.github.CymChad.**
-keep class com.github.CymChad.**{*;}
#
-dontwarn com.yzq.zxinglibrary.**
-keep class com.yzq.zxinglibrary.**{*;}
#
-dontwarn com.squareup.leakcanary.**
-keep class com.squareup.leakcanary.**{*;}
#
-dontwarn com.zhy.view.flowlayout.**
-keep class com.zhy.view.flowlayout.**{*;}
# 查看大图
-dontwarn com.github.SherlockGougou.**
-keep class com.github.SherlockGougou.**{*;}
# AOP
-dontwarn org.aspectj.**
-keep class org.aspectj.**{*;}
# autosize
-keep class me.jessyan.autosize.** { *; }
-keep interface me.jessyan.autosize.** { *; }
# routes
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep public class com.alibaba.android.arouter.facade.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
# 如果使用了 byType 的方式获取 Service，需添加下面规则，保护接口
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider
# 如果使用了 单类注入，即不定义接口实现 IProvider，需添加下面规则，保护实现
-keep class * implements com.alibaba.android.arouter.facade.template.IProvider
# rxhttp
-keep class rxhttp.**{*;}
# -----------------------------第三方不混淆,end -----------------------------
#
#-------------------------------------------第三方SDK混淆区域结束----------------------------------------------
#
