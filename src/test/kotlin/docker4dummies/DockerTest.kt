package docker4dummies

import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient.ListContainersParam.filter
import com.spotify.docker.client.messages.ContainerConfig
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.UUID.randomUUID

class DockerTest {

    val client = DefaultDockerClient.fromEnv().build()

    val docker = Docker()

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

    @Test
    fun shouldStartContainer() {
        // When
        docker.ensureIsRunning(containerName, container)

        // Then
        val containers = client.listContainers(filter("name", containerName))
        assertThat(containers).hasSize(1)
    }

    @Test
    fun shouldRestartContainer() {
        // Given
        docker.ensureIsRunning(containerName, container)
        client.stopContainer(containerName, 2)

        // When
        docker.ensureIsRunning(containerName, container)

        // Then
        val containers = client.listContainers(filter("name", containerName))
        assertThat(containers).hasSize(1)
    }

}