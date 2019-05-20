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

class FakeCell(val father: Cell): Cell("") {

    private val fatherRigthBorderColor:Int = father.cellBorder.rightColor

    init {
        father.cellBorder.rightColor = Color.TRANSPARENT
        super.cellBorder.leftColor = Color.TRANSPARENT
        super.cellBorder.rightColor = Color.TRANSPARENT
        rowSpan = father.rowSpan
    }
    fun paintLast(){
        cellBorder.rightColor = fatherRigthBorderColor
    }

    override fun applyStyle(cellStyle: CellStyle?): Cell {
        // do nothing :D
        return this
    }
    override fun copy(): FakeCell {
        val cell = FakeCell(father)

        cell.paragraph = paragraph
        cell.cellBorder = cellBorder.copy()
        cell.padding = padding.copy()
        cell.backColor = backColor
        cell.colSpan = colSpan
        cell.rowSpan = rowSpan
        return cell
    }

}