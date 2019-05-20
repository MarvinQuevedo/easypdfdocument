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

package com.quevedo.marvin.easypdfdocument.elements.table

import android.graphics.Color
import com.quevedo.marvin.easypdfdocument.settigns.EasyPadding
import com.quevedo.marvin.easypdfdocument.settigns.EasyPageMargin


class CellBorder(var borderTop:Float, var borderRight:Float, var borderBottom:Float, var borderLeft:Float) : EasyPageMargin(borderTop,borderRight,borderBottom, borderLeft){
    var topColor = Color.BLACK
    var leftColor = Color.BLACK
    var rightColor = Color.BLACK
    var bottomColor = Color.BLACK

    enum class BorderColor{
        TOP,LEFT, RIGHT, BOTTOM
    }


    fun setBordersColor(color:Int){
        this.topColor = color
        this.leftColor = color
        this.rightColor = color
        this.bottomColor = color
    }
    fun setBorderColor(color:Int, border: BorderColor){
        when (border) {
            BorderColor.TOP -> this.topColor = color
            BorderColor.LEFT -> this.leftColor = color
            BorderColor.RIGHT -> this.rightColor = color
            BorderColor.BOTTOM -> this.bottomColor = color
        }
    }
    fun plusPadding(padding: EasyPadding): CellBorder {
        borderTop += padding.paddingTop
        borderLeft += padding.paddingLeft
        borderRight += padding.paddingRight
        borderBottom += padding.paddingBottom

        //Copy this values for use in DrawSettings
        super.marginTop = borderTop
        super.marginRight = borderRight
        super.marginBottom = borderBottom
        super.marginLeft = borderLeft
        return this
    }

    fun horizontalPadding():Float{
        return borderLeft + borderRight
    }fun verticalPadding():Float{
        return borderTop + marginBottom
    }
    override fun copy(): CellBorder {
        return CellBorder(borderTop, borderRight, marginBottom, borderLeft)
    }
    companion object {
        val DEFAULT = CellBorder(1f, 1f, 1f, 1f)
    }
}