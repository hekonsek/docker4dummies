package com.github.hekonsek.spring.boot.docker.spotify

import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SpotifyDockerAutoConfiguration {

    @Bean(destroyMethod = "close")
    open fun dockerClient(): DockerClient =
            DefaultDockerClient.fromEnv().build()

    @Bean(destroyMethod = "close")
    open fun docker(applicationContext: ApplicationContext, dockerClient: DockerClient): DockerTemplate {
        val docker = DockerTemplate(dockerClient)
        applicationContext.getBeansOfType(NamedContainer::class.java).values.forEach { container ->
            docker.ensureIsRunning(container.name, container.config)
        }
        return docker
    }

}