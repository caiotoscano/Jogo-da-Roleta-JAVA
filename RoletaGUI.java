import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RoletaGUI extends JFrame {
    private Roleta roleta;
    private Jogador jogador;
    private listaLigadaD historico;
    private JTextField campoNome;
    private JTextField campoCreditosIniciais;
    private JButton botaoCadastrar;
    private JLabel labelJogador;
    private JLabel labelCreditos;
    private JLabel labelResultado;

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
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        montarPainelCadastro();
        montarPainelJogo();
        montarPainelHistorico();
        bloquearJogo();
    }
    private void montarPainelCadastro(){
        JPanel painelCadastro = new JPanel();
        painelCadastro.setLayout(new FlowLayout());
        JLabel labelNome = new JLabel("Nome:");
        campoNome = new JTextField(10);
        JLabel labelCreditosIniciais = new JLabel("Créditos iniciais:");
        campoCreditosIniciais = new JTextField(5);
        botaoCadastrar = new JButton("Cadastrar Jogador");
        labelJogador = new JLabel("Jogador não cadastrado");
        labelCreditos = new JLabel("Créditos: 0");
        painelCadastro.add(labelNome);
        painelCadastro.add(campoNome);
        painelCadastro.add(labelCreditosIniciais);
        painelCadastro.add(campoCreditosIniciais);
        painelCadastro.add(botaoCadastrar);
        painelCadastro.add(labelJogador);
        painelCadastro.add(labelCreditos);

        add(painelCadastro, BorderLayout.NORTH);
        botaoCadastrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cadastrarJogador();
            }
        });
    }
    private void montarPainelJogo() {
        JPanel painelJogo = new JPanel();
        painelJogo.setLayout(new GridLayout(8, 2, 5, 5));
        comboTipoAposta = new JComboBox<String>();
        comboTipoAposta.addItem("NUMERO");
        comboTipoAposta.addItem("COR");
        comboTipoAposta.addItem("PARIDADE");
        comboTipoAposta.addItem("ALTURA");
        campoNumero = new JTextField();
        comboEscolha = new JComboBox<String>();
        campoValorAposta = new JTextField();
        botaoGirar = new JButton("Girar Roleta");
        labelResultado = new JLabel("Resultado: aguardando jogada");
        painelJogo.add(new JLabel("Tipo de aposta:"));
        painelJogo.add(comboTipoAposta);
        painelJogo.add(new JLabel("Número escolhido (0 a 36):"));
        painelJogo.add(campoNumero);
        painelJogo.add(new JLabel("Escolha externa"));
        painelJogo.add(comboEscolha);
        painelJogo.add(new JLabel("Valor da aposta:"));
        painelJogo.add(campoValorAposta);
        painelJogo.add(new JLabel(""));
        painelJogo.add(botaoGirar);
        painelJogo.add(new JLabel("Resultado da rodada:"));
        painelJogo.add(labelResultado);
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
        JPanel painelHistorico = new JPanel();
        painelHistorico.setLayout(new BorderLayout());
        JLabel tituloHistorico = new JLabel("Histórico de Rodadas");
        areaHistorico = new JTextArea();
        areaHistorico.setEditable(false);
        JScrollPane scroll = new JScrollPane(areaHistorico);
        painelHistorico.add(tituloHistorico, BorderLayout.NORTH);
        painelHistorico.add(scroll, BorderLayout.CENTER);
        add(painelHistorico, BorderLayout.SOUTH);
    }
    private void cadastrarJogador(){
        String nome = campoNome.getText();
        String creditosTexto = campoCreditosIniciais.getText();
        if (nome.equals("")) {
            JOptionPane.showMessageDialog(this, "Infome o nome do jogador.");
            return;
        }
        if (creditosTexto.equals("")) {
            JOptionPane.showMessageDialog(this, "Infome os créditos iniciais.");
            return;
        }
        int creditos;
        try {
            creditos = Integer.parseInt(creditosTexto);
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(this, "Os créditos devem ser um número inteiro.");
            return;
        }
        if (creditos <= 0) {
            JOptionPane.showMessageDialog(this, "Os créditos precisam ser maiores que zero.");
            return;
        }
        jogador = new Jogador(nome, creditos);
        labelJogador.setText("Jogador: " + jogador.getNome());
        labelCreditos.setText("Créditos: " + jogador.getCreditos());
        campoNome.setEnabled(false);
        campoCreditosIniciais.setEnabled(false);
        botaoCadastrar.setEnabled(false);
        liberarJogo();
        JOptionPane.showMessageDialog(this, "Jogador cadastrado com sucesso!");
    }
private void atualizarOpcoesAposta(){
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
        JOptionPane.showMessageDialog(this, "Você não possui créditos disponíveis.");
        bloquearJogo();
        return;
    }

    String tipoAposta = (String) comboTipoAposta.getSelectedItem();
    String escolha = "";
    if (tipoAposta.equals("NUMERO")) {
        escolha = campoNumero.getText();
        if (escolha.equals("")) {
            JOptionPane.showMessageDialog(this, "Infome um número entre 0 e 36.");
            return;
        }
        int numeroEscolhido;
        try{
            numeroEscolhido = Integer.parseInt(escolha);
        } catch (Exception erro) {
            JOptionPane.showMessageDialog(this, "O número escolhido deve ser inteiro.");
            return;
        }
        if (!roleta.numeroValido(numeroEscolhido)) {
            JOptionPane.showMessageDialog(this, "O número precisa estar entre 0 e 36.");
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
        JOptionPane.showMessageDialog(this, "Você não pode apostar mais créditos do que possui.");
        return;
    }
    Rodada rodada = roleta.jogar(jogador, tipoAposta, escolha, valorApostado);
    historico.adiciona(rodada);
    labelCreditos.setText("Créditos: " + jogador.getCreditos());
    if (rodada.getGanhou()) {
        labelResultado.setText("Resultado: GANHOU - Sorteado: " + 
            rodada.getNumeroSorteado() + " " + rodada.getCorSorteada());
    } else {
        labelResultado.setText("Resultado: PERDEU -Sorteado: " + 
            rodada.getNumeroSorteado() + " " + rodada.getCorSorteada()); 
    }
    atualizarHistorico();
    campoNumero.setText("");
    campoValorAposta.setText("");
    if (jogador.getCreditos() <= 0) {
        JOptionPane.showMessageDialog(this, "Fim de jogo! Seus créditos acabaram.");
        bloquearJogo();
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
    comboTipoAposta.setEnabled(true);
    campoValorAposta.setEnabled(true);
    botaoGirar.setEnabled(true);
    atualizarOpcoesAposta();
}
}