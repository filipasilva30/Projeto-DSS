package data;

import business.SSUtilizadores.Diretor;
import java.sql.*;
import java.util.*;

public class DiretoresDAO {
    private Map<String, Diretor> diretores;

    public DiretoresDAO() {
        this.diretores = new HashMap<>();
        loadDiretores();
    }

    public List<Diretor> getDiretores() {
        if(diretores.isEmpty()) {
            loadDiretores();
        }
        return new ArrayList<>(diretores.values());
    }

    private void loadDiretores() {
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Diretor");

            while (rs.next()) {
                Diretor diretor = new Diretor(
                        rs.getString("Id"),
                        rs.getString("Nome"),

                        rs.getString("Curso"),
                        rs.getString("Senha")

                );
                diretores.put(diretor.getId(), diretor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveDiretor(Diretor diretor) {
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD)) {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO Diretor (Id, Nome, Senha, Curso) VALUES (?, ?, ?, ?) " +
                            "ON DUPLICATE KEY UPDATE Nome=?, Curso=?, Senha=?"
            );

            ps.setString(1, diretor.getId());
            ps.setString(2, diretor.getNome());
            ps.setString(3, diretor.getPassword());
            ps.setString(4, diretor.getCurso());

            ps.executeUpdate();
            diretores.put(diretor.getId(), diretor);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
