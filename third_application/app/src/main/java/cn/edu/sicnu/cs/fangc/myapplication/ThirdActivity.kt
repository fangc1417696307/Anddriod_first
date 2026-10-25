package cn.edu.sicnu.cs.fangc.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.edu.sicnu.cs.fangc.myapplication.databinding.ActivityThirdBinding
import kotlin.getValue

/**
 * 第 2 个二级 Activity：任务 (2) 的接收端。
 * 输入内容后点击“确定”，把内容通过 setResult 返回给 MainActivity。
 */
class ThirdActivity : BaseActivity() {

    private val binding: ActivityThirdBinding by lazy { ActivityThirdBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.confirm.setOnClickListener {
            val text = binding.editText.text.toString().trim()
            if (text.isEmpty()) {
                Toast.makeText(this, R.string.empty_input_tip, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 把要返回的数据放进 Intent，通过 setResult 交回给启动它的 MainActivity
            val data = Intent().apply { putExtra(RESULT_MSG, text) }
            setResult(RESULT_OK, data)
            finish()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
