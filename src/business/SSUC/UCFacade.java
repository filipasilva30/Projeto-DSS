package business.SSUC;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import data.UCDAO;
import data.TurnosDAO;
import java.sql.*;
import java.util.List;

public class UCFacade implements ISSUC {
    Scanner scanner = new Scanner(System.in);
    private Map<Integer, UC> ucs;
    private final UCDAO ucDAO;
    private final TurnosDAO turnosDAO;

    public UCFacade() {
        this.ucs = new HashMap<>();
        this.ucDAO = new UCDAO();
        this.turnosDAO = new TurnosDAO();

        this.ucs = ucDAO.getAllUCs();
    }   

    @Override
    public Boolean definirCapacidadeTurno(Integer idUC, String idTurno, int capacidade) throws SQLException{
        UC uc = ucs.get(idUC);
        if (uc != null) {
            List<Turno> turnos = turnosDAO.getTurnosPorUC(idUC);
            Turno turno = null;
            for(Turno t : turnos) {
                if(t.getId().equals(idTurno)) {
                    turno = t;
                    break;
                } else {
                    return false;
                }
            }
            if (turno != null) {
                turno.setCapacidade(capacidade);
                turnosDAO.atualizarCapacidade(Integer.parseInt(idTurno), capacidade);
            }
        }
        return true;
    }

    @Override
    public Boolean reservarLugaresEstatuto(Integer idUC, String idTurno, int lugares) throws SQLException {
        UC uc = ucs.get(idUC);
        if (uc != null) {
            List<Turno> turnos = turnosDAO.getTurnosPorUC(idUC);
            Turno turno = null;
            for(Turno t : turnos) {
                if(t.getId().equals(idTurno)) {
                    turno = t;
                    break;
                } else {
                    return false;
                }
            }
            if (turno != null) {
                turno.setLugaresReservadosEstatuto(lugares);
                turnosDAO.atualizarLugares(Integer.parseInt(idTurno), lugares);
            }
        }
        return true;
    }

    public Map<Integer,UC> getUCs() {
        return ucDAO.getAllUCs();
    }

    public List<Integer> getUCsAluno(String alunoId) throws SQLException {
        return ucDAO.getUCsAluno(alunoId);
    }

    public List<Turno> getTurnosUc(int ucId) throws SQLException {
        return turnosDAO.getTurnosPorUC(ucId);
    }

    public void atualizarLugares(int turnoId, int novosLugares) throws SQLException {
        turnosDAO.atualizarLugares(turnoId, novosLugares);
    }
}