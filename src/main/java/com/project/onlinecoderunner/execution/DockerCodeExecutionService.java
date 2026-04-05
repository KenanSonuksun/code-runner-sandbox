package com.project.onlinecoderunner.execution;

import com.project.onlinecoderunner.workspace.WorkspaceContext;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DockerCodeExecutionService {

    private final LocalProcessExecutor processExecutor;

    public DockerCodeExecutionService(LocalProcessExecutor processExecutor){
        this.processExecutor = processExecutor;
    }

    public ProcessExecutionResult compile(WorkspaceContext workspaceContext,String className) {
        String hostPath = workspaceContext.getWorkspaceDir().toAbsolutePath().toString();
        String containerPath = "/workspace";
        String command = "javac " + containerPath + "/" + className + ".java" ;

        List<String> dockerCommand = List.of(
                "docker", "run",
                "--rm",
                "--memory=256m",
                "--cpus=1",
                "--network=none",
                "--read-only",
                "--tmpfs", "/tmp:rw,size=64m",
                "--pids-limit=64",
                "--security-opt=no-new-privileges",
                "--cap-drop=ALL",
                "-v", hostPath + ":" + containerPath,
                "eclipse-temurin:21-jdk",
                "sh", "-c", command
        );

        return processExecutor.execute(dockerCommand, java.time.Duration.ofSeconds(5),"");
    }


    public ProcessExecutionResult run(WorkspaceContext workspaceContext,String className, String stdin){
        String hostPath = workspaceContext.getWorkspaceDir().toAbsolutePath().toString();
        String containerPath = "/workspace";
        String command = "java -Xms32m -Xmx128m -cp " + containerPath + " " + className;

        List<String> dockerCommand = List.of(
                "docker", "run",
                "--rm",
                "-i",
                "--memory=256m",
                "--cpus=1",
                "--network=none",
                "--read-only",
                "--tmpfs", "/tmp:rw,size=64m",
                "--pids-limit=64",
                "--security-opt=no-new-privileges",
                "--cap-drop=ALL",
                "-v", hostPath + ":" + containerPath,
                "eclipse-temurin:21-jdk",
                "sh", "-c", command
        );

        return processExecutor.execute(dockerCommand, java.time.Duration.ofSeconds(5), stdin);
    }
}
