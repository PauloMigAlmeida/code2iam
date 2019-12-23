package com.github.paulomigalmeida.code2iampolicy.pojo;

import com.github.paulomigalmeida.code2iampolicy.utils.SDKInconsistenciesUtils;

import java.util.ArrayList;
import java.util.List;

public class AWSIntermediateModel {
    private String asyncClient;
    private String asyncInterface;
    private String packageName;
    private String iamServiceName;
    private String syncClient;
    private String syncInterface;
    private List<String> operations;

    public AWSIntermediateModel() {
        operations = new ArrayList<>();
    }

    public String getAsyncClient() {
        return asyncClient;
    }

    public void setAsyncClient(String asyncClient) {
        this.asyncClient = asyncClient;
    }

    public String getAsyncInterface() {
        return asyncInterface;
    }

    public void setAsyncInterface(String asyncInterface) {
        this.asyncInterface = asyncInterface;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getIamServiceName() {
        return iamServiceName;
    }

    public void setIamServiceName(String iamServiceName) {
        this.iamServiceName = SDKInconsistenciesUtils.fixIAMServiceName(iamServiceName);
    }

    public String getSyncClient() {
        return syncClient;
    }

    public void setSyncClient(String syncClient) {
        this.syncClient = syncClient;
    }

    public String getSyncInterface() {
        return syncInterface;
    }

    public void setSyncInterface(String syncInterface) {
        this.syncInterface = syncInterface;
    }

    public List<String> getOperations() {
        return operations;
    }

    public void setOperations(List<String> operations) {
        this.operations = operations;
    }
}
