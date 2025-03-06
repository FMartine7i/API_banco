package utn.frbb.tup.service.validations;
import utn.frbb.tup.exceptions.ClienteNotFoundException;
import utn.frbb.tup.DTO.ClienteRequestDTO;
import java.time.LocalDate;
import java.time.Period;

public class ClienteValidation {
    public ClienteValidation() {}

    public void validarCliente(ClienteRequestDTO cliente) {
        if (cliente == null) throw new ClienteNotFoundException("Error: no se ha ingresado un cliente.");
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) throw new IllegalArgumentException("Error: campo 'nombre' vacío.");
        if (cliente.getApellido() == null || cliente.getApellido().isBlank()) throw new IllegalArgumentException("Error: campo 'apellido' vacío.");
        if (cliente.getFechaNacimiento() == null) throw new IllegalArgumentException("Error: campo 'fecha nacimiento' vacío");
        int edad = Period.between(cliente.getFechaNacimiento(), LocalDate.now()).getYears();
        if (edad < 18) throw new IllegalArgumentException("Error: el usuario debe ser mayor de 18 años.");
        if (cliente.getDni() <= 0) throw new IllegalArgumentException("Error: DNI inválido.");
        if (cliente.getTipoPersona() == null) throw new IllegalArgumentException("Error: campo 'tipo' vacío.");
        if (cliente.getBanco() == null) throw new IllegalArgumentException("Error: campo 'banco' vacío.");
    }
}