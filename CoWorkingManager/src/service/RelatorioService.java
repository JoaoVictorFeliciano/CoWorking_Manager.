package service;
import dao.EspacoDao;
import dao.ReservaDao;
import dao.PagamentoDao;
import model.Espaco;
import model.Reserva;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class RelatorioService {
    private ReservaDao reservaDao;
    private EspacoDao espacoDao;
    private PagamentoDao pagamentoDao;

    public RelatorioService(){
        this.reservaDao = new ReservaDao();
        this.espacoDao = new EspacoDao();
        this.pagamentoDao = new PagamentoDao();
    }

    // reservas feitas no periodo
    public List<Reserva> reservasNoPeriodo(LocalDateTime inicioPeriodo, LocalDateTime fimPeriodo){

        List<Reserva> todasReservas = reservaDao.listarTodos();
        List<Reserva> reservasPeriodo = new ArrayList<>();

        for(int i = 0; i < todasReservas.size(); i++){
            Reserva reserva = todasReservas.get(i);
            if(!reserva.getInicio().isBefore(inicioPeriodo) &&
            !reserva.getFim().isAfter(fimPeriodo)){
                reservasPeriodo.add(reserva);
            }
        }
        return reservasPeriodo;
    }

    // faturamento de cada espaço
    public Map<String,Double> faturamentoEspacos(){
        List<Reserva> reservas = reservaDao.listarTodos();
        Map<String,Double> faturamento = new HashMap<>();

        for(int i = 0; i < reservas.size(); i++){
            Reserva reserva = reservas.get(i);
            if(reserva.getStatus() == Reserva.statusDaReserva.CONFIRMADA){
                String tipo = reserva.getEspaco().getClass().getSimpleName();
                double valor = reserva.calcularValorTotal();

                double valorAtual = faturamento.getOrDefault(tipo, 0.0);
                faturamento.put(tipo,valorAtual + valor);
            }
        }
        return faturamento;
    }

    // espaços utilizados por mais horas
    public Map<String,Double> utilizacaoEspacos(){
        List<Reserva> reservas = reservaDao.listarTodos();
        List<Espaco> espacos = espacoDao.listarTodos();
        Map<String,Double> utilizacao = new HashMap<>();

        for(int i = 0; i < reservas.size(); i++){
            Reserva reserva = reservas.get(i);

            if(reserva.getStatus() == Reserva.statusDaReserva.CONFIRMADA){
                String nomeDoEspaco = reserva.getEspaco().getNome();
                double horas = reserva.calcularHoras();

                double horasAtuais = utilizacao.getOrDefault(nomeDoEspaco, 0.0);
                utilizacao.put(nomeDoEspaco, horasAtuais + horas);
            }
        }
        for(int i = 0; i < espacos.size(); i++){
            String nomeDoEspaco = espacos.get(i).getNome();
            utilizacao.putIfAbsent(nomeDoEspaco, 0.0);
        }
        return utilizacao;
    }

    // ranking espaços mais utilizados
    public List<Map.Entry<String, Integer>> rankingEspacos(){
        List<Reserva> reservas = reservaDao.listarTodos();
        Map<String, Integer> ranking = new HashMap<>();

        for(int i = 0; i < reservas.size(); i++){
            Reserva reserva = reservas.get(i);

            if(reserva.getStatus() == Reserva.statusDaReserva.CONFIRMADA){
                String nomeDoEspaco = reserva.getEspaco().getNome();
                int rankingAtual = ranking.getOrDefault(nomeDoEspaco, 0);
                ranking.put(nomeDoEspaco, rankingAtual + 1);
            }
        }
        List<Map.Entry<String, Integer>> rankingOrdenado = new ArrayList<>(ranking.entrySet());
        for(int i = 0; i < rankingOrdenado.size(); i++){
            for(int j = i + 1; j < rankingOrdenado.size(); j++){

                if(rankingOrdenado.get(i).getValue() < rankingOrdenado.get(j).getValue()){
                    Map.Entry<String, Integer> temp = rankingOrdenado.get(i);
                    rankingOrdenado.set(i, rankingOrdenado.get(j));
                    rankingOrdenado.set(j, temp);
                }
            }
        }
        return rankingOrdenado;
    }
}
