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

    private boolean reservasSobrepostas(Reserva nova) {
        List<Reserva> existentes = reservaDao.listarTodos();

        for (Reserva r : existentes) {

            if (r.getEspaco().getId() == nova.getEspaco().getId()
                    && r.getStatus() != Reserva.statusDaReserva.CANCELADA) {

                boolean sobrepoe =
                        nova.getInicio().isBefore(r.getFim()) &&
                                nova.getFim().isAfter(r.getInicio());
                if (sobrepoe) {
                    return true;
                }
            }
        }
        return false;
    }

    public Reserva criarReserva(Reserva reserva) throws ReservaInvalida{
        if (!reserva.getInicio().isBefore(reserva.getFim())){
            throw new ReservaInvalida("Reserva não pode ser concluida.");
        }
        if(reservasSobrepostas(reserva)){
            throw new ReservaInvalida("Já existe uma reserva nesse horário e nesse espaço.");
        }

        reserva.getEspaco().setDisponivel(false);
        espacoDao.atualizar(reserva.getEspaco());
        reservaDao.salvar(reserva);
        return reserva;
    }

    public void cancelarReserva(int id) throws ReservaInvalida{
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

        reserva.getEspaco().setDisponivel(true);
        espacoDao.atualizar(reserva.getEspaco());

        reserva.setStatus(Reserva.statusDaReserva.CANCELADA);
        reservaDao.atualizar(reserva);
    }

    public List<Reserva> listarReservas() throws ReservaInvalida{
        List<Reserva> reservas = reservaDao.listarTodos();
        if(reservas.isEmpty()){
            throw new ReservaInvalida("As reservas estão vazias.");
        }
        return reservas;
    }
}