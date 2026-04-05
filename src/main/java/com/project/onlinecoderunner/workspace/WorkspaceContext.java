package com.project.onlinecoderunner.workspace;

import java.nio.file.Path;

public class WorkspaceContext {

    private final Path workspaceDir;
    private final Path sourceFile;

    public WorkspaceContext(Path workspaceDir, Path sourceFile){
        this.workspaceDir = workspaceDir;
        this.sourceFile = sourceFile;
    }

    public Path getWorkspaceDir() {
        return workspaceDir;
    }

    public Path getSourceFile() {
        return sourceFile;
    }
}
