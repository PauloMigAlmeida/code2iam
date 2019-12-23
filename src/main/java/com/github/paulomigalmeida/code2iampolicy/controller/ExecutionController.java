package com.github.paulomigalmeida.code2iampolicy.controller;

import com.github.paulomigalmeida.code2iampolicy.operation.file.AWSJavaSourceFilterOperation;
import com.github.paulomigalmeida.code2iampolicy.operation.file.JavaSourceFilterOperation;
import com.github.paulomigalmeida.code2iampolicy.operation.json.JSONIAMPolicyGenerator;
import com.github.paulomigalmeida.code2iampolicy.operation.json.JSONIntermediateParserOperation;
import com.github.paulomigalmeida.code2iampolicy.operation.parser.ParseCodeOperation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class ExecutionController {

    private final Path sourceDirectory;
    private final Path awsSdkDirectory;

    public ExecutionController(Path sourceDirectory, Path awsSdkDirectory) {
        this.sourceDirectory = sourceDirectory;
        this.awsSdkDirectory = awsSdkDirectory;
    }

    public void execute() throws IOException {
        var permMap = new HashMap<String, Set<String>>();

        var awsJavaSDKHolder = new AWSJavaSourceFilterOperation(awsSdkDirectory).execute();
        var intermediateModelsMap = new JSONIntermediateParserOperation(
                awsJavaSDKHolder.getIntermediateJsonPaths()).execute();

        var pathsForSymbolResolver = new ArrayList<Path>();
        pathsForSymbolResolver.add(sourceDirectory);
        pathsForSymbolResolver.addAll(awsJavaSDKHolder.getServicePaths());

        var searchListener = new ParseCodeOperation.APIPermissionSearchListener() {
            @Override
            public void found(String signingName, String apiMethod) {
                if(permMap.containsKey(signingName)){
                    permMap.get(signingName).add(apiMethod);
                }else {
                    permMap.put(signingName, new HashSet<>(Collections.singletonList(apiMethod)));
                }
            }
        };

        var javaFiles = new JavaSourceFilterOperation(sourceDirectory).execute();
        for (var path : javaFiles) {
//            System.out.println("Parsing: " + path.toString());
            new ParseCodeOperation(pathsForSymbolResolver, path, intermediateModelsMap, searchListener).execute();
        }

        System.out.println("####################");
        System.out.println("IAM Policy Generated");
        System.out.println("####################");
        System.out.println(new JSONIAMPolicyGenerator(permMap).execute());
    }
}
