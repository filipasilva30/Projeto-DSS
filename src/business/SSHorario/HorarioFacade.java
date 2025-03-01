package business.SSHorario;

import java.sql.*;
import data.HorarioDAO;
import business.SSUC.Turno;
import java.util.List;

public class HorarioFacade implements ISSHorario {   
    private final HorarioDAO horarioDAO;

    public HorarioFacade() {
        this.horarioDAO = new HorarioDAO(); 
    }

    @Override
    public String consultarHorario(String alunoId) {
        return horarioDAO.consultarHorarioAluno(alunoId);
    }    

    public void adicionarHorario(String alunoId, Integer turnoId) throws SQLException {
        horarioDAO.adicionarHorario(alunoId, turnoId);
    }

    public int getAlunosTurno(Integer turnoId) throws SQLException {
        return horarioDAO.getAlunosPorTurno(turnoId);
    }

    public List<Turno> getTurnosAluno(String alunoId) throws SQLException {
        return horarioDAO.getTurnosAluno(alunoId);
    }
}