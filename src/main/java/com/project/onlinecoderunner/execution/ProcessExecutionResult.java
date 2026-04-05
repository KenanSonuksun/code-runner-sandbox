package com.project.onlinecoderunner.execution;

public class ProcessExecutionResult {

    private final int exitCode;
    private final String stdout;
    private final String stderr;
    private final boolean timedOut;

    public ProcessExecutionResult(int exitCode, String stdout, String stderr, boolean timedOut) {
        this.exitCode = exitCode;
        this.stdout = stdout;
        this.stderr = stderr;
        this.timedOut = timedOut;
    }

    public String getStdout() {
        return stdout;
    }

    public int getExitCode() {
        return exitCode;
    }

    public String getStderr() {
        return stderr;
    }

    public boolean isTimedOut() {
        return timedOut;
    }
}
