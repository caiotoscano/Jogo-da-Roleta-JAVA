# Jogo da Roleta em Java

Projeto simples de roleta de cassino feito em Java com interface Swing.

## Como rodar

Este projeto nao usa Maven ou Gradle. Compile os arquivos Java para a pasta `out`:

```powershell
javac -encoding UTF-8 -d out src\java\*.java
```

Depois execute a interface grafica:

```powershell
java -cp "out;src" Main
```

Tambem existe um teste simples pelo terminal:

```powershell
java -cp "out;src" TesteRoleta
```

## Estrutura

```text
src/
  java/
    Main.java
    RoletaGUI.java
    Roleta.java
    Rodada.java
    Jogador.java
    ...
  images/
    roulette.png
    ball.png
```

## Como funciona o jogo

1. Cadastre o jogador informando nome e creditos iniciais.
2. Escolha o tipo de aposta.
3. Informe o numero, escolha externa e valor da aposta conforme o tipo selecionado.
4. Clique em `Girar Roleta`.
5. O sistema sorteia um numero, atualiza os creditos e registra a rodada no historico.

## Tipos de aposta

- `NUMERO`: aposta em um numero de 0 a 36.
- `COR`: aposta em `VERMELHO` ou `PRETO`.
- `PARIDADE`: aposta em `PAR` ou `IMPAR`.
- `ALTURA`: aposta em `BAIXO` para numeros de 1 a 18 ou `ALTO` para numeros de 19 a 36.

O numero `0` e verde. Em apostas de paridade ou altura, o `0` nao conta como acerto.

## Interface

A tela usa um visual escuro inspirado em cassino, mostra a imagem da roleta e uma bolinha animada sobre ela. A animacao e apenas visual: o resultado, saldo, vitoria e derrota continuam sendo calculados pela logica do jogo.
