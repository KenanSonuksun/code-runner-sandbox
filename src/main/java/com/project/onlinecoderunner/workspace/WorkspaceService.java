package com.project.onlinecoderunner.workspace;

import com.project.onlinecoderunner.model.CodeRunRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;

@Service
public class WorkspaceService {

    public WorkspaceContext createWorkspace(CodeRunRequest request) {
        try {
            Path workspaceDir = Files.createTempDirectory("code-runner-");

            Path sourceFile = workspaceDir.resolve(request.getClassName() + ".java");
            Files.writeString(sourceFile, request.getSourceCode(), StandardCharsets.UTF_8);

            return new WorkspaceContext(workspaceDir, sourceFile);

        } catch (IOException e) {
            throw new RuntimeException("Failed to create workspace.", e);
        }
    }

    public void deleteWorkspace(WorkspaceContext context) {
        if (context == null || context.getWorkspaceDir() == null) {
            return;
        }

        try {
            Files.walk(context.getWorkspaceDir())
                    .sorted(Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            Files.deleteIfExists(path);
                        } catch (IOException ignored) {
                        }
                    });
        } catch (IOException ignored) {
        }
    }

}
