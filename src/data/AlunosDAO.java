package data;

import business.SSUtilizadores.Aluno;
import java.sql.*;
import java.util.*;

public class AlunosDAO {
    public List<Aluno> getAlunos() {
        List<Aluno> alunos = new ArrayList<>();

        String sql = "SELECT * FROM Aluno";

        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("Id");
                String nome = rs.getString("Nome");
                String password = rs.getString("Senha");
                String curso = rs.getString("Curso");
                Boolean estatuto = rs.getBoolean("estatuto");
                Float media = rs.getFloat("Media");


                Aluno aluno = new Aluno(id, nome, curso, estatuto, media, password, new ArrayList<>());
                alunos.add(aluno);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return alunos;
    }

    public boolean alunoExiste(String id) {
        String sql = "SELECT COUNT(*) AS total FROM Aluno WHERE Id = ?";
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") > 0; // Se o total for maior que 0, o aluno existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public void registarAluno(String id, String nome, String curso, Boolean estatuto, Float media, String senha, List<Integer> ucs) {
        String sqlAluno = "INSERT INTO Aluno (Id, Nome, Curso, Estatuto, Media, Senha) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlAlunoUC = "INSERT INTO Aluno_UC (Id_Uc, Id_Aluno) VALUES (?, ?)";
    
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD)) {
            // Inserir o aluno
            try (PreparedStatement psAluno = conn.prepareStatement(sqlAluno)) {
                psAluno.setString(1, id);
                psAluno.setString(2, nome);
                psAluno.setString(3, curso);
                psAluno.setBoolean(4, estatuto);
                psAluno.setFloat(5, media);
                psAluno.setString(6, senha);
                psAluno.executeUpdate();
            }
    
            // Inserir as UCs associadas
            try (PreparedStatement psAlunoUC = conn.prepareStatement(sqlAlunoUC)) {
                for (Integer uc : ucs) {
                    psAlunoUC.setInt(1, uc);
                    psAlunoUC.setString(2, id);
                    psAlunoUC.addBatch();
                }
                psAlunoUC.executeBatch();
            }
    
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}