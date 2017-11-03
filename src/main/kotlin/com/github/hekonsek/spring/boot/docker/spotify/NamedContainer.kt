package com.github.hekonsek.spring.boot.docker.spotify

import com.spotify.docker.client.messages.ContainerConfig

class NamedContainer(val name: String, val config: ContainerConfig)