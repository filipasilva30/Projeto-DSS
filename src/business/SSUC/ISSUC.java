package business.SSUC;

import java.sql.SQLException;

public interface ISSUC {
    public Boolean definirCapacidadeTurno(Integer idUC, String idTurno, int capacidade) throws SQLException;
    public Boolean reservarLugaresEstatuto(Integer idUC, String idTurno, int lugares) throws SQLException;
}