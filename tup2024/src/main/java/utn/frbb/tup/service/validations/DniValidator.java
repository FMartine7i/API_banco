package utn.frbb.tup.service.validations;
import org.springframework.stereotype.Component;
import utn.frbb.tup.exceptions.DatosNulosException;

@Component
public class DniValidator {
    public DniValidator() {}

    public long validarDni(String dniStr) {
        if (dniStr == null || dniStr.trim().isEmpty()) throw new DatosNulosException("Error: número de DNI nulo.");
        long dni;
        try { dni = Long.parseLong(dniStr); } catch (NumberFormatException e) { throw new NumberFormatException("Error: el DNI debe ser un número."); }
        if (dni <= 0) throw new IllegalArgumentException("Error: número de DNI no es positivo.");
        return dni;
    }
}