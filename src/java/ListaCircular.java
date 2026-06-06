class ListaCircular {
    private Celula cursor;
    private int total;
    public ListaCircular() {
        cursor = null;
    }
    public void proximo() {
        cursor = cursor.proximo;
    }
    public Object getelement() {
        return cursor.elemento;
    }
    public boolean ultelement() {
        return (cursor == cursor.proximo);
    }
    public void avanca(int posicao) {
        for (int i = 1; i <= posicao; i++) {
            this.proximo();
        }
    }
    public void adiciona(Object elemento) {
        Celula nodo = new Celula(elemento);
        if (cursor == null) {
            nodo.proximo = nodo;
            cursor = nodo;
        } else {
            nodo.proximo = cursor.proximo;
            cursor.proximo = nodo;
            cursor = nodo;
        }
        total = total + 1;
    }
    public void remove() {
        if (cursor != null) {
            if (cursor == cursor.proximo) {
                cursor = null;
            } else {
                cursor.proximo = cursor.proximo.proximo;
                total = total - 1;
            }
        }
    }
    public int tamanho() {
        return total;
    }
    public String toString() {
        if (cursor == null) return "[]";
        String s = "[" + this.getelement();
        Celula velhocursor = cursor;
        for (this.proximo(); velhocursor != cursor; this.proximo()) {
            s += "," + this.getelement();
        }
        return s + "]";
    }
}