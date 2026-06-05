// classe para organizarmos os dados do jogo
class Jogador {
    private String nome;
    private int creditos;
    private int vitorias;
    private int derrotas;

    Jogador(String nome, int creditos) {
        this.nome = nome;
        this.creditos = creditos;
        this.vitorias = 0;
        this.derrotas = 0;
    }
    String getNome() {
        return this.nome;
    }
    int getCreditos() {
        return this.creditos;
    }

    int getVitorias() {
        return this.vitorias;
    }
    int getDerrotas() {
        return this.derrotas;
    }
    void adicionarCreditos(int valor) {
        this.creditos = this.creditos + valor;
    }

    void removerCreditos(int valor) {
        this.creditos = this.creditos - valor;
    }

    void registrarVitoria() {
        this.vitorias = this.vitorias + 1;
    }

    void registrarDerrotas() {
        this.derrotas = this.derrotas + 1;
    }

    public String toString() {
        return this.nome + " | Créditos: " + this.creditos +
            " | Vitórias: " + this.vitorias +
            " | Derrotas: " + this.derrotas;
    }
}
