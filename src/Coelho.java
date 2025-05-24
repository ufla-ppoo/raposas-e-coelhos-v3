import java.util.List;

/**
 * Um modelo simples de um coelho.
 * Coelhos envelhecem, se movem, se reproduzem e morrem.
 * 
 * @author David J. Barnes e Michael Kölling
 *  Traduzido por Julio César Alves
 * @version 2025.05.24
 */
public class Coelho extends Animal
{
    // Características compartilhadas por todos os coelhos (atributos estáticos, da classe).

    // A idade em que um coelho pode começar a se reproduzir.
    private static final int IDADE_REPRODUCAO = 5;
    // A idade máxima que um coelho pode atingir.
    private static final int IDADE_MAXIMA = 40;
    // A probabilidade de um coelho se reproduzir.
    private static final double PROBABILIDADE_REPRODUCAO = 0.12;
    // O número máximo de filhotes que podem nascer de cada vez.
    private static final int TAMANHO_MAXIMO_NINHADA = 4;

    /**
     * Cria um novo coelho. Um coelho pode ser criado com idade
     * zero (recém-nascido) ou com uma idade aleatória.
     * 
     * @param idadeAleatoria Se verdadeiro, o coelho terá uma idade aleatória.
     * @param campo O campo atualmente ocupado.
     * @param localizacao A localização dentro do campo.
     */
    public Coelho(boolean idadeAleatoria, Campo campo, Localizacao localizacao)
    {
        super(idadeAleatoria, campo, localizacao);
    }

    @Override
    protected int obterIdadeMaxima() {
        return IDADE_MAXIMA;
    }

    /**
     * Isto é o que o coelho faz na maior parte do tempo: ele corre por aí.
     * Às vezes, ele se reproduz ou morre de velhice.
     * @param novosCoelhos Uma lista para retornar os coelhos recém-nascidos.
     */
    @Override
    public void agir(List<Animal> novosCoelhos)
    {
        incrementarIdade();
        if(estaVivo()) {
            reproduzir(novosCoelhos);            
            // Tenta se mover para uma localização livre.
            Localizacao novaLocalizacao = obterCampo().localizacaoVizinhaLivre(obterLocalizacao());
            if(novaLocalizacao != null) {
                definirLocalizacao(novaLocalizacao);
            }
            else {
                // Superlotação.
                morrer();
            }
        }
    }

    @Override
    protected Animal criarNovoFilhote(boolean idadeAleatoria, Campo campo, Localizacao localizacao) {
        return new Coelho(false, obterCampo(), localizacao);
    }

    protected int obterIdadeReproducao() {
        return IDADE_REPRODUCAO;
    }

    protected double obterProbabilidadeReproducao() {
        return PROBABILIDADE_REPRODUCAO;
    }

    protected int obterTamanhoMaximoNinhada() {
        return TAMANHO_MAXIMO_NINHADA;
    }
}