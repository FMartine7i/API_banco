package utn.frbb.tup.exceptions;

public class ClienteAlreadyExistsException extends RuntimeException {
    public ClienteAlreadyExistsException(String message) {
        super(message);
    }
}