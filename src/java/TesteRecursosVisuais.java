class TesteRecursosVisuais {
    public static void main(String[] args) {
        verificarRecurso("/images/roulette.png");
        verificarRecurso("/images/ball.png");
        System.out.println("Recursos visuais encontrados.");
    }

    private static void verificarRecurso(String caminho) {
        if (TesteRecursosVisuais.class.getResource(caminho) == null) {
            throw new IllegalStateException("Recurso nao encontrado: " + caminho);
        }
    }
}
