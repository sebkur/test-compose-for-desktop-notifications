-dontoptimize
-dontobfuscate

-dontwarn kotlinx.**

-keepclasseswithmembers public class de.mobanisto.test.MainKt {
    public static void main(java.lang.String[]);
}
-keep class org.jetbrains.skia.** { *; }
-keep class org.jetbrains.skiko.** { *; }
