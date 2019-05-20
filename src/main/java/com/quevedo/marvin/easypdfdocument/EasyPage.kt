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

package com.quevedo.marvin.easypdfdocument

import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.util.Log
import com.quevedo.marvin.easypdfdocument.elements.EasyImage
import com.quevedo.marvin.easypdfdocument.elements.EasyTable
import com.quevedo.marvin.easypdfdocument.elements.text.EasyText
import com.quevedo.marvin.easypdfdocument.elements.Paragraph
import com.quevedo.marvin.easypdfdocument.interfaces.EasyDocumentInterface
import com.quevedo.marvin.easypdfdocument.interfaces.EmptyEasyDocumentInterface
import com.quevedo.marvin.easypdfdocument.settigns.CanvasDebug
import com.quevedo.marvin.easypdfdocument.settigns.DrawSettings
import com.quevedo.marvin.easypdfdocument.settigns.EasyPageMargin
import com.quevedo.marvin.easypdfdocument.settigns.EasyPageSize

class EasyPage(val pageSize: EasyPageSize, val pageNumber:Int = 1) {


    val pageInfo  = PdfDocument.PageInfo.Builder(pageSize.width, pageSize.heigth, pageNumber).create()
    var page:PdfDocument.Page? = null
    var canvas:Canvas? = null
    var started = false
    var finished = false
    var margins = EasyPageMargin.DEFAULT.copy()
    var documentInterface:EasyDocumentInterface = EmptyEasyDocumentInterface()
    val drawSettings = DrawSettings(margins,pageSize.width, pageSize.heigth, 0f, 0f)
    var verticalSpaceAvailable = 0f

    fun startPage(document:PdfDocument ){
        page = document.startPage(pageInfo)
       // margins.apply(page?.info?.contentRect)
        canvas = page?.canvas
        CanvasDebug.dCanvas = canvas;
        CanvasDebug.height = pageSize.heigth
        CanvasDebug.width = pageSize.width
        verticalSpaceAvailable = drawSettings.resetVerticalSpaceAvailable()
        canvas?.translate(margins.marginLeft, margins.marginTop)
        started = true

    }
    fun plusPage():EasyPage{
        return EasyPage(this.pageSize, this.pageNumber+1)
    }

    fun finish(document: PdfDocument){
       try {
           document.finishPage(this.page)

       }catch (ex:Exception){
           Log.e("finishPage", ex.toString())
       }
        finished = true
    }
    fun add(easyText: EasyText):Paragraph{
        val paragraph = Paragraph(easyText)
        add(paragraph)
        return paragraph
    }
    fun add(paragraph: Paragraph){
        if(started){
            val paragraphSettings = drawSettings
            paragraphSettings.margins.marginRight  += paragraph.margins.marginRight
            paragraphSettings.verticalSpaceAvailable = verticalSpaceAvailable

            var pixels = paragraph.draw(canvas,  paragraphSettings, this.documentInterface)
           // Log.e("pixels", pixels.toString())

            verticalSpaceAvailable -= pixels//Change the avaliable space in the page
        }else{

        }
    }
    fun add(easyImage: EasyImage){
        if(started){
            val paragraphSettings = drawSettings
            paragraphSettings.margins.marginRight  += easyImage.marginRight
            paragraphSettings.verticalSpaceAvailable = verticalSpaceAvailable
            paragraphSettings.resetHorizontalSpaceAvailable()
            //dCanvas?.translate(easyImage.borderLeft, easyImage.borderTop)

            var pixels = easyImage.draw(canvas,  paragraphSettings, this.documentInterface) + easyImage.marginBottom
            Log.e("image pixels", pixels.toString())

            verticalSpaceAvailable -= pixels
            if(verticalSpaceAvailable>0 && !this.finished){

               try {
                   canvas?.translate(0f, pixels)
               }catch (ex:Exception){
                   Log.e("image pixels", ex.toString())
               }

            }


        }else{

        }
    }
    fun add(table: EasyTable){
        if(started){
            val paragraphSettings = drawSettings
            paragraphSettings.margins.marginRight  += table.marginRight
            paragraphSettings.verticalSpaceAvailable = verticalSpaceAvailable
            paragraphSettings.resetHorizontalSpaceAvailable()
            //dCanvas?.translate(table.borderLeft, table.borderTop)

            var pixels = table.draw(canvas,  paragraphSettings, this.documentInterface) + table.marginBottom
            Log.e("image pixels", pixels.toString())

            verticalSpaceAvailable -= pixels
            if(verticalSpaceAvailable>0 && !this.finished){

                try {
                    canvas?.translate(0f, pixels)
                }catch (ex:Exception){
                    Log.e("image pixels", ex.toString())
                }

            }


        }else{

        }
    }

    fun getNumberPage():Int = pageInfo.pageNumber

}