package cn.edu.sicnu.cs.fangc.myapplication

import android.content.ActivityNotFoundException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.content.Intent
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import cn.edu.sicnu.cs.fangc.myapplication.databinding.ActivityFourthBinding
import java.io.File
import kotlin.getValue

/**
 * 第 3 个二级 Activity：任务 (3)。
 * 用隐式 Intent（MediaStore.ACTION_IMAGE_CAPTURE）调用系统相机，
 * 拍照结果写入 FileProvider 提供的 Uri，成功后在本界面显示照片。
 *
 * 注意：使用系统相机的隐式调用不需要声明 CAMERA 权限（自己用 Camera API 才需要）。
 */
class FourthActivity : BaseActivity() {

    private val binding: ActivityFourthBinding by lazy { ActivityFourthBinding.inflate(layoutInflater) }

    /** 拍照输出文件的 Uri，需要在拍照前创建 */
    private var photoUri: Uri? = null

    /** 拍照结果回调，success 为 true 表示照片已成功写入 photoUri 指向的文件 */
    private val takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        val uri = photoUri
        if (success && uri != null) {
            showPhoto(uri)
        } else {
            Toast.makeText(this, R.string.camera_canceled, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        // 旋转屏幕重建时把已拍好的照片恢复出来
        savedInstanceState?.getString(KEY_PHOTO_URI)?.let {
            photoUri = Uri.parse(it)
            showPhoto(photoUri!!)
        }

        binding.takePhoto.setOnClickListener { openCamera() }

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    /** 隐式 Intent 启动系统相机 */
    private fun openCamera() {
        val dir = File(cacheDir, "photos").apply { if (!exists()) mkdirs() }
        val file = File(dir, "photo_${System.currentTimeMillis()}.jpg")

        val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
        photoUri = uri

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, uri)
            addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }

        try {
            takePicture.launch(uri)
        } catch (e: ActivityNotFoundException) {
            // 设备上没有相机应用
            Toast.makeText(this, R.string.camera_unavailable, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 相机拍出的原图通常很大，直接解码容易 OOM，
     * 这里先用 inJustDecodeBounds 读尺寸，再按 1024px 上限做二次采样解码。
     */
    private fun showPhoto(uri: Uri) {
        val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
        contentResolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }

        var sampleSize = 1
        while (bounds.outWidth / sampleSize > MAX_SIZE || bounds.outHeight / sampleSize > MAX_SIZE) {
            sampleSize *= 2
        }

        val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
        val bitmap: Bitmap? = contentResolver.openInputStream(uri)?.use {
            BitmapFactory.decodeStream(it, null, options)
        }
        binding.imageView.setImageBitmap(bitmap)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_PHOTO_URI, photoUri?.toString())
    }

    private companion object {
        const val KEY_PHOTO_URI = "key_photo_uri"
        const val MAX_SIZE = 1024
    }
}
