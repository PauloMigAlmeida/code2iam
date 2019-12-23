package com.github.paulomigalmeida.code2iampolicy.operation.file;

import com.github.paulomigalmeida.code2iampolicy.operation.AbstractTypedOperation;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class JavaSourceFilterOperation extends AbstractTypedOperation<List<Path>> {

    private Path directoryPath;

    public JavaSourceFilterOperation(Path directoryPath) {
        this.directoryPath = directoryPath;
    }

    public List<Path> execute() throws IOException {
        List<Path> files = new ArrayList<>();
        Files.walkFileTree(directoryPath, new SimpleFileVisitor<>(){
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if(file.toString().endsWith(".java")){
                    files.add(file);
                }
                return super.visitFile(file,attrs);
            }
        });
        return files;
    }
}
