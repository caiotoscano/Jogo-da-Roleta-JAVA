import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.net.URL;

public class RoletaGUI extends JFrame {
    private static final Color FUNDO = new Color(10, 10, 12);
    private static final Color PAINEL = new Color(24, 24, 28);
    private static final Color PAINEL_ESCURO = new Color(16, 16, 20);
    private static final Color BORDA_DOURADA = new Color(192, 151, 72);
    private static final Color TEXTO = new Color(245, 241, 230);
    private static final Color TEXTO_SECUNDARIO = new Color(190, 188, 180);
    private static final Color VERMELHO = new Color(163, 30, 42);
    private static final Color VERDE = new Color(23, 126, 76);
    private static final Font FONTE_BASE = new Font("SansSerif", Font.PLAIN, 14);
    private static final Font FONTE_TITULO = new Font("Serif", Font.BOLD, 24);
    private static final Font FONTE_DESTAQUE = new Font("SansSerif", Font.BOLD, 16);

    private static final int[] ORDEM_VISUAL_ROLETA = {
        0, 32, 15, 19, 4, 21, 2, 25, 17, 34,
        6, 27, 13, 36, 11, 30, 8, 23, 10, 5,
        24, 16, 33, 1, 20, 14, 31, 9, 22, 18,
        29, 7, 28, 12, 35, 3, 26
    };

    private Roleta roleta;
    private Jogador jogador;
    private listaLigadaD historico;
    private JTextField campoNome;
    private JTextField campoCreditosIniciais;
    private JButton botaoCadastrar;
    private JLabel labelJogador;
    private JLabel labelCreditos;
    private JLabel labelResultado;
    private PainelRoleta painelRoleta;
    private Timer timerDestaqueResultado;
    private boolean rodadaEmAnimacao;

    private JComboBox<String> comboTipoAposta;
    private JTextField campoNumero;
    private JComboBox<String> comboEscolha;
    private JTextField campoValorAposta;
    private JButton botaoGirar;

    private JTextArea areaHistorico;

    public RoletaGUI() {
        roleta = new Roleta();
        historico = new listaLigadaD();
        setTitle("Projeto - Jogo da Roleta");
        setSize(1050, 720);
        setMinimumSize(new Dimension(900, 620));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel conteudo = new JPanel(new BorderLayout(14, 14));
        conteudo.setBackground(FUNDO);
        conteudo.setBorder(new EmptyBorder(16, 16, 16, 16));
        setContentPane(conteudo);

        montarPainelCadastro();
        montarPainelJogo();
        montarPainelHistorico();
        bloquearJogo();
    }

