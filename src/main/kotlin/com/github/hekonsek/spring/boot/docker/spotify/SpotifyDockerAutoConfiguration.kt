package com.github.hekonsek.spring.boot.docker.spotify

import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class SpotifyDockerAutoConfiguration {

    @Bean
    open fun docker(applicationContext: ApplicationContext): Docker {
        val docker = Docker()
        applicationContext.getBeansOfType(NamedContainer::class.java).values.forEach { container ->
            docker.ensureIsRunning(container.name, container.config)
        }
        return docker
    }

}