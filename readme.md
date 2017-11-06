# Spring Boot Docker Spotify

[![Version](https://img.shields.io/badge/Spring%20Boot%20Docker%20Spotify-0.4-blue.svg)](https://github.com/hekonsek/spring-boot-docker-spotify/releases)
[![Build](https://api.travis-ci.org/hekonsek/spring-boot-docker-spotify.svg)](https://travis-ci.org/hekonsek/spring-boot-docker-spotify)

**Spring Boot Docker Spotify** provides a template around [Spotify Docker client](https://github.com/spotify/docker-client) simplifying common
operations over Docker containers. As project name indicates Docker template is a first class Spring Boot citizen.

## Maven setup

I highly recommend to import spring-boot-docker-spotify BOM instead of Spring Boot BOM. Otherwise Jersey version defined in Spring Boot BOM conflicts
with Jersey version used by Spotify Docker Client. Spring-boot-docker-spotify BOM imports Spring Boot BOM on your behalf, but downgrades Jersey to
avoid classpath clashes.

```
<dependencyManagement>
  <dependencies>
    <dependency>
      <groupId>com.github.hekonsek</groupId>
      <artifactId>spring-boot-docker-spotify</artifactId>
      <version>0.4</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
</dependencyManagement>
```

Then add an actual spring-boot-docker-spotify jar into your classpath:

```
<dependencies>
  <dependency>
    <groupId>com.github.hekonsek</groupId>
    <artifactId>spring-boot-docker-spotify</artifactId>
    <version>0.4</version>
  </dependency>
<dependencies>
```

## Injecting Docker template

In order to use Docker template, just inject it into your bean of choice:

```
import com.github.hekonsek.spring.boot.docker.spotify.DockerTemplate;
...
@Autowired
DockerTemplate dockerTemplate;
```

## Docker template operations

Executing Docker container and returning stdout+stderr as a list of Strings:

```
import com.spotify.docker.client.messages.ContainerConfig;
import com.github.hekonsek.spring.boot.docker.spotify.DockerTemplate;

...

ContainerConfig containerConfig = ContainerConfig.builder().image("fedora:26").cmd("echo", "foo").build();
List<String> output = dockerTemplate.execute(containerConfig);
```

Ensuring that containerized daemon is running:

```
import com.spotify.docker.client.messages.ContainerConfig;
import com.github.hekonsek.spring.boot.docker.spotify.DockerTemplate;
import com.github.hekonsek.spring.boot.docker.spotify.NamedContainer;

...

ContainerConfig containerConfig = ContainerConfig.builder().image("mongo").build();
NamedContainer container = new NamedContainer("mongo", container, containerConfig);
dockerTemplate.ensureIsRunning(container);
```

## License

This project is distributed under Apache 2.0 license.