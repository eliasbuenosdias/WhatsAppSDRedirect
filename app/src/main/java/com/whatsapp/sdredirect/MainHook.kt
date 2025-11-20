package com.whatsapp.sdredirect

import android.os.Environment
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.io.File

class MainHook : IXposedHookLoadPackage {

    private var SD_CARD_PATH: String? = null
    private val WHATSAPP_FOLDER = "WhatsApp/Media"

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (lpparam.packageName != "com.whatsapp") return

        logDebug("Módulo cargado para WhatsApp")

        try {
            SD_CARD_PATH = detectSDCard()

            if (SD_CARD_PATH != null) {
                logDebug("✓ MicroSD detectada en: $SD_CARD_PATH")
                createSDStructure()
                hookExternalFilesDir(lpparam)
                hookExternalStorageDir(lpparam)
                hookFileConstructors(lpparam)
            } else {
                logDebug("⚠️ No se detectó microSD. Módulo desactivado.")
            }
        } catch (e: Exception) {
            logDebug("Error: ${e.message}")
        }
    }

    private fun detectSDCard(): String? {
        try {
            val storageDir = File("/storage")
            if (!storageDir.exists()) return null

            val storageDirs = storageDir.listFiles() ?: return null
            logDebug("Buscando microSD...")

            for (dir in storageDirs) {
                val dirName = dir.name.lowercase()

                if (dirName.contains("emulated") || dirName == "self" || 
                    dirName == "sdcard0") {
                    continue
                }

                if (dir.isDirectory && dir.canRead() && dir.canWrite()) {
                    logDebug("  ✓ SD encontrada: ${dir.absolutePath}")
                    return dir.absolutePath
                }
            }

            val commonPaths = arrayOf(
                "/storage/sdcard1",
                "/storage/extSdCard",
                "/storage/external_sd"
            )

            for (path in commonPaths) {
                val dir = File(path)
                if (dir.exists() && dir.canWrite()) {
                    return path
                }
            }

            for (dir in storageDirs) {
                if (dir.name.matches(Regex("[0-9A-F]{4}-[0-9A-F]{4}"))) {
                    if (dir.isDirectory && dir.canWrite()) {
                        return dir.absolutePath
                    }
                }
            }

            return null
        } catch (e: Exception) {
            logDebug("Error detectando SD: ${e.message}")
            return null
        }
    }

    private fun createSDStructure() {
        try {
            if (SD_CARD_PATH == null) return

            val whatsappSD = File(SD_CARD_PATH!!, WHATSAPP_FOLDER)
            if (!whatsappSD.exists()) {
                whatsappSD.mkdirs()
            }

            val subfolders = arrayOf(
                "WhatsApp Images", "WhatsApp Video", "WhatsApp Audio",
                "WhatsApp Documents", "WhatsApp Animated Gifs", 
                "WhatsApp Voice Notes", "WhatsApp Stickers"
            )

            subfolders.forEach { folder ->
                File(whatsappSD, folder).apply { if (!exists()) mkdirs() }
            }
        } catch (e: Exception) {
            logDebug("Error creando estructura: ${e.message}")
        }
    }

    private fun hookExternalFilesDir(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            "android.app.ContextImpl",
            lpparam.classLoader,
            "getExternalFilesDir",
            String::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val originalPath = param.result as? File
                    if (originalPath != null && isSDAvailable()) {
                        param.result = File(redirectToSD(originalPath.absolutePath))
                    }
                }
            }
        )
    }

    private fun hookExternalStorageDir(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            Environment::class.java,
            "getExternalStorageDirectory",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    if (isSDAvailable()) {
                        param.result = File(SD_CARD_PATH!!)
                    }
                }
            }
        )
    }

    private fun hookFileConstructors(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedHelpers.findAndHookMethod(
            File::class.java,
            "getAbsolutePath",
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val originalPath = param.result as String
                    if (originalPath.contains("/WhatsApp/Media") && isSDAvailable()) {
                        param.result = redirectToSD(originalPath)
                    }
                }
            }
        )
    }

    private fun redirectToSD(originalPath: String): String {
        if (SD_CARD_PATH == null) return originalPath
        return originalPath.replace("/storage/emulated/0", SD_CARD_PATH!!)
    }

    private fun isSDAvailable(): Boolean {
        if (SD_CARD_PATH == null) return false
        return File(SD_CARD_PATH!!).exists() && File(SD_CARD_PATH!!).canWrite()
    }

    private fun logDebug(message: String) {
        XposedBridge.log("[WhatsAppSDRedirect] $message")
    }
}
