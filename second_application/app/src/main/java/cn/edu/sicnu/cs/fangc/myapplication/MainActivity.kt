package cn.edu.sicnu.cs.fangc.myapplication

import android.os.Binder
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.edu.sicnu.cs.fangc.myapplication.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    val expert = ProgramExpert()
    private var count = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val button =findViewById<Button>(R.id.button1)
//        val spinner =findViewById<Spinner>(R.id.spinner)
//        val textView =findViewById<TextView>(R.id.textView1)
//        button.setOnClickListener{
//            textView.text=expert.getLanguage(spinner.selectedItem.toString())
//        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.button1.setOnClickListener {
            binding.textView1.text = expert.getLanguage(binding.spinner.selectedItem.toString())
        }
        binding.button2.setOnClickListener {
            count++
            val newTv = TextView(this)
            newTv.text = "新文本:$count"
            newTv.textSize = 22f
            binding.llContainer.addView(newTv)
        }
    }
}