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

package com.quevedo.marvin.easypdfdocument.extension

import android.text.SpannableString
import android.text.SpannableStringBuilder
import java.util.ArrayList

fun String.getIndexsOf(string:String): ArrayList<Int> {
    var lastPos = 0
    val strLen = string.length;
    var positions = ArrayList<Int>()
    do {
        lastPos = this.indexOf(string,lastPos+1);
        if(lastPos>=0){
            positions.add(lastPos)
        }
    }while (lastPos>=0)

   return positions;
}
fun CharSequence.replaceAll(filter: String, other:String):CharSequence{
    var ssb =  SpannableStringBuilder()
    ssb = ssb.append(this, 0, this.length)
    ssb = ssb.replaceAll(filter, other)
    return SpannableString.valueOf(ssb)
}

fun SpannableStringBuilder.replaceAll(filter:String, other:String):SpannableStringBuilder{
    var copy = this;
    val fLen = filter.length
    var i:Int
    do{
        i = copy.indexOf(filter)
        if(i>=0){
            copy = copy.replace(i, i+fLen, other)
        }
    }while ((i>=0))
    return copy;
}
