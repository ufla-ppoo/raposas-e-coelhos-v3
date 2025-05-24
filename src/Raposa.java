import java.util.List;
import java.util.Iterator;
import java.util.Random;

/**
 * Um modelo simples de uma raposa.
 * Raposas envelhecem, se movem, comem coelhos e morrem.
 * 
 * @author David J. Barnes e Michael Kölling
 *  Traduzido por Julio César Alves
 * @version 2025.05.24
 */
public class Raposa extends Animal
{
    // Características compartilhadas por todas as raposas (atributos estáticos, da classe).
    
    // A idade em que uma raposa pode começar a procriar.
    private static final int IDADE_REPRODUCAO = 15;
    // A idade máxima que uma raposa pode atingir.
    private static final int IDADE_MAXIMA = 150;
    // A probabilidade de uma raposa se reproduzir.
    private static final double PROBABILIDADE_REPRODUCAO = 0.08;
    // O número máximo de filhotes que podem nascer de cada vez.
    private static final int TAMANHO_MAXIMO_NINHADA = 2;
    // O valor nutricional de um único coelho. Na prática, este é o
    // número de passos que uma raposa pode dar antes de precisar comer novamente.
    private static final int VALOR_COMIDA_COELHO = 9;
    // nível máximo de comida que a raposa pode atingir (fica cheia)
    private static final int NIVEL_COMIDA_MAXIMO = 20;
    // Um gerador de números aleatórios compartilhado para controlar a reprodução.
    private static final Random rand = Randomizador.obterRandom();
    
    // Características individuais (atributos comuns, de instância).

    // O nível de comida da raposa, que aumenta ao comer coelhos.
    private int nivelComida;

    /**
     * Cria uma raposa. Uma raposa pode ser criada como recém-nascida (idade zero
     * e sem fome) ou com idade e nível de fome aleatórios.
     * 
     * @param idadeAleatoria Se verdadeiro, a raposa terá idade e nível de fome aleatórios.
     * @param campo O campo atualmente ocupado.
     * @param localizacao A localização dentro do campo.
     */
    public Raposa(boolean idadeAleatoria, Campo campo, Localizacao localizacao)
    {
        super(idadeAleatoria, campo, localizacao);
        if(idadeAleatoria) {
            nivelComida = rand.nextInt(VALOR_COMIDA_COELHO);
        }
        else {
            nivelComida = VALOR_COMIDA_COELHO;
        }
    }

    @Override
    protected int obterIdadeMaxima() {
        return IDADE_MAXIMA;
    }
    
    /**
     * Isto é o que a raposa faz na maior parte do tempo: ela caça coelhos.
     * Durante o processo, ela pode se reproduzir, morrer de fome
     * ou morrer de velhice.
     * @param novasRaposas Uma lista para retornar as raposas recém-nascidas.
     */
    @Override
    public void agir(List<Animal> novasRaposas)
    {
        incrementarIdade();
        incrementarFome();
        if(estaVivo()) {
            reproduzir(novasRaposas);            
            // Move-se em direção a uma fonte de comida, se encontrada.
            Localizacao novaLocalizacao = buscarComida();
            if(novaLocalizacao == null) { 
                // Nenhuma comida encontrada - tenta se mover para uma localização livre.
                novaLocalizacao = obterCampo().localizacaoVizinhaLivre(obterLocalizacao());
            }
            // Verifica se foi possível se mover.
            if(novaLocalizacao != null) {
                definirLocalizacao(novaLocalizacao);
            }
            else {
                // Superlotação.
                morrer();
            }
        }
    }
    
    /**
     * Faz com que esta raposa fique mais faminta. Isso pode resultar na morte da raposa.
     */
    private void incrementarFome()
    {
        nivelComida--;
        if(nivelComida <= 0) {
            morrer();
        }
    }
    
    /**
     * Procura por coelhos adjacentes à localização atual.
     * Apenas o primeiro coelho vivo é comido.
     * @return Onde a comida foi encontrada, ou null se não foi.
     */
    private Localizacao buscarComida()
    {
        List<Localizacao> vizinhas = obterCampo().localizacoesVizinhas(obterLocalizacao());
		Iterator<Localizacao> it = vizinhas.iterator();
		Localizacao localizacaoFinal = null;
		while(it.hasNext()) {
		    Localizacao onde = it.next();
		    Object animal = obterCampo().obterObjetoEm(onde);
		    if(animal instanceof Coelho) {
		        Coelho coelho = (Coelho) animal;
		        if(coelho.estaVivo()) { 
		            coelho.morrer();
		            nivelComida += VALOR_COMIDA_COELHO;
		            if (nivelComida > NIVEL_COMIDA_MAXIMO) {
		                nivelComida = NIVEL_COMIDA_MAXIMO;
		            }
		            localizacaoFinal = onde;
		        }
		    }
		}
		return localizacaoFinal;
    }

    @Override
    protected Animal criarNovoFilhote(boolean idadeAleatoria, Campo campo, Localizacao localizacao) {
        return new Raposa(false, obterCampo(), localizacao);
    }

    @Override
    protected int obterIdadeReproducao() {
        return IDADE_REPRODUCAO;
    }

    @Override
    protected double obterProbabilidadeReproducao() {
        return PROBABILIDADE_REPRODUCAO;
    }

    @Override
    protected int obterTamanhoMaximoNinhada() {
        return TAMANHO_MAXIMO_NINHADA;
    }
}