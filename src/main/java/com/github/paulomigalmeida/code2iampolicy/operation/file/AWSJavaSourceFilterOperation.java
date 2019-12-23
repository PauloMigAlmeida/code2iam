package com.github.paulomigalmeida.code2iampolicy.operation.file;

import com.github.paulomigalmeida.code2iampolicy.operation.AbstractTypedOperation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class AWSJavaSourceFilterOperation extends AbstractTypedOperation<AWSJavaSourceFilterOperation.AWSJavaSDKHolder> {

    private final Path directory;

    public AWSJavaSourceFilterOperation(Path directory) {
        this.directory = directory;
    }

    @Override
    public AWSJavaSDKHolder execute() throws IOException {
        AWSJavaSDKHolder awsJavaSDKHolder = new AWSJavaSDKHolder();
        awsJavaSDKHolder.servicePaths = Files.walk(directory, 4)
                .filter((it) ->
                        it.getFileName().toString().equals("java") &&
                                it.getParent().getFileName().toString().equals("main"))
                .collect(Collectors.toList());

        awsJavaSDKHolder.intermediateJsonPaths = Files.walk(directory)
                .filter((it) ->
                        it.getFileName().toString().equals("models") &&
                                it.getParent().getFileName().toString().equals("resources") &&
                                it.toString().contains("aws-java-sdk-models")
                ).collect(Collectors.toList());
        return awsJavaSDKHolder;
    }

    public static class AWSJavaSDKHolder {
        private List<Path> servicePaths;
        private List<Path> intermediateJsonPaths;

        public List<Path> getServicePaths() {
            return servicePaths;
        }

        public List<Path> getIntermediateJsonPaths() {
            return intermediateJsonPaths;
        }

    }
}
