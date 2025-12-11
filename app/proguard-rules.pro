# Note Flow - ProGuard Rules
# Optimized for offline-first note app with Firebase Remote Config

# ===================================================================
# GENERAL OPTIMIZATIONS
# ===================================================================
-optimizationpasses 5
-dontusemixedcaseclassnames
-dontskipnonpubliclibraryclasses
-verbose

# ===================================================================
# KOTLIN
# ===================================================================
-keep class kotlin.** { *; }
-keep class kotlin.Metadata { *; }
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    public static void checkNotNull(java.lang.Object);
    public static void checkNotNull(java.lang.Object, java.lang.String);
    public static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

# ===================================================================
# JETPACK COMPOSE
# ===================================================================
-keep class androidx.compose.** { *; }
-keep class androidx.compose.runtime.** { *; }
-keep class androidx.compose.ui.** { *; }
-keep class androidx.compose.material3.** { *; }
-keep class androidx.compose.foundation.** { *; }
-dontwarn androidx.compose.**

# Compose Compiler
-keepclassmembers class androidx.compose.** {
    <methods>;
}
-keep,allowobfuscation,allowshrinking class androidx.compose.runtime.** { *; }

# ===================================================================
# ROOM DATABASE
# ===================================================================
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *
-dontwarn androidx.room.paging.**

# Keep Room entity classes
-keep class com.example.noteflow.data.Note { *; }
-keep class com.example.noteflow.data.NoteDao { *; }
-keep class com.example.noteflow.data.NoteDatabse { *; }
-keep class com.example.noteflow.data.Converters { *; }

# ===================================================================
# FIREBASE REMOTE CONFIG (ONLY)
# ===================================================================
-keep class com.google.firebase.** { *; }
-keep class com.google.firebase.remoteconfig.** { *; }
-dontwarn com.google.firebase.**
-dontwarn com.google.firebase.analytics.**

# Firebase Remote Config specific
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses

# ===================================================================
# REMOVE UNUSED FIREBASE SERVICES
# ===================================================================
# We're explicitly removing Auth, Storage, Functions
-dontwarn com.google.firebase.auth.**
-dontwarn com.google.firebase.storage.**
-dontwarn com.google.firebase.functions.**
-dontwarn com.google.firebase.firestore.**
-dontwarn com.google.firebase.database.**

# ===================================================================
# COROUTINES
# ===================================================================
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-keepclassmembers class kotlin.coroutines.SafeContinuation {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# ===================================================================
# ANDROIDX LIFECYCLE
# ===================================================================
-keep class * extends androidx.lifecycle.ViewModel {
    <init>();
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(android.app.Application);
}
-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
-keepclassmembers class * extends androidx.lifecycle.AndroidViewModel {
    <init>(...);
}

# Keep ViewModels
-keep class com.example.noteflow.viewmodel.** { *; }

# ===================================================================
# NAVIGATION COMPOSE
# ===================================================================
-keep class androidx.navigation.** { *; }
-keepnames class androidx.navigation.fragment.NavHostFragment
-keepnames class * extends androidx.navigation.Navigator

# ===================================================================
# DATASTORE
# ===================================================================
-keep class androidx.datastore.** { *; }
-keep class com.example.noteflow.data.ThemePreferenceManager { *; }

# ===================================================================
# COIL (Image Loading)
# ===================================================================
-keep class coil.** { *; }
-dontwarn coil.**

# ===================================================================
# LOTTIE ANIMATIONS
# ===================================================================
-keep class com.airbnb.lottie.** { *; }
-dontwarn com.airbnb.lottie.**

# ===================================================================
# APPLICATION CLASSES
# ===================================================================
-keep class com.example.noteflow.** { *; }
-keep class com.noteflowlite.** { *; }

# Keep Remote Config Manager
-keep class com.example.noteflow.remoteconfig.RemoteConfigManager { *; }

# Keep all Composables
-keep @androidx.compose.runtime.Composable class * { *; }
-keep @androidx.compose.runtime.Composable public class * { *; }

# ===================================================================
# PARCELABLE
# ===================================================================
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# ===================================================================
# SERIALIZABLE
# ===================================================================
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# ===================================================================
# R8 FULL MODE
# ===================================================================
-allowaccessmodification
-repackageclasses ''

# ===================================================================
# REMOVE LOGGING IN RELEASE
# ===================================================================
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}

# ===================================================================
# REMOVE DEBUG CODE
# ===================================================================
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}

# ===================================================================
# END OF PROGUARD RULES
# ===================================================================
