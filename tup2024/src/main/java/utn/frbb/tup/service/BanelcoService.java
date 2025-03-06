package utn.frbb.tup.service;
import org.springframework.stereotype.Service;
import utn.frbb.tup.model.TipoMoneda;

@Service
public class BanelcoService {
    public boolean transferir(long origen, long destino, TipoMoneda tipoMoneda, float monto) {
        return Math.random() > 0.1;
    }
}