

package com.quevedo.marvin.easypdfdocument.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.widget.Toast
import java.io.ByteArrayOutputStream


fun imageToBase64():String{
    val bm = BitmapFactory.decodeFile("/path/to/image.jpg")
    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 100, baos) //bm is the bitmap object
    val byteArrayImage = baos.toByteArray()
    return Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
}
fun showToast(context: Context, text:Int, duration: Int = Toast.LENGTH_SHORT){
    Toast.makeText(context, text, duration).show()
}