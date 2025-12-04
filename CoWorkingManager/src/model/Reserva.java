package model;

import java.time.Duration;
import java.time.LocalDateTime;
import Exception.ReservaInvalida;

public class Reserva {
    private int id;
    private Espaco espaco;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private statusDaReserva status;
    private double taxaCancelamento;

    public enum statusDaReserva{
        PENDENTE, CONFIRMADA, CANCELADA
    }

    public Reserva(int id, Espaco espaco, LocalDateTime inicio, LocalDateTime fim) throws ReservaInvalida{
        this.id = id;
        this.espaco = espaco;
        this.status = statusDaReserva.PENDENTE;
        if(!inicio.isBefore(fim)){
            throw new ReservaInvalida("Reserva não pode ser concluída.");
        }else{
            this.inicio = inicio;
            this.fim = fim;
        }
    }
    public double calcularHoras(){
        Duration duracao = Duration.between(inicio, fim);
        return (double) duracao.toMinutes() / 60;
    }
    public double calcularValorTotal(){
        double horas = calcularHoras();
        return espaco.calcularCustoReserva(horas);
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Espaco getEspaco() {
        return espaco;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public statusDaReserva getStatus() {
        return status;
    }

    public void setStatus(statusDaReserva status) {
        this.status = status;
    }

    public double getTaxaCancelamento() {
        return taxaCancelamento;
    }

    public void setTaxaCancelamento(double taxaCancelamento) {
        this.taxaCancelamento = taxaCancelamento;
    }
}
