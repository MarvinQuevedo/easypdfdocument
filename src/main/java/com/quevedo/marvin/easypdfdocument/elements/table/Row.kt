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

open class Row {
    var rowPos = 0
    var cells =  ArrayList<Cell>()
    var defaultStyle: CellStyle? = null
    val rowsSpanWorkers = ArrayList<RowSpanWorker>()

    fun hasRowsSpanWorkers():Boolean = !rowsSpanWorkers.isEmpty()
    fun setRowIndex(index:Int){
        for (worker:RowSpanWorker in rowsSpanWorkers){
            worker.rowPos = index
        }
        this.rowPos = index
    }
    open fun add(text: EasyText, colSpan:Int = 1, rowSpan:Int = 1): Row {
        this.add(Cell(text).setColSpan(colSpan).setRowSpan(rowSpan))
        return this
    }


    open fun add(string:String, colSpan:Int = 1, rowSpan:Int = 1): Row {
        this.add(EasyText(string), colSpan, rowSpan)
        return this
    }
    open fun add(cell: Cell): Row {
        var inserted = false
        if(cell.rowSpan>1){
            cell.backColor = Color.MAGENTA
            val spanWorker = RowSpanWorker(cell, cells.size)
            rowsSpanWorkers.add(spanWorker)

        }

        if(cell.colSpan >1){
            /*for apply the colspan, only add other rows with empty text
            * and with the border transparent*/


            val fakeCell = FakeCell(cell)
            inserted = true
            this.cells.add(cell)
            for( i in 2..cell.colSpan){
                if(i == cell.colSpan-1){
                    fakeCell.paintLast()
                }
                val fCell = fakeCell.copy()

                rowsSpanWorkers.add(RowSpanWorker(fCell, cells.size))
                this.cells.add(fCell)
            }

        }
        if(!inserted){
            this.cells.add(cell)
        }
        return this
    }
    private fun notifyInsert(index:Int, count:Int){
        for(worker:RowSpanWorker in rowsSpanWorkers){
           worker.notifiInsert(index, count)
        }
    }
    fun insertCell(cell:Cell, index:Int){
        val newList = ArrayList<Cell>()
        for(i in 0..(index-1)){
            newList.add(cells[i])
        }
        newList.add(cell)
        notifyInsert(index,1)
        val l = cells.size-1
        for(i in index..l){
            newList.add(cells[i])
        }
        cells = newList
    }
    internal fun applyRowSpanWorkers(list:ArrayList<RowSpanWorker>){
        for(worker:RowSpanWorker in list){
            if(worker.isAlive() && worker.rowPos != this.rowPos){
                insertCell(worker.proccess(),worker.columnPos)
            }
        }
    }

    internal open fun applyStyle(){
        if(defaultStyle != null){
            for(cell: Cell in cells){
                if(cell::class == FakeCell::class){

                }else{
                    cell.applyStyle(this.defaultStyle)
                }

            }
        }
    }
   open fun setDefaultStyle(style: CellStyle?): Row {
        if(this.defaultStyle == null){
            this.defaultStyle = style?.copy()
        }
        return this
    }

}