package com.github.hekonsek.spring.boot.docker.spotify

import com.spotify.docker.client.DefaultDockerClient
import com.spotify.docker.client.DockerClient.ListContainersParam.allContainers
import com.spotify.docker.client.DockerClient.ListContainersParam.filter
import com.spotify.docker.client.DockerClient.ListImagesParam.byName
import com.spotify.docker.client.DockerClient.LogsParam.stderr
import com.spotify.docker.client.DockerClient.LogsParam.stdout
import com.spotify.docker.client.messages.ContainerConfig
import org.awaitility.Awaitility.await

class Docker {

    val client = DefaultDockerClient.fromEnv().build()

    fun execute(container: ContainerConfig) : List<String> {
        val imageExists = !client.listImages(byName(container.image())).isEmpty()
        if(!imageExists) {
            client.pull(container.image())
        }
        val containerId = client.createContainer(container).id()
        client.startContainer(containerId)
        await().until {
            !client.inspectContainer(containerId).state().running()
        }
        return client.logs(containerId, stdout(), stderr()).readFully().split("\n")
    }

    fun ensureIsRunning(name : String, container: ContainerConfig) {
        val isRunning = !client.listContainers(filter("name", name)).isEmpty()
        if(!isRunning) {
            val containers = client.listContainers(allContainers(true), filter("name", name))
            val isCreated = !containers.isEmpty()
            val containerId : String?
            if(isCreated) {
                containerId = containers.first().id()
            } else {
                val imageExists = !client.listImages(byName(container.image())).isEmpty()
                if(!imageExists) {
                    client.pull(container.image())
                }
                containerId = client.createContainer(container, name).id()
            }
            client.startContainer(containerId)
        }
    }

}