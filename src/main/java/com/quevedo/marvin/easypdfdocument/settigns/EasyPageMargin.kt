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


open class EasyPageMargin(open var marginTop:Float, open var marginRight:Float, open var marginBottom:Float, open var marginLeft:Float){

    fun rightMargins(lines:Int): IntArray {
        val arr = IntArray(lines)
        for (i in 0 until lines) {
            arr[i] = marginRight.toInt();
        }
        return arr
    }
    fun leftMargins(lines:Int): IntArray {
        val arr = IntArray(lines)
        for (i in 0 until lines) {
            arr[i] = marginLeft.toInt()
        }
        return arr;
    }
    open fun copy():EasyPageMargin{
        return EasyPageMargin(marginTop, marginRight, marginBottom, marginLeft)
    }
    open fun horizontalLess(): Float {
        return marginRight + marginLeft
    }
    open fun toJson(): JSONObject {
        val json = JSONObject()
        if(!isEmpty()){
            json.put(MARGIN_TOP, marginTop)
            json.put(MARGIN_LEFT, marginLeft)
            json.put(MARGIN_RIGTH, marginRight)
            json.put(MARGIN_BOTTOM, marginBottom)
        }
        return json
    }
    open fun isEmpty():Boolean{
        return marginTop == 0f
                && marginBottom == 0f
                && marginLeft == 0f
                && marginRight == 0f
    }



    companion object {
        val NO_MARGIN = EasyPageMargin(0f,0f,0f,0f)
        val STRECH = EasyPageMargin(2f,2f,2f,2f)
        val DEFAULT = EasyPageMargin(40f,25f,20f,25f)

        const val MARGIN_TOP = "mT"
        const val MARGIN_LEFT = "mL"
        const val MARGIN_RIGTH = "mR"
        const val MARGIN_BOTTOM = "mB"
        fun ofJson(json:JSONObject):EasyPageMargin{
            if(json.length()==0){
                return EasyPageMargin.NO_MARGIN
            }
            val top = json.getDouble(MARGIN_TOP).toFloat()
            val bottom = json.getDouble(MARGIN_BOTTOM).toFloat()
            val left = json.getDouble(MARGIN_LEFT).toFloat()
            val right = json.getDouble(MARGIN_RIGTH).toFloat()
            return EasyPageMargin(top,right, bottom, left)
        }
    }
}
