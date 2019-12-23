package com.github.paulomigalmeida.code2iampolicy.operation;

import java.io.IOException;

public abstract class AbstractTypedOperation<T> {

    public abstract T execute() throws IOException;
}
