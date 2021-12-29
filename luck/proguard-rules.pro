# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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



#############################################
#
# 对于一些基本指令的添加
#
#############################################

#不要警告
#-dontwarn
#忽略警告
#-ignorewarnings
#优化 0-7
-optimizationpasses 7
#不要使用混合大小写的类名
-dontusemixedcaseclassnames
#不要优化
#-dontoptimize
#冗长
-verbose
#不要预先验证
-dontpreverify
#过度超载
-overloadaggressively
#重新打包类
-repackageclasses repack
#允许访问修改
-allowaccessmodification
#调整类字符串
-adaptclassstrings
#调整资源文件名
-adaptresourcefilenames
#调整资源文件内容
-adaptresourcefilecontents
#不要跳过非公共library classes
-dontskipnonpubliclibraryclasses
#不要跳过非公共library class members
-dontskipnonpubliclibraryclassmembers

-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable,Exceptions
-optimizations !code/simplification/cast,!field/*,!class/merging/*

#############################################
#
# Android开发中一些需要保留的公共部分
#
#############################################

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep class android.support.** {*;}
-keep class **.R$* {*;}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#webView处理，项目中没有使用到webView忽略即可
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-keepclassmembers class * extends android.webkit.webViewClient {
#    public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap);
#    public boolean *(android.webkit.WebView, java.lang.String);
#}
#-keepclassmembers class * extends android.webkit.webViewClient {
#    public void *(android.webkit.webView, jav.lang.String);
#}

#############################################
#
# 项目中特殊处理部分
#
#############################################

#-----------处理反射类---------------



#-----------处理js交互---------------



#-----------处理实体类---------------



#-----------处理第三方依赖库---------





