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

class RowFakeCell(private val father: Cell): Cell("") {
    private val fatherBottomColor:Int = father.cellBorder.rightColor
    var isLast = false
    init {

        cellBorder.bottomColor = Color.TRANSPARENT
        cellBorder.topColor = Color.TRANSPARENT
        cellBorder.leftColor = Color.TRANSPARENT
        cellBorder.rightColor = Color.TRANSPARENT
        colSpan = father.colSpan
    }
    fun paintLast(){
        cellBorder.bottomColor = fatherBottomColor
        cellBorder.bottomColor = Color.RED
        isLast = true
    }
    override fun copy(): RowFakeCell {
        val cell = RowFakeCell(father)
        cell.paragraph = paragraph
        cell.cellBorder = cellBorder.copy()
        cell.padding = padding.copy()
        cell.backColor = backColor
        cell.colSpan = colSpan
        cell.rowSpan = rowSpan
        return cell
    }
}