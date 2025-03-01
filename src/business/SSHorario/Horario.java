package business.SSHorario;

import business.SSUC.Turno;
import java.util.*;

public class Horario {
    private List<Turno> turnos;

    public Horario() {
        this.turnos = new ArrayList<>();
    }

    public void adicionarTurno(Turno turno) {
        this.turnos.add(turno);
    }

    public List<Turno> getTurnos() {
        return turnos;
    }
}