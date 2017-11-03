package docker4dummies

import com.spotify.docker.client.messages.ContainerConfig

class NamedContainer(val name: String, val config: ContainerConfig)