package cn.edu.sicnu.cs.fangc.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.edu.sicnu.cs.fangc.myapplication.databinding.ActivitySecondBinding
import kotlin.getValue

class SecondActivity : BaseActivity() {
    private val binding : ActivitySecondBinding by lazy { ActivitySecondBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)


        val intent = getIntent()
        binding.textView.text = intent.getStringExtra(FIRST_MSG).orEmpty()


        
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}