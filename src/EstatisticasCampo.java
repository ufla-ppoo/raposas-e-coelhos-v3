import java.util.HashMap;

/**
 * Esta classe coleta e fornece alguns dados estatísticos sobre o estado 
 * de um campo. É flexível: cria e mantém um contador para qualquer classe 
 * de objeto encontrada no campo.
 * 
 * @author David J. Barnes e Michael Kölling
 *  Traduzido por Julio César Alves
 * @version 2025.05.24
 */
public class EstatisticasCampo
{
    // Contadores para cada tipo de entidade (raposa, coelho, etc.) na simulação.
    private HashMap<Class<?>, Contador> contadores;
    // Indica se os contadores estão atualmente atualizados.
    private boolean contagensValidas;

    /**
     * Constrói um objeto EstatisticasCampo.
     */
    public EstatisticasCampo()
    {
        // Configura uma coleção de contadores para cada tipo de animal que
        // possa ser encontrado.
        contadores = new HashMap<>();
        contagensValidas = true;
    }

    /**
     * Retorna detalhes sobre o que está no campo.
     * @return Uma string descrevendo o que está no campo.
     */
    public String obterDetalhesPopulacao(Campo campo)
    {
        StringBuffer buffer = new StringBuffer();
        if(!contagensValidas) {
            gerarContagens(campo);
        }
        for(Class<?> chave : contadores.keySet()) {
            Contador info = contadores.get(chave);
            buffer.append(info.obterNome());
            buffer.append(": ");
            buffer.append(info.obterContagem());
            buffer.append(' ');
        }
        return buffer.toString();
    }


    
    /**
     * Obtém o número de indivíduos na população de uma determinada classe
     * @return  Um inteiro com o número para a classe passada.
     */
    public int obterContagemPopulacao(Campo campo, Class<?> key)
    {
        if(!contagensValidas) {
            gerarContagens(campo);
        }

        Contador contador = contadores.get(key);
        return contador.obterContagem();
    }
    
    /**
     * Invalida o conjunto atual de estatísticas; redefine todas 
     * as contagens para zero.
     */
    public void reiniciar()
    {
        contagensValidas = false;
        for(Class<?> chave : contadores.keySet()) {
            Contador contador = contadores.get(chave);
            contador.reiniciar();
        }
    }

    /**
     * Incrementa a contagem para uma classe de animal.
     * @param classeAnimal A classe do animal a ser incrementada.
     */
    public void incrementarContagem(Class<?> classeAnimal)
    {
        Contador contador = contadores.get(classeAnimal);
        if(contador == null) {
            // Ainda não há um contador para esta espécie.
            // Cria um.
            contador = new Contador(classeAnimal.getName());
            contadores.put(classeAnimal, contador);
        }
        contador.incrementar();
    }

    /**
     * Indica que a contagem de animais foi concluída.
     */
    public void finalizarContagem()
    {
        contagensValidas = true;
    }

    /**
     * Determina se a simulação ainda é viável.
     * Ou seja, se deve continuar a ser executada.
     * @return true Se houver mais de uma espécie viva.
     */
    public boolean ehViavel(Campo campo)
    {
        // Quantas contagens são diferentes de zero.
        int naoZero = 0;
        if(!contagensValidas) {
            gerarContagens(campo);
        }
        for(Class<?> chave : contadores.keySet()) {
            Contador info = contadores.get(chave);
            if(info.obterContagem() > 0) {
                naoZero++;
            }
        }
        return naoZero > 1;
    }
    
    /**
     * Gera contagens do número de raposas e coelhos.
     * Essas contagens não são mantidas atualizadas à medida que raposas e coelhos
     * são colocados no campo, mas apenas quando uma solicitação
     * é feita para obter as informações.
     * @param campo O campo para o qual gerar as estatísticas.
     */
    private void gerarContagens(Campo campo)
    {
        reiniciar();
        for(int linha = 0; linha < campo.obterComprimento(); linha++) {
            for(int coluna = 0; coluna < campo.obterLargura(); coluna++) {
                Object animal = campo.obterObjetoEm(linha, coluna);
                if(animal != null) {
                    incrementarContagem(animal.getClass());
                }
            }
        }
        contagensValidas = true;
    }
}