    private void montarPainelCadastro() {
        JPanel painelCadastro = criarPainelBase(new BorderLayout(16, 8));
        JLabel titulo = new JLabel("Roleta Casino");
        titulo.setFont(FONTE_TITULO);
        titulo.setForeground(BORDA_DOURADA);

        JPanel formulario = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        formulario.setOpaque(false);

        JLabel labelNome = criarLabel("Nome:");
        campoNome = new JTextField(10);
        estilizarCampo(campoNome);

        JLabel labelCreditosIniciais = criarLabel("Creditos iniciais:");
        campoCreditosIniciais = new JTextField(5);
        estilizarCampo(campoCreditosIniciais);

        botaoCadastrar = new JButton("Cadastrar Jogador");
        estilizarBotao(botaoCadastrar, VERMELHO);

        formulario.add(labelNome);
        formulario.add(campoNome);
        formulario.add(labelCreditosIniciais);
        formulario.add(campoCreditosIniciais);
        formulario.add(botaoCadastrar);

        JPanel status = new JPanel(new GridLayout(2, 1, 0, 4));
        status.setOpaque(false);
        labelJogador = criarLabelDestaque("Jogador nao cadastrado");
        labelCreditos = criarLabelDestaque("Creditos: 0");
        status.add(labelJogador);
        status.add(labelCreditos);

        painelCadastro.add(titulo, BorderLayout.WEST);
        painelCadastro.add(formulario, BorderLayout.CENTER);
        painelCadastro.add(status, BorderLayout.EAST);

        add(painelCadastro, BorderLayout.NORTH);
        botaoCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cadastrarJogador();
            }
        });
    }

    private void montarPainelJogo() {
        JPanel painelJogo = new JPanel(new BorderLayout(16, 0));
        painelJogo.setOpaque(false);

        JPanel painelApostas = criarPainelBase(new GridBagLayout());
        GridBagConstraints grade = new GridBagConstraints();
        grade.insets = new Insets(7, 0, 7, 0);
        grade.fill = GridBagConstraints.HORIZONTAL;
        grade.gridx = 0;
        grade.weightx = 1.0;

        comboTipoAposta = new JComboBox<String>();
        comboTipoAposta.addItem("NUMERO");
        comboTipoAposta.addItem("COR");
        comboTipoAposta.addItem("PARIDADE");
        comboTipoAposta.addItem("ALTURA");
        estilizarCombo(comboTipoAposta);

        campoNumero = new JTextField();
        estilizarCampo(campoNumero);

        comboEscolha = new JComboBox<String>();
        estilizarCombo(comboEscolha);

        campoValorAposta = new JTextField();
        estilizarCampo(campoValorAposta);

        botaoGirar = new JButton("Girar Roleta");
        estilizarBotao(botaoGirar, VERDE);

        labelResultado = criarLabelDestaque("Resultado: aguardando jogada");
        labelResultado.setOpaque(true);
        labelResultado.setBackground(PAINEL_ESCURO);
        labelResultado.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDA_DOURADA, 1),
            new EmptyBorder(12, 12, 12, 12)
        ));

        adicionarLinha(painelApostas, grade, "Tipo de aposta:", comboTipoAposta);
        adicionarLinha(painelApostas, grade, "Numero escolhido (0 a 36):", campoNumero);
        adicionarLinha(painelApostas, grade, "Escolha externa", comboEscolha);
        adicionarLinha(painelApostas, grade, "Valor da aposta:", campoValorAposta);

        grade.gridy++;
        painelApostas.add(botaoGirar, grade);

        grade.gridy++;
        painelApostas.add(criarLabel("Resultado da rodada:"), grade);

        grade.gridy++;
        painelApostas.add(labelResultado, grade);

        grade.gridy++;
        grade.weighty = 1.0;
        painelApostas.add(Box.createVerticalGlue(), grade);

        painelRoleta = new PainelRoleta();

        painelJogo.add(painelApostas, BorderLayout.WEST);
        painelJogo.add(painelRoleta, BorderLayout.CENTER);

        add(painelJogo, BorderLayout.CENTER);
        atualizarOpcoesAposta();

        comboTipoAposta.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                atualizarOpcoesAposta();
            }
        });
        botaoGirar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jogarRodada();
            }
        });
    }

    private void montarPainelHistorico() {
        JPanel painelHistorico = criarPainelBase(new BorderLayout(0, 8));
        painelHistorico.setPreferredSize(new Dimension(0, 170));

        JLabel tituloHistorico = criarLabelDestaque("Historico de Rodadas");
        areaHistorico = new JTextArea();
        areaHistorico.setEditable(false);
        areaHistorico.setLineWrap(true);
        areaHistorico.setWrapStyleWord(true);
        areaHistorico.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaHistorico.setForeground(TEXTO);
        areaHistorico.setBackground(new Color(9, 9, 11));
        areaHistorico.setCaretColor(TEXTO);
        areaHistorico.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scroll = new JScrollPane(areaHistorico);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(55, 45, 30)));
        scroll.getViewport().setBackground(areaHistorico.getBackground());

        painelHistorico.add(tituloHistorico, BorderLayout.NORTH);
        painelHistorico.add(scroll, BorderLayout.CENTER);
        add(painelHistorico, BorderLayout.SOUTH);
    }

    private void cadastrarJogador() {
        String nome = campoNome.getText();
        String creditosTexto = campoCreditosIniciais.getText();
        if (nome.equals("")) {
            JOptionPane.showMessageDialog(this, "Informe o nome do jogador.");
            return;
        }
        if (creditosTexto.equals("")) {
            JOptionPane.showMessageDialog(this, "Informe os creditos iniciais.");
            return;
        }
        int creditos;
        try {
            creditos = Integer.parseInt(creditosTexto);
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(this, "Os creditos devem ser um numero inteiro.");
            return;
        }
        if (creditos <= 0) {
            JOptionPane.showMessageDialog(this, "Os creditos precisam ser maiores que zero.");
            return;
        }
        jogador = new Jogador(nome, creditos);
        labelJogador.setText("Jogador: " + jogador.getNome());
        labelCreditos.setText("Creditos: " + jogador.getCreditos());
        campoNome.setEnabled(false);
        campoCreditosIniciais.setEnabled(false);
        botaoCadastrar.setEnabled(false);
        liberarJogo();
        JOptionPane.showMessageDialog(this, "Jogador cadastrado com sucesso!");
    }

    private void atualizarOpcoesAposta() {
        String tipo = (String) comboTipoAposta.getSelectedItem();
        comboEscolha.removeAllItems();

        if (tipo.equals("NUMERO")) {
            campoNumero.setEnabled(true);
            comboEscolha.setEnabled(false);
        }
        if (tipo.equals("COR")) {
            campoNumero.setEnabled(false);
            comboEscolha.setEnabled(true);
            comboEscolha.addItem("VERMELHO");
            comboEscolha.addItem("PRETO");
        }
        if (tipo.equals("PARIDADE")) {
            campoNumero.setEnabled(false);
            comboEscolha.setEnabled(true);
            comboEscolha.addItem("PAR");
            comboEscolha.addItem("IMPAR");
        }
        if (tipo.equals("ALTURA")) {
            campoNumero.setEnabled(false);
            comboEscolha.setEnabled(true);
            comboEscolha.addItem("BAIXO");
            comboEscolha.addItem("ALTO");
        }
    }

    private void jogarRodada() {
        if (jogador == null) {
            JOptionPane.showMessageDialog(this, "Cadastre um jogador antes de jogar.");
            return;
        }

        if (jogador.getCreditos() <= 0) {
            JOptionPane.showMessageDialog(this, "Voce nao possui creditos disponiveis.");
            bloquearJogo();
            return;
        }

        String tipoAposta = (String) comboTipoAposta.getSelectedItem();
        String escolha = "";
        if (tipoAposta.equals("NUMERO")) {
            escolha = campoNumero.getText();
            if (escolha.equals("")) {
                JOptionPane.showMessageDialog(this, "Informe um numero entre 0 e 36.");
                return;
            }
            int numeroEscolhido;
            try {
                numeroEscolhido = Integer.parseInt(escolha);
            } catch (Exception erro) {
                JOptionPane.showMessageDialog(this, "O numero escolhido deve ser inteiro.");
                return;
            }
            if (!roleta.numeroValido(numeroEscolhido)) {
                JOptionPane.showMessageDialog(this, "O numero precisa estar entre 0 e 36.");
                return;
            }
        } else {
            escolha = (String) comboEscolha.getSelectedItem();
        }
        String valorTexto = campoValorAposta.getText();
        if (valorTexto.equals("")) {
            JOptionPane.showMessageDialog(this, "Informe o valor da aposta");
            return;
        }
        int valorApostado;
        try {
            valorApostado = Integer.parseInt(valorTexto);
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(this, "O valor da aposta deve ser inteiro.");
            return;
        }
        if (valorApostado <= 0) {
            JOptionPane.showMessageDialog(this, "O valor da aposta precisa ser maior que zero");
            return;
        }
        if (valorApostado > jogador.getCreditos()) {
            JOptionPane.showMessageDialog(this, "Voce nao pode apostar mais creditos do que possui.");
            return;
        }

        final Rodada rodada = roleta.jogar(jogador, tipoAposta, escolha, valorApostado);
        prepararAnimacaoRodada();
        painelRoleta.animarParaNumero(rodada.getNumeroSorteado(), new Runnable() {
            public void run() {
                finalizarExibicaoRodada(rodada);
            }
        });
        campoNumero.setText("");
        campoValorAposta.setText("");
    }

    private void prepararAnimacaoRodada() {
        rodadaEmAnimacao = true;
        comboTipoAposta.setEnabled(false);
        campoNumero.setEnabled(false);
        comboEscolha.setEnabled(false);
        campoValorAposta.setEnabled(false);
        botaoGirar.setEnabled(false);
        labelResultado.setText("Resultado: roleta girando...");
        labelResultado.setForeground(TEXTO);
        labelResultado.setBackground(PAINEL_ESCURO);
        labelResultado.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDA_DOURADA, 1),
            new EmptyBorder(12, 12, 12, 12)
        ));
        painelRoleta.limparResultado();
    }

    private void finalizarExibicaoRodada(Rodada rodada) {
        historico.adiciona(rodada);
        labelCreditos.setText("Creditos: " + jogador.getCreditos());
        if (rodada.getGanhou()) {
            labelResultado.setText("Resultado: GANHOU - Sorteado: " +
                rodada.getNumeroSorteado() + " " + rodada.getCorSorteada());
        } else {
            labelResultado.setText("Resultado: PERDEU - Sorteado: " +
                rodada.getNumeroSorteado() + " " + rodada.getCorSorteada());
        }
        painelRoleta.mostrarResultado(
            rodada.getNumeroSorteado(),
            rodada.getCorSorteada(),
            rodada.getGanhou()
        );
        aplicarDestaqueResultado(rodada.getGanhou());
        atualizarHistorico();
        rodadaEmAnimacao = false;
        if (jogador.getCreditos() <= 0) {
            JOptionPane.showMessageDialog(this, "Fim de jogo! Seus creditos acabaram.");
            bloquearJogo();
        } else {
            liberarJogo();
        }
    }

    private void atualizarHistorico() {
        areaHistorico.setText("");

        for (int i = 0; i < historico.tamanho(); i++) {
            areaHistorico.append(historico.pega(i).toString());
            areaHistorico.append("\n");
        }
    }

    private void bloquearJogo() {
        comboTipoAposta.setEnabled(false);
        campoNumero.setEnabled(false);
        comboEscolha.setEnabled(false);
        campoValorAposta.setEnabled(false);
        botaoGirar.setEnabled(false);
    }

    private void liberarJogo() {
        if (rodadaEmAnimacao) {
            return;
        }
        comboTipoAposta.setEnabled(true);
        campoValorAposta.setEnabled(true);
        botaoGirar.setEnabled(true);
        atualizarOpcoesAposta();
    }

    private JPanel criarPainelBase(LayoutManager layout) {
        JPanel painel = new JPanel(layout);
        painel.setBackground(PAINEL);
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(65, 50, 28), 1),
            new EmptyBorder(14, 14, 14, 14)
        ));
        return painel;
    }

    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_BASE);
        label.setForeground(TEXTO_SECUNDARIO);
        return label;
    }

    private JLabel criarLabelDestaque(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(FONTE_DESTAQUE);
        label.setForeground(TEXTO);
        return label;
    }

    private void adicionarLinha(JPanel painel, GridBagConstraints grade, String texto, JComponent componente) {
        grade.gridy++;
        painel.add(criarLabel(texto), grade);
        grade.gridy++;
        painel.add(componente, grade);
    }

    private void estilizarCampo(JTextField campo) {
        campo.setFont(FONTE_BASE);
        campo.setForeground(TEXTO);
        campo.setCaretColor(TEXTO);
        campo.setBackground(PAINEL_ESCURO);
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 58, 37), 1),
            new EmptyBorder(7, 8, 7, 8)
        ));
    }

    private void estilizarCombo(JComboBox<String> combo) {
        combo.setFont(FONTE_BASE);
        combo.setForeground(TEXTO);
        combo.setBackground(PAINEL_ESCURO);
        combo.setBorder(BorderFactory.createLineBorder(new Color(70, 58, 37), 1));
    }

    private void estilizarBotao(JButton botao, Color corBase) {
        botao.setFont(FONTE_DESTAQUE);
        botao.setForeground(Color.WHITE);
        botao.setBackground(corBase);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDA_DOURADA, 1),
            new EmptyBorder(9, 14, 9, 14)
        ));
    }

    private void aplicarDestaqueResultado(final boolean ganhou) {
        if (timerDestaqueResultado != null && timerDestaqueResultado.isRunning()) {
            timerDestaqueResultado.stop();
        }

        final Color corBase = ganhou ? VERDE : VERMELHO;
        labelResultado.setForeground(Color.WHITE);
        labelResultado.setBackground(corBase.darker());
        labelResultado.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDA_DOURADA, 2),
            new EmptyBorder(12, 12, 12, 12)
        ));

        timerDestaqueResultado = new Timer(90, null);
        timerDestaqueResultado.addActionListener(new ActionListener() {
            private int passo = 0;

            public void actionPerformed(ActionEvent e) {
                passo++;
                if (passo % 2 == 0) {
                    labelResultado.setBackground(corBase.darker());
                } else {
                    labelResultado.setBackground(corBase);
                }
                if (passo >= 8) {
                    timerDestaqueResultado.stop();
                    labelResultado.setBackground(corBase.darker());
                }
            }
        });
        timerDestaqueResultado.start();
    }

    private static class PainelRoleta extends JPanel {
        private BufferedImage imagemRoleta;
        private BufferedImage imagemBolinha;
        private Timer timerAnimacao;
        private double anguloBolinha = -Math.PI / 2.0;
        private double anguloInicial;
        private double anguloFinal;
        private int quadroAtual;
        private final int totalQuadros = 72;
        private boolean resultadoVisivel;
        private String resultadoStatus = "";
        private String resultadoDetalhe = "";
        private boolean resultadoGanhou;

        PainelRoleta() {
            setOpaque(true);
            setBackground(FUNDO);
            setPreferredSize(new Dimension(560, 440));
            imagemRoleta = carregarImagem("/images/roulette.png");
            imagemBolinha = carregarImagem("/images/ball.png");
        }

        void limparResultado() {
            resultadoVisivel = false;
            repaint();
        }

        void mostrarResultado(int numero, String cor, boolean ganhou) {
            resultadoGanhou = ganhou;
            resultadoStatus = ganhou ? "GANHOU" : "PERDEU";
            resultadoDetalhe = "Sorteado: " + numero + " " + cor;
            resultadoVisivel = true;
            repaint();
        }

        void animarParaNumero(int numero, Runnable aoFinal) {
            if (timerAnimacao != null && timerAnimacao.isRunning()) {
                timerAnimacao.stop();
            }

            anguloInicial = anguloBolinha;
            double destino = calcularAnguloNumero(numero);
            while (destino <= normalizarAngulo(anguloInicial)) {
                destino = destino + Math.PI * 2.0;
            }
            anguloFinal = destino + Math.PI * 2.0 * 3.0;
            quadroAtual = 0;

            timerAnimacao = new Timer(18, new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    quadroAtual++;
                    double progresso = (double) quadroAtual / (double) totalQuadros;
                    double suavizado = 1.0 - Math.pow(1.0 - progresso, 3.0);
                    anguloBolinha = anguloInicial + (anguloFinal - anguloInicial) * suavizado;
                    repaint();

                    if (quadroAtual >= totalQuadros) {
                        timerAnimacao.stop();
                        anguloBolinha = calcularAnguloNumero(numero);
                        if (aoFinal != null) {
                            aoFinal.run();
                        }
                        repaint();
                    }
                }
            });
            timerAnimacao.start();
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            int largura = getWidth();
            int altura = getHeight();
            int tamanho = Math.max(120, Math.min(largura, altura) - 44);
            int x = (largura - tamanho) / 2;
            int y = (altura - tamanho) / 2;

            pintarFundo(g2, largura, altura, tamanho);
            if (imagemRoleta != null) {
                g2.drawImage(imagemRoleta, x, y, tamanho, tamanho, null);
            } else {
                pintarRoletaFallback(g2, x, y, tamanho);
            }
            pintarBolinha(g2, largura / 2, altura / 2, tamanho);
            pintarResultadoSobreRoleta(g2, largura, altura);
            g2.dispose();
        }

        private void pintarFundo(Graphics2D g2, int largura, int altura, int tamanhoRoleta) {
            int sombra = tamanhoRoleta + 28;
            int x = (largura - sombra) / 2;
            int y = (altura - sombra) / 2;
            g2.setColor(new Color(0, 0, 0, 95));
            g2.fillOval(x, y + 10, sombra, sombra);
            g2.setColor(new Color(34, 26, 16));
            g2.drawOval(x + 10, y + 10, sombra - 20, sombra - 20);
        }

        private void pintarBolinha(Graphics2D g2, int centroX, int centroY, int tamanhoRoleta) {
            int tamanhoBolinha = Math.max(16, tamanhoRoleta / 13);
            int raioOrbita = (int) (tamanhoRoleta * 0.39);
            int x = centroX + (int) (Math.cos(anguloBolinha) * raioOrbita) - tamanhoBolinha / 2;
            int y = centroY + (int) (Math.sin(anguloBolinha) * raioOrbita) - tamanhoBolinha / 2;

            g2.setColor(new Color(0, 0, 0, 120));
            g2.fillOval(x + 3, y + 4, tamanhoBolinha, tamanhoBolinha);
            if (imagemBolinha != null) {
                g2.drawImage(imagemBolinha, x, y, tamanhoBolinha, tamanhoBolinha, null);
            } else {
                g2.setColor(Color.WHITE);
                g2.fillOval(x, y, tamanhoBolinha, tamanhoBolinha);
                g2.setColor(new Color(220, 220, 220));
                g2.drawOval(x, y, tamanhoBolinha, tamanhoBolinha);
            }
        }

        private void pintarRoletaFallback(Graphics2D g2, int x, int y, int tamanho) {
            int fatias = ORDEM_VISUAL_ROLETA.length;
            for (int i = 0; i < fatias; i++) {
                int numero = ORDEM_VISUAL_ROLETA[i];
                if (numero == 0) {
                    g2.setColor(VERDE);
                } else if (i % 2 == 0) {
                    g2.setColor(new Color(38, 38, 42));
                } else {
                    g2.setColor(VERMELHO);
                }
                g2.fillArc(x, y, tamanho, tamanho, 90 - (i + 1) * 360 / fatias, 360 / fatias);
            }
            g2.setColor(BORDA_DOURADA);
            g2.setStroke(new BasicStroke(4));
            g2.drawOval(x, y, tamanho, tamanho);
            g2.fillOval(x + tamanho / 2 - 32, y + tamanho / 2 - 32, 64, 64);
        }

        private void pintarResultadoSobreRoleta(Graphics2D g2, int largura, int altura) {
            if (!resultadoVisivel) {
                return;
            }

            int larguraCaixa = Math.min(360, Math.max(260, largura - 120));
            int alturaCaixa = 104;
            int x = (largura - larguraCaixa) / 2;
            int y = (altura - alturaCaixa) / 2;
            Color corResultado = resultadoGanhou ? VERDE : VERMELHO;

            Composite original = g2.getComposite();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.92f));
            g2.setColor(new Color(12, 12, 14));
            g2.fillRoundRect(x, y, larguraCaixa, alturaCaixa, 16, 16);
            g2.setComposite(original);

            g2.setColor(BORDA_DOURADA);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, larguraCaixa, alturaCaixa, 16, 16);

            g2.setColor(corResultado);
            g2.setFont(FONTE_TITULO);
            FontMetrics statusMetrics = g2.getFontMetrics();
            int statusX = x + (larguraCaixa - statusMetrics.stringWidth(resultadoStatus)) / 2;
            g2.drawString(resultadoStatus, statusX, y + 42);

            g2.setColor(TEXTO);
            g2.setFont(FONTE_DESTAQUE);
            FontMetrics detalheMetrics = g2.getFontMetrics();
            int detalheX = x + (larguraCaixa - detalheMetrics.stringWidth(resultadoDetalhe)) / 2;
            g2.drawString(resultadoDetalhe, detalheX, y + 74);
        }

        private double calcularAnguloNumero(int numero) {
            for (int i = 0; i < ORDEM_VISUAL_ROLETA.length; i++) {
                if (ORDEM_VISUAL_ROLETA[i] == numero) {
                    return -Math.PI / 2.0 + (Math.PI * 2.0 * i / ORDEM_VISUAL_ROLETA.length);
                }
            }
            return -Math.PI / 2.0;
        }

        private double normalizarAngulo(double angulo) {
            double volta = Math.PI * 2.0;
            double resultado = angulo % volta;
            if (resultado < 0) {
                resultado = resultado + volta;
            }
            return resultado;
        }

        private BufferedImage carregarImagem(String caminho) {
            try {
                URL url = PainelRoleta.class.getResource(caminho);
                if (url == null) {
                    return null;
                }
                return ImageIO.read(url);
            } catch (Exception erro) {
                return null;
            }
        }
    }
}
