# Criptossistema Simétrico em um Cubo ARX

## Apresentação e Motivação

Este repositório contém a implementação em Java de um criptossistema simétrico, baseado numa cifra de fluxo no paradigma ARX com a topologia de um cubo 3x3x3, fortemente inspirado no Criptossistema ChaCha20.

Este projeto foi desenvolvido como a segunda parte de um trabalho prático para a disciplina de Introdução à Criptografia do Instituto de Computação da Universidade Federal Fluminense.

##  A Topologia 

O principal diferencial desse projeto é, sem dúvidas, a nova proposta topológica: a keystream ser um cubo 3x3x3 de ints e suas consequências:

* **Estado interno massivo:** O estado interno possui $27\cdot32=864$ bits.
* **Difusão:** A rota escolhida permite a difusão em toda a tridimensionalidade da keystream.

##  Funcionalidades

* Escolha de Chave Privada
* Escolha de Texto Claro (Terminal)
* Cifragem
* Decifragem

## Inicialização

**Pré-requisitos:**
* Java Development Kit (JDK) 17 ou superior.

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/Costa-08/CubeCryptography.git
   cd CubeCryptography
   ```

2. **Compilar:**
   O código está no pacote `code`, por isso:
   ```bash
   javac code/*.java
   ```

3. **Executar:**
   ```bash
   java code.Cube
   ```


## Considerações Finais

* Este é um projeto acadêmico e não deve ser utilizado industrialmente.
* Documentação completa pode ser encontrada no [Relatório](./docs/Relatorio.pdf).
