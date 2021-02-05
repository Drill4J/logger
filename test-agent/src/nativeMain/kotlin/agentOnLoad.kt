/**
 * Copyright 2020 EPAM Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@file:Suppress("unused", "FunctionName")

package test

import com.epam.drill.jvmapi.gen.*
import com.epam.drill.kni.*

object Agent : JvmtiAgent {
    override fun agentOnLoad(options: String): Int {
        println("agentOnLoad")
        return JNI_OK
    }

    override fun agentOnUnload() {
        println("agentOnUnload")
    }

}
