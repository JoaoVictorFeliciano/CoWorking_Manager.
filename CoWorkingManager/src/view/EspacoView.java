package view;
import service.EspacoService;
import model.*;
import java.util.List;
import java.util.Scanner;


public class EspacoView {
    private Scanner sc;
    private EspacoService espacoService;

    public EspacoView(){
        this.espacoService = new EspacoService();
        this.sc = new Scanner(System.in);
    }

    public void menuEspacos(){
        int opcao;
        do {
            System.out.println(" \nGERENCIAMENTO DE ESPAÇOS: ");
            System.out.println("Escolha uma opcão: ");
            System.out.println("1- Cadastrar Espaço.");
            System.out.println("2- Listar os Espaços.");
            System.out.println("3- Buscar um Espaço pelo ID.");
            System.out.println("4- Atualizar um Espaço.");
            System.out.println("5- Deletar um Espaço.");
            System.out.println("0- Voltar ao Início.");
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao){
                case 1:
                    cadastrarEspacoView();
                    break;
                case 2:
                    listarEspacoView();
                    break;
                case 3:
                    buscarEspacoIdView();
                    break;
                case 4:
                    atualizarEspacoView();
                    break;
                case 5:
                    deletarEspacoView();
                    break;
                case 0:
                    System.out.println("Voltando ao Início...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }while (opcao != 0);
    }

    private void cadastrarEspacoView(){

        try {
            System.out.println("ESPAÇOS: ");
            System.out.println("Sala de Reunião");
            System.out.println("Cabine Individual");
            System.out.println("Auditório");

            System.out.print("\nDigite o nome do espaço: ");
            String nome = sc.nextLine();

            System.out.print("Digite a capacidade: ");
            int capacidade = sc.nextInt();
            sc.nextLine();

            System.out.println("Escolha o espaço: ");
            System.out.println("1. Sala de Reunião - R$ 50,00/hora");
            System.out.println("2. Cabine Individual - R$ 30,00/hora");
            System.out.println("3. Auditório - R$ 100,00/hora");
            int tipo = sc.nextInt();
            sc.nextLine();

            Espaco espaco;
            switch (tipo) {
                case 1:
                    System.out.print("Incluir projetor? (true/false): ");
                    boolean projetor = sc.nextBoolean();
                    espaco = new SalaDeReuniao(0, nome, capacidade, 50.0, projetor);
                    break;
                case 2:
                    espaco = new CabineIndividual(0, nome, capacidade, 30.0);
                    break;
                case 3:
                    espaco = new Auditorio(0, nome, capacidade, 100.0);
                    break;
                default:
                    System.out.println("Tipo inválido!");
                    return;
            }
            espacoService.Cadastrar_o_Espaco(espaco);
            System.out.println("Espaço cadastrado.");

        } catch (Exception erro) {
            System.out.println("ERRO: " + erro.getMessage());
        }
    }
    private void listarEspacoView(){
        try{
            List<Espaco> espacos = espacoService.listarEspacos();
            if(espacos.isEmpty()){
                System.out.println("Não há espaços cadastrados.");
                return;
            }
            for(int i = 0; i < espacos.size(); i++){
                Espaco espaco = espacos.get(i);
                System.out.println("ID: " + espaco.getId() +
                        " | Nome: " + espaco.getNome() +
                        " | Capacidade: " + espaco.getCapacidade() + " pessoas" +
                        " | Preço: R$ " + espaco.getPrecoPorHora() + "/hora" +
                        " | Tipo: " + espaco.getClass().getSimpleName() +
                        " | Disponível: " + (espaco.isDisponivel() ? "Sim" : "Não"));
            }
            System.out.println("Total de Espaços cadastrados: " + espacos.size());

        } catch (Exception erro) {
            System.out.println("ERRO: " + erro.getMessage());
        }
    }
    private void buscarEspacoIdView(){
        try {
            System.out.print("Digite o ID do espaço: ");
                int id = sc.nextInt();
                sc.nextLine();
                Espaco espaco = espacoService.BuscarporId(id);

                System.out.println("\nEspaço encontrado: ");
                System.out.println("ID: " + espaco.getId());
                System.out.println("Nome: " + espaco.getNome());
                System.out.println("Capacidade: " + espaco.getCapacidade() + " pessoas");
                System.out.println("Preço: R$ " + espaco.getPrecoPorHora() + "/hora");
                System.out.println("Tipo: " + espaco.getClass().getSimpleName());
                System.out.println("Disponível: " + (espaco.isDisponivel() ? "Sim" : "Não"));

                if (espaco instanceof SalaDeReuniao) {
                    SalaDeReuniao sala = (SalaDeReuniao) espaco;
                    System.out.println("Projetor: " + (sala.isUsarProjetor() ? "Sim" : "Não"));
                } else if (espaco instanceof Auditorio) {
                    Auditorio auditorio = (Auditorio) espaco;
                    System.out.println("Taxa fixa: R$ " + auditorio.getTaxa());
                }
            } catch (Exception erro) {
            System.out.println("ERRO: " + erro.getMessage());
            }
    }
    private void atualizarEspacoView() {
        try {
            System.out.print("Digite o ID do espaço que deseja atualizar: ");
            int id = sc.nextInt();
            sc.nextLine();

            Espaco espacoExistente = espacoService.BuscarporId(id);
            System.out.println("\nEspaço atual:");
            System.out.println("Nome: " + espacoExistente.getNome());
            System.out.println("Capacidade: " + espacoExistente.getCapacidade());

            System.out.println("\nDigite os novos dados:");
            System.out.print("Novo nome: ");
            String novoNome = sc.nextLine();

            System.out.print("Nova capacidade: ");
            int novaCapacidade = sc.nextInt();
            sc.nextLine();
            espacoExistente.setNome(novoNome);
            espacoExistente.setCapacidade(novaCapacidade);

            if (espacoExistente instanceof SalaDeReuniao) {
                System.out.print("Incluir projetor? (true/false): ");
                boolean projetor = sc.nextBoolean();
                ((SalaDeReuniao) espacoExistente).setUsarProjetor(projetor);
            }
            espacoService.atualizarEspaco(espacoExistente);
            System.out.println("Espaço atualizado.");

        } catch (Exception erro) {
            System.out.println("ERRO: " + erro.getMessage());
        }
    }
    private void deletarEspacoView() {
        try {
            System.out.print("Digite o ID do espaço a ser deletado: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Tem certeza que deseja deletar? (s/n): ");
            String confirmacao = sc.nextLine();

            if (confirmacao.equalsIgnoreCase("s")) {
                espacoService.deletarEspaco(id);
                System.out.println("Espaço deletado.");
            } else {
                System.out.println("Operação cancelada.");
            }
        } catch (Exception erro) {
            System.out.println("ERRO: " + erro.getMessage());
        }
    }
}