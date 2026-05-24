# Keep Kotlinx Serialization metadata
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

-keep,includedescriptorclasses class app.gymbuddy.**$$serializer { *; }
-keepclassmembers class app.gymbuddy.** {
    *** Companion;
}
-keepclasseswithmembers class app.gymbuddy.** {
    kotlinx.serialization.KSerializer serializer(...);
}
