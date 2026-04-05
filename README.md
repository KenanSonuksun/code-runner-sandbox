# 🧠 Java Code Runner Sandbox

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3-green)
![Docker](https://img.shields.io/badge/Docker-Sandbox-blue)
![Status](https://img.shields.io/badge/status-learning--project-orange)

## 📌 Overview

A **secure Java code execution sandbox** built with Spring Boot and Docker.

It executes user-submitted code in an **isolated container environment** with strict resource limits to safely handle untrusted code.

---

## Features

- Dynamic Java code execution
- Docker-based isolation
- Separate **compile & run phases**
- STDIN / STDOUT / STDERR support
- Timeout protection
- Automatic cleanup
- Resource limits (CPU & Memory)
- Network disabled sandbox

---

## Architecture

Client
↓
Spring Boot API
↓
Service Layer
↓
Workspace (temp dir)
↓
Docker Container
→ Compile (javac)
→ Run (java)
↓
Response

---

## API

### POST `/api/code/run`

```json
{
  "className": "Main",
  "sourceCode": "...",
  "stdin": ""
}
```

* Sandbox Security
	•	Container isolation
	•	--memory & --cpus limits
	•	--network=none
	•	Read-only filesystem
	•	Limited processes
	•	No privilege escalation

⸻

* Key Concepts
	•	Sandbox architecture
	•	Process & container isolation
	•	Resource control
	•	Secure code execution
	•	JVM vs container separation

⸻

* Limitations
	•	No queue system
	•	No output size limit
	•	Not production-ready

⸻

* Future Improvements
	•	Worker/queue system
	•	Multi-language support
	•	Execution metrics
	•	Rate limiting

⸻
