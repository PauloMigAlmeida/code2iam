package com.github.paulomigalmeida.code2iampolicy.operation.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paulomigalmeida.code2iampolicy.operation.AbstractTypedOperation;
import com.github.paulomigalmeida.code2iampolicy.pojo.AWSIntermediateModel;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JSONIntermediateParserOperation extends AbstractTypedOperation<Map<String, AWSIntermediateModel>> {

    private final List<Path> directory;

    public JSONIntermediateParserOperation(List<Path> directory) {
        this.directory = directory;
    }

    @Override
    public Map<String, AWSIntermediateModel> execute() throws IOException {
        var returnMap = new HashMap<String, AWSIntermediateModel>();

        ObjectMapper objectMapper = new ObjectMapper();

        for (var path : directory) {
            Files.walkFileTree(path, new SimpleFileVisitor<>(){
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

                    if(file.getFileName().toString().endsWith("intermediate.json")){
                        var bytes = Files.readAllBytes(file);
                        var holder = parseJsonTree(objectMapper.readTree(bytes));
                        addToMap(returnMap, holder);
                    }

                    return super.visitFile(file, attrs);
                }
            });
        }

        return returnMap;
    }

    private AWSIntermediateModel parseJsonTree(JsonNode node){
        var ret = new AWSIntermediateModel();

        JsonNode metadata = node.get("metadata");
        JsonNode operations = node.get("operations");

        // metadata
        ret.setAsyncClient(metadata.get("asyncClient").asText());
        ret.setAsyncInterface(metadata.get("asyncInterface").asText());
        ret.setPackageName(metadata.get("packageName").asText());
        ret.setIamServiceName(metadata.get("signingName").asText());
        ret.setSyncClient(metadata.get("syncClient").asText());
        ret.setSyncInterface(metadata.get("syncInterface").asText());

        // operations
        operations.fieldNames().forEachRemaining(ret.getOperations()::add);

        return ret;
    }

    private void addToMap(Map<String, AWSIntermediateModel> retMap, AWSIntermediateModel holder){
        retMap.put(holder.getAsyncClient(), holder);
        retMap.put(holder.getAsyncInterface(), holder);
        retMap.put(holder.getPackageName(), holder);
        retMap.put(holder.getIamServiceName(), holder);
        retMap.put(holder.getSyncClient(), holder);
        retMap.put(holder.getSyncInterface(), holder);
    }

}
