class TesteRoleta {
    public static void main(String[] args) {
        Roleta roleta = new Roleta();
        Jogador jogador = new Jogador("Caio", 100);
        listaLigadaD historico = new listaLigadaD();
        System.out.println("=== TESTE DO JOGO DA ROLETA ===");
        System.out.println("Jogador criado:");
        System.out.println(jogador);
        System.out.println();

        Rodada rodada1 = roleta.jogar(jogador, "NUMERO", "17", 10);
        historico.adiciona(rodada1);
        Rodada rodada2 = roleta.jogar(jogador, "COR", "VERMELHO", 10);
        historico.adiciona(rodada2);
        Rodada rodada3 = roleta.jogar(jogador, "PARIDADE", "PAR", 10);
        historico.adiciona(rodada3);
        Rodada rodada4 = roleta.jogar(jogador, "PARIDADE", "PAR", 10);
        historico.adiciona(rodada4);
        System.out.println("=== HISTÓRICO DE RODADAS ===");
        for (int i = 0; i < historico.tamanho(); i++) {
            System.out.println((historico.pega(i)));
        }
        System.out.println();
        System.out.println("=== SITUAÇÃO FINAL DO JOGADOR ===");
        System.out.println(jogador);
    }  
}
