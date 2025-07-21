package utn.frbb.tup.service.validations;
import org.springframework.stereotype.Component;
import utn.frbb.tup.DTO.ClienteDTO;
import utn.frbb.tup.DTO.ClienteRequestDTO;
import utn.frbb.tup.exceptions.ClienteMenorDeEdadException;
import utn.frbb.tup.exceptions.DatosNulosException;
import utn.frbb.tup.model.Tipo;

import java.time.LocalDate;
import java.time.Period;

@Component
public class ClienteValidator {
    public ClienteValidator() {}

    public void validarCliente(ClienteDTO cliente) {
        final String regex = "[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+";
        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) throw new DatosNulosException("Error: campo 'nombre' vacío.");
        if (!cliente.getNombre().matches(regex)) throw new IllegalArgumentException("Error: el nombre solo puede contener letras");
        if (cliente.getApellido() == null || cliente.getApellido().isBlank()) throw new DatosNulosException("Error: campo 'apellido' vacío.");
        if (!cliente.getApellido().matches(regex)) throw new IllegalArgumentException("Error: el apellido solo puede contener letras");
        if (cliente.getFechaNacimiento() == null) throw new DatosNulosException("Error: campo 'fecha nacimiento' vacío");
        if (cliente.getFechaNacimiento().isAfter(LocalDate.now())) throw new IllegalArgumentException("Error: No se permite ingresar fechas futuras.");
        int edad = Period.between(cliente.getFechaNacimiento(), LocalDate.now()).getYears();
        if (edad < 18) throw new ClienteMenorDeEdadException("Error: el usuario debe ser mayor de 18 años.");
        if (cliente.getBanco() == null) throw new DatosNulosException("Error: campo 'banco' vacío.");
    }

    public void validarTipoPersona(Tipo tipoPersona) {
        if (tipoPersona == null) throw new DatosNulosException("Error: campo 'tipo' vacío.");
    }
}