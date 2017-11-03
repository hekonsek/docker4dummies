# Spring Cloud Docker Spotify

[![Version](https://img.shields.io/badge/spring-boot-docker-spotify-0.1-blue.svg)](https://github.com/hekonsek/spring-boot-docker-spotify/releases)

**Spring Cloud Docker Spotify** provides template around [Spotify Docker client](https://github.com/spotify/docker-client) simplifying common
operations over Docker containers.

## Usage

Executing Docker container and returning stdout+stderr as a list of Strings:

```
@Autowired
DockerTemplate dockerTemplate;
...
ContainerConfig container = ContainerConfig.builder().image("fedora:26").cmd("echo", "foo").build();
List<String> output = dockerTemplate.execute(container);
```