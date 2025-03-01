package data;

import business.SSUC.Restricao;
import business.SSUC.Sala;
import business.SSUC.Turno;
import java.sql.*;
import java.util.*;

public class TurnosDAO {
    public List<Turno> getTurnosPorUC(int ucId) throws SQLException {
        String sql = "SELECT t.Id, t.HoraInicio, t.HoraFim, t.DiaSemana, t.Tipo, t.Restricao, t.Capacidade, t.LugaresReservados, " +
                 "s.Numero AS SalaNumero, s.Capacidade AS SalaCapacidade, " +
                "t.Capacidade, t.LugaresReservados, " +
                "uc.Nome AS UCNome " +
                "FROM Turno t " +
                "JOIN Sala s ON t.Id_Sala = s.Numero " +
                "JOIN Uc uc ON t.Id_Uc = uc.Id " +
                 "WHERE t.Id_Uc = ?";

        List<Turno> turnos = new ArrayList<>();
    
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
    
            ps.setInt(1, ucId);
            ResultSet rs = ps.executeQuery();
    
            while (rs.next()) {
                Restricao restricao = Restricao.valueOf(rs.getString("Restricao").toUpperCase());
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
                turno.setUcNome(rs.getString("UCNome")); // Atribui o nome da UC ao turno
                turnos.add(turno);
            }
        }
        return turnos;
    }

    public void atualizarCapacidade(int turnoId, int novaCapacidade) throws SQLException {
        String sql = "UPDATE Turno SET Capacidade = ? WHERE Id = ?";
    
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, novaCapacidade);
            ps.setInt(2, turnoId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Falha ao atualizar turno: Nenhuma linha foi afetada.");
            }
        }
    }

    public void atualizarLugares(int turnoId, int novosLugares) throws SQLException {
        String sql = "UPDATE Turno SET LugaresReservados = ? WHERE Id = ?";
    
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, novosLugares);
            ps.setInt(2, turnoId);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Falha ao atualizar turno: Nenhuma linha foi afetada.");
            }
        }
    }
}