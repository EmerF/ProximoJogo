package br.com.proximojogo.proximojogo.entity;

/**
 * Created by emer on 07/11/17.
 */

public class Arena {
    private String idArena;
    private String Responsavel;
    private String NomeArena;
    private String Endereco;
    private String Telefone;
    private Long HorarioIniAtendimento;
    private Long HorarioFimAtendimento;

    public String getIdArena() {
        return idArena;
    }

    public void setIdArena(String idArena) {
        this.idArena = idArena;
    }

    public String getResponsavel() {
        return Responsavel;
    }

    public void setResponsavel(String responsavel) {
        Responsavel = responsavel;
    }

    public String getNomeArena() {
        return NomeArena;
    }

    public void setNomeArena(String nomeArena) {
        NomeArena = nomeArena;
    }

    public String getEndereco() {
        return Endereco;
    }

    public void setEndereco(String endereco) {
        Endereco = endereco;
    }

    public String getTelefone() {
        return Telefone;
    }

    public void setTelefone(String telefone) {
        Telefone = telefone;
    }

    public Long getHorarioIniAtendimento() {
        return HorarioIniAtendimento;
    }

    public void setHorarioIniAtendimento(Long horarioIniAtendimento) {
        HorarioIniAtendimento = horarioIniAtendimento;
    }

    public Long getHorarioFimAtendimento() {
        return HorarioFimAtendimento;
    }

    public void setHorarioFimAtendimento(Long horarioFimAtendimento) {
        HorarioFimAtendimento = horarioFimAtendimento;
    }
}
