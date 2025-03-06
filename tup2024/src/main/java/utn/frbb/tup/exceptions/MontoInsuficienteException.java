package utn.frbb.tup.exceptions;

public class MontoInsuficienteException extends RuntimeException {
    public MontoInsuficienteException(String message) {
        super("Error: monto insuficiente. " + message);
    }
}
