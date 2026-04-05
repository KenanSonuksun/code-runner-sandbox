package com.project.onlinecoderunner.service;

import com.project.onlinecoderunner.execution.DockerCodeExecutionService;
import com.project.onlinecoderunner.execution.ProcessExecutionResult;
import com.project.onlinecoderunner.model.CodeRunRequest;
import com.project.onlinecoderunner.model.CodeRunResponse;
import com.project.onlinecoderunner.model.ExecutionStatus;
import com.project.onlinecoderunner.workspace.WorkspaceContext;
import com.project.onlinecoderunner.workspace.WorkspaceService;
import org.springframework.stereotype.Service;

@Service
public class CodeRunnerService {

    private final WorkspaceService  workspaceService;
    private final DockerCodeExecutionService dockerCodeExecutionService;

    public CodeRunnerService(WorkspaceService workspaceService,DockerCodeExecutionService dockerCodeExecutionService) {
        this.workspaceService = workspaceService;
        this.dockerCodeExecutionService = dockerCodeExecutionService;
    }

    public CodeRunResponse run(CodeRunRequest request) {
        validateRequest(request);
        validateClassName(request.getClassName());

        WorkspaceContext workspaceContext = null;

        try{
            workspaceContext = workspaceService.createWorkspace(request);

            ProcessExecutionResult compileResult = dockerCodeExecutionService.compile(workspaceContext, request.getClassName());

            if (compileResult.isTimedOut()){
                return buildResponse(ExecutionStatus.TIMEOUT, "", compileResult.getStderr(), compileResult.getExitCode());
            }

            if (compileResult.getExitCode() != 0) {
                return buildResponse(
                        ExecutionStatus.COMPILE_ERROR,
                        compileResult.getStdout(),
                        compileResult.getStderr(),
                        compileResult.getExitCode()
                );
            }

            ProcessExecutionResult runResult = dockerCodeExecutionService.run(workspaceContext, request.getClassName(), request.getStdin());

            if (runResult.isTimedOut()){
                return buildResponse(ExecutionStatus.TIMEOUT, "", runResult.getStderr(), runResult.getExitCode());
            }

            if (runResult.getExitCode() != 0) {
                return buildResponse(
                        ExecutionStatus.RUNTIME_ERROR,
                        runResult.getStdout(),
                        runResult.getStderr(),
                        runResult.getExitCode()
                );
            }

            return buildResponse(
                    ExecutionStatus.SUCCESS,
                    runResult.getStdout(),
                    runResult.getStderr(),
                    runResult.getExitCode()
            );
        }finally {
            workspaceService.deleteWorkspace(workspaceContext);
        }

    }

    private CodeRunResponse buildResponse(ExecutionStatus status, String stdout, String stderr, Integer exitCode) {
        CodeRunResponse response = new CodeRunResponse();
        response.setStatus(status);
        response.setStdout(stdout);
        response.setStderr(stderr);
        response.setExitCode(exitCode);
        return response;
    }

    private void validateRequest(CodeRunRequest request){
        if (request == null){
            throw new IllegalArgumentException("Request cannot be null");
        }
        if(isBlank(request.getClassName())){
            throw new IllegalArgumentException("Class name cannot be blank");
        }
        if(isBlank(request.getSourceCode())){
            throw new IllegalArgumentException("Source code cannot be blank");
        }
        if (request.getSourceCode().length() > 100_000){
            throw new IllegalArgumentException("Source code is too long");
        }
        if (request.getStdin() == null){
            request.setStdin("");
        }
    }

    private void validateClassName(String className) {
        if (isBlank(className)) {
            throw new IllegalArgumentException("className cannot be blank.");
        }

        if (!className.matches("[A-Za-z_$][A-Za-z0-9_$]*")) {
            throw new IllegalArgumentException("className is not a valid Java class name.");
        }
    }

    private boolean isBlank(String value){
        return value == null || value.trim().isEmpty();
    }

}
