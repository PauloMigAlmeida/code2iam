package com.github.paulomigalmeida.code2iampolicy.operation.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.paulomigalmeida.code2iampolicy.operation.AbstractTypedOperation;
import com.github.paulomigalmeida.code2iampolicy.pojo.IAMPolicy;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.github.paulomigalmeida.code2iampolicy.pojo.IAMPolicy.Statement;

public class JSONIAMPolicyGenerator extends AbstractTypedOperation<String> {

    private Map<String, Set<String>> iamPermissions;

    public JSONIAMPolicyGenerator(Map<String, Set<String>> iamPermissions) {
        this.iamPermissions = iamPermissions;
    }

    @Override
    public String execute() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        IAMPolicy iamPolicy = new IAMPolicy();
        iamPermissions.forEach((key, value) -> {
            Statement statement = new Statement();
            statement.setResource("enter-resource-arn");

            var actions = new HashSet<String>();
            value.forEach((it) -> actions.add(String.format("%s:%s", key, it)));
            statement.setActions(actions);

            iamPolicy.getStatements().add(statement);
        });

        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(iamPolicy);
    }
}
