package com.github.paulomigalmeida.code2iampolicy.operation.parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.javaparsermodel.declarations.JavaParserMethodDeclaration;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import com.github.paulomigalmeida.code2iampolicy.pojo.AWSIntermediateModel;
import static java.lang.System.out;

public class ParseCodeOperation {

    private final Path path;
    private final Map<String, AWSIntermediateModel> intermediateModelsMap;
    private final APIPermissionSearchListener searchListener;
    private final List<Path> pathsForSymbolResolver;

    public ParseCodeOperation(List<Path> pathsForSymbolResolver, Path path,
                              Map<String, AWSIntermediateModel> intermediateModelsMap,
                              APIPermissionSearchListener searchListener) {
        this.pathsForSymbolResolver = pathsForSymbolResolver;
        this.path = path;
        this.intermediateModelsMap = intermediateModelsMap;
        this.searchListener = searchListener;
    }

    public void execute() throws IOException {
        try (var reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
            var typeSolver = new CombinedTypeSolver(
                    new ReflectionTypeSolver()
            );
            pathsForSymbolResolver.forEach((it) -> typeSolver.add(new JavaParserTypeSolver(it)));

            var javaParserFacade = JavaParserFacade.get(typeSolver);

            // parse the file
            var cu = JavaParser.parse(reader);

            cu.findAll(MethodCallExpr.class).forEach((it) -> {
                try {
                    var methodRef = javaParserFacade.solve(it);
                    if (methodRef.isSolved()) {
                        var methodDecl = methodRef.getCorrespondingDeclaration();

                        if (methodDecl.getPackageName().startsWith("com.amazonaws.services")) {
                            out.println(" * Found on Line:" + it.getBegin().get().line + " :: " + it.toString());
                            parseAwsReference(methodDecl);

                        }
                    }
                } catch (UnsolvedSymbolException ex) {
//                    err.println(ex.getMessage());
                }
            });
        }
    }

    private void parseAwsReference(ResolvedMethodDeclaration methodDecl) {

        var packageName = methodDecl.getPackageName();
        var className = methodDecl.getClassName();
        var methodName = ((JavaParserMethodDeclaration) methodDecl).getWrappedNode().getName().asString();

        out.println("methodDecl.getPackageName() -> " + packageName);
        out.println("methodDecl.getClassName() -> " + className);
        out.println("methodDecl.getMethodName -> " + methodName);

        if (intermediateModelsMap.containsKey(className)) {
            var model = intermediateModelsMap.get(className);

            // sanity check to ensure we are on the same page :)
            if (model.getPackageName().equals(packageName)) {
                var apiMethod = model.getOperations().stream().filter(x -> x.equalsIgnoreCase(methodName)).findAny();
                apiMethod.ifPresent(s -> searchListener.found(model.getIamServiceName(), s));
            }
        }
        out.println();
    }

    public interface APIPermissionSearchListener {
        void found(String signingName, String apiMethod);
    }

}
