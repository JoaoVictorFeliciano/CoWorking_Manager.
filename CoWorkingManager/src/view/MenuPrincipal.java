package view;
import service.EspacoService;
import service.PagamentoService;
import service.RelatorioService;
import service.ReservaService;
import java.util.Scanner;

public class MenuPrincipal {
    private EspacoService espacoService;
    private ReservaService reservaService;
    private PagamentoService pagamentoService;
    private RelatorioService relatorioService;
    private Scanner sc;

    public MenuPrincipal(){
        this.espacoService = new EspacoService();
        this.reservaService = new ReservaService();
        this.pagamentoService = new PagamentoService();
        this.relatorioService = new RelatorioService();
        this.sc = new Scanner(System.in);
    }

    public void exibirMenu(){
        int opcao;
        do{
            System.out.println("\nCoWorking Manager – Sistema de Gerenciamento de Espaços Compartilhado.");
            System.out.println("Escolha uma opção: ");
            System.out.println("1- Gerenciar Espaços.");
            System.out.println("2- Gerenciar Reservas.");
            System.out.println("3- Gerenciar Pagamentos.");
            System.out.println("4- Relátorios.");
            System.out.println("0- Sair.");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao){
                case 1:
                    menuEspacos();
                    break;
                case 2:
                    menuReservas();
                    break;
                case 3:
                    menuPagamentos();
                    break;
                case 4:
                    menuRelatorios();
                    break;
                case 0:
                    System.out.println("Sistema encerrando...");
                    System.out.println("Obrigado!");
                    break;
                default:
                    System.out.println("Opção inválida tente novamente.");
            }
        } while (opcao != 0);
    }

    private void menuEspacos() {
        EspacoView espacoView = new EspacoView();
        espacoView.menuEspacos();
    }

    private void menuReservas(){
        ReservaView reservaView = new ReservaView();
        reservaView.menuReservas();
    }

    public void menuPagamentos(){
        PagamentoView pagamentoView = new PagamentoView();
        pagamentoView.menuPagamentos();
    }

    public void menuRelatorios(){
        RelatorioView relatorioView = new RelatorioView();
        relatorioView.menuRelatorios();
    }

    public static void main(String[] args){
        MenuPrincipal menu = new MenuPrincipal();
        menu.exibirMenu();
    }
}
