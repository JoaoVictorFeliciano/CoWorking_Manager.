package dao;


import java.io.FileWriter;
import java.util.List;
import model.Auditorio;
import model.CabineIndividual;
import model.Espaco;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Files;
import java.nio.file.Paths;
import model.SalaDeReuniao;
import Exception.ErroAoSalvar;

public class EspacoDao implements Persistencia<Espaco> {
    private List<Espaco> espacos;

    private void salvarJson() {
        try {
            JsonArray jsonArray = new JsonArray();
            Gson gson = new Gson();

            for (Espaco espaco : espacos) {
                JsonElement element = gson.toJsonTree(espaco);
                element.getAsJsonObject().addProperty("tipo", espaco.getClass().getSimpleName());
                jsonArray.add(element);
            }

            String json = gson.toJson(jsonArray);
            try (FileWriter writer = new FileWriter("Espacos.json")) {
                writer.write(json);
            }

        } catch (Exception erro) {
            throw new ErroAoSalvar("Não foi possível salvar o espaço.");
        }
    }

    private void carregarJson() {
        try {
            // le todos os bytes do arquivo e transforma em uma string.
            String jsonString = new String(Files.readAllBytes(Paths.get("Espacos.json")));

            if (jsonString.trim().isEmpty()) {
                this.espacos = new ArrayList<>();
                return;
            }
            // pega a o jsonstring e transforma num array de objetos
            JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
            this.espacos = new ArrayList<>();
            Gson gson = new Gson();
            for(JsonElement elemento : jsonArray){
                JsonObject objeto = elemento.getAsJsonObject();
                String tipo = objeto.get("tipo").getAsString();

                Espaco espaco;
                switch (tipo){
                    case "SalaDeReuniao":
                        espaco = gson.fromJson(elemento, SalaDeReuniao.class);
                        break;
                    case "CabineIndividual":
                        espaco = gson.fromJson(elemento, CabineIndividual.class);
                        break;
                    case "Auditorio":
                        espaco = gson.fromJson(elemento, Auditorio.class);
                        break;
                    default:
                        continue;
                }
                this.espacos.add(espaco);
            }
        } catch (Exception erro) {
            this.espacos = new ArrayList<>();
        }
    }

    public EspacoDao(){
        carregarJson();
    }

    public int gerarNovoId() {
        List<Espaco> espacos = listarTodos();
        if (espacos.isEmpty()) {
            return 1;
        }
        int ultimoId = espacos.get(espacos.size() - 1).getId();
        return ultimoId + 1;
    }

    @Override
    public void salvar(Espaco obj) {
        int novoId = gerarNovoId();
        obj.setId(novoId);
        espacos.add(obj);
        salvarJson();
    }

    @Override
    public List<Espaco> listarTodos() {
        return espacos;
    }

    @Override
    public Espaco buscarPorId(int id) {
        for(int i = 0; i < espacos.size(); i++){
            Espaco espaco = espacos.get(i);
            if(espaco.getId() == id){
                return espaco;
            }
        }
        return null;
    }

    @Override
    public void atualizar(Espaco obj) {
        for(int i = 0; i < espacos.size(); i++){
            Espaco espaco = espacos.get(i);
            if(espaco.getId() == obj.getId()){
                espacos.set(i, obj);
                salvarJson();
                return;
            }
        }
    }

    @Override
    public void deletar(int id) {
        for(int i = 0; i <espacos.size(); i++){
            Espaco espaco = espacos.get(i);
            if(espaco.getId() == id){
                espacos.remove(i);
                salvarJson();
            }
        }
    }
}