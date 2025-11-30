package view;

import service.ReservaService;
import service.EspacoService;
import model.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class ReservaView {
    private Scanner sc;
    private ReservaService reservaService;
    private EspacoService espacoService;

    public ReservaView(){
        this.reservaService = new ReservaService();
        this.espacoService = new EspacoService();
        this.sc = new Scanner(System.in);
    }
    public void menuReservas(){
        int opcao;

        do {
            System.out.println(" \nGERENCIAMENTO DE RESERVAS: ");
            System.out.println("Escolha uma opcão: ");
            System.out.println("1- Criar Reserva.");
            System.out.println("2- Cancelar Reserva.");
            System.out.println("3- Listar Reservas.");
            System.out.println("0- Voltar ao Início.");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao){
                case 1:
                    criarReservaView();
                    break;
                case 2:
                    cancelarReservaView();
                    break;
                case 3:
                    listarReservaView();
                    break;
                case 0:
                    System.out.println("Voltando ao Início...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }while (opcao != 0);
    }
    private void criarReservaView(){
        try{
            System.out.println("Espaços dispóniveis: ");
            List<Espaco> espacos = espacoService.listarEspacos();
            for(Espaco espaco : espacos){
                if(espaco.isDisponivel()){
                    System.out.println("ID: " + espaco.getId() + " | " +
                            espaco.getNome() + " | R$ " +
                            espaco.getPrecoPorHora() + "/hora");
                }
            }

            System.out.println("Digite o ID do Espaço: ");
            int idEspaco = sc.nextInt();
            sc.nextLine();

            Espaco espaco = espacoService.BuscarporId(idEspaco);

            System.out.print("Digite a data/hora de início (Ano-Mes-Dia(T)Hora:Minutos): ");
            String inicioSave = sc.nextLine();
            LocalDateTime inicio = LocalDateTime.parse(inicioSave);

            System.out.print("Digite a data/hora de fim (Ano-Mes-Dia(T)Hora:Minutos): ");
            String fimSave = sc.nextLine();
            LocalDateTime fim = LocalDateTime.parse(fimSave);

            Reserva reserva = new Reserva(0, espaco, inicio, fim);
            reservaService.criarReserva(reserva);

            System.out.println("Reserva criada: ");
            System.out.println("Valor total: R$ " + reserva.calcularValorTotal());

        } catch (Exception erro) {
            System.out.println("ERRO: " + erro.getMessage());
        }
    }

    private void cancelarReservaView(){
        try{
            System.out.println("Reservas Ativas: ");
            List<Reserva> reservas = reservaService.listarReservas();
            for(Reserva reserva : reservas){
                if(reserva.getStatus() == Reserva.statusDaReserva.CONFIRMADA ||
                reserva.getStatus() == Reserva.statusDaReserva.PENDENTE){
                    System.out.println("ID: " + reserva.getId() +
                            " | Espaço: " + reserva.getEspaco().getNome() +
                            " | Início: " + reserva.getInicio() +
                            " | Valor: R$ " + reserva.calcularValorTotal());
                }
            }

            System.out.println("Digite o ID da reserva que quer cancelar: ");
            int cancelar = sc.nextInt();
            sc.nextLine();

            System.out.println("Tem certeza que deseja realizar essa ação? (s/n) ");
            String acao = sc.nextLine();

            if (acao.equalsIgnoreCase("s")){
                reservaService.cancelarReserva(cancelar);
                System.out.println("Reserva Cancelada.");
            }else{
                System.out.println("O cancelamento não foi finalizado.");
            }
        } catch (Exception erro) {
            System.out.println("ERRO: " + erro.getMessage());
        }
    }

    private void listarReservaView(){
        try{
            System.out.println("Reservas: ");
            List<Reserva> reservas = reservaService.listarReservas();
            if(reservas.isEmpty()){
                System.out.println("Nenhuma reserva foi feita.");
                return;
            }
            for (Reserva reserva : reservas){

                switch (reserva.getStatus()){
                    case PENDENTE:
                        System.out.println("Reserva Pendente.");
                        break;
                    case CONFIRMADA:
                        System.out.println("Reserva Confirmada.");
                        break;
                    case CANCELADA:
                        System.out.println("Reserva Cancelada.");
                }
                if(reserva.getTaxaCancelamento() > 0){
                    System.out.println("Taxa de Cancelamento: " + reserva.getTaxaCancelamento());
                }

                System.out.println("Total de Reservas: " + reservas.size());

            }
        } catch (Exception erro) {
            System.out.println("ERRO:" + erro.getMessage());
        }
    }
}
