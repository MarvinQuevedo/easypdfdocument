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

open class RowSpanWorker(fatherCell:Cell,  var columnPos:Int) {
    private val rowFakeCell:RowFakeCell = RowFakeCell(fatherCell)
    var processed = 1//Because start with the same row
    internal set
    var rowPos = 0
    internal set
    var totalRowSpan = fatherCell?.rowSpan
    internal set
    fun plusProcessed(){
        processed++
    }
    init {
        rowFakeCell.backColor = Color.GREEN
    }
    fun isAlive() = totalRowSpan > (processed)
    fun proccess():RowFakeCell{
        processed++
        if(!isAlive()){
            rowFakeCell.paintLast()
        }
        return rowFakeCell.copy()
    }
    fun notifiInsert(index:Int, count:Int){
        if(index<=columnPos){
            columnPos+= count
        }
    }
}
