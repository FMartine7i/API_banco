package utn.frbb.tup.exceptions;

public class CuentaNotFoundException extends RuntimeException {
    public CuentaNotFoundException (String message) {
        super(message);
    }
}
