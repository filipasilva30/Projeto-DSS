package data;

import business.SSUC.UC;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UCDAO {
    public Map<Integer, UC> getAllUCs() {
        Map<Integer, UC> ucs = new HashMap<>();

        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
             Statement stm = conn.createStatement()) {

            String sql = "SELECT * FROM Uc";
            ResultSet rs = stm.executeQuery(sql);

            while (rs.next()) {
                Integer id = rs.getInt("Id");
                String nome = rs.getString("Nome");

                UC uc = new UC(id, nome, new ArrayList<>()); 
                ucs.put(id, uc);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ucs;
    }

    public List<Integer> getUCsAluno(String alunoId) throws SQLException {
        String sql = "SELECT Id_Uc FROM Aluno_UC WHERE Id_Aluno = ?";
        List<Integer> ucs = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DAOConfig.URL, DAOConfig.USERNAME, DAOConfig.PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, alunoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ucs.add(rs.getInt("Id_Uc"));
            }
        }
        return ucs;
    }
}