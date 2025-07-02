package RNA;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Perceptron {

    public List<Ponto> amostras = new ArrayList<>();
    public List<Integer> saidas = new ArrayList<>();
    public double taxa_aprendizado;
    public int geracoes, limiar, numAmostras;

    public double[] pesos;

    public Perceptron(List<Ponto> amostras, List<Integer> saidas, double taxa_aprendizado, int geracoes, int limiar) {
        this.amostras = amostras;
        this.saidas = saidas;
        this.taxa_aprendizado = taxa_aprendizado;
        this.geracoes = geracoes;
        this.limiar = limiar;
        this.numAmostras = amostras.size();
        this.pesos = new double[3];
    }

    private int funcao_ativacao_signal(double soma) {
        if (soma >= 0) return 1;
        return -1;
    }


    public void treinar() {

        for (int i = 0; i < this.amostras.size(); i++) {
            amostras.get(i).limiar = this.limiar;
        }

        Random gerador = new Random();
        pesos[0] = limiar;
        pesos[1] = gerador.nextDouble();
        pesos[2] = gerador.nextDouble();

        int conta = 0;
        boolean aprendeu;
        double soma;
        int saida_gerada;

        while (true) {
            aprendeu = true;
            soma = 0;

            for (int i = 0; i < amostras.size(); i++) {
                soma = soma + (amostras.get(i).limiar * pesos[0]) + (amostras.get(i).x * pesos[1]) + (amostras.get(i).y * pesos[2]);

                saida_gerada = funcao_ativacao_signal(soma);

                if (saida_gerada != this.saidas.get(i)) {
                    aprendeu = false;
                    double erro = this.saidas.get(i) - saida_gerada;

                    this.pesos[0] = this.pesos[0] + this.taxa_aprendizado * erro * this.amostras.get(i).limiar;
                    this.pesos[1] = this.pesos[1] + this.taxa_aprendizado * erro * this.amostras.get(i).x;
                    this.pesos[2] = this.pesos[2] + this.taxa_aprendizado * erro * this.amostras.get(i).y;
                }
            }

            conta++;

            if (aprendeu || conta > this.geracoes) {
                System.out.println("Geracoes de treinamento: " + conta);
                break;
            }
        }
    }

    public void testar(Ponto amostra) {

        amostra.limiar = this.limiar;

        double soma = 0;
        soma = soma + (amostra.limiar * pesos[0]) + (amostra.x * pesos[1]) + (amostra.y * pesos[2]);

        double saida_gerada = this.funcao_ativacao_signal(soma);

        if (saida_gerada == 1) {
            System.out.println("Classe: " + saida_gerada + " ou Time Azul");
        } else {
            System.out.println("Classe: " + saida_gerada + " ou Time Vermelho");
        }

    }

}
