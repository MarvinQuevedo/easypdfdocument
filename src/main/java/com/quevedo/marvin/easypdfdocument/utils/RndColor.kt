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

package com.quevedo.marvin.easypdfdocument.utils

import android.graphics.Color

object RndColor {
    var colors : IntArray = intArrayOf(Color.RED, Color.BLACK, Color.BLUE, Color.YELLOW, Color.MAGENTA, Color.CYAN, Color.LTGRAY)
    val len = colors.size
    var i = 0;
    fun getColor():Int{
        i++
        if(i >= len){
            i = 0
        }
        return colors[i]
    }
}