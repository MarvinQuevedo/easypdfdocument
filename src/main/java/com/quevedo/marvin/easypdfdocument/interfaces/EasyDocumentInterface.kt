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

package com.quevedo.marvin.easypdfdocument.interfaces

import android.graphics.Canvas
import com.quevedo.marvin.easypdfdocument.EasyPdfDocument
import java.lang.Exception

interface EasyDocumentInterface {
    fun requestNewPage():Canvas?
}
class EmptyEasyDocumentInterface:EasyDocumentInterface {
    override fun requestNewPage(): Canvas? {
        throw Exception("You cant not use a EasyDocumentInterface, you need a implementation")
    }
}