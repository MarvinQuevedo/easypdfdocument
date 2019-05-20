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

package com.quevedo.marvin.easypdfdocument.settigns

import org.json.JSONObject


class EasyPadding(var paddingTop:Float, var paddingRight:Float, var paddingBottom:Float, var paddingLeft:Float):EasyPageMargin(paddingTop, paddingRight,paddingBottom,paddingLeft){
    fun rightPaddings(lines:Int): IntArray {
        val arr = IntArray(lines)
        for (i in 0 until lines) {
            arr[i] = paddingRight.toInt()
        }
        return arr
    }
    fun leftPaddings(lines:Int): IntArray {
        val arr = IntArray(lines)
        for (i in 0 until lines) {
            arr[i] = paddingLeft.toInt()
        }
        return arr;
    }
    override fun horizontalLess():Float{
        return paddingRight + paddingLeft
    }
    fun verticalLess():Float{
        return paddingTop + paddingBottom
    }
    override fun copy():EasyPadding{
        return EasyPadding(paddingTop, paddingRight,paddingBottom,paddingLeft)
    }
    override fun toJson():JSONObject{
        val json = JSONObject()
        if(!isEmpty()){
            json.put(PADDING_TOP, paddingTop)
            json.put(PADDING_LEFT, paddingLeft)
            json.put(PADDING_RIGTH, paddingRight)
            json.put(PADDING_BOTTOM, paddingBottom)
        }
        return json
    }
    override fun isEmpty():Boolean{
        return paddingTop == 0f
                && paddingBottom == 0f
                && paddingLeft == 0f
                && paddingRight == 0f
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EasyPadding) return false

        if (paddingTop != other.paddingTop) return false
        if (paddingRight != other.paddingRight) return false
        if (paddingBottom != other.paddingBottom) return false
        if (paddingLeft != other.paddingLeft) return false

        return true
    }

    override fun hashCode(): Int {
        var result = paddingTop.hashCode()
        result = 31 * result + paddingRight.hashCode()
        result = 31 * result + paddingBottom.hashCode()
        result = 31 * result + paddingLeft.hashCode()
        return result
    }

    companion object {
        val NO_PADDING= EasyPadding(0f,0f,0f,0f)
        val STRECH = EasyPadding(2f,2f,2f,2f)
        val DEFAULT = EasyPadding(40f,25f,20f,25f)
        const val PADDING_TOP = "pT"
        const val PADDING_LEFT = "pL"
        const val PADDING_RIGTH = "pR"
        const val PADDING_BOTTOM = "pB"
        fun ofJson(json:JSONObject):EasyPadding{
            if(json.length() == 0) return NO_PADDING

            val top = json.getDouble(PADDING_TOP).toFloat()
            val bottom = json.getDouble(PADDING_BOTTOM).toFloat()
            val left = json.getDouble(PADDING_LEFT).toFloat()
            val right = json.getDouble(PADDING_RIGTH).toFloat()
            return EasyPadding(top,right, bottom, left)
        }
    }
}

