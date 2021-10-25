package cloud.dbchain.sample.util

import android.util.Log
import com.gcigb.dbchain.ILog
import com.gcigb.dbchain.util.toJsonString

class LogImpl : ILog {
    override fun logD(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    override fun logE(msg: String) {
        Log.e("blog_error", msg)
    }

    override fun logE(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    override fun logHttp(msg: String) {
        Log.i("blog_http", msg)
    }

    override fun logI(any: Any) {
        Log.i("blog_test", any.toJsonString())
    }

    override fun logI(msg: String) {
        Log.i("blog_test", msg)
    }

    override fun logI(tag: String, msg: String) {
        Log.i(tag, msg)
    }

    override fun logV(tag: String, msg: String) {
        Log.v(tag, msg)
    }

    override fun logW(tag: String, msg: String) {
        Log.w(tag, msg)
    }
}