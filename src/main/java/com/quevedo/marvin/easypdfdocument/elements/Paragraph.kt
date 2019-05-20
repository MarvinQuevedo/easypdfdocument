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
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.text.*
import android.text.style.*
import android.util.Log
import com.quevedo.marvin.easypdfdocument.extension.getIndexsOf

import com.quevedo.marvin.easypdfdocument.interfaces.EasyDocumentInterface
import com.quevedo.marvin.easypdfdocument.elements.text.EasyLinkText
import com.quevedo.marvin.easypdfdocument.elements.text.EasyStyle
import com.quevedo.marvin.easypdfdocument.elements.text.EasyText
import com.quevedo.marvin.easypdfdocument.settigns.DrawSettings


import android.text.SpannableStringBuilder
import com.quevedo.marvin.easypdfdocument.extension.replaceAll

import android.text.Spanned
import android.text.style.ImageSpan
import android.text.SpannableString
import android.view.View
import com.quevedo.marvin.easypdfdocument.extension.BlankSpace
import com.quevedo.marvin.easypdfdocument.extension.ParagraphVeryLong
import com.quevedo.marvin.easypdfdocument.settigns.EasyPadding
import com.quevedo.marvin.easypdfdocument.settigns.EasyPageMargin


class Paragraph(): EasyElement {
    var text = ""
    var mainStyle = EasyStyle()
    var stylesList = ArrayList<EasyText>()
    var density = 1f
    var scaledSize = 0f
    var heightTotal = 0f
    internal  set
    var margins = EasyPageMargin.NO_MARGIN.copy()
    internal  set

    //Se agrega un texto, y ademas se establece su estilo
    fun add(easyText: EasyText): Paragraph {
        val lastPos = text.length//Length before adding the new text
        text += easyText.text
        easyText.text = ""//Ya no se necesitará
        easyText.setPosition(lastPos)
        stylesList.add(easyText)
        return this
    }

    fun add(string:String):Paragraph{
        text += string
        return this
    }
    fun setPadding(padding: EasyPadding):Paragraph{
        this.mainStyle.padding = padding
        return this
    }
    fun setPadding(left:Float = mainStyle.padding.paddingLeft, right:Float = mainStyle.padding.paddingRight, top:Float = mainStyle.padding.paddingTop, bottom:Float = mainStyle.padding.paddingBottom ):Paragraph{
        this.mainStyle.padding.paddingLeft  = left
        this.mainStyle.padding.paddingRight = right
        this.mainStyle.padding.paddingTop = top
        this.mainStyle.padding.paddingBottom = bottom
        return this
    }
    fun setMargins(margins:EasyPageMargin):Paragraph{
        this.margins = margins
        return this
    }
    fun setMargins(left:Float = margins.marginLeft, rigth:Float = margins.marginRight, top:Float = margins.marginTop, bottom:Float = margins.marginBottom ):Paragraph{
        this.margins.marginLeft  = left
        this.margins.marginRight = rigth
        this.margins.marginTop = top
        this.margins.marginBottom = bottom
        return this
    }
    fun setTextAlignment(alignment: EasyText.EasyAlignment):Paragraph{
        this.mainStyle.textAlignment = alignment
        return this
    }
    fun setParagraphBackColor(color:Int): Paragraph {
        this.mainStyle.paragraphBackColor = color
        return this
    }
    //Se agregan solo los estilos de un texto ya definido
    fun addStyle(easyStyle: EasyStyle): Paragraph {
        stylesList.add(easyStyle)
        return this
    }
    //Se agrega un texto, se debe aplicar los estilos mediante addStyle
    constructor(str:String) : this() {
        text = str
    }
    constructor(easyText: EasyText):this(){
        this.mainStyle = easyText.toEasyStyle()
        add(easyText)
    }
    fun setBold(bold:Boolean):Paragraph{
        this.mainStyle.setBold(bold)
        return this
    }
    fun setForeColor(foreColor:Int):Paragraph{
        mainStyle.setForeColor(foreColor)
        return this
    }
    fun setFontSize(fontSize:Int):Paragraph{
        mainStyle.fontSize = fontSize
        return this
    }
    fun setBackColor(backColor:Int):Paragraph{
        mainStyle.setBackColor(backColor)
        return this
    }
    private fun calcSize(size:Int):Int{
        return ((size + scaledSize)  * density).toInt()
    }
    fun calcSize() = calcSize(mainStyle.fontSize).toFloat()
    fun setMainStyle(style: EasyStyle):Paragraph{
        mainStyle = style.toEasyStyle()
        return this
    }
    fun removeInternStyleSpan(span:StyleSpan, start: Int, end: Int):Boolean{
        val l = stylesList.size
        var done = false
        for(i in 0 until l){
            val style = stylesList[i] as EasyStyle
            if(style.start<= start && style.end>=end){
                val oldEnd = style.end
                val nStyle = style.copy()
                nStyle.start = end
                nStyle.end = oldEnd

                var prepare = fun(){
                    style.end = start
                }

                if(style.bold && span.style == Typeface.BOLD){
                    prepare()
                    nStyle.bold = true
                    addStyle(nStyle)
                }else if(style.italic && span.style == Typeface.ITALIC){
                    prepare()
                    nStyle.italic = true
                    addStyle(nStyle)
                }else if(style.italic && span.style == Typeface.BOLD_ITALIC){
                    prepare()
                    nStyle.italic = true
                    nStyle.bold = true
                    addStyle(nStyle)
                }
            }
        }
        return done
    }

