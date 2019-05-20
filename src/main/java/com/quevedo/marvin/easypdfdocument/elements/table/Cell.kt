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
import com.quevedo.marvin.easypdfdocument.elements.Paragraph
import com.quevedo.marvin.easypdfdocument.elements.text.EasyText
import com.quevedo.marvin.easypdfdocument.settigns.EasyPadding
import com.quevedo.marvin.easypdfdocument.settigns.EasyPageMargin

open class Cell(val text: EasyText){
    var colSpan = 1
    internal set
    var rowSpan = 1
    internal set
    var cellBorder = CellBorder.DEFAULT.copy()
    var padding = EasyPadding.NO_PADDING.copy()
    var paragraph = Paragraph(text)
    var backColor = Color.TRANSPARENT
    var specialHeight = 0f //is used for the row span
    init {
        paragraph.setMargins(EasyPageMargin.NO_MARGIN.copy())
    }

    constructor(string: String) : this(EasyText(string))
    fun setColSpan(span:Int): Cell {
        this.colSpan = span
        return this
    }
    fun getHorizontalPadding():Int{
        return padding.horizontalLess().toInt()
    }
    fun setRowSpan(span:Int): Cell {
        this.rowSpan = span
        return this
    }
    fun getTotalPadding(): CellBorder {
        val marginTotal = cellBorder.copy()
        return marginTotal.plusPadding(padding)
    }
    fun toCellStyle(): CellStyle {
        val style = CellStyle()
        style.textStyle = text.toEasyStyle()
        style.cellBorder = cellBorder.copy()
        style.padding = padding.copy()
        style.backColor = backColor
        return style
    }
    open fun applyStyle(cellStyle: CellStyle?): Cell {
        paragraph.setMainStyle(cellStyle?.textStyle?.copy()!!)
        backColor = cellStyle?.backColor!!
        if(!haveSpans()){
            cellBorder = cellStyle?.cellBorder?.copy()!!
        }
        padding = cellStyle?.padding?.copy()!!
        return this
    }

    fun haveSpans(): Boolean {
        return colSpan>1 || rowSpan>1
    }
    open fun copy(): Cell {
        val cell = Cell("")

        cell.paragraph = paragraph
        cell.cellBorder = cellBorder.copy()
        cell.padding = padding.copy()
        cell.backColor = backColor
        cell.colSpan = colSpan
        cell.rowSpan = rowSpan
        return cell
    }

}
