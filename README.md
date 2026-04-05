# 🧠 Java Code Runner Sandbox

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-green)
![Docker](https://img.shields.io/badge/Docker-Sandbox-blue)

--------------------------------------------------

## Overview

A secure Java code execution sandbox built with Spring Boot and Docker.

This system executes user-submitted code in an isolated container environment
with strict resource limits, preventing any impact on the host system.

--------------------------------------------------

## Features

- Dynamic Java code execution
- Docker-based isolation
- Separate compile & run phases
- STDIN / STDOUT / STDERR handling
- Timeout protection
- Automatic workspace cleanup
- CPU & Memory limits
- Network disabled sandbox

--------------------------------------------------

## Architecture

- Client  
  ↓  
- Spring Boot API  
  ↓  
- Service Layer  
  ↓  
- Workspace (temp directory)  
  ↓  
- Docker Container  
  - Compile (javac)  
  - Run (java)  
  ↓  
- Response

--------------------------------------------------

## API

POST /api/code/run

Request:
```json
{
  "className": "Main",
  "sourceCode": "public class Main { public static void main(String[] args) { System.out.println(\"Test Message\"); } }",
  "stdin": ""
}
```

Response:
```json
{
  "status": "SUCCESS",
  "stdout": "Test Message",
  "stderr": "",
  "exitCode": 0
}
```
--------------------------------------------------

## Sandbox Security

- Container isolation
- Separate JVM for user code
- CPU limit (--cpus=1)
- Memory limit (--memory=256m)
- Network disabled (--network=none)
- Read-only filesystem
- No privilege escalation
- Process limit protection

--------------------------------------------------

## Key Concepts

- Sandbox architecture
- Process & container isolation
- Resource management
- Secure execution of untrusted code
- JVM vs container separation
- Compile vs runtime error handling

--------------------------------------------------

## Future Improvements

- Queue / worker architecture
- Output size restriction
- Multi-language support
- Execution metrics
- Rate limiting
- Container reuse optimization

--------------------------------------------------

## Conclusion

This project demonstrates how to build a secure, isolated, and controlled
code execution system using Docker and Java.

Focus areas:
- Untrusted code execution
- Resource control
- System isolation
- Backend system design
