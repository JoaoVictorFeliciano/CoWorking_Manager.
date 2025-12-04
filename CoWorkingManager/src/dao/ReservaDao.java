package dao;

import com.google.gson.*;
import model.Espaco;
import model.Reserva;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.time.LocalDateTime;
import Exception.ErroAoSalvar;

public class ReservaDao implements Persistencia<Reserva>{
    private List<Reserva> reservas;
    private Gson gson;
    private EspacoDao espacoDao;

    private void salvarJson() {
        try {
            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            for (Reserva reserva : reservas) {
                JsonObject reservaObj = new JsonObject();
                reservaObj.addProperty("id", reserva.getId());
                reservaObj.addProperty("espacoId", reserva.getEspaco().getId());
                reservaObj.addProperty("inicio", reserva.getInicio().toString());
                reservaObj.addProperty("fim", reserva.getFim().toString());
                reservaObj.addProperty("status", reserva.getStatus().name());
                reservaObj.addProperty("taxaCancelamento", reserva.getTaxaCancelamento());

                jsonArray.add(reservaObj);
            }

            String json = gson.toJson(jsonArray);
            try (FileWriter writer = new FileWriter("Reservas.json")) {
                writer.write(json);
            }
        } catch (Exception erro) {
            throw new ErroAoSalvar("Não foi possível salvar a reserva.");
        }
    }

    private void carregarJson() {
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get("Reservas.json")));

            if (jsonString.trim().isEmpty()) {
                this.reservas = new ArrayList<>();
                return;
            }

            JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
            this.reservas = new ArrayList<>();
            this.espacoDao = new EspacoDao();

            for (JsonElement elemento : jsonArray) {
                JsonObject objeto = elemento.getAsJsonObject();

                int id = objeto.get("id").getAsInt();
                int espacoId = objeto.get("espacoId").getAsInt();
                LocalDateTime inicio = LocalDateTime.parse(objeto.get("inicio").getAsString());
                LocalDateTime fim = LocalDateTime.parse(objeto.get("fim").getAsString());
                Reserva.statusDaReserva status = Reserva.statusDaReserva.valueOf(objeto.get("status").getAsString());
                double taxaCancelamento = objeto.get("taxaCancelamento").getAsDouble();

                Espaco espaco = espacoDao.buscarPorId(espacoId);

                if (espaco != null) {
                    Reserva reserva = new Reserva(id, espaco, inicio, fim);
                    reserva.setStatus(status);
                    reserva.setTaxaCancelamento(taxaCancelamento);

                    this.reservas.add(reserva);
                }
            }
        } catch (Exception erro) {
            this.reservas = new ArrayList<>();
        }
    }

    public ReservaDao(){
        carregarJson();
    }

    public int gerarNovoId() {
        List<Reserva> reservas = listarTodos();
        if (reservas.isEmpty()){
            return 1;
        }
        int ultimoId = reservas.get(reservas.size() - 1).getId();
        return ultimoId + 1;
    }

    @Override
    public void salvar(Reserva obj) {
        int novoId = gerarNovoId();
        obj.setId(novoId);
        reservas.add(obj);
        salvarJson();
    }

    @Override
    public List<Reserva> listarTodos() {
        return reservas;
    }

    @Override
    public Reserva buscarPorId(int id) {
        for(int i = 0; i < reservas.size(); i++){
            Reserva reserva = reservas.get(i);
            if(reserva.getId() == id){
                return reserva;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Reserva obj) {
        for(int i = 0; i < reservas.size(); i++){
            Reserva reserva = reservas.get(i);
            if(reserva.getId() == obj.getId()){
                reservas.set(i, obj);
                salvarJson();
                return;
            }
        }
    }

    @Override
    public void deletar(int id) {
        for(int i = 0; i < reservas.size(); i++){
            Reserva reserva = reservas.get(i);
            if(reserva.getId() == id){
                reservas.remove(i);
                salvarJson();
            }
        }
    }
}
