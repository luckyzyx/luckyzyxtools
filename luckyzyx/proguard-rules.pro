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
-optimizationpasses 10
#不要使用混合大小写的类名
-dontusemixedcaseclassnames
#不优化输入的类文件
-dontoptimize
#混淆时是否记录日志
-verbose
#不要预先验证
#-dontpreverify
#过度超载
-overloadaggressively
#重新打包类
-repackageclasses com.repack
#允许访问修改
-allowaccessmodification
#调整类字符串
-adaptclassstrings
#调整资源文件名
-adaptresourcefilenames
#调整资源文件内容
-adaptresourcefilecontents
#不要跳过非公共library classes
#-dontskipnonpubliclibraryclasses
#不要跳过非公共library class members
#-dontskipnonpubliclibraryclassmembers
#保持反射
-keepattributes EnclosingMethod
#保留注释
-keepattributes *Annotation*,InnerClasses
#保持泛型
-keepattributes Signature
#保留行号,异常
-keepattributes SourceFile,LineNumberTable,Exceptions
#混淆时采用的算法
-optimizations !code/simplification/cast,!field/*,!class/merging/*

#############################################
#
# Android开发中一些需要保留的公共部分
#
#############################################

# 保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Appliction
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep class android.support.** {*;}
-keep class **.R$* {*;}
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService

#-keep class * implements de.robv.android.xposed.IXposedHookLoadPackage {
#    public void *(de.robv.android.xposed.callbacks.XC_LoadPackage$LoadPackageParam);
#}
#
#-keep class * implements de.robv.android.xposed.IXposedHookInitPackageResources {
#    public void *(de.robv.android.xposed.callbacks.XC_InitPackageResources$InitPackageResourcesParam);
#}

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


##记录生成的日志数据,gradle build时在本项目根目录输出##
#apk 包内所有 class 的内部结构
#-dump proguard/class_files.txt
#未混淆的类和成员
#-printseeds proguard/seeds.txt
#列出从 apk 中删除的代码
#-printusage proguard/unused.txt
#混淆前后的映射
#-printmapping proguard/mapping.txt