    fun removeInternStyleSpan(span:ParcelableSpan, start: Int, end: Int):Boolean{
        if(span is StyleSpan){
            return removeInternStyleSpan(span, start, end)
        }
        return false
    }
    //Se establecen los estilos
    fun processText(spannedStringSource: SpannableString = SpannableString(this.text), reApply:Boolean = false, tStart:Int = 0, tEnd:Int = text.length):SpannableString{
        //Apply bold style

        var spannableString = spannedStringSource
        val len = spannableString.length

        var list = stylesList.listIterator()
        if(!reApply){
            this.mainStyle.setPosition(0, text.length)
        }

        list.add(this.mainStyle)
        for( easyText in list){
            var start = easyText.start
            var end  = easyText.end
           // val diference = end-start;
            if((start>= tStart && start <= tEnd) && end >= tEnd){
                end = tEnd
            }else if((end>= tStart && end <= tEnd) && start <= tStart){
                start = 0
                end -= tStart
            }else if(tStart>= start && tEnd <= end){
                start = 0
                end  = spannableString.length
            }else if(start >= tStart && end <= tEnd){
                //Good
            }else{
                continue
            }
            if(start == 0 && end == 0){
                //throw StyleMalformedException("start and end can't equal to 0")
                continue
            }
            if(start<0){
                //throw StyleMalformedException("start and end can't to be less than 0")
                continue
            }else if(end>len){
                end = len
            }
            if(!reApply && easyText.fontSize != 12){
                val size = calcSize(easyText.fontSize)
                Log.e("textSize", "apply = $size")
                spannableString.setSpan(AbsoluteSizeSpan(size),start,end,0)
            }
            if(easyText.bold){
               spannableString.setSpan(StyleSpan(Typeface.BOLD),start, end, 0)
            }
            if (easyText.italic){
                spannableString.setSpan(StyleSpan(Typeface.ITALIC),start, end, 0)
            }
            if(easyText.italic && easyText.bold){
                spannableString.setSpan(StyleSpan(Typeface.BOLD_ITALIC),start, end, 0)
            }
            if(easyText.underline){
                spannableString.setSpan(UnderlineSpan(),start, end, 0)
            }
            if(easyText.strike){
                spannableString.setSpan(StrikethroughSpan(),start, end, 0)
            }
            if(easyText.foreColor != Color.BLACK){
                spannableString.setSpan(ForegroundColorSpan(easyText.foreColor), start, end, 0)
            }
            if(easyText.backColor != Color.TRANSPARENT){
                spannableString.setSpan(BackgroundColorSpan(easyText.backColor), start, end, 0)
            }



            if(easyText::class == EasyLinkText::class){
                easyText as EasyLinkText
                spannableString.setSpan(URLSpan(easyText.url), start, end, 0)
            }
            if(easyText.scriptStyle != EasyText.ScriptStyle.NONE){
                if(easyText.scriptStyle == EasyText.ScriptStyle.UPPER){
                    spannableString.setSpan(SuperscriptSpan(), start, end, 0)
                }else{
                    spannableString.setSpan(SubscriptSpan(), start, end, 0)
                }
            }
        }
        return spannableString
    }
    fun getHeigth(drawSettings: DrawSettings):Int{
        var spannedString  = this.processText()
        val textPaint = TextPaint()
        textPaint.isAntiAlias = true
        textPaint.textSize = calcSize(mainStyle.fontSize).toFloat()
        textPaint.color = mainStyle.foreColor
        textPaint.linkColor = Color.BLUE
        var layout = this.generateLayout(spannedString,textPaint, drawSettings, 0, text.length)
        return layout.height
    }
    override fun draw(c: Canvas?, drawSettings: DrawSettings, easyDocumentInterface: EasyDocumentInterface):Float {
        heightTotal = 0f
        var canvas = c
        val textPaint = TextPaint()
        textPaint.isAntiAlias = true
        textPaint.textSize = calcSize(mainStyle.fontSize).toFloat()
        textPaint.color = mainStyle.foreColor
        textPaint.linkColor = Color.BLUE
        c?.drawColor(Color.TRANSPARENT)

        //move margins borderLeft and borderTop
        canvas?.translate(this.margins.marginLeft, this.margins.marginTop)
        drawSettings.width -= this.mainStyle.padding.horizontalLess().toInt()


        var spannedString  = this.processText()
        var breaks = text.getIndexsOf("\n")
        var lastBreak = 0
        if(breaks.size>0){
            val layoutList = ArrayList<Layout>()
            var layoutHeight = mainStyle.padding.verticalLess()
            for(pos in breaks){
                var layout = this.generateLayout(spannedString,textPaint,drawSettings, lastBreak, pos)
                layoutHeight += layout.height
                Log.e("layoutH", "${layout.height}")
                layoutList.add(layout)
                lastBreak = pos+1
            }
            //if the page have sufficient space, then only draw the paragraphs
            if(layoutHeight<=drawSettings.verticalSpaceAvailable){
                prepareVerticalAlignment(canvas, drawSettings,layoutHeight.toInt())
                if(mainStyle.paragraphBackColor != Color.TRANSPARENT){
                    val celBgColorPaint = Paint()
                    celBgColorPaint.color = this.mainStyle.paragraphBackColor
                    canvas?.drawRect(0f,0f, layoutList[0].width + mainStyle.padding.horizontalLess(),layoutHeight, celBgColorPaint)
                }
                canvas?.translate(mainStyle.padding.paddingLeft, mainStyle.padding.paddingTop)
                for(layout in layoutList){

                    drawInLayout(canvas,layout,drawSettings)
                }
                canvas?.translate(-mainStyle.padding.paddingLeft, mainStyle.padding.paddingBottom)

            }else{
                prepareVerticalAlignment(canvas, drawSettings, drawSettings.verticalSpaceAvailable.toInt())

                var i = 0;
                var max = layoutList.size
                for(layout in layoutList){
                    Log.e("layout", " ${layout.height}, ${drawSettings.verticalSpaceAvailable},   ${drawSettings.getRealVerticalSpace()}")
                    if(layout.height>drawSettings.verticalSpaceAvailable){

                        //If the page have sufficient space for the layout, only draw it
                        if(layout.height <= drawSettings.getRealVerticalSpace()){
                            drawSettings.resetVerticalSpaceAvailable()
                            drawSettings.resetHorizontalSpaceAvailable()
                            canvas = easyDocumentInterface.requestNewPage()
                            layout.draw(canvas)
                        }else if(layout.height > drawSettings.getRealVerticalSpace()){
                            throw ParagraphVeryLong("The paragraph is very Long for the page and don't have a break '\\n', you can divide in parts with breaks \"\\n\"")
                            //Log.e("layout", " ${layout.height} > ${drawSettings.getRealVerticalSpace()} ${layout.text}")
                           // continue
                        }

                    }

                    if(mainStyle.paragraphBackColor != Color.TRANSPARENT){
                        val celBgColorPaint = Paint()
                        celBgColorPaint.color = this.mainStyle.paragraphBackColor
                        //celBgColorPaint.color = RndColor.getColor()
                        var tempHeight = 0f
                        if(i == 0){
                            tempHeight = mainStyle.padding.paddingTop
                        }else if(i == max-1){
                            tempHeight = mainStyle.padding.paddingBottom
                        }
                        canvas?.drawRect(0f,0f, layout.width + mainStyle.padding.horizontalLess(),layout.height.toFloat() + tempHeight, celBgColorPaint)
                        if(i == 0){
                            canvas?.translate(0f, mainStyle.padding.paddingTop)
                            drawSettings.verticalSpaceAvailable -= mainStyle.padding.paddingTop
                        }
                    }
                    canvas?.translate(mainStyle.padding.paddingLeft,0f)

                    drawInLayout(canvas,layout,drawSettings)

                    canvas?.translate(-mainStyle.padding.paddingLeft, 0f)
                    i++
                }
                canvas?.translate(0f, mainStyle.padding.paddingBottom)
                drawSettings.verticalSpaceAvailable -= mainStyle.padding.paddingBottom

            }
        }else{
            var layout = this.generateLayout(spannedString,textPaint,drawSettings)

            if(mainStyle.paragraphBackColor != Color.TRANSPARENT){
                val celBgColorPaint = Paint()
                celBgColorPaint.color = this.mainStyle.paragraphBackColor

                canvas?.drawRect(0f,0f, layout.width + mainStyle.padding.horizontalLess(),layout.height + mainStyle.padding.verticalLess(), celBgColorPaint)
            }

            canvas?.translate(mainStyle.padding.paddingLeft, mainStyle.padding.paddingTop)
            //before draw, prepare the aligments
            prepareVerticalAlignment(canvas, drawSettings,layout.height)

            drawInLayout(canvas,layout,drawSettings)

            canvas?.translate(-mainStyle.padding.paddingLeft, mainStyle.padding.paddingBottom)

        }
        canvas?.translate(-this.margins.marginLeft, 0f)
        Log.e("imageHeight", heightTotal.toString())

        if(this.margins.marginBottom>0 && c == canvas){
            canvas?.translate(0f, this.margins.marginBottom)

        }
        //dCanvas?.save()
        return heightTotal

    }
   private fun prepareVerticalAlignment(c:Canvas?, drawSettings: DrawSettings, height:Int){
       if(this.mainStyle.textVerticalAlignment == EasyText.EasyVerticalAlignment.MIDDLE){
           val verticalSpace = drawSettings.verticalSpaceAvailable//Space in the page
           val realFree = verticalSpace - height// Space after of drawing
           val yMoved = realFree / 2
           c?.translate(0f, yMoved)
           heightTotal += yMoved
       }else if(this.mainStyle.textVerticalAlignment == EasyText.EasyVerticalAlignment.BOTTOM){
           val verticalSpace = drawSettings.verticalSpaceAvailable//Space in the page
           val realFree = verticalSpace - height// Space after of drawing
           c?.translate(0f, realFree)
           heightTotal += realFree
       }
   }
   private fun drawInLayout(c: Canvas?, layout: Layout, drawSettings: DrawSettings):Canvas?{
        var height = layout.height.toFloat()

        layout.draw(c)
        c?.translate(0f, height)
        heightTotal += height
        drawSettings.verticalSpaceAvailable -= height

        return c
    }
    internal fun generateLayout(spannedString:SpannableString, textPaint:TextPaint, drawSettings: DrawSettings, start:Int = 0, end:Int = spannedString.length):Layout{
       // var charWidht = StaticLayout.getDesiredWidth("M", textPaint)
        var tempLayout = getLayout(spannedString,textPaint,drawSettings,start,end)

        if(this.mainStyle.textAlignment == EasyText.EasyAlignment.JUSTIFY && tempLayout.lineCount>1){
            val linesCount = tempLayout.lineCount
            val textViewWidth = tempLayout.width
            val builder = SpannableStringBuilder()
            for(i in 0..linesCount){
                val lineStart = tempLayout.getLineStart(i)
                val lineEnd = tempLayout.getLineEnd(i)
                var lineString = spannedString.subSequence(lineStart, lineEnd) as SpannableString
                if(i == (linesCount-1)){
                    builder.append(lineString)
                    break
                }
                //val trimSpaceText = lineString.trim()
                val removeSpaceText = lineString.replaceAll(" ", "")

                val removeSpaceWidth = textPaint.measureText(removeSpaceText.toString())
                val spaceCount = (lineString.length - removeSpaceText.length).toFloat()
                val spanceToFill = textViewWidth - removeSpaceWidth

                var widthUsed = 0f
                var spaceDo = 0f
                val spannableString = lineString
                for (j in 0 until lineString.length) {
                    val c = lineString[j]
                    if (c == ' ') {
                        val available = spanceToFill-widthUsed;
                        var restantes = 1f
                        if((spaceCount - spaceDo)>0){
                            restantes = spaceCount - spaceDo
                        }
                        var eachSpaceWidth = available / restantes
                       // Log.e("eachSpaceWidth", eachSpaceWidth.toString());
                        var blankSpace = BlankSpace(eachSpaceWidth)

                        widthUsed += blankSpace.getUtilizedWidth()
                        spaceDo++
                        val span = ImageSpan(blankSpace)
                        spannableString.setSpan(span, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    }
                }
                builder.append(spannableString)
            }
            var justifySpannable = processText(SpannableString.valueOf(builder), true, start, end)//re apply styles

            return getLayout(justifySpannable,textPaint,drawSettings,0, justifySpannable.length)

        }else{
            return tempLayout
        }

    }

    internal fun getLayout(spannedString:SpannableString, textPaint:TextPaint, drawSettings: DrawSettings, start:Int = 0, end:Int = spannedString.length):StaticLayout{
        var alignment = getTextAlign(this.mainStyle.textAlignment)

        return  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var builder = StaticLayout.Builder.obtain(spannedString, start, end,textPaint,  (drawSettings.width-drawSettings.margins.horizontalLess()).toInt())
            builder.setAlignment(alignment)
            builder.setLineSpacing(0f,1.0f)
            builder.setIncludePad(false)

            builder.build()


        } else {
            StaticLayout(spannedString, start, end, textPaint, (drawSettings.width-drawSettings.margins.horizontalLess()).toInt(), alignment,
                    1.0f, 0f, false)
        }
    }

companion object {
    fun getTextAlign(align:EasyText.EasyAlignment?):Layout.Alignment{
        return when(align){

            EasyText.EasyAlignment.CENTER -> Layout.Alignment.ALIGN_CENTER
            EasyText.EasyAlignment.NORMAL -> Layout.Alignment.ALIGN_NORMAL
            EasyText.EasyAlignment.LEFT -> Layout.Alignment.ALIGN_LEFT
            EasyText.EasyAlignment.RIGHT -> Layout.Alignment.ALIGN_RIGHT

            else -> {
                Layout.Alignment.ALIGN_NORMAL
            }
        }
    }
    fun getTextAlign2(align:EasyText.EasyAlignment?):Int{
        return when(align){

            EasyText.EasyAlignment.CENTER -> View.TEXT_ALIGNMENT_CENTER
            EasyText.EasyAlignment.NORMAL -> View.TEXT_ALIGNMENT_TEXT_START
            EasyText.EasyAlignment.LEFT -> View.TEXT_ALIGNMENT_TEXT_START
            EasyText.EasyAlignment.RIGHT -> View.TEXT_ALIGNMENT_TEXT_END

            else -> {
                View.TEXT_ALIGNMENT_TEXT_START
            }
        }
    }
}


}