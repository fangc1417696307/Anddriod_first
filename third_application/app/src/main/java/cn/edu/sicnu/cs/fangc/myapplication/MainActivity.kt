package cn.edu.sicnu.cs.fangc.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.edu.sicnu.cs.fangc.myapplication.databinding.ActivityMainBinding
import kotlin.getValue

/** 任务 (1)：正向传参使用的 key */
const val FIRST_MSG = "First Message"

/** 任务 (2)：返回值使用的 key */
const val RESULT_MSG = "Result Message"

class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    /**
     * 任务 (2)：注册一个用于接收 ThirdActivity(第 2 个二级 Activity) 返回结果的启动器。
     * resultCode 为 RESULT_OK 时，从 data 中取出 RESULT_MSG 并显示到界面上。
     */
    private val returnResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val msg = result.data?.getStringExtra(RESULT_MSG).orEmpty()
            binding.textView.text = getString(R.string.returned_result, msg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // 任务 (1)：点击第 1 个按钮，启动第 1 个二级 Activity(SecondActivity) 并正向传参
        binding.first.setOnClickListener {
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            intent.putExtra(FIRST_MSG, binding.editText.text.toString())
            startActivity(intent)
        }

        // 任务 (2)：点击第 2 个按钮，启动第 2 个二级 Activity(ThirdActivity)，等待它返回数据
        binding.second.setOnClickListener {
            returnResult.launch(Intent(this@MainActivity, ThirdActivity::class.java))
        }

        // 任务 (3)：点击第 3 个按钮，启动第 3 个二级 Activity(FourthActivity)，里面用隐式 Intent 调用相机
        binding.third.setOnClickListener {
            startActivity(Intent(this@MainActivity, FourthActivity::class.java))
        }

        // 任务 (4)：点击第 4 个按钮，启动第 4 个二级 Activity(FifthActivity)，实现秒表
        binding.fourth.setOnClickListener {
            startActivity(Intent(this@MainActivity, FifthActivity::class.java))
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // 横竖屏切换后依然保留界面上已经输入的文本，避免重建后内容丢失
        outState.putString(KEY_INPUT, binding.editText.text.toString())
        outState.putString(KEY_RESULT, binding.textView.text.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        binding.editText.setText(savedInstanceState.getString(KEY_INPUT))
        binding.textView.text = savedInstanceState.getString(KEY_RESULT)
    }

    private companion object {
        const val KEY_INPUT = "key_input"
        const val KEY_RESULT = "key_result"
    }
}
