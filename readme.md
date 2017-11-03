# Spring Cloud Docker Spotify

[![Version](https://img.shields.io/badge/docker4dummies-0.1-blue.svg)](https://github.com/hekonsek/docker4dummies/releases)

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