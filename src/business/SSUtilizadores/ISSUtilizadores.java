package business.SSUtilizadores;

import java.util.List;

public interface ISSUtilizadores {
    void registarAluno(String id, String nome, String curso, Boolean estatuto, Float media, String senha, List<Integer> ucs);
    boolean login(String id, String password);
}