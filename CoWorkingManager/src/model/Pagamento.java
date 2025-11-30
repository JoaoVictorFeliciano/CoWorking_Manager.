package model;

import java.time.LocalDateTime;

public class Pagamento {
    private int id;
    private Reserva reserva;
    private double valorPago;
    private LocalDateTime dataPagamento;
    private MetodoPagamento metodo;

    public enum MetodoPagamento{
        PIX, CARTAO, DINHEIRO
    }

    public Pagamento(int id, Reserva reserva, double valorPago, LocalDateTime dataPagamento, MetodoPagamento metodo){
        this.id = id;
        this.reserva = reserva;
        this.valorPago = valorPago;
        this.dataPagamento = dataPagamento;
        this.metodo = metodo;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Reserva getReserva() {
        return reserva;
    }
    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }
    public double getValorPago() {
        return valorPago;
    }
    public void setValorPago(double valorPago) {
        this.valorPago = valorPago;
    }

    public LocalDateTime getDataPagamento() {
        return dataPagamento;
    }
    public void setDataPagamento(LocalDateTime dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public MetodoPagamento getMetodo() {
        return metodo;
    }
    public void setMetodo(MetodoPagamento metodo) {
        this.metodo = metodo;
    }
}
