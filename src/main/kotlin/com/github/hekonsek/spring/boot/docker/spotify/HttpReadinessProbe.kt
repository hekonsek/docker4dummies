/**
 * Licensed to the spring-boot-docker-spotify under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.hekonsek.spring.boot.docker.spotify

import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.Callable

class HttpReadinessProbe(val url : String) : Callable<Boolean> {

    private val http = OkHttpClient()

    override fun call(): Boolean {
        try {
            val request = Request.Builder().get().url(url).build()
            http.newCall(request).execute().use { response ->
                return response.code() == 200
            }
        } catch (e : Exception) {
            return false
        }
    }

}