package model;

public class Auditorio extends Espaco {
    private double taxa;

    public Auditorio(int id, String nome, int capacidade, double precoPorHora){
        super(id, nome, capacidade, precoPorHora);
        this.taxa = 100;
    }

    public double getTaxa() {
        return taxa;
    }
    public void setTaxa(double taxa) {
        this.taxa = taxa;
    }

    @Override
    public double calcularCustoReserva(double horas) {
        double valor;
        valor = (precoPorHora * horas) + this.taxa;
        return valor;
    }
}
