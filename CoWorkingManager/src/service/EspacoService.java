package service;

import dao.EspacoDao;
import dao.ReservaDao;
import model.CabineIndividual;
import model.Espaco;
import java.util.List;
import Exception.EspacoInvalido;
import Exception.ErroAoSalvar;

public class EspacoService {
    private EspacoDao espacoDao;
    private ReservaDao reservaDao;

    public EspacoService(){
        this.espacoDao = new EspacoDao();
        this.reservaDao = new ReservaDao();
    }

    public void Cadastrar_o_Espaco(Espaco espaco) throws EspacoInvalido{
        if(espaco.getNome() == null || espaco.getNome().trim().isEmpty()){
            throw new EspacoInvalido("O nome é obrigátorio.");
        }
        if(espaco.getCapacidade() <= 0){
            throw new EspacoInvalido("Capacidade Invalída.");
        }
        if (espaco instanceof CabineIndividual && espaco.getCapacidade() != 1) {
            throw new EspacoInvalido("Cabine Individual só pode ter 1 pessoa.");
        }
        espacoDao.salvar(espaco);
    }
    public List<Espaco> listarEspacos() throws EspacoInvalido{
        List<Espaco> espacos = espacoDao.listarTodos();
        if (espacos.isEmpty()){
            throw new EspacoInvalido("Espaços Vazios.");
        }
        return espacos;
    }

    public Espaco BuscarporId(int id) throws EspacoInvalido{
        Espaco espaco = espacoDao.buscarPorId(id);
        if(espaco == null){
            throw new EspacoInvalido("ID Inválido.");
        }
        return espaco;
    }

    public void atualizarEspaco(Espaco espaco) throws EspacoInvalido{
        if(espaco.getNome() == null || espaco.getNome().trim().isEmpty()){
            throw new EspacoInvalido("O nome é obrigatório.");
        }
        if(espaco.getCapacidade() <= 0){
            throw new EspacoInvalido("Capacidade inválida.");
        }
        if (espaco instanceof CabineIndividual && espaco.getCapacidade() != 1) {
            throw new EspacoInvalido("Cabine Individual só pode ter 1 pessoa.");
        }
        espacoDao.atualizar(espaco);
    }

    public void deletarEspaco(int id) throws EspacoInvalido{
        Espaco espaco = espacoDao.buscarPorId(id);
        if(espaco == null){
            throw new EspacoInvalido("ID inválido.");
        }

        espacoDao.deletar(id);
    }
}