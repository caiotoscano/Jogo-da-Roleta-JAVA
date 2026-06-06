class Rodada {
    private String nomeJogador;
    private String tipoAposta;
    private String escolha;
    private int valorApostado;
    private int numeroSorteado;
    private String corSorteada;
    private boolean ganhou;
    private int creditosDepois;

    Rodada(String nomeJogador, String tipoAposta, String escolha, int valorApostado, int numeroSorteado, String corSorteada,
        boolean ganhou, int creditoDepois) {
            this.nomeJogador = nomeJogador;
            this.tipoAposta = tipoAposta;
            this.escolha = escolha;
            this.valorApostado = valorApostado;
            this.numeroSorteado = numeroSorteado;
            this.corSorteada = corSorteada;
            this.ganhou = ganhou;
            this.creditosDepois = creditoDepois;
        }

        String getNomeJogador() {
            return this.nomeJogador;
        }
        String getTipoAposta() {
            return this.tipoAposta;
        }
        String getEscolha() {
            return this.escolha;
        }
        int getValorApostado() {
            return this.valorApostado;
        }
        int getNumeroSorteado(){
            return this.numeroSorteado;
        }
        String getCorSorteada() {
            return this.corSorteada;
        }
        boolean getGanhou() {
            return this.ganhou;
        }
        int getCreditoDepois() {
            return this.creditosDepois;
        }

        public String toString() {
            String resultado;
            if (this.ganhou) {
                resultado = "GANHOU";
            } else {
                resultado = "PERDEU";
            }

            return "Jogador: " + this.nomeJogador +
                   " | Aposta: " + this.tipoAposta +
                   " | Escolha: " + this.escolha +
                   " | Valor: " + this.valorApostado +
                   " | Sorteado: " + this.numeroSorteado + " " + this.corSorteada +
                   " | Resultado: " + resultado +
                   " | Créditos após rodada: " + this.creditosDepois;
        }
}
