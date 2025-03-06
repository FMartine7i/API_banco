package utn.frbb.tup.exceptions;

public class CuentaAlreadyExistsException extends RuntimeException {
    public CuentaAlreadyExistsException(String message) {
        super("Error: la cuenta ya existe. ");
    }
}
