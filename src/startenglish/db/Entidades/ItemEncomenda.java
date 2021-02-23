/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package startenglish.db.Entidades;

/**
 *
 * @author azeve
 */
public class ItemEncomenda{
        private Livro livro;
        private int quantidade;
        private double valor;

        public ItemEncomenda() {
        }

        public ItemEncomenda(Livro livro, int quantidade, double valor) {
            this.livro = livro;
            this.quantidade = quantidade;
            this.valor = valor;
        }

        public ItemEncomenda(Livro livro, int quantidade) {
            this.livro = livro;
            this.quantidade = quantidade;
        }

        

        public double getValor() {
            return valor;
        }

        public void setValor(double valor) {
            this.valor = valor;
        }

        public Livro getLivro() {
            return livro;
        }

        public void setLivro(Livro livro) {
            this.livro = livro;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }  
    }
