-keep class de.robv.android.xposed.** { *; }
-keep class com.whatsapp.sdredirect.MainHook { *; }
-keep class * implements de.robv.android.xposed.IXposedHook* {
    <methods>;
}
