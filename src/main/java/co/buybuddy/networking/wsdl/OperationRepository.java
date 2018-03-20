package co.buybuddy.networking.wsdl;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public interface OperationRepository {
    public Operation findOperationByName(String operation);
}
