// a implementação dessa classe já conta com métodos para utilizarmos tanto na Lista Duplamente ligada quanto na lista circular 
class Celula {
    Object elemento; // lista circular
    Celula proximo; // lista circular
    Celula anterior; // listaDligada
    Celula(Object elemento) {
        this.elemento = elemento;
        this.proximo = null;
        this.anterior = null;
    }

    Celula(Celula proximo, Object elemento) { // construtor da lista DLigada
        this.elemento = elemento;
        this.proximo = proximo;
        this.anterior = null;

        if (proximo != null) {
            proximo.anterior = this;
        }
    }

    Object getElemento() { //lista DLigada
        return this.elemento;
    }
    Celula getProxima() { //lista DLigada
        return this.proximo;
    }

    void setProxima(Celula proximo) { //lista DLigada
        this.proximo = proximo;

        if (proximo != null) {
            proximo.anterior = this;
        }
    }
    Celula getAnterior() { //lista DLigada
        return this.anterior;
    }

    void setAnterior(Celula anterior) { //lista DLigada
        this.anterior = anterior;
    }
}
