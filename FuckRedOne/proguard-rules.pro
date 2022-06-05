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

#不要警告
-dontwarn
#忽略警告
-ignorewarnings

#指定代码优化级别，值在0-7之间，默认为5
-optimizationpasses 10
#混淆时不使用大小写混合类名
-dontusemixedcaseclassnames

#关闭代码优化
-dontoptimize
#输出详细信息
-verbose
-overloadaggressively
-allowaccessmodification

-adaptclassstrings
-adaptresourcefilenames
-adaptresourcefilecontents

-renamesourcefileattribute P
-keepattributes SourceFile,LineNumberTable
