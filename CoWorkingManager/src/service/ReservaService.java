package service;

import model.Reserva;
import dao.EspacoDao;
import dao.ReservaDao;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import Exception.ReservaInvalida;

public class ReservaService {
    private EspacoDao espacoDao;
    private ReservaDao reservaDao;

    public ReservaService(){
        this.espacoDao = new EspacoDao();
        this.reservaDao = new ReservaDao();
    }

    private boolean reservasSobrepostas(Reserva novaReserva){
        List<Reserva> reservaSalvas = reservaDao.listarTodos();
        for (int i = 0; i < reservaSalvas.size(); i++){
            Reserva salvas = reservaSalvas.get(i);
            if(novaReserva.getEspaco().getId() == salvas.getEspaco().getId() &&
                    !novaReserva.getFim().isBefore(salvas.getInicio()) &&
                    !novaReserva.getInicio().isAfter(salvas.getFim())){
                    return true;
            }
        }
        return false;
    }

    public Reserva criarReserva(Reserva reserva){
        if (!reserva.getInicio().isBefore(reserva.getFim())){
            throw new ReservaInvalida("Reserva não pode ser concluida.");
        }
        if(!reserva.getEspaco().isDisponivel()){
            throw new ReservaInvalida("Reserva indispónivel.");
        }
        if(reservasSobrepostas(reserva)){
            throw new ReservaInvalida("Já existe uma reserva nesse hórario.");
        }
        reservaDao.salvar(reserva);
        return reserva;
    }

    public void cancelarReserva(int id){
        Reserva reserva = reservaDao.buscarPorId(id);

        if(reserva == null){
            throw new ReservaInvalida("Reserva não foi encontrada");
        }

        LocalDateTime agora = LocalDateTime.now();
        Duration tempoateIniciar = Duration.between(agora, reserva.getInicio());
        double horasateIniciar = (double) tempoateIniciar.toHours();

        if(horasateIniciar < 24){
            double valorTotal = reserva.calcularValorTotal();
            double taxa = valorTotal * 0.20;
            reserva.setTaxaCancelamento(taxa);
        }
        reserva.setStatus(Reserva.statusDaReserva.CANCELADA);
        reservaDao.atualizar(reserva);
    }

    public List<Reserva> listarReservas(){
        List<Reserva> reservas = reservaDao.listarTodos();
        if(reservas.isEmpty()){
            throw new ReservaInvalida("As reservas estão vazias.");
        }
        return reservas;
    }
}
