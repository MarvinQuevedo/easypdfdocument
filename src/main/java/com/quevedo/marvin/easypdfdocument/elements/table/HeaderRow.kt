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
import com.quevedo.marvin.easypdfdocument.elements.text.EasyText

class HeaderRow: Row() {

    override fun add(text: EasyText, colSpan:Int, rowSpan:Int): HeaderRow {
        this.add(Cell(text))
        return this
    }

    override fun add(string:String,  colSpan:Int, rowSpan:Int): HeaderRow {
        this.add(EasyText(string))
        return this
    }

    override fun applyStyle() {
        if(defaultStyle != null){
            defaultStyle?.textStyle?.setForeColor(Color.WHITE)
            defaultStyle?.textStyle?.setBold(true)
            defaultStyle?.textStyle?.setFontSize(14)

            for(cell: Cell in cells){
                cell.paragraph.setMainStyle(this.defaultStyle?.textStyle?.copy()!!)
                cell.backColor = Color.rgb(57, 57, 57)
                cell.cellBorder = defaultStyle?.cellBorder?.copy()!!
                cell.cellBorder.setBorderColor(Color.WHITE, CellBorder.BorderColor.LEFT)
                cell.cellBorder.setBorderColor(Color.WHITE, CellBorder.BorderColor.RIGHT)
                cell.padding = defaultStyle?.padding?.copy()!!
            }
        }
    }

    override fun add(cell: Cell): HeaderRow {


        super.add(cell)
        return this
    }


}