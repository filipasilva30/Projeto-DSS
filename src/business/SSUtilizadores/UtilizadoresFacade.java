package business.SSUtilizadores;

import data.AlunosDAO;
import data.DiretoresDAO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilizadoresFacade implements ISSUtilizadores {
    private Map<String, Utilizador> utilizadores;
    private final AlunosDAO alunosDAO;
    private final DiretoresDAO diretoresDAO;

    public UtilizadoresFacade() {
        this.utilizadores = new HashMap<>();
        this.alunosDAO = new AlunosDAO();
        this.diretoresDAO = new DiretoresDAO();
        
        List<Aluno> alunos = alunosDAO.getAlunos();
        for (Aluno aluno : alunos) {
            utilizadores.put(aluno.getId(), aluno);
        }
        List <Diretor> diretores = diretoresDAO.getDiretores();
        for(Diretor diretor : diretores) {
            utilizadores.put(diretor.getId(),diretor);
        }
    }

    @Override
    public boolean login(String id, String password) {
        Utilizador utilizador = this.utilizadores.get(id);
        if (utilizador == null) {
            return false;
        }
        return utilizador.getPassword().equals(password);
    }

    @Override
        public void registarAluno(String id, String nome, String curso, Boolean estatuto, Float media, String senha, List<Integer> ucs) {
            if (alunoExiste(id)) {
                System.out.println("Erro: Um aluno com este ID já está registado.");
                return;
            }

            Utilizador aluno = new Aluno(id, nome, curso, estatuto, media, senha, ucs);
            utilizadores.put(id, aluno);

            alunosDAO.registarAluno(id, nome, curso, estatuto, media, senha, ucs);
        }

    public boolean alunoExiste(String id) {
        return alunosDAO.alunoExiste(id);
    }

    public List<Aluno> getAlunos() {
        return alunosDAO.getAlunos();
    }
}