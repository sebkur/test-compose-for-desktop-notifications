package de.mobanisto.test

object OsUtils {
    private val os = System.getProperty("os.name")
    private val vendor = System.getProperty("java.vendor")
    val isWindows: Boolean
        get() = os != null && os.contains("Windows")
    val isMac: Boolean
        get() = os != null && os.contains("Mac OS")
    val isLinux: Boolean
        get() = os != null && os.contains("Linux") && !isAndroid
    val isAndroid: Boolean
        get() = vendor != null && vendor.contains("Android")
}