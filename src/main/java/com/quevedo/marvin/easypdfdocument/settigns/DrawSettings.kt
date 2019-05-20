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

class DrawSettings(var margins: EasyPageMargin, var width: Int, var heigth:Int, var verticalSpaceAvailable:Float = 0f, var horizontalSpaceAvailable:Float =0f) {
    init {
        resetHorizontalSpaceAvailable()
        resetVerticalSpaceAvailable()
    }
    fun resetVerticalSpaceAvailable():Float{
        this.verticalSpaceAvailable = getRealVerticalSpace()
        return  this.verticalSpaceAvailable
    }
    fun resetHorizontalSpaceAvailable():Float{
        this.horizontalSpaceAvailable = getRealHorizontalSpace()
        return  this.horizontalSpaceAvailable
    }
    fun getRealVerticalSpace():Float{
        return heigth.toFloat() - (margins.marginTop + margins.marginBottom)
    }
    fun getRealHorizontalSpace():Float{
        return width.toFloat() - (margins.marginLeft + margins.marginRight)
    }
    fun copy():DrawSettings{
        return DrawSettings(margins,width,heigth,verticalSpaceAvailable,horizontalSpaceAvailable)
    }
}