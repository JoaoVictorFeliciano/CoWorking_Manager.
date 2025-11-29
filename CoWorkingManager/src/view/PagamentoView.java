package view;

import service.PagamentoService;
import service.ReservaService;
import model.Pagamento;
import model.Reserva;
import java.util.List;
import java.util.Scanner;

public class PagamentoView {
    private Scanner sc;
    private PagamentoService pagamentoService;
    private ReservaService reservaService;

    public PagamentoView() {
        this.pagamentoService = new PagamentoService();
        this.reservaService = new ReservaService();
        this.sc = new Scanner(System.in);
    }

    public void menuPagamentos() {
        int opcao;
        do {
            System.out.println(" \nGERENCIAMENTO DE PAGAMENTOS: ");
            System.out.println("Escolha uma opção: ");
            System.out.println("1- Efetuar Pagamento.");
            System.out.println("2- Listar Pagamentos.");
            System.out.println("0- Voltar ao Início.");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    registrarPagamentoView();
                    break;
                case 2:
                    listarPagamentosView();
                    break;
                case 0:
                    System.out.println("Voltando ao Início...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void registrarPagamentoView() {
        try {

            List<Reserva> reservas = reservaService.listarReservas();
            boolean hasPendentes = false;

            for (Reserva reserva : reservas) {
                if (reserva.getStatus() == Reserva.statusDaReserva.PENDENTE) {
                    System.out.println("ID: " + reserva.getId() +
                            " | Espaço: " + reserva.getEspaco().getNome() +
                            " | Valor: R$ " + reserva.calcularValorTotal() +
                            " | Início: " + reserva.getInicio());
                    hasPendentes = true;
                }
            }
            if (!hasPendentes) {
                System.out.println("Não há reservas pendentes de pagamento.");
                return;
            }

            System.out.print("Digite o ID da reserva: ");
            int reservaId = sc.nextInt();
            sc.nextLine();

            Reserva reserva = null;
            for (Reserva reserva1 : reservas) {
                if (reserva1.getId() == reservaId && reserva1.getStatus() == Reserva.statusDaReserva.PENDENTE) {
                    reserva = reserva1;
                    break;
                }
            }

            if (reserva == null) {
                System.out.println("Reserva não encontrada ou já paga/cancelada.");
                return;
            }

            double valorReserva = reserva.calcularValorTotal();
            System.out.println("Valor da reserva: R$ " + valorReserva);

            System.out.print("Valor pago: R$ ");
            double valorPago = sc.nextDouble();
            sc.nextLine();

            System.out.println("Método de pagamento:");
            System.out.print("Escolha uma opção: ");
            System.out.println("1- PIX");
            System.out.println("2- Cartão");
            System.out.println("3- Dinheiro");
            int metodoOpcao = sc.nextInt();
            sc.nextLine();

            Pagamento.MetodoPagamento metodo;
            switch (metodoOpcao) {
                case 1:
                    metodo = Pagamento.MetodoPagamento.PIX;
                    break;
                case 2:
                    metodo = Pagamento.MetodoPagamento.CARTAO;
                    break;
                case 3:
                    metodo = Pagamento.MetodoPagamento.DINHEIRO;
                    break;
                default:
                    System.out.println("Método inválido!");
                    return;
            }

            Pagamento pagamento = pagamentoService.registrarPagamentos(reservaId, valorPago, metodo);
            System.out.println("Pagamento efetuado.");
            System.out.println("ID do pagamento: " + pagamento.getId());
            System.out.println("Troco: R$ " + (valorPago - valorReserva));

        } catch (Exception erro) {
            System.out.println("ERRO: " + erro.getMessage());
        }
    }
    private void listarPagamentosView() {
        try {

            List<Pagamento> pagamentos = pagamentoService.listarPagamentos();

            if (pagamentos.isEmpty()) {
                System.out.println("Nenhum pagamento registrado.");
                return;
            }

            for (Pagamento pagamento : pagamentos) {
                System.out.println("ID Pagamento: " + pagamento.getId() +
                        " | ID Reserva: " + pagamento.getReserva().getId() +
                        " | Espaço: " + pagamento.getReserva().getEspaco().getNome() +
                        " | Valor: R$ " + pagamento.getValorPago() +
                        " | Método: " + pagamento.getMetodo() +
                        " | Data: " + pagamento.getDataPagamento());
            }
            System.out.println("Total de pagamentos: " + pagamentos.size());

        } catch (Exception erro) {
            System.out.println("ERRO: " + erro.getMessage());
        }
    }
}