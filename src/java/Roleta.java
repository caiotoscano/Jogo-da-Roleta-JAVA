//precisa trocar os arrays int por lista simples wagner vai gostar 
class Roleta {
    private ListaCircular numerosRoleta;
    private ListaSkip numerosValidos;
    private ListaSkip numerosVermelhos;
    private ListaSkip numerosPretos;

    Roleta() {
        numerosRoleta = new ListaCircular();
        numerosValidos = new ListaSkip(-1, 6);
        numerosVermelhos = new ListaSkip(-1, 6);
        numerosPretos = new ListaSkip(-1, 6);
        montarRoleta();
        montarListaDeBusca();
    }
    private void montarRoleta() {
        int [] ordemRoleta = {
            0, 32, 15, 19, 4, 21, 2, 25, 17, 34,
            6, 27, 13, 36, 11, 30, 8, 23, 10, 5,
            24, 16, 33, 1, 20, 14, 31, 9, 22, 18,
            29, 7, 28, 12, 35, 3, 26
        };
        for (int i = 0; i < ordemRoleta.length; i++) {
            numerosRoleta.adiciona(ordemRoleta[i]);
        }
    }
    private void montarListaDeBusca() {
        for (int i = 0; i <= 36; i++) {
            numerosValidos.insert(i);
        }
        int [] vermelhos = {
            1, 3, 5, 7, 9, 12, 14, 16, 18,
            19, 21, 23, 25, 27, 30, 32, 34, 36
        };
        int[] pretos = {
            2, 4, 6, 8, 10, 11, 13, 15, 17,
            20, 22, 24, 26, 28, 29, 31, 33, 35
        };
        for (int i = 0; i < vermelhos.length; i++) {
            numerosVermelhos.insert(vermelhos[i]);
        }
        for (int i = 0; i < pretos.length; i++) {
            numerosPretos.insert(pretos[i]);
        }
    }
    int girar() {
        int passos = (int) (Math.random() * 200) + 37;
        numerosRoleta.avanca(passos);
        return (Integer) numerosRoleta.getelement();
    }
    boolean numeroValido(int numero) {
        if (numero < 0 || numero > 36) {
            return false;
        }
        return numerosValidos.search(numero);
    }
    String getCor(int numero) {
        if (numero == 0) {
            return "VERDE";
        }
        if (numerosVermelhos.search(numero)) {
            return "VERMELHO";
        }
        if (numerosPretos.search(numero)) {
            return "PRETO";
    }
        return "INDEFINIDO";
}

boolean verificarAposta(String tipoAposta, String escolha, int numeroSorteado) {
    if (tipoAposta.equals("NUMERO")) {
        int numeroEscolhido = Integer.parseInt(escolha);
        return numeroEscolhido == numeroSorteado;
    }
    if (tipoAposta.equals("COR")) {
        String corSorteada = getCor(numeroSorteado);
        return escolha.equals(corSorteada);
    }
    if (tipoAposta.equals("PARIDADE")) {
        if (numeroSorteado == 0) {
            return false;
        }
        if (escolha.equals("PAR")) {
            return numeroSorteado % 2 == 0;
        } else {
            return numeroSorteado % 2 != 0;
        }
    }

    if (tipoAposta.equals("ALTURA")) {
        if (numeroSorteado == 0) {
            return false;
        }
        if (escolha.equals("BAIXO")) {
            return numeroSorteado >= 1 && numeroSorteado <= 18;
        } else {
            return numeroSorteado >= 19 && numeroSorteado <= 36;
        }
    }
    return false;
}

int calcularPagamento(String tipoAposta, int valorApostado, boolean ganhou) {
    if (!ganhou) {
        return 0;
    }
    if (tipoAposta.equals("NUMERO")) {
        return valorApostado * 35;
    }
    return valorApostado;
}

Rodada jogar(Jogador jogador, String tipoAposta, String escolha, int valorApostado) {
    int numeroSorteado = girar();
    String corSorteada = getCor(numeroSorteado);

    boolean ganhou = verificarAposta(tipoAposta, escolha, numeroSorteado);
    if (ganhou) {
        int pagamento = calcularPagamento(tipoAposta, valorApostado, true);
        jogador.adicionarCreditos(pagamento);
        jogador.registrarVitoria();
    } else {
        jogador.removerCreditos(valorApostado);
        jogador.registrarDerrotas();
    }

    Rodada rodada = new Rodada(
        jogador.getNome(),
        tipoAposta,
        escolha,
        valorApostado,
        numeroSorteado,
        corSorteada,
        ganhou,
        jogador.getCreditos()
    );
        return rodada;
    }
}
