package com.chistadata.authorizationframework.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class AfcasAccessor {
    private Process process;
    private BufferedReader inputReader;
    private BufferedReader errorReader;
    private PrintWriter outputWriter;
    private Thread outputThread;
    private Thread outputErrorThread;
    private BlockingQueue<String> outputQueue;
    private BlockingQueue<String> errorQueue;

    public AfcasAccessor() {
        try {
            this.startProcess("afcas -h localhost -p 5432 -U postgres -W 123456");
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void startProcess(String command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
        process = processBuilder.start();

        // Input stream reader
        InputStream inputStream = process.getInputStream();
        inputReader = new BufferedReader(new InputStreamReader(inputStream));

        // Error stream reader
        InputStream errorStream = process.getErrorStream();
        errorReader = new BufferedReader(new InputStreamReader(errorStream));

        // Output stream writer
        OutputStream outputStream = process.getOutputStream();
        outputWriter = new PrintWriter(outputStream);

        // Output reader thread
        outputQueue = new LinkedBlockingQueue<>();

        outputThread = new Thread(() -> {
            String line;
            try {
                while ((line = inputReader.readLine()) != null) {
                    outputQueue.offer(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        outputThread.start();

        // Output error reader thread
        errorQueue = new LinkedBlockingQueue<>();
        outputErrorThread = new Thread(() -> {
            String line;
            try {
                while ((line = errorReader.readLine()) != null) {
                    errorQueue.offer(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        outputErrorThread.start();

        process.waitFor(1000, TimeUnit.MILLISECONDS);

        String output = readFromOutputQueue();
        if (!output.contains("Database connected successfully")) {
            throw new RuntimeException("Unable to connect to afcas database");
        }
    }

    public String executeCommand(String command) {
        try {
            clearOutputQueue();
            clearErrorQueue();
            outputWriter.println(command);
            outputWriter.flush();
            process.waitFor(1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String output = readFromOutputQueue();

        if (output.isEmpty()) {
            String error = readFromErrorQueue();
            String[] tokens = error.split("\n", 2);
            return tokens[0];
        }

        return output;
    }

    private void clearOutputQueue() throws InterruptedException {
        while (outputQueue.size() != 0) {
            outputQueue.take();
        }
    }

    private void clearErrorQueue() throws InterruptedException {
        while (errorQueue.size() != 0) {
            errorQueue.take();
        }
    }

    private String readFromOutputQueue() {
        StringBuilder outputBuilder = new StringBuilder();
        try {
            while (true) {
                if (outputQueue.size() == 0) {
                    break;
                }
                String line = outputQueue.take();
                if (line.isEmpty()) {
                    break;
                }
                outputBuilder.append(line).append(System.lineSeparator());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = outputBuilder.toString();
        System.out.println(result);
        return result;
    }

    private String readFromErrorQueue() {
        StringBuilder outputBuilder = new StringBuilder();
        try {
            while (true) {
                if (errorQueue.size() == 0) {
                    break;
                }
                String line = errorQueue.take();
                if (line.isEmpty()) {
                    break;
                }
                outputBuilder.append(line).append(System.lineSeparator());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = outputBuilder.toString();
        System.out.println(result);
        return result;
    }

    public void stopProcess() {
        try {
            inputReader.close();
            outputWriter.close();
            process.destroy();
            outputThread.join();
            outputErrorThread.join();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
