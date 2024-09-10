-optimizationpasses 5

#okhttp
-dontwarn okio.**
-dontwarn okhttp3.**
-dontwarn javax.annotation.**
-adaptresourcefilenames okhttp3/internal/publicsuffix/PublicSuffixDatabase.gz
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn okhttp3.internal.platform.**
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# Retrofit
-dontwarn javax.annotation.**
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
-dontwarn kotlin.Unit
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}


#Moshi
-keep class com.squareup.moshi.** { *; }
-keepclassmembers class com.squareup.moshi.** { *; }
-keepnames @kotlin.Metadata class com.squareup.moshi.**

-dontwarn org.jetbrains.annotations.**
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep @com.squareup.moshi.JsonQualifier @interface *
-keepclassmembers @com.squareup.moshi.JsonClass class * extends java.lang.Enum {
    <fields>;
    **[] values();
}
-keepclassmembers class com.squareup.moshi.internal.Util {
    private static java.lang.String getKotlinMetadataClassName();
}
-keepclassmembers class * {
  @com.squareup.moshi.FromJson <methods>;
  @com.squareup.moshi.ToJson <methods>;
}
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}


#mqtt
-keep class org.eclipse.paho.client.mqttv3.* { *; }
-keep class org.eclipse.paho.client.mqttv3.*$* { *; }


#Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-keepclassmembers class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}
-dontwarn java.lang.instrument.ClassFileTransformer
-dontwarn sun.misc.SignalHandler
-dontwarn java.lang.instrument.Instrumentation
-dontwarn sun.misc.Signal
-dontwarn java.lang.ClassValue

#Parcelable
-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}
-keep @kotlinx.parcelize.Parcelize public class *

#firebaseMessaging
-dontwarn com.google.firebase.messaging.TopicOperation$TopicOperations
-keep class com.google.firebase.auth.api.fallback.service.FirebaseAuthFallbackService { *; }
-keep class com.google.firebase.provider.FirebaseInitProvider


#Lifecycle
-keep class androidx.lifecycle.** { *; }

#WorkManager
-keepclasseswithmembernames class * {
   native <methods>;
}
-keep class androidx.renderscript.** { *; }
-keepclassmembers class * extends androidx.work.ListenableWorker {
    public <init>(android.content.Context,androidx.work.WorkerParameters);
}



#hawk
-keep class com.facebook.** { *; }
-keepclassmembers class com.facebook.** { *; }
-keepnames @kotlin.Metadata class com.facebook.**

-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep,allowobfuscation @interface com.facebook.crypto.proguard.annotations.DoNotStrip
-keep,allowobfuscation @interface com.facebook.crypto.proguard.annotations.KeepGettersAndSetters
-keep @com.facebook.crypto.proguard.annotations.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.crypto.proguard.annotations.DoNotStrip *;
}
-keepclassmembers @com.facebook.crypto.proguard.annotations.KeepGettersAndSetters class * {
  void set*(***);
  *** get*();
}


#chucker
-keep class com.chuckerteam.chucker.internal.data.entity.HttpTransaction { *; }
-keep class com.chuckerteam.chucker.** { *; }
-keepclassmembers class com.chuckerteam.chucker.** { *; }
-keepnames @kotlin.Metadata class com.chuckerteam.chucker.**
-keep class com.google.gson.** { *; }
-keepclassmembers class com.google.gson.** { *; }
-keepnames @kotlin.Metadata class com.google.gson.**


#android
-keep @interface androidx.annotation.Keep
-keep @androidx.annotation.Keep class *
-keepclasseswithmembers class * {
  @androidx.annotation.Keep <fields>;
}
-keepclasseswithmembers class * {
  @androidx.annotation.Keep <methods>;
}
-keepclassmembernames class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers class * extends java.lang.Enum {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class com.firebase.** { *; }
-dontwarn com.fasterxml.**
-keep class com.google.android.gms.** { *; }

-keep class kotlinx.coroutines.android.** {*;}
-keep class kotlinx.coroutines.android.AndroidDispatcherFactory {*;}
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}



