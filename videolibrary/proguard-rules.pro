# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in F:\AndroidSDK/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法
-optimizationpasses 5   # 指定代码的压缩级别
-dontusemixedcaseclassnames  # 是否使用大小写混合
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify    # 混淆时是否做预校验
-verbose # 混淆时是否记录日志
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-ignorewarnings  # 抑制警告

-keep class com.wyt.picturebook.bean.** { *; }
#-keep class com.wyt.picturebook.openRelevant.Opens {
#    public void openInternalHB();
#}
#-keep class cn.jzvd.**{*;}
#-keep class com.wyt.videolibrary.**{*;}
-keep class com.wyt.videolibrary.bean.VideoUrlBean{*;}

#xutils
-keepattributes Signature,*Annotation*
-keep public class org.xutils.** {
    public protected *;
}
-keep public interface org.xutils.** {
    public protected *;
}
-keepclassmembers class * extends org.xutils.** {
    public protected *;
}
-keepclassmembers @org.xutils.db.annotation.* class * {*;}
-keepclassmembers @org.xutils.http.annotation.* class * {*;}
-keepclassmembers class * {
    @org.xutils.view.annotation.Event <methods>;
}
#--简单的说就是你的第三方包邮版本过低的,如果你运行正常的话可以使用 -dontwarn把上面223个警告取消
-dontwarn org.eclipse.jetty.http.**
-dontwarn org.seamless.**
-dontwarn org.eclipse.jetty.server.**
-dontwarn org.eclipse.jetty.servlet.**
-dontwarn com.squareup.picasso.**
-dontwarn okio.**
-dontwarn org.eclipse.jetty.continuation.**
-dontwarn org.eclipse.jetty.security.**
-dontwarn org.fourthline.cling.**
-dontwarn org.eclipse.jetty.util.**

#-------------jar
-dontwarn bitter.jnibridge.**
-keep class  bitter.jnibridge.** { *;}
-dontwarn com.unity3d.player.**
-keep class  com.unity3d.player.** { *;}
-dontwarn org.fmod.**
-keep class  org.fmod.** { *;}
-dontwarn org.fmod.**
-keep class  org.fmod.** { *;}
-dontwarn org.apache.tools.**
-keep class  org.apache.tools.** { *;}

-keep class  com.umeng.** { *;}
-keep class  com.visiontalk.basesdk.** { *;}
-keep class  com.wyt.picturebook.constant.ConfigureConstants { *;}
-keep class  com.visiontalk.vtbrsdk.** { *;}

#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}

#不要混淆MyBean的所有属性与方法
#-keepclasseswithmembers class BaseApplication {
#    <fields>;
#    <methods>;
#}

-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
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
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}
-keepclassmembers class * {
    void *(**On*Event);
}
  -assumenosideeffects class android.util.Log {
  public static *** d(...);
  public static *** e(...);
  public static *** i(...);
  public static *** v(...);
  public static *** println(...);
  public static *** w(...);
  public static *** wtf(...);
  }
#----------------------------------------------------------------------------

-keep public class cn.jzvd.JZMediaSystem {*; }
-keep public class cn.jzvd.demo.CustomMedia.CustomMedia {*; }
-keep public class cn.jzvd.demo.CustomMedia.JZMediaIjk {*; }
-keep public class cn.jzvd.demo.CustomMedia.JZMediaSystemAssertFolder {*; }

-keep class tv.danmaku.ijk.media.player.** {*; }
-dontwarn tv.danmaku.ijk.media.player.*
-keep interface tv.danmaku.ijk.media.player.** { *; }

#---------------------------------webview------------------------------------
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
#----------------------------------------------------------------------------
#---------------------------------------------------------------------------------------------------
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

#-keep class com.wyt.download.util.OpenContentUtils {*;}
#-keep class com.wyt.download.util.OnNoDataCallbackListener {*;}
#
##---------------------NotProguard 是个编译时注解，不会对运行时性能有任何影响。可修饰类、方法、构造函数、属性。
# keep annotated by NotProguard  - by sollyu
#-keep @com.sollyu.android.not.proguard.NotProguard class * {*;}
#-keep,allowobfuscation @interface com.sollyu.android.not.proguard.NotProguard
#-keepclassmembers class * { @com.sollyu.android.not.proguard.NotProguard *;}

#整个类不混淆
#@NotProguard
#public class User {}
#
#单个属性不混淆
#@NotProguard
#public int id;
#单个方法不混淆
#@NotProguard
#public void callButton1(Activity activity) {}

-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}


-keep class com.wyt.picturebook.openRelevant.** { *; }
#-keep class com.wyt.picturebook.openRelevant.Opens {
#    public void openInternalHB();
#}


-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-keepclassmembers class * {
   @android.webkit.JavascriptInterface <methods>;
}


-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

-keep class okio.** { *; }
-keep interface okio.** { *; }
-dontwarn okio.**

-keep class com.hitomi.** { *; }
-keep interface com.hitomi.** { *; }
-dontwarn com.hitomi.**

-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*; }
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*; }


-keep class com.tencent.mm.opensdk.** {
    *;
}

-keep class com.tencent.wxop.** {
    *;
}

-keep class com.tencent.mm.sdk.** {
    *;
}