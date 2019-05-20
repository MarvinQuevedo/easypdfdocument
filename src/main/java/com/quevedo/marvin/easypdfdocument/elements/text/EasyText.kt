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

package com.quevedo.marvin.easypdfdocument.elements.text

import android.graphics.Color
import com.quevedo.marvin.easypdfdocument.settigns.EasyPadding

open class EasyText(open var text:String) {
    enum class ScriptStyle(val style:String){
        UPPER("upper"),UNDER("under"), NONE("none")
    }
    enum class EasyAlignment(val align:String){
        CENTER("center"),NORMAL("normal"), LEFT("left"),RIGHT("right"),JUSTIFY("justify")

    }
    enum class EasyVerticalAlignment(val align:String){
        TOP("top"),MIDDLE("middle"), BOTTOM("bottom")
    }

    var bold = false
        internal set
    var start = 0
        internal set
    var end = text.length
        internal set
    var italic = false
        internal set
    var underline = false
        internal set
    var tached = false
        internal set
    var fontSize = 12
        internal set
    var strike = false
        internal set
    var foreColor = Color.BLACK
        internal set
    var backColor = Color.TRANSPARENT
        internal set
    var scriptStyle: ScriptStyle = ScriptStyle.NONE
        internal set
    var textAlignment: EasyAlignment = EasyAlignment.NORMAL
        internal set
    var textVerticalAlignment = EasyVerticalAlignment.TOP
         internal set
    var padding = EasyPadding.NO_PADDING.copy()
        internal set
    var paragraphBackColor = Color.TRANSPARENT
       internal set
    fun setParagraphBackColor(color:Int): EasyText {
        this.paragraphBackColor = color
        return this
    }
    fun setPadding(padding: EasyPadding): EasyText {
        this.padding = padding
        return this
    }
    fun setPadding(left:Float = padding.paddingLeft, rigth:Float = padding.paddingRight, top:Float = padding.paddingTop, bottom:Float = padding.paddingBottom ): EasyText {
        this.padding.paddingLeft  = left
        this.padding.paddingRight = rigth
        this.padding.paddingTop = top
        this.padding.paddingBottom = bottom
        return this
    }
    fun setBold(bold:Boolean): EasyText {
        this.bold = bold
        return this
    }
    fun setItalic(italic:Boolean): EasyText {
        this.italic = italic
        return this
    }
    fun setUnderline(underline:Boolean): EasyText
    {
        this.underline = underline
        return this
    }
    fun setTached(tached:Boolean): EasyText {
        this.tached = tached
        return this
    }
    fun setFontSize(fontSize:Int): EasyText {
        this.fontSize = fontSize
        return this
    }
    fun setStrike(strike:Boolean): EasyText {
        this.strike = strike
        return this
    }
    fun setVerticalAlignment(alignment: EasyVerticalAlignment): EasyText {
        this.textVerticalAlignment = alignment
        return this
    }

    fun setForeColor(alpha: Int, red: Int, green: Int, blue: Int): EasyText {
       this.foreColor = Color.argb(alpha,red,green,blue)
        return this
    }
    fun setForeColor(color: Int): EasyText {
        this.foreColor = color
        return this
    }
    fun setBackColor(alpha: Int,red: Int,green: Int,blue: Int): EasyText {
        this.backColor = Color.argb(alpha,red,green,blue)
        return this
    }
    fun setScriptStyle(scriptStyle:ScriptStyle):EasyText{
        this.scriptStyle = scriptStyle
        return this
    }
    fun setBackColor(color: Int): EasyText {
        this.backColor = color
        return this
    }

    open fun setPosition(pos:Int): EasyText {
        start += pos
        end += pos
        return this
    }

    fun setTextAlignment(alignment: EasyAlignment): EasyText {
        this.textAlignment = alignment
        return this
    }


    fun toEasyStyle(): EasyStyle {
        var style = EasyStyle()
        style.bold = this.bold

        style.start = this.start

        style.end = this.end

        style.italic = this.italic

        style.underline = this.underline

        style.tached = this.tached

        style.fontSize = this.fontSize

        style.strike = this.strike

        style.foreColor = this.foreColor
        style.backColor = this.backColor
        style.scriptStyle = this.scriptStyle
        style.textAlignment = this.textAlignment
        style.textVerticalAlignment = this.textVerticalAlignment
        style.padding = this.padding
        style.paragraphBackColor = this.paragraphBackColor

        return style
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EasyText) return false

        if (text != other.text) return false
        if (bold != other.bold) return false
        if (start != other.start) return false
        if (end != other.end) return false
        if (italic != other.italic) return false
        if (underline != other.underline) return false
        if (tached != other.tached) return false
        if (fontSize != other.fontSize) return false
        if (strike != other.strike) return false
        if (foreColor != other.foreColor) return false
        if (backColor != other.backColor) return false
        if (scriptStyle != other.scriptStyle) return false
        if (textAlignment != other.textAlignment) return false
        if (textVerticalAlignment != other.textVerticalAlignment) return false
        if (padding != other.padding) return false
        if (paragraphBackColor != other.paragraphBackColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + bold.hashCode()
        result = 31 * result + start
        result = 31 * result + end
        result = 31 * result + italic.hashCode()
        result = 31 * result + underline.hashCode()
        result = 31 * result + tached.hashCode()
        result = 31 * result + fontSize
        result = 31 * result + strike.hashCode()
        result = 31 * result + foreColor
        result = 31 * result + backColor
        result = 31 * result + scriptStyle.hashCode()
        result = 31 * result + textAlignment.hashCode()
        result = 31 * result + textVerticalAlignment.hashCode()
        result = 31 * result + padding.hashCode()
        result = 31 * result + paragraphBackColor
        return result
    }

}
class EasyStyle(): EasyText(""){
    constructor(start:Int, end:Int):this(){
        this.start = start
        this.end = end
    }
    @Deprecated("For EasyStyle it do nothing", ReplaceWith("setPosicion(start:Int, end:Int)"))
    override  fun setPosition(pos: Int): EasyText {
        return this
    }
    fun setPosition(start:Int, end:Int){
        this.start = start
        this.end = end
    }
    fun copy(): EasyStyle = toEasyStyle()

}