-dontobfuscate

# --- Fresco ---
# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

#-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**
# --- /Fresco ---

# --- Butter Knife ---
# Finder.castParam() is stripped when not needed and ProGuard notes it
# unnecessarily. When castParam() is needed, it's not stripped. e.g.:
#
#  @OnItemSelected(value = R.id.history_entry_list)
#  void foo(ListView bar) {
#      L.d("baz");
#  }

-dontnote butterknife.internal.**
# --- /Butter Knife ---

-keep class com.mobsandgeeks.saripaar.** {*;}

-keep class uk.co.senab.photoview.** {*;}

-keep class com.github.kevinsawicki.http.** {*;}

# --- Retrofit2 ---
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# --- /Retrofit ---

# --- OkHttp + Okio ---
-dontwarn okhttp3.**
-keep class okhttp3.** { *; }
-dontwarn okio.*
# --- /OkHttp + Okio ---

# --- Gson ---
# https://github.com/google/gson/blob/master/examples/android-proguard-example/proguard.cfg

# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
#-keepattributes Signature (already specified)

# Gson specific classes
-keep class sun.misc.Unsafe { *; }
# --- /Gson ---

# --- Wikipedia ---
-keep class org.wikipedia.** { <init>(...); *; }
-keep enum org.wikipedia.** { <init>(...); *; }
-keep class org.mediawiki.api.json.** {*;}
-keep enum org.wikipedia.api.json.** { <init>(...); *; }
# --- /Wikipedia ---
