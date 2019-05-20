/*
 * Copyright 2018 Marvin O. Quevedo, https://www.marvinquevedo.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.quevedo.marvin.easypdfdocument.elements

import android.graphics.drawable.Drawable
import com.quevedo.marvin.easypdfdocument.interfaces.EasyDocumentInterface
import com.quevedo.marvin.easypdfdocument.settigns.DrawSettings
import java.io.File
import android.util.Base64
import android.graphics.*
import android.graphics.Bitmap
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.PorterDuff
import android.graphics.BitmapFactory
import android.util.Log

class EasyImage(val bitmap:Bitmap, var imageWidth:Int = bitmap.width):Drawable(),EasyElement {
    enum class ImageStyle(val style:String){
        CORNER_ROUNDED_IMAGE("corner_rounded"), CIRCLE_IMAGE("circle"), SQUARE_IMAGE("square")
    }
    enum class ImageAlign(val align:String){
        LEFT("left"), CENTER("center"), RIGHT("right")
    }
    var height = bitmap.height.toFloat()
    var imageStyle = ImageStyle.SQUARE_IMAGE
        private set
    var rounded = 7f
        private set
    var imageAlign = ImageAlign.LEFT
        private set
    var marginTop = 0f
        private set
    var marginBottom = 5f
        private set
    var marginLeft = 0f
        private set
    var marginRight = 0f
        private set
    var widthPercentage = 0
        private set
    init {
        //It for calc the height
       scaleToFitWidth(bitmap,imageWidth)
    }
    fun isSquared():Boolean{
        return bitmap.width == bitmap.height;
    }
    override fun draw(c: Canvas?, drawSettings: DrawSettings, easyDocumentInterface: EasyDocumentInterface): Float {
        var canvas = c
        //if it is true, the image only used the middle, then quit 100 percentage, only apply centered
        if(widthPercentage == 100 && imageStyle == ImageStyle.CIRCLE_IMAGE && !isSquared()){
            Log.e("EasyImage", "Image with 100%, MIDDLE, CIRCLE_IMAGE is only apply with squared image")
            widthPercentage = 0
            this.imageWidth = (drawSettings.getRealHorizontalSpace() * 1.9).toInt()
            imageAlign = ImageAlign.CENTER
        }
        if(widthPercentage>0){
            imageWidth = calcWidthPercentage(drawSettings.getRealHorizontalSpace())
        }

        drawInter(canvas, imageWidth, drawSettings, easyDocumentInterface)

        return height
    }

    fun setImageStyle(style:ImageStyle):EasyImage{
        this.imageStyle = style
        return this
    }
    fun setRounded(rounded:Float):EasyImage{
        this.rounded = rounded
        return this
    }
    fun setImageAlign(align: ImageAlign):EasyImage{
        this.imageAlign = align
        return this
    }
    fun setWidthPercentage(percentage: Int):EasyImage{
        this.widthPercentage = percentage
        return this
    }


    override fun draw(canvas: Canvas) {

    }

    private fun drawInter(c: Canvas?, width:Int, drawSettings: DrawSettings, easyDocumentInterface: EasyDocumentInterface):Float {
        var canvas = c
        var bit = scaleToFitWidth(bitmap, width)
        when (imageStyle) {
            ImageStyle.CORNER_ROUNDED_IMAGE -> bit = getRoundedCornerBitmap(bit)
            ImageStyle.CIRCLE_IMAGE -> bit  = getCircleBitmap2(bit)
            else -> {
                //Noting, is only for hide warning on build
            }
        }

        var leftSpace = 0f
        if(imageAlign == ImageAlign.CENTER){
            val realSpace:Float =drawSettings.getRealHorizontalSpace()
            val freeSpace:Float = realSpace - bit.width
            leftSpace = freeSpace / 2
        }else if(imageAlign == ImageAlign.RIGHT){
            leftSpace = drawSettings.getRealHorizontalSpace() - bit.width
        }

        if(height>drawSettings.verticalSpaceAvailable){
            canvas = easyDocumentInterface.requestNewPage()
        }

        canvas?.translate(leftSpace, marginTop)
        val rect = Rect(0, 0, width, bit.height)
        val paint = Paint()
        paint.isAntiAlias = true

        canvas?.drawBitmap(bit,rect,rect,paint)
        canvas?.translate(leftSpace*-1, marginTop*-1)
        return  height
    }

    fun scaleToFitWidth(b: Bitmap, width: Int): Bitmap {
        val factor = width / b.width.toFloat()
        val height = (b.height * factor)
        this.height = height
        return Bitmap.createScaledBitmap(b, width, height.toInt(), true)
    }

    fun getRoundedCornerBitmap(bitmap: Bitmap): Bitmap {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val roundPx = rounded

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)

        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)

        return output
    }

    private fun calcWidthPercentage(space:Float):Int{
        var calcWidth = 1
        if(widthPercentage>0f){
            //val space = space
            var percentage:Float = widthPercentage/100f
            val result = space * percentage
            calcWidth = result.toInt()
            if(calcWidth <=0){
                calcWidth = 1
            }
        }
        return calcWidth

    }

   private fun getCircleBitmap2(source: Bitmap): Bitmap {
        var size = Math.min(source.width, source.height)
        var x = (source.width - size) / 2
        var y = (source.height - size) / 2
        var squaredBitmap = Bitmap.createBitmap(source, x, y, size, size)

        if (squaredBitmap != source) {
            source.recycle()
        }

        val bitmap = Bitmap.createBitmap(size, size, source.config)

        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(squaredBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.shader = shader
        paint.isAntiAlias = true

        val r = size / 2f
        canvas.drawCircle(r, r, r, paint)

        squaredBitmap.recycle()
        return bitmap
    }

    /*fun getCircleBitmap3(source: Bitmap): Bitmap {
        val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), source)
        roundedBitmapDrawable.isCircular = true
        //int size = Math.min(source.getImageWidth(), source.getHeight());
        //float r = size/2.0f;
        //roundedBitmapDrawable.setCornerRadius(r);
        return drawableToBitmap(roundedBitmapDrawable)
    }*/
/*
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        var width = drawable.intrinsicWidth
        width = if (width > 0) width else 1
        var height = drawable.intrinsicHeight
        height = if (height > 0) height else 1

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val dCanvas = Canvas(bitmap)
        drawable.setBounds(0, 0, dCanvas.width, dCanvas.height)
        drawable.draw(dCanvas)

        return bitmap
    }*/


    override fun setAlpha(alpha: Int) {

    }

    override fun getOpacity(): Int {
        return opacity
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    companion object {
        fun fromFile(file: File, width: Int):EasyImage{
            val filePath = file.path
            val bitmap = BitmapFactory.decodeFile(filePath)
            return EasyImage(bitmap, width)
        }
        fun fromFile(file: File):EasyImage{
            val filePath = file.path
            val bitmap = BitmapFactory.decodeFile(filePath)
            return EasyImage(bitmap)
        }
        fun fromBase64(encodedImage:String, width: Int):EasyImage{
            var base64Image:String = encodedImage
            if(encodedImage.contains(",")){
                base64Image = encodedImage.split(",")[1]
            }


            val decodedString = Base64.decode(base64Image, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            return EasyImage(decodedByte, width)
        }
        fun fromBase64(encodedImage:String):EasyImage{
            var base64Image:String = encodedImage
            if(encodedImage.contains(",")){
                base64Image = encodedImage.split(",")[1]
            }

            val decodedString = Base64.decode(base64Image, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            return EasyImage(decodedByte)
        }
    }
}
