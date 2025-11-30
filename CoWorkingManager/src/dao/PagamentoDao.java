package dao;

import model.Pagamento;
import model.Reserva;
import java.util.List;
import java.util.ArrayList;
import java.io.FileWriter;
import Exception.ErroAoSalvar;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class PagamentoDao implements Persistencia<Pagamento> {
    private List<Pagamento> pagamentos;
    private ReservaDao reservaDao;

    private void salvarJson(){
        try {
            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            for (Pagamento pagamento : pagamentos) {
                JsonObject pagamentoObj = new JsonObject();
                pagamentoObj.addProperty("id", pagamento.getId());
                pagamentoObj.addProperty("reservaId", pagamento.getReserva().getId());
                pagamentoObj.addProperty("valorPago", pagamento.getValorPago());
                pagamentoObj.addProperty("dataPagamento", pagamento.getDataPagamento().toString());
                pagamentoObj.addProperty("metodo", pagamento.getMetodo().name());

                jsonArray.add(pagamentoObj);
            }

            String json = gson.toJson(jsonArray);
            try (FileWriter writer = new FileWriter("Pagamentos.json")) {
                writer.write(json);
            }

        } catch (Exception erro) {
            throw new ErroAoSalvar("Não foi possível salvar o pagamento.");
        }
    }

    private void carregarJson(){
        try{
            String jsonString = new String(Files.readAllBytes(Paths.get("Pagamentos.json")));

            if (jsonString.trim().isEmpty()) {
                this.pagamentos = new ArrayList<>();
                return;
            }

            JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
            this.pagamentos = new ArrayList<>();
            this.reservaDao = new ReservaDao();

            for(JsonElement elemento : jsonArray){
                JsonObject objeto = elemento.getAsJsonObject();

                int id = objeto.get("id").getAsInt();
                int reservaId = objeto.get("reservaId").getAsInt();
                double valorPago = objeto.get("valorPago").getAsDouble();
                LocalDateTime dataPagamento = LocalDateTime.parse(objeto.get("dataPagamento").getAsString());
                Pagamento.MetodoPagamento metodo = Pagamento.MetodoPagamento.valueOf(objeto.get("metodo").getAsString());

                Reserva reserva = reservaDao.buscarPorId(reservaId);
                if (reserva != null) {
                    Pagamento pagamento = new Pagamento(id, reserva, valorPago, dataPagamento, metodo);
                    this.pagamentos.add(pagamento);
                }
            }
        } catch (Exception erro) {
            this.pagamentos = new ArrayList<>();
        }
    }

    public PagamentoDao(){
        carregarJson();
    }

    public int gerarNovoId() {
        List<Pagamento> pagamentos = listarTodos();
        if (pagamentos.isEmpty()){
            return 1;
        }
        int ultimoId = pagamentos.get(pagamentos.size() - 1).getId();
        return ultimoId + 1;
    }

    @Override
    public void salvar(Pagamento obj) {
        int novoId = gerarNovoId();
        obj.setId(novoId);
        pagamentos.add(obj);
        salvarJson();
    }

    @Override
    public List<Pagamento> listarTodos() {
        return pagamentos;
    }

    @Override
    public Pagamento buscarPorId(int id) {
        for(int i = 0; i < pagamentos.size(); i++){
            Pagamento pagamento = pagamentos.get(i);
            if(pagamento.getId() == id){
                return pagamento;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Pagamento obj) {
        for(int i = 0; i < pagamentos.size(); i++){
            Pagamento pagamento = pagamentos.get(i);
            if(pagamento.getId() == obj.getId()){
                pagamentos.set(i, obj);
                salvarJson();
                return;
            }
        }
    }

    @Override
    public void deletar(int id) {
        for(int i = 0; i < pagamentos.size(); i++){
            Pagamento pagamento = pagamentos.get(i);
            if(pagamento.getId() == id){
                pagamentos.remove(i);
                salvarJson();
                return;
            }
        }
    }
}