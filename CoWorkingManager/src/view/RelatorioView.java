package view;

import service.RelatorioService;
import model.Reserva;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class RelatorioView {
    private Scanner sc;
    private RelatorioService relatorioService;

    public RelatorioView() {
        this.relatorioService = new RelatorioService();
        this.sc = new Scanner(System.in);
    }

    public void menuRelatorios() {
        int opcao;
        do {
            System.out.println(" \nRELATÓRIOS: ");
            System.out.println("1- Reservas por Período.");
            System.out.println("2- Faturamento por Tipo de Espaço.");
            System.out.println("3- Utilização por horas de cada Espaço.");
            System.out.println("4- Ranking dos Espaços.");
            System.out.println("0- Voltar ao início.");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    reservasPeriodoView();
                    break;
                case 2:
                    faturamentoEspacosView();
                    break;
                case 3:
                    utilizacaoEspacosView();
                    break;
                case 4:
                    topEspacosView();
                    break;
                case 0:
                    System.out.println("Voltando ao Início...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void reservasPeriodoView() {
        try {
            System.out.println("Reservas no Período: ");
            System.out.print("Data inicial (Ano-Mes-Dia(T)Hora:Minutos): ");
            String inicioStr = sc.nextLine();
            LocalDateTime inicio = LocalDateTime.parse(inicioStr);

            System.out.print("Data final (Ano-Mes-Dia(T)Hora:Minutos): ");
            String fimStr = sc.nextLine();
            LocalDateTime fim = LocalDateTime.parse(fimStr);

            List<Reserva> reservas = relatorioService.reservasNoPeriodo(inicio, fim);

            System.out.println("Reservas no período: ");
            for (Reserva reserva : reservas) {
                System.out.println("ID: " + reserva.getId() +
                        " | Espaço: " + reserva.getEspaco().getNome() +
                        " | Início: " + reserva.getInicio() +
                        " | Fim: " + reserva.getFim() +
                        " | Valor: R$ " + reserva.calcularValorTotal());
            }
            System.out.println("Total: " + reservas.size() + " reservas");

        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    private void faturamentoEspacosView() {
        try {
            System.out.println("Faturamento de cada Espaço: ");
            Map<String, Double> faturamento = relatorioService.faturamentoEspacos();

            double totalGeral = 0;
            for (Map.Entry<String, Double> entry : faturamento.entrySet()) {
                System.out.println(entry.getKey() + ": R$ " + entry.getValue());
                totalGeral += entry.getValue();
            }
            System.out.println("Total Geral: R$ " + totalGeral);

        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    private void utilizacaoEspacosView() {
        try {
            System.out.println("Utilização dos Espaços em Horas: ");
            Map<String, Double> utilizacao = relatorioService.utilizacaoEspacos();

            for (Map.Entry<String, Double> entry : utilizacao.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue() + " horas");
            }

        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    private void topEspacosView() {
        try {
            System.out.println("Ranking Espaços mais Utilizados: ");
            List<Map.Entry<String, Integer>> ranking = relatorioService.rankingEspacos();

            for (int i = 0; i < ranking.size(); i++) {
                Map.Entry<String, Integer> entry = ranking.get(i);
                System.out.println((i + 1) + "º - " + entry.getKey() +
                        ": " + entry.getValue() + " reservas");
            }

        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }
}