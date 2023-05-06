package io.adaptant.labs.flutter_windowmanager


import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.github.ponnamkarthik.toast.fluttertoast.MethodCallHandlerImpl

/** FlutterWindowmanagerPlugin */
class FlutterWindowManagerPlugin: FlutterActivity() {

  companion object {
    private const val CHANNEL = "flutter_windowmanager"
  }

  override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
    super.configureFlutterEngine(flutterEngine)
    MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL)
      .setMethodCallHandler { call, result ->
        setupChannel(call,result)
      }
  }


  private fun setupChannel(call: MethodCall, result: MethodChannel.Result) {
    val handler = MethodCallHandlerImpl(call,result,this)
    handler.onMethodCall()
  }
}
