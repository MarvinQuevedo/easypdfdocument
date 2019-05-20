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

package com.quevedo.marvin.easypdfdocument.extension

import android.graphics.Canvas
import android.graphics.Color

import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.drawable.Drawable
import java.math.RoundingMode
import java.text.DecimalFormat

class BlankSpace(private val widthSpace:Float): Drawable() {
    val intWidth:Int

    init{
        val df = DecimalFormat("#")
        df.roundingMode = RoundingMode.CEILING
        intWidth = df.format(widthSpace).toInt()
        setBounds(0,0,intWidth,1)

    }
    override fun draw(canvas: Canvas) {
        var paint = Paint()
        paint.color = Color.TRANSPARENT
        //paint.alpha = 0
        paint.strokeWidth = 0f
        canvas.drawRect(0f,0f,intWidth.toFloat(),1f,paint)
    }
    fun getUtilizedWidth():Int{
        return intWidth
    }

    override fun setAlpha(alpha: Int) {

    }

    override fun getOpacity(): Int {
        return 0
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }
}