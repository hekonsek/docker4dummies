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
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.test.context.junit4.SpringRunner
import java.util.UUID.randomUUID

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(SpotifyDockerAutoConfiguration::class, DockerTemplateSpringTest::class))
open class DockerTemplateSpringTest {

    companion object {
        val containerName = "container-${randomUUID()}"
    }

    @Autowired
    lateinit var dockerTemplate: DockerTemplate

    @Test
    fun shouldStartSleeperContainer() {
        val sleeperContainer = dockerTemplate.client.listContainers(filter("name", containerName)).first()
        assertThat(sleeperContainer).isNotNull()
    }

    @Bean
    open fun topContainer(): NamedContainer =
            NamedContainer(containerName, ContainerConfig.builder().image("fedora:26").cmd("sleep", "60").build())

}