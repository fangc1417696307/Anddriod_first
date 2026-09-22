package cn.edu.sicnu.cs.fangc.myapplication

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

//        // 必须先 setContentView 加载布局，findViewById 才能找到控件
////        setContentView(R.layout.activity_main)
////
////        var button=findViewById<Button>(R.id.button)
////        var textView=findViewById<TextView>(R.id.textView)
////        button.setOnClickListener {
//////            R -> res
////            textView.text=getString(R.string.clicked)
////        }
//      先定义线性布局容
        val linearLayout = LinearLayout(this)
        linearLayout.orientation= LinearLayout.VERTICAL

//// 1. 创建ImageView
        val imageView = ImageView(this)
        val imgParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        imgParams.setMargins(0,30,0,30)
        imageView.layoutParams = imgParams
        imageView.setImageResource(R.drawable.flag)
        linearLayout.addView(imageView)


        val textView= TextView(this)
        textView.layoutParams= ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        textView.textSize=30F
        textView.text=getString(R.string.textview)
        linearLayout.addView(textView)

        val button= Button(this)
        button.width= ViewGroup.LayoutParams.MATCH_PARENT
        button.height= ViewGroup.LayoutParams.WRAP_CONTENT
        button.textSize=30F
        button.text=getString(R.string.button)
        linearLayout.addView(button)

        setContentView(linearLayout)

        button.setOnClickListener {
//////            R -> res
           textView.text=getString(R.string.clicked)
        }

        // 这里的根视图是上面代码动态创建的 linearLayout，
        // 不能再 findViewById(R.id.main)——那个 id 属于未被加载的 activity_main.xml
        ViewCompat.setOnApplyWindowInsetsListener(linearLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}