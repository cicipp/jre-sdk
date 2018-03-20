package co.buybuddy.networking.wsdl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OperationRemoteRepository {
    private List<Operation> operations;

    public OperationRemoteRepository() {
        this.operations = new ArrayList<>();
    }

    public Operation findOperationByName(String name) {
        return this.operations.stream()
                .filter(operation -> operation.getName().equals(name))
                .collect(Collectors.toList())
                .get(0);
    }
}
