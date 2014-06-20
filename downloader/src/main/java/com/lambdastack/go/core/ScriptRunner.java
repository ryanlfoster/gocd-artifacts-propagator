package com.lambdastack.go.core;

import com.lambdastack.go.exceptions.ScriptRunnerException;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScriptRunner {
    public static boolean execute(String workingDir, String script, List<String> args) throws Exception {
        List<String> cmdList = new ArrayList<String>();
        cmdList.addAll(Arrays.asList("/bin/bash", script));
        cmdList.addAll(args);
        ProcessBuilder processBuilder = new ProcessBuilder(cmdList);
        processBuilder.directory(new File(workingDir));
        Process process = processBuilder.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;

        System.out.println("Running script " + script + " in " + workingDir);
        while ((line = br.readLine()) != null) {
            DependencyResolver.logMessage(line);
        }
        process.waitFor();
        if(process.exitValue() != 0) {
            throw new ScriptRunnerException("Script " + script + " has an exit status : " + process.exitValue());
        }
        return true;
    }
}
