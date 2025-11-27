package service;

import dao.EspacoDao;
import dao.ReservaDao;
import model.CabineIndividual;
import model.Espaco;
import java.util.List;
import Exception.NomeInvalido;
import Exception.CapacidadeInvalida;
import Exception.IDInvalido;
import Exception.EspacoInvalido;

public class EspacoService {
    private EspacoDao espacoDao;
    private ReservaDao reservaDao;

    public EspacoService(){
        this.espacoDao = new EspacoDao();
        this.reservaDao = new ReservaDao();
    }

    public void Cadastrar_o_Espaco(Espaco espaco){
        if(espaco.getNome() == null || espaco.getNome().trim().isEmpty()){
            throw new NomeInvalido("O nome é obrigátorio.");
        }
        if(espaco.getCapacidade() <= 0){
            throw new CapacidadeInvalida("Capacidade Invalída.");
        }
        if (espaco instanceof CabineIndividual && espaco.getCapacidade() != 1) {
            throw new CapacidadeInvalida("Cabine Individual só pode ter 1 pessoa.");
        }
        espacoDao.salvar(espaco);
    }
    public List<Espaco> listarEspacos(){
        List<Espaco> espacos = espacoDao.listarTodos();
        if (espacos.isEmpty()){
            throw new EspacoInvalido("Espaços Vazios.");
        }
        return espacos;
    }

    public Espaco BuscarporId(int id){
        Espaco espaco = espacoDao.buscarPorId(id);
        if(espaco == null){
            throw new IDInvalido("ID Inválido.");
        }
        return espaco;
    }

    public void atualizarEspaco(Espaco espaco){
        if(espaco.getNome() == null || espaco.getNome().trim().isEmpty()){
            throw new NomeInvalido("O nome é obrigatório.");
        }
        if(espaco.getCapacidade() <= 0){
            throw new CapacidadeInvalida("Capacidade inválida.");
        }
        if (espaco instanceof CabineIndividual && espaco.getCapacidade() != 1) {
            throw new CapacidadeInvalida("Cabine Individual só pode ter 1 pessoa.");
        }
        espacoDao.atualizar(espaco);
    }

    public void deletarEspaco(int id){
        Espaco espaco = espacoDao.buscarPorId(id);
        if(espaco == null){
            throw new IDInvalido("ID inválido.");
        }

        espacoDao.deletar(id);
    }
}