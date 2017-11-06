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

import com.spotify.docker.client.DockerClient.ListContainersParam.filter
import com.spotify.docker.client.messages.ContainerConfig
import com.spotify.docker.client.messages.HostConfig
import com.spotify.docker.client.messages.PortBinding
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.util.SocketUtils.findAvailableTcpPort
import java.util.UUID.randomUUID

class DockerTemplateTest {

    val docker = DockerTemplate()

    val containerName = randomUUID().toString()

    val container = ContainerConfig.builder().image("hekonsek/echogo").build()

    @Test
    fun shouldExecuteContainer() {
        // Given
        val container = ContainerConfig.builder().image("fedora:26").cmd("echo", "foo").build()

        // When
        val output = docker.execute(container)

        // Then
        assertThat(output).contains("foo")
    }

    @Test(expected = NullPointerException::class)
    fun shouldValidateExecutingContainerWithoutImage() {
        // Given
        val container = ContainerConfig.builder().build()

        // When
        val output = docker.execute(container)
    }

    @Test
    fun shouldStartContainer() {
        // When
        docker.ensureIsRunning(NamedContainer(containerName, container))

        // Then
        val containers = docker.client.listContainers(filter("name", containerName))
        assertThat(containers).hasSize(1)
    }

    @Test
    fun shouldRestartContainer() {
        // Given
        docker.ensureIsRunning(NamedContainer(containerName, container))
        docker.client.stopContainer(containerName, 2)

        // When
        docker.ensureIsRunning(NamedContainer(containerName, container))

        // Then
        val containers = docker.client.listContainers(filter("name", containerName))
        assertThat(containers).hasSize(1)
    }

    @Test
    fun shouldWaitForHttpProbe() {
        // Given
        val port = findAvailableTcpPort()
        val hostConfig = HostConfig.builder().portBindings(mapOf("3000/tcp" to listOf(PortBinding.create("0.0.0.0", "$port")))).build()
        val containerConfig = ContainerConfig.builder().image("grafana/grafana").hostConfig(hostConfig).exposedPorts("3000/tcp").build()
        val readinessProbe = HttpReadinessProbe("http://localhost:$port")

        // When
        docker.ensureIsRunning(NamedContainer(containerName, containerConfig, readinessProbe))

        // Then
        val ready = readinessProbe.call()
        assertThat(ready).isTrue()
    }

}