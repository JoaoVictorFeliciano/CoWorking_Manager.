package model;

public abstract class Espaco{
    protected int id;
    protected String nome;
    protected int capacidade;
    protected boolean disponivel = true;
    protected double precoPorHora;

    public Espaco(int id, String nome, int capacidade, double precoPorHora){
        this.id = id;
        this.nome = nome;
        this.capacidade = capacidade;
        this.precoPorHora = precoPorHora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public double getPrecoPorHora() {
        return precoPorHora;
    }

    public void setPrecoPorHora(double precoPorHora) {
        this.precoPorHora = precoPorHora;
    }

    public abstract double calcularCustoReserva(double horas);


}
