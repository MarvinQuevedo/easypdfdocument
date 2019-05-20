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

class EasyPageSize(val width:Int, val heigth:Int) {

    companion object {
        val A4 = EasyPageSize(595, 842)
        val A4_LANDSCAPE = EasyPageSize(595, 842)
        val LETTER = EasyPageSize(612, 792)
        val LETTER_LANDSCAPE = EasyPageSize(792,612)
    }
}