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

import android.graphics.Canvas
import com.quevedo.marvin.easypdfdocument.interfaces.EasyDocumentInterface
import com.quevedo.marvin.easypdfdocument.settigns.DrawSettings
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.quevedo.marvin.easypdfdocument.elements.table.*
import com.quevedo.marvin.easypdfdocument.exeptions.ColumnsDontMatchException
import com.quevedo.marvin.easypdfdocument.exeptions.RowSpanNotAplicableException
import com.quevedo.marvin.easypdfdocument.settigns.EasyPadding


class EasyTable(private val numColumns: Int):EasyElement {
    var rows = ArrayList<Row>()
    val columnsWidth = ArrayList<Int>()
    val tableRowsSpanWorkers = ArrayList<RowSpanWorker>()
    var marginTop = 0f
        private set
    var marginBottom = 5f
        private set
    var marginLeft = 0f
        private set
    var marginRight = 0f
        private set
    var defaultStyle: CellStyle? = null
        internal  set
    var rowHeights = FloatArray(0)
    fun setDefaultStyle(style: CellStyle?):EasyTable{
        this.defaultStyle = style
        return this
    }
    private fun appendRowsSpanWorkers(list:ArrayList<RowSpanWorker>){
        for(row:RowSpanWorker in list){
            this.tableRowsSpanWorkers.add(row)
        }
    }

