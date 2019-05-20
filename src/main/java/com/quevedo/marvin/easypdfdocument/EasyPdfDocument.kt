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

import android.content.Context
import android.graphics.Canvas
import android.graphics.pdf.PdfDocument
import android.util.Log
import com.quevedo.marvin.easypdfdocument.exeptions.FileAlreadyExistException
import com.quevedo.marvin.easypdfdocument.interfaces.EasyDocumentInterface
import com.quevedo.marvin.easypdfdocument.settigns.CanvasDebug
import com.quevedo.marvin.easypdfdocument.settigns.EasyPageSize
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class EasyPdfDocument(val context:Context, fileName:String):EasyDocumentInterface {
    override fun requestNewPage(): Canvas? {

        val page = activePage?.plusPage()
        addPage(page)
        return page?.canvas
    }



    val file: File = File(fileName)
    val document = PdfDocument()
    var activePage: EasyPage? = null
    fun addPage(page: EasyPage?):EasyPdfDocument{
        //si esta abierta la pagina, se finaliza
        if(activePage != null){
            activePage?.finish(document)
        }
        page?.startPage(document)
        activePage = page
        activePage?.documentInterface = this
        return this
    }
    //Add page equal the after
    fun addPage(){
        if(activePage != null){
            val page = activePage?.plusPage()
            this.addPage(page)
        }

    }

    fun save(reWrite: Boolean):Boolean{
        try {
            CanvasDebug.dCanvas = null
            if(file.exists()){
                if(reWrite){
                    file.delete()
                }else{
                    throw FileAlreadyExistException("The file ${file.absoluteFile} already exist")
                }
            }
            if(activePage?.finished == false){
                activePage?.finish(document)
            }
            document.writeTo(FileOutputStream(file))
            return true
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("saving doc", e.toString())
            return false
        }
    }

}