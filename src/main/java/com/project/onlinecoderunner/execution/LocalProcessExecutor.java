package com.project.onlinecoderunner.execution;

import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Component
public class LocalProcessExecutor {

    public ProcessExecutionResult execute(List<String> command, Duration timeout,String stdin){

        Process process = null;
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        try{
            ProcessBuilder processBuilder = new ProcessBuilder(command);
            process = processBuilder.start();

            Process finalProcess = process;

            Future<?> stdinFuture = executorService.submit(() -> writeStdin(finalProcess.getOutputStream(), stdin));
            Future<String> stdoutFuture = executorService.submit(() -> readStream(finalProcess.getInputStream()));
            Future<String> stderrFuture = executorService.submit(() -> readStream(finalProcess.getErrorStream()));

            boolean finished = process.waitFor(timeout.toMillis(), TimeUnit.MILLISECONDS);

            if (!finished) {
                process.destroyForcibly();
                return new ProcessExecutionResult(-1, "", "Process timed out.", true);
            }

            stdinFuture.get(3, TimeUnit.SECONDS);
            String stdout = stdoutFuture.get(3, TimeUnit.SECONDS);
            String stderr = stderrFuture.get(3, TimeUnit.SECONDS);

            return new ProcessExecutionResult(process.exitValue(), stdout, stderr, false);


        } catch (Exception e) {
            throw new RuntimeException("Failed to execute process",e);
        }finally {
            executorService.shutdownNow();
        }
    }

    private void writeStdin(OutputStream outputStream, String stdin) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8))) {
            if (stdin != null) {
                writer.write(stdin);
            }
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write stdin.", e);
        }
    }

    private String readStream(InputStream inputStream) throws IOException {
        StringBuilder sb = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