    fun add(row: Row):EasyTable{
        if(this.defaultStyle != null){
            row.setDefaultStyle(defaultStyle)
        }

        row.applyStyle()//Apply the default style if exists
        row.setRowIndex(rows.size-1)
        row.applyRowSpanWorkers(tableRowsSpanWorkers)
        if(row.hasRowsSpanWorkers()){
            appendRowsSpanWorkers(row.rowsSpanWorkers)

        }



        this.rows.add((row))
        return this
    }
    private fun calAffectedRows(available:Float, start:Int):Int{
        val realAvailable = available + rowHeights[start]
        var affected = 0
        var processed = 0f
        var i = start
        val numRows = rows.size
        while (i<numRows){
            var height = rowHeights[i]
            if((processed + height)<=realAvailable){
                processed += height
                affected++
                i++
            }else{
                break
            }
        }
        return affected
    }
    override fun draw(c: Canvas?, drawSettings: DrawSettings, easyDocumentInterface: EasyDocumentInterface): Float {
        val borderWidth = 1f
        var canvas = c
        canvas?.drawColor(Color.WHITE)

        val numRows = rows.size
        if (numColumns == 0 || numRows == 0) {
            return 0f
        }
        val fakeInterface = object:EasyDocumentInterface{
            override fun requestNewPage(): Canvas? {
                Log.e("fake_interface", "is called")
                return canvas
            }
        }
        val width = drawSettings.getRealHorizontalSpace()
        val tCellWidth:Int =  (width / numColumns).toInt()
        for(i in 0 until numColumns){
            this.columnsWidth.add(tCellWidth)
        }

        rowHeights = FloatArray(numRows)
        for(i in 0 until numRows){
            var maxHeigth = 0f
            for(j in 0 until numColumns){
                if(rows[i].cells.size<j){
                    throw ColumnsDontMatchException("The table have ''$numColumns'', but the row number $i have only ''${rows[i].cells.size}'' columns")
                    continue
                }
                val cell = rows[i].cells[j]
                if(cell.rowSpan>1){
                    //have rowspan, continue, we work with this later

                   // cell.paragraph.setForeColor(Color.WHITE)
                    continue
                }
                val cellPadding = cell.getTotalPadding()
                var cellWidth = columnsWidth[j]
                if(cell.colSpan>1){
                    for(l in 1 until cell.colSpan){
                        cellWidth += columnsWidth[j+l]

                    }
                }
                if(cellWidth == 1){
                    continue
                }


                var tempSett = DrawSettings(cellPadding, cellWidth, drawSettings.verticalSpaceAvailable.toInt())
                var h = cell.paragraph.getHeigth(tempSett) + cellPadding.verticalPadding()
                if(h > maxHeigth){
                    maxHeigth = h
                }
            }
            rowHeights[i] = maxHeigth
        }
        for(i in 0 until numRows){
            //var maxHeigth = 0f
            for(j in 0 until numColumns){
                val cell = rows[i].cells[j]
                if(cell.rowSpan>1){
                    val cellPadding = cell.getTotalPadding()
                    var cellWidth = columnsWidth[j]
                    if(cell.colSpan>1){
                        for(l in 1 until cell.colSpan){
                            cellWidth += columnsWidth[j+l]

                        }
                    }
                    if(cellWidth == 1){
                        continue
                    }
                    var tempSett = DrawSettings(cellPadding, cellWidth, drawSettings.verticalSpaceAvailable.toInt())
                    var h = cell.paragraph.getHeigth(tempSett) + cellPadding.verticalPadding()
                    var totalOriginalHeight = 0f
                   try {
                       //Sum the heights
                       for(l in 0 until cell.rowSpan){
                           totalOriginalHeight += rowHeights[i+l]
                       }
                       //Is originalHeight is bigger than the cell height, only continue
                       if(totalOriginalHeight> h){
                           cell.specialHeight = totalOriginalHeight
                           continue
                       }
                       val heigths = h / cell.rowSpan
                       for(l in 0 until cell.rowSpan){
                           rowHeights[i+l] = heigths
                       }
                       cell.specialHeight = h
                   }catch (ex:Exception){
                       Log.e("easytable", ex.toString())
                        throw RowSpanNotAplicableException("The RowSpan can't applicable because the rows not exists\n${ex.toString()}")
                   }

                }
            }

        }

        var lasYMoved = 0f
        var lastXMoved = 0f
        var widthToMoved = 0f //With moved in all the row
        var cellXMoved = 0f //It for moved 0 in the firs cell
       // var maxVerticalPadding = 0f

        for (i in 0 until numRows) {

            var cellHeight =  rowHeights[i]
            val originalHeight = cellHeight
           // drawSettings.verticalSpaceAvailable -= maxVerticalPadding
            //maxVerticalPadding = 0f
            widthToMoved = 0f // x position moved to right, to move again to left
            if(drawSettings.verticalSpaceAvailable<cellHeight){
                canvas = easyDocumentInterface.requestNewPage()
                drawSettings.resetVerticalSpaceAvailable()
                drawSettings.resetHorizontalSpaceAvailable()
                cellXMoved = 0f //Restart to a goog x position
            }else{
                drawSettings.verticalSpaceAvailable -= cellHeight
            }

            for (j in 0 until numColumns) {
                val cell = rows[i].cells[j]
                val cellTotalMargin = cell.getTotalPadding()//plus the padding again
                val nextCell: Cell
                if(cell.rowSpan>1 && cell.specialHeight>0){
                    cellHeight = cell.specialHeight
                    if(drawSettings.verticalSpaceAvailable<cellHeight){
                        val rowAffected = calAffectedRows(drawSettings.verticalSpaceAvailable, i)
                        cellHeight = drawSettings.verticalSpaceAvailable + (cell.cellBorder.borderBottom + cell.cellBorder.borderTop)
                        if(cellHeight>drawSettings.getRealVerticalSpace()){
                            cellHeight = drawSettings.getRealVerticalSpace()
                        }
                        val fakeCell = Cell("")
                        fakeCell.rowSpan =cell.rowSpan -  rowAffected
                        fakeCell.applyStyle(cell.toCellStyle())

                        fakeCell.specialHeight =  (cell.specialHeight - drawSettings.verticalSpaceAvailable) - (cell.cellBorder.borderBottom + cell.cellBorder.borderTop)

                        rows[i+rowAffected].cells[j] = fakeCell
                    }

                }else{
                    cellHeight = originalHeight
                }


                if(numRows <= i && numColumns <j){
                    nextCell = rows[i+1].cells[j+1]
                }else{
                    nextCell = Cell("")
                    nextCell.cellBorder = cell.cellBorder
                    nextCell.padding = EasyPadding.NO_PADDING

                }
                //We can draw after moved x,. y
                canvas?.translate(cellXMoved, 0f)
                var cellWidth = columnsWidth[j].toFloat()
                if(cell.colSpan>1){
                    for(l in 1 until cell.colSpan){
                        cellWidth += columnsWidth[j+l]

                    }
                    cell.backColor = Color.YELLOW
                }
                if(cell::class == FakeCell::class){
                    cellXMoved = 0f
                    continue
                }
                if(cell::class == RowFakeCell::class){
                    Log.e("fakeCell", "is Row fake cell")
                    cellXMoved = cellWidth
                    widthToMoved += cellXMoved//How much moved
                    continue
                }

                Log.e("Table", "Process $i,$j = ${cell.paragraph.text}")
                Log.e("Table", canvas.toString())
                val paragraph = cell.paragraph
               // paragraph.setParagraphBackColor(RndColor.getColor())



                var settings2 = DrawSettings(cellTotalMargin, cellWidth.toInt() ,cellHeight.toInt())
                Log.e("move x, y" , "x = $cellXMoved y = 0.0")

                if(cellXMoved == 0f){
                    //cellXMoved = cellTotalMargin.borderLeft
                }

                var startLeft = when(j == 0){
                    true->-cellTotalMargin.borderLeft
                    else->0f
                }

                if(cell.backColor != Color.TRANSPARENT){
                    val celBgColorPaint = Paint()
                    celBgColorPaint.color = cell.backColor
                    canvas?.drawRect(startLeft,0f, cellWidth,cellHeight,celBgColorPaint)
                }


                drawLines(canvas, cell, cellWidth, cellHeight, (j == 0), (j == (numColumns-1)))

                canvas?.translate(cellTotalMargin.borderLeft, cellTotalMargin.borderTop)


                cellXMoved = cellWidth

                widthToMoved += cellXMoved//How much moved
                val pixels = paragraph.draw(canvas,settings2,fakeInterface) + cellTotalMargin.borderTop

                canvas?.translate(-cellTotalMargin.borderLeft, -pixels)//draw move down, then it move up again
                //this move to up again for the new column start the same relative y position
            }
            lasYMoved = cellHeight
            lastXMoved = widthToMoved
            canvas?.translate(-widthToMoved, cellHeight)
            //drawSettings.verticalSpaceAvailable -= cellHeight //less the heigth of this cell, it include the padding top and bottom
        }
        // for exit of draw the rows, need to move x start position and the las y moved
        canvas?.translate(-lastXMoved, lasYMoved)


        return 0f
    }
    private fun drawLines(canvas: Canvas?, cell: Cell, cellWidth:Float, cellHeight:Float, isFirst:Boolean, isEnd:Boolean){
        //remember, cellHeigth include the boder and padding space

        val cellPadding = cell.getTotalPadding()
        val blackPaint = Paint()
        blackPaint.style = Paint.Style.STROKE
        val marginLeft = cellPadding.marginLeft
        val cellFloatWidth = cellWidth.toFloat()
        val isFakeCell = cell::class == FakeCell::class
        val isFakeRowCell = cell::class == RowFakeCell::class
        val leftBorder = when(isFirst){
            true->-marginLeft
            false->0f
        }
        var bottom2 = cell.cellBorder.borderBottom/2
        var top2 = cell.cellBorder.borderTop /2
        var left2 = -marginLeft - (cell.cellBorder.borderLeft /2)
        var rigth2 = cell.cellBorder.borderRight
        if(!isFirst){
            rigth2 -= cell.cellBorder.borderLeft /2
            left2 = 0f
        }

        //horizontal line borderTop
        blackPaint.color = cell.cellBorder.topColor
        blackPaint.strokeWidth = cell.cellBorder.borderTop
       /// blackPaint.color = RndColor.getColor()
        canvas?.drawLine(left2, 0f, cellFloatWidth+rigth2, 0f, blackPaint)


        //horizontal line borderBottom
        blackPaint.color = cell.cellBorder.bottomColor
        blackPaint.strokeWidth = cell.cellBorder.borderBottom
       // blackPaint.color = RndColor.getColor()
        canvas?.drawLine(left2,cellHeight, cellFloatWidth+rigth2, cellHeight, blackPaint)

        //vertical borderLeft line
        blackPaint.color = cell.cellBorder.leftColor
        blackPaint.strokeWidth = cell.cellBorder.borderLeft
        if(isFirst && blackPaint.color != Color.BLACK && cell.backColor != Color.TRANSPARENT){
            blackPaint.color = cell.backColor
        }
        if(isFakeCell){
            blackPaint.color = Color.TRANSPARENT
        }
        //blackPaint.color = RndColor.getColor()
        canvas?.drawLine(leftBorder, top2, leftBorder, cellHeight-bottom2, blackPaint)

        //vertical right line
        blackPaint.color = cell.cellBorder.rightColor
        blackPaint.strokeWidth = cell.cellBorder.borderRight
        if(isEnd && blackPaint.color != Color.BLACK && cell.backColor != Color.TRANSPARENT){
            blackPaint.color = cell.backColor
        }
        if(isFakeCell){
            blackPaint.color = Color.TRANSPARENT
        }
       // blackPaint.color = RndColor.getColor()
        canvas?.drawLine(cellFloatWidth, top2, cellFloatWidth, cellHeight-bottom2, blackPaint)

    }


}
