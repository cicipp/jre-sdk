package co.buybuddy.networking.wsdl;

import okhttp3.Response;

import java.io.IOException;

public class UnexpectedOperationResultException extends RuntimeException {
    private Operation operation;
    private int code;
    private String payload;

    public UnexpectedOperationResultException(Response response) {
        this.code = response.code();

        try {
            if (response.body() != null) {
                this.payload = response.body().string();
            }
        } catch (IOException ignored) { }
    }

    public Operation getOperation() {
        return operation;
    }

    public int getCode() {
        return code;
    }

    public String getPayload() {
        return payload;
    }
}
