package business.SSUtilizadores;

import business.SSHorario.Horario;
import java.util.List;

public class Aluno extends Utilizador {
    private Boolean estatuto; 
    private Float media;
    private List<Integer> ucs;
    private Horario horario;


    public Aluno(String id, String nome, String curso, Boolean estatuto, Float media, String password, List<Integer> ucs) {
        super(id, nome, curso, password);
        this.estatuto = estatuto;
        this.media = media;
        this.ucs = ucs;
    }

    public Boolean getEstatuto() {
        return this.estatuto;
    }

    public Float getMedia() {
        return this.media;
    }

    public List<Integer> getUCs() {
        return this.ucs;
    }

    public void setEstatuto(Boolean estatuto) {
        this.estatuto = estatuto;
    }

    public void setMedia(Float media) {
        this.media = media;
    }

    public void setUCs(List<Integer> ucs) {
        this.ucs = ucs;
    }

    public Horario getHorario() {
        return this.horario;
    }
    
    public void setHorario(Horario horario) {
        this.horario = horario;
    }
}