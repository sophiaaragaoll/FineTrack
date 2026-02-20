package ufrn.finetrack.model;

import java.time.LocalDate;
import java.util.UUID; // para geração de uids aleátorios em cada nova transação cadastrada

public class Transaction {
	private String id;
    private TransactionType tipo;
    private String categoria;  // valor derivado dos enums
    private double valor;
    private LocalDate data;
    
    public Transaction(TransactionType tipo, String categoria, double valor, LocalDate data) {
        this.id = UUID.randomUUID().toString();
        this.tipo = tipo;
        this.categoria = categoria;
        this.valor = valor;
        this.data = data;
    }
    
    // Construtor vazio (necessário para jSON)
    //public Transaction() {}

    // metodos getters e setters 
	public TransactionType getTipo() {
		return tipo;
	}
	
	public String getCategoria() {
		return categoria;
	}
	
	public double getValor() {
		return valor;
	}
	
	public LocalDate getData() {
		return data;
	}
	
	public String getId() {
		return id;
	}
	
	public void setTipo(TransactionType tipo) {
		this.tipo = tipo;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public void setValor(double valor) {
		this.valor = valor;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}
    
    
}