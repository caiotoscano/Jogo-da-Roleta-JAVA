class listaLigadaD {
    Celula primeira;
    Celula ultima;
    int total = 0;

    // Retorna o tamanho da lista 
    int tamanho() {
        return this.total;
    }

    // Verifica se uma posição é válida dentro da lista 
    boolean posicaoOcupada(int posicao) {
        return posicao >= 0 && posicao < this.total;
    }

    // Captura a célula presente em uma determinada posição
    Celula pegaCelula(int posicao) {
        if(!this.posicaoOcupada(posicao)) {
            throw new IllegalArgumentException("Posição não existe");
        }
        Celula atual = this.primeira;
        for (int i = 0; i < posicao; i++) {
            atual = atual.getProxima();
        }
        return atual;
    }

    // Captura apenas o dado de uma determinada posição
    Object pega(int posicao) {
        return this.pegaCelula(posicao).getElemento();
    }

    // Remove do início (método base que apoia as outras remoções)
    void removeDoComeco() {
        if(!this.posicaoOcupada(0)) {
            throw new IllegalArgumentException("Posição não existe");
        }
        this.primeira = this.primeira.getProxima();
        this.total = this.total - 1;
        
        if (this.total == 0) {
            this.ultima = null;
        }
    }


    // Adiciona um elemento no começo
    void adicionaNoComeco(Object elemento) {
        if(this.total == 0) {
            Celula nova = new Celula(elemento);
            this.primeira = nova;
            this.ultima = nova;
        } else {
            Celula nova = new Celula(this.primeira, elemento);
            this.primeira.setAnterior(nova);
            this.primeira = nova;
        }
        this.total = this.total + 1;
    }

    // Adiciona um elemento no final 
    void adiciona(Object elemento) {
        if(this.total == 0) {
            this.adicionaNoComeco(elemento);
        } else {
            Celula nova = new Celula(elemento);
            this.ultima.setProxima(nova);
            nova.setAnterior(this.ultima);
            this.ultima = nova;
            this.total = this.total + 1;
        }
    }

    // Adiciona um elemento em uma posição escolhida
    void adiciona(int posicao, Object elemento) {
        if (posicao == 0) {
            this.adicionaNoComeco(elemento);
        } else if (posicao == this.total) {
            this.adiciona(elemento);
        } else {
            Celula anterior = this.pegaCelula(posicao - 1);
            Celula nova = new Celula(anterior.getProxima(), elemento);
            anterior.setProxima(nova);
            this.total = this.total + 1;
        }
    }

    // Remove dados do fim da lista
    void removeDoFim() {
        if(!this.posicaoOcupada(this.total - 1)) {
            throw new IllegalArgumentException("Posição não existe");
        }
        
        if(this.total == 1) {
            this.removeDoComeco();
        } else {
            Celula penultima = this.ultima.getAnterior();
            this.ultima = penultima;
            this.total = this.total - 1;
        }
    }

    // Remove dados de qualquer posição
    void remove(int posicao) {
        if(!this.posicaoOcupada(posicao)) {
            throw new IllegalArgumentException("Posição não existe");
        }
        
        if (posicao == 0) {
            this.removeDoComeco();
        } else {
            Celula anterior = this.pegaCelula(posicao - 1);
            Celula atual = anterior.getProxima();
            Celula proxima = atual.getProxima();
            
            anterior.setProxima(proxima);
            proxima.setAnterior(anterior);
            this.total = this.total - 1;
        }
    }
}