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

import com.quevedo.marvin.easypdfdocument.elements.text.EasyStyle
import com.quevedo.marvin.easypdfdocument.elements.text.EasyText

class CellStyle: Cell(EasyText("")){
    var textStyle = EasyStyle()
    internal set
    fun setTextStyle(style: EasyStyle): CellStyle {
        this.textStyle = style
        return this
    }
    fun setTextStyle(style: EasyText): CellStyle {
        if(style::class == EasyStyle::class){
            this.textStyle = style as EasyStyle
        }else{
            this.textStyle = style.toEasyStyle()
        }
    return this
    }
    override fun copy(): CellStyle {
        val style = CellStyle()
        style.textStyle = textStyle.copy()
        style.cellBorder = cellBorder.copy()
        style.padding = padding.copy()
        style.backColor = backColor

        return style
    }



}