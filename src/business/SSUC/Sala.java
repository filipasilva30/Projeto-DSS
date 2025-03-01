package business.SSUC;

public class Sala {
    private Float numero;
    private int capacidade;

    public Sala(Float numero, int capacidade) {
        this.numero = numero;
        this.capacidade = capacidade;
    }

    public Float getNumero() {
        return this.numero;
    }

    public int getCapacidade() {
        return this.capacidade;
    }

    public void setNumero(Float numero) {
        this.numero = numero;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }
}