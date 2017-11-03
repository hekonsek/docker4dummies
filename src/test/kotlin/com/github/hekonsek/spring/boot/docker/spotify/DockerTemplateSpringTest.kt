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