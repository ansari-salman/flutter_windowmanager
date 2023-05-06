package io.adaptant.labs.flutter_windowmanager

import android.app.Activity
import android.os.Build
import android.view.WindowManager
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

internal class MethodCallHandlerImpl(var call: MethodCall,var result: MethodChannel.Result,var activity:Activity ) {

    fun onMethodCall() {
        val flags = call.argument<Int>("flags")!!

        if (activity == null) {
            result.error(
                "FlutterWindowManagerPlugin",
                "FlutterWindowManagerPlugin: ignored flag state change, current activity is null",
                null
            )
        }

        if (!validLayoutParams(result, flags)) {
            return
        }

        when (call.method) {
            "addFlags" -> {
                activity.getWindow().addFlags(flags)
                result.success(true)
            }
            "clearFlags" -> {
                activity.getWindow().clearFlags(flags)
                result.success(true)
            }
            else -> result.notImplemented()
        }
    }


    private fun validLayoutParam(flag: Int): Boolean {
        return when (flag) {
            WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_DIM_BEHIND, WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_SCALED, WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER, WindowManager.LayoutParams.FLAG_SPLIT_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH -> true
            WindowManager.LayoutParams.FLAG_BLUR_BEHIND -> false
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD -> Build.VERSION.SDK_INT < 26
            WindowManager.LayoutParams.FLAG_DITHER -> false
            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS -> true
            WindowManager.LayoutParams.FLAG_LAYOUT_ATTACHED_IN_DECOR -> Build.VERSION.SDK_INT >= 22
            WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN -> true
            WindowManager.LayoutParams.FLAG_LOCAL_FOCUS_MODE -> true
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED -> Build.VERSION.SDK_INT < 27
            WindowManager.LayoutParams.FLAG_TOUCHABLE_WHEN_WAKING -> false
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS -> true
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON -> Build.VERSION.SDK_INT < 27
            else -> false
        }
    }

    private fun validLayoutParams(result: MethodChannel.Result, flags: Int): Boolean {
        for (i in 0 until Integer.SIZE) {
            val flag = 1 shl i
            if (flags and flag == 1) {
                if (!validLayoutParam(flag)) {
                    result.error(
                        "FlutterWindowManagerPlugin",
                        "FlutterWindowManagerPlugin: invalid flag specification: " + Integer.toHexString(
                            flag
                        ),
                        null
                    )
                    return false
                }
            }
        }
        return true
    }
}
