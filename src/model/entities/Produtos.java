package model.entities;

import model.exceptions.DomainException;

public class Produtos {

	private String marca;
	private String modalidade;
	private String serie;
	private Integer quantidade;
	private Double preco;

	public Produtos() {
	}

	public Produtos(String marca, String modalidade, String serie, Integer quantidade, Double preco) throws DomainException {
		this.marca = marca;
		this.modalidade = modalidade;
		this.serie = serie;
		setQuantidade(quantidade);
		setPreco(preco);
	}

	public String getMarca() {
		return marca;
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public String getModalidade() {
		return modalidade;
	}

	public void setModalidade(String modalidade) {
		this.modalidade = modalidade;
	}

	public String getSerie() {
		return serie;
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) throws DomainException {
		// EVITAR QUANTIDADE NEGATIVA
		if (quantidade <= 0) {
			throw new DomainException("Error: Não pode adicionar quantidade negativa no sistema");
		}
		this.quantidade = quantidade;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) throws DomainException {
		// EVITAR PREÇO NEGATIVO
		if (preco <= 0.0) {
			throw new DomainException("Erros: Não pode adicionar valores negativos no sistema");
		}
		this.preco = preco;
	}

	public Double total() {
		return quantidade * preco;
	}

	@Override
	public String toString() {
		return String.format("%-15s %-15s %-20s %-15d %-15.2f %-15.2f", marca, modalidade, serie, quantidade, preco,
				total());
	}

}
