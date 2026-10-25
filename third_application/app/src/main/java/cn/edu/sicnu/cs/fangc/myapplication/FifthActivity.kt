package cn.edu.sicnu.cs.fangc.myapplication

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.edu.sicnu.cs.fangc.myapplication.databinding.ActivityFifthBinding
import java.util.Locale
import kotlin.getValue

/**
 * 第 4 个二级 Activity：任务 (4) 秒表（StopWatch）。
 *
 * 关键点：计时不能依赖“自己累加 10ms”，否则横竖屏重建后会从 0 开始。
 * 这里记录 SystemClock.elapsedRealtime()（开机后的单调时间），
 * 再把 running / baseMs / startAt 存进 onSaveInstanceState，
 * 界面重建后只要用 当前 elapsedRealtime - startAt 就能算出真实经过的时间，
 * 因此横竖屏切换时计时是连续的。
 */
class FifthActivity : BaseActivity() {

    private val binding: ActivityFifthBinding by lazy { ActivityFifthBinding.inflate(layoutInflater) }

    private val handler = Handler(Looper.getMainLooper())

    /** 是否正在计时 */
    private var running = false

    /** 暂停前已经累计的毫秒数 */
    private var baseMs = 0L

    /** 本段计时开始时的 elapsedRealtime */
    private var startAt = 0L

    /** 计次记录 */
    private val laps = mutableListOf<Long>()

    /** 界面刷新任务：每 10ms 更新一次显示 */
    private val ticker = object : Runnable {
        override fun run() {
            binding.time.text = formatTime(elapsedMs())
            handler.postDelayed(this, REFRESH_INTERVAL_MS)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // 横竖屏切换后恢复状态
        savedInstanceState?.let {
            running = it.getBoolean(KEY_RUNNING)
            baseMs = it.getLong(KEY_BASE_MS)
            startAt = it.getLong(KEY_START_AT)
            it.getLongArray(KEY_LAPS)?.let { saved -> laps.addAll(saved.toList()) }
            Log.d(tag, "onCreate() 恢复状态: running=$running, baseMs=$baseMs")
        }

        binding.start.setOnClickListener {
            if (running) pause() else start()
        }
        binding.lap.setOnClickListener { addLap() }
        binding.reset.setOnClickListener { reset() }

        renderLaps()
        renderTime()
        updateStartButton()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /** 当前已经经过的毫秒数 */
    private fun elapsedMs(): Long =
        if (running) baseMs + (SystemClock.elapsedRealtime() - startAt) else baseMs

    private fun start() {
        startAt = SystemClock.elapsedRealtime()
        running = true
        handler.post(ticker)
        updateStartButton()
    }

    private fun pause() {
        baseMs = elapsedMs()
        running = false
        handler.removeCallbacks(ticker)
        renderTime()
        updateStartButton()
    }

    private fun reset() {
        handler.removeCallbacks(ticker)
        running = false
        baseMs = 0L
        startAt = 0L
        laps.clear()
        renderTime()
        renderLaps()
        updateStartButton()
    }

    private fun addLap() {
        laps.add(elapsedMs())
        renderLaps()
    }

    private fun renderTime() {
        binding.time.text = formatTime(elapsedMs())
    }

    private fun updateStartButton() {
        binding.start.setText(if (running) R.string.stopwatch_pause else R.string.stopwatch_start)
    }

    /** 把计次记录渲染成一列 TextView */
    private fun renderLaps() {
        binding.lapsContainer.removeAllViews()
        laps.forEachIndexed { index, ms ->
            val item = TextView(this).apply {
                text = getString(R.string.lap_item, index + 1, formatTime(ms))
                textSize = 18f
                setPadding(0, 8, 0, 8)
                visibility = View.VISIBLE
            }
            binding.lapsContainer.addView(item)
        }
    }

    /** 格式化为 mm:ss.SS（分:秒.百分秒） */
    private fun formatTime(ms: Long): String {
        val minutes = ms / 60000
        val seconds = ms % 60000 / 1000
        val hundredths = ms % 1000 / 10
        return String.format(Locale.getDefault(), "%02d:%02d.%02d", minutes, seconds, hundredths)
    }

    override fun onStart() {
        super.onStart()
        // 回到前台时继续刷新（不改变计时起点，所以时间依然是连续的）
        if (running) handler.post(ticker)
    }

    override fun onStop() {
        super.onStop()
        // 离开界面时停止刷新界面，避免资源浪费
        handler.removeCallbacks(ticker)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_RUNNING, running)
        outState.putLong(KEY_BASE_MS, baseMs)
        outState.putLong(KEY_START_AT, startAt)
        outState.putLongArray(KEY_LAPS, laps.toLongArray())
    }

    private companion object {
        const val REFRESH_INTERVAL_MS = 10L
        const val KEY_RUNNING = "key_running"
        const val KEY_BASE_MS = "key_base_ms"
        const val KEY_START_AT = "key_start_at"
        const val KEY_LAPS = "key_laps"
    }
}
