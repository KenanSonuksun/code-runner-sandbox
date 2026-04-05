package com.project.onlinecoderunner.controller;

import com.project.onlinecoderunner.model.CodeRunRequest;
import com.project.onlinecoderunner.model.CodeRunResponse;
import com.project.onlinecoderunner.model.ExecutionStatus;
import com.project.onlinecoderunner.service.CodeRunnerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/code")
public class CodeRunnerController {

    private final CodeRunnerService codeRunnerService;

    public CodeRunnerController(CodeRunnerService codeRunnerService) {
        this.codeRunnerService = codeRunnerService;
    }


    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/run")
    public CodeRunResponse run(@RequestBody CodeRunRequest request) {
        return codeRunnerService.run(request);
    }

}
