# ProGuard configurations for Bugtags
-keepattributes LineNumberTable,SourceFile

-keep class com.bugtags.library.** {*;}
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient
-dontwarn com.bugtags.library.**
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class LaunchActivity
# End Bugtags
