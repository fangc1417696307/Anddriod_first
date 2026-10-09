package cn.edu.sicnu.cs.fangc.myapplication

import android.R


class ProgramExpert {

    fun getLanguage(feature :String): String{
        when (feature){
            "快速" -> return "C++"
            "容易" -> return  "Python"
            "新语言" -> return "Kotlin"
            "面向对象" -> return "Java"
            else -> return "Unkown"
        }

    }
}