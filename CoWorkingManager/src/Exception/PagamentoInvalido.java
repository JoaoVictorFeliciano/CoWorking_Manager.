package Exception;

public class PagamentoInvalido extends RuntimeException {
    public PagamentoInvalido(String message) {
        super(message);
    }
}
