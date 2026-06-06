// Classe ListaCircular - Slides 14, 15 e seguintes
class ListaCircular {
 
    // Atributos - Slide 15
    private Celula cursor;
    private int total;
 
    // Construtor - Slide 15
    public ListaCircular() {
        cursor = null;
    }
 
    // Desloca para o proximo elemento - Slide 17
    public void proximo() {
        cursor = cursor.proximo;
    }
 
    // Retorna um elemento da lista - Slide 18
    public Object getelement() {
        return cursor.elemento;
    }
 
    // Retorna positivo caso seja o ultimo elemento - Slide 20
    public boolean ultelement() {
        return (cursor == cursor.proximo);
    }
 
    // Avanca para o proximo elemento - Slide 21
    public void avanca(int posicao) {
        for (int i = 1; i <= posicao; i++) {
            this.proximo();
        }
    }
 
    // Adiciona elemento na Lista Circular - Slide 22
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
 
    // Remove dados da Lista Circular - Slide 23
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
 
    // Tamanho da Lista Circular - Slide 19
    public int tamanho() {
        return total;
    }
 
    // Visualizacao Linear da Lista Circular - Slide 24
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