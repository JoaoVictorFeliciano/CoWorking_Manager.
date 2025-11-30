package model;

public class CabineIndividual extends Espaco{

    public CabineIndividual(int id, String nome, int capacidade, double precoPorHora){
        super(id, nome, capacidade, precoPorHora);
    }

    @Override
    public double calcularCustoReserva(double horas) {
        double valor;
        double desconto;
        if( horas >= 4 ){
            valor = precoPorHora * horas;
            desconto = valor * 0.10;
            valor = valor - desconto;
            return valor;
        }else{
          valor = precoPorHora * horas;
            return valor;
        }
    }
}


