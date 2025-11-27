package service;
import model.Pagamento;
import model.Reserva;
import dao.PagamentoDao;
import dao.ReservaDao;
import java.time.LocalDateTime;
import java.util.List;
import Exception.IDInvalido;
import Exception.ReservaInvalida;
import Exception.PagamentoInvalido;

public class PagamentoService {
    private PagamentoDao pagamentoDao;
    private ReservaDao reservaDao;

    public PagamentoService(){
        this.pagamentoDao = new PagamentoDao();
        this.reservaDao = new ReservaDao();
    }

    public Pagamento registrarPagamentos(int reservaID, double valorPago, Pagamento.MetodoPagamento metodo){
        Reserva reserva = reservaDao.buscarPorId(reservaID);

        if (reserva == null){
            throw new IDInvalido("ID não encontrado.");
        }
        if(reserva.getStatus() == Reserva.statusDaReserva.CANCELADA){
            throw new ReservaInvalida("A reserva foi cancelada.");
        }
        double valorReserva = reserva.calcularValorTotal();

        if (valorPago < valorReserva) {
            throw new RuntimeException("Valor pago é menor que o valor da reserva.");
        }

        int novoID = pagamentoDao.gerarNovoId();
        Pagamento pagamento = new Pagamento(novoID, reserva, valorPago, LocalDateTime.now(), metodo);

        pagamentoDao.salvar(pagamento);
        reserva.setStatus(Reserva.statusDaReserva.CONFIRMADA);
        reservaDao.atualizar(reserva);
        return pagamento;
    }

    public List<Pagamento> listarPagamentos(){
        List<Pagamento> pagamentos = pagamentoDao.listarTodos();
        if(pagamentos.isEmpty()){
            throw new PagamentoInvalido("A lista de pagamentos está vazia.");
        }
        return pagamentos;
    }



}
