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

import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient
import com.spotify.docker.client.DockerClient.ListContainersParam.allContainers
import com.spotify.docker.client.DockerClient.ListContainersParam.filter
import com.spotify.docker.client.DockerClient.ListImagesParam.byName
import com.spotify.docker.client.DockerClient.LogsParam.stderr
import com.spotify.docker.client.DockerClient.LogsParam.stdout
import com.spotify.docker.client.messages.ContainerConfig
import org.apache.commons.lang3.Validate
import org.awaitility.Awaitility.await

class DockerTemplate(val client: DockerClient = DefaultDockerClient.fromEnv().build()) {

    fun close() {
        client.close()
    }

    fun execute(container: ContainerConfig): List<String> {
        Validate.notBlank(container.image(), "Container name cannot be empty.")

        val imageExists = !client.listImages(byName(container.image())).isEmpty()
        if (!imageExists) {
            client.pull(container.image())
        }
        val containerId = client.createContainer(container).id()
        client.startContainer(containerId)
        await().until {
            !client.inspectContainer(containerId).state().running()
        }
        return client.logs(containerId, stdout(), stderr()).readFully().split("\n")
    }

    fun ensureIsRunning(container: NamedContainer) {
        val image = container.config.image()
        Validate.notBlank(image, "Container image cannot be empty.")

        val name = container.name
        val isRunning = !client.listContainers(filter("name", name)).isEmpty()
        if (!isRunning) {
            val containers = client.listContainers(allContainers(true), filter("name", name))
            val isCreated = !containers.isEmpty()
            val containerId = when {
                isCreated -> containers.first().id()
                else -> {
                    val imageExists = !client.listImages(byName(image)).isEmpty()
                    if (!imageExists) {
                        client.pull(image)
                    }
                    client.createContainer(container.config, name).id()
                }
            }
            client.startContainer(containerId)
        }
        if(container.readinessProbe != null) {
           await().until(container.readinessProbe)
        }
    }

}