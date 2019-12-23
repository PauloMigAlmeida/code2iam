package com.github.paulomigalmeida.code2iampolicy.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IAMPolicy {
    @JsonProperty("Version")
    private String version;
    @JsonProperty("Statement")
    private List<Statement> statements;

    public IAMPolicy() {
        this.version = "2012-10-17";
        this.statements = new ArrayList<>();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<Statement> getStatements() {
        return statements;
    }

    public void setStatements(List<Statement> statements) {
        this.statements = statements;
    }

    public static class Statement{
        @JsonProperty("Effect")
        private String effect;
        @JsonProperty("Resource")
        private String resource;
        @JsonProperty("Action")
        private Set<String> actions;

        public Statement() {
            effect = "Allow";
        }

        public String getEffect() {
            return effect;
        }

        public void setEffect(String effect) {
            this.effect = effect;
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }

        public Set<String> getActions() {
            return actions;
        }

        public void setActions(Set<String> actions) {
            this.actions = actions;
        }
    }
}
