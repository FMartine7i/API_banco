package utn.frbb.tup.exceptions;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String message) {
        super("Error: monto insuficiente. " + message);
    }
}
