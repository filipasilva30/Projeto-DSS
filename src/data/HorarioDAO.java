package data;

import business.SSHorario.Horario;
import business.SSUC.Restricao;
import business.SSUC.Sala;
import business.SSUC.Turno;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HorarioDAO {
    public Horario getHorarioAluno(String alunoId) {
    Horario horario = new Horario();

    String sql = "SELECT t.Id AS TurnoId, t.HoraInicio, t.HoraFim, t.DiaSemana, t.Tipo, t.Restricao, t.Capacidade, t.LugaresReservados, " +
                 "s.Numero AS SalaNumero, s.Capacidade AS SalaCapacidade, " +
                 "uc.Nome AS UCNome " +
                 "FROM Horario h " +
                 "JOIN Turno t ON h.Id_Turno = t.Id " +
                 "JOIN Sala s ON t.Id_Sala = s.Numero " +
                 "JOIN Uc uc ON t.Id_Uc = uc.Id " +
                 "WHERE h.Id_Aluno = ?";

    try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, alunoId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Sala sala = new Sala(rs.getFloat("SalaNumero"), rs.getInt("SalaCapacidade"));

            Turno turno = new Turno(
                String.valueOf(rs.getInt("TurnoId")),
                rs.getTime("HoraInicio").toLocalTime(),
                rs.getTime("HoraFim").toLocalTime(),
                rs.getString("DiaSemana"),
                rs.getString("Tipo"),
                sala,
                Restricao.valueOf(rs.getString("Restricao")), 
                rs.getInt("Capacidade"),
                rs.getInt("LugaresReservados")
            );

            turno.setUcNome(rs.getString("UCNome"));

            horario.adicionarTurno(turno);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return horario;
    }

    public void adicionarHorario(String alunoId, Integer turnoId) throws SQLException {
        String sql = "INSERT INTO Horario (Id_Aluno, Id_Turno) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, alunoId);
            ps.setInt(2, turnoId);
            ps.executeUpdate();
        }
    }

    public int getAlunosPorTurno(Integer turnoId) throws SQLException {
        String sql = "SELECT COUNT(*) AS total FROM Horario WHERE Id_Turno = ?";
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, turnoId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }  
    
    public List<Turno> getTurnosAluno(String alunoId) throws SQLException {
        String sql = "SELECT t.Id, t.HoraInicio, t.HoraFim, t.DiaSemana, t.Tipo, t.Restricao, t.Capacidade, t.LugaresReservados, " +
                 "s.Numero AS SalaNumero, s.Capacidade AS SalaCapacidade, " +
                 "uc.Nome AS UCNome " +
                 "FROM Horario h " +
                 "JOIN Turno t ON h.Id_Turno = t.Id " +
                 "JOIN Sala s ON t.Id_Sala = s.Numero " +
                 "JOIN Uc uc ON t.Id_Uc = uc.Id " +
                 "WHERE h.Id_Aluno = ?";
        List<Turno> turnos = new ArrayList<>();
    
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setString(1, alunoId);
            ResultSet rs = ps.executeQuery();
    
            while (rs.next()) {
                Restricao restricao = Restricao.valueOf(rs.getString("Restricao"));
                Sala sala = new Sala(rs.getFloat("SalaNumero"), rs.getInt("SalaCapacidade"));
                Turno turno = new Turno(
                    String.valueOf(rs.getInt("Id")),
                    rs.getTime("HoraInicio").toLocalTime(),
                    rs.getTime("HoraFim").toLocalTime(),
                    rs.getString("DiaSemana"),
                    rs.getString("Tipo"),
                    sala,
                    restricao,
                    rs.getInt("Capacidade"),
                    rs.getInt("LugaresReservados")
                );
                turno.setUcNome(rs.getString("UCNome")); // Define o nome da UC no turno
                turnos.add(turno);
            }
        }
        return turnos;
    }

    public String consultarHorarioAluno(String alunoId) {
        Horario horario = getHorarioAluno(alunoId);
        StringBuilder sb = new StringBuilder();
    
        if (horario.getTurnos().isEmpty()) {
            return "Nenhum horário encontrado para o aluno.";
        }
    
        for (Turno turno : horario.getTurnos()) {
            sb.append(String.format(
                "%s: %s - %s | UC: %s | Tipo: %s | Sala: %.2f\n",
                turno.getDiaSemana(),
                turno.getHoraInicio(),
                turno.getHoraFim(),
                turno.getUcNome(),
                turno.getTipo(),
                turno.getSala().getNumero()
            ));
        }
    
        return sb.toString();
    }
}