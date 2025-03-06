package utn.frbb.tup.exceptions;

public class TransferenciaFallidaException extends RuntimeException {
    public TransferenciaFallidaException(String message) {
        super("Error: transferencia fallida. " + message);
    }
}
