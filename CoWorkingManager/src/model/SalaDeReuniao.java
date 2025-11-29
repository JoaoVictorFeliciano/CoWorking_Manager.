package model;
public class SalaDeReuniao extends Espaco {
      private boolean usarProjetor;

      public SalaDeReuniao(int id, String nome, int capacidade, double precoPorHora, boolean usarProjetor){
          super(id, nome, capacidade, precoPorHora);
          this.usarProjetor = usarProjetor;
      }

    public boolean isUsarProjetor() {
        return usarProjetor;
    }

    public void setUsarProjetor(boolean usarProjetor) {
        this.usarProjetor = usarProjetor;
    }

    @Override
    public double calcularCustoReserva(double horas) {
          double valor;
          valor = precoPorHora  * horas;
          if (usarProjetor == true){
              valor += 15.0;
          }
          return valor;
    }
}


