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

class EasyTitleText(text:String, bigSize: BigSize): EasyText(text) {
    enum class BigSize(val size:String){
        H1("h1"),H2("h2"),H3("h3"),H4("h4"),H5("h5"), H6("h6")
    }

    init {
        fontSize = when(bigSize){

            BigSize.H1 -> 32
            BigSize.H2 -> 24
            BigSize.H3 -> 19
            BigSize.H4 -> 16
            BigSize.H5 -> 14
            BigSize.H6 -> 13

        }
        bold = true
    }

}