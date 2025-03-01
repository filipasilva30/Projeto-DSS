package business.SSUC;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UC {
    private Integer id;
    private String nome;
    private Map<String, Turno> turnos;
    private List<String> alunos;

    public UC(Integer id, String nome, List<Turno> turnos) {
        this.id = id;
        this.nome = nome;
        this.turnos = new HashMap<>();
        if (turnos != null) {
            for (Turno turno : turnos) {
                this.turnos.put(turno.getId(), turno);
            }
        }
        this.alunos = new ArrayList<>();
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Turno getTurno(String idTurno) {
        return turnos.get(idTurno);
    }

    public List<Turno> getTurnos() {
        if (turnos == null) {
            turnos = new HashMap<>();
        }
        return new ArrayList<>(turnos.values());
    }

    public List<String> getAlunos() {
        return alunos;
    }
}