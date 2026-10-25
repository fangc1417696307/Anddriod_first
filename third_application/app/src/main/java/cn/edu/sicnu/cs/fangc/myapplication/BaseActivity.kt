package cn.edu.sicnu.cs.fangc.myapplication

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

/**
 * 实验目的 (3)：观察 Activity 生命周期。
 * 所有 Activity 都继承本类，这样每次生命周期回调都会在 Logcat 中打印一条日志
 * （过滤 TAG 为 MainActivity / SecondActivity / ... 即可看到完整的生命周期顺序）。
 */
open class BaseActivity : AppCompatActivity() {

    /** 用子类的类名作为 Log 的 TAG */
    protected val tag: String get() = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(tag, "onCreate()")
    }

    override fun onStart() {
        super.onStart()
        Log.d(tag, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d(tag, "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d(tag, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(tag, "onStop()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(tag, "onRestart()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(tag, "onDestroy()")
    }

    /** 横竖屏切换时会先保存状态再重建，这两条日志能直接看到“保存 — 重建 — 恢复”的过程 */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(tag, "onSaveInstanceState()")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(tag, "onRestoreInstanceState()")
    }
}
