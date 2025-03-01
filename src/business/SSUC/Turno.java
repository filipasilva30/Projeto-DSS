package business.SSUC;

import business.SSUtilizadores.Aluno;

import java.time.LocalTime;
import java.util.Map;

public class Turno {
    private String id;
    private LocalTime horaInicio;
    private LocalTime horaFim;
    private String diaSemana;
    private Sala sala;
    private Restricao restricao;
    private String tipo; 
    private String ucNome;
    private Map <String, Aluno> alunos; // idAluno -> Aluno
    private int capacidade;
    private int lugaresReservadosEstatuto;

    public Turno(String id, LocalTime horaInicio, LocalTime horaFim, String diaSemana, String tipo,  Sala sala, Restricao restricao, int capacidade, int lugaresReservadosEstatuto) {
        this.id = id;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.diaSemana = diaSemana;
        this.tipo = tipo;
        this.sala = sala;
        this.restricao = restricao;
        this.capacidade = sala.getCapacidade(); // Capacidade padrão é a capacidade da sala
        this.lugaresReservadosEstatuto = 0; 
    }

    public String getId() {
        return this.id;
    }

    public LocalTime getHoraInicio() {
        return this.horaInicio;
    }

    public LocalTime getHoraFim() {
        return this.horaFim;
    }

    public String getDiaSemana() {
        return this.diaSemana;
    }

    public Sala getSala() {
        return this.sala;
    }

    public Restricao getRestricao() {
        return this.restricao;
    }

    public void setRestricao(Restricao restricao) {
        this.restricao = restricao;
    }

    public String getTipo() {
        return this.tipo;
    }

    public String getUcNome() {
        return this.ucNome;
    }

    public void setUcNome(String ucNome) {
        this.ucNome = ucNome;
    }

    public Map<String, Aluno> getAlunos() {
        return this.alunos;
    }

    public void setAlunos(Map<String, Aluno> alunos) {
        this.alunos = alunos;
    }

    public int getLugaresReservadosEstatuto() {
        return this.lugaresReservadosEstatuto;
    }

    public void setLugaresReservadosEstatuto(int lugaresReservadosEstatuto) {
        this.lugaresReservadosEstatuto = lugaresReservadosEstatuto;
    }

    public int getCapacidade() {
        return this.capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }
}

