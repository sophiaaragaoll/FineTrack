package ufrn.finetrack.service;

import ufrn.finetrack.model.Transaction;
import ufrn.finetrack.model.TransactionType;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionService {
	
	private static TransactionService instance;
	
	public static TransactionService getInstance() {
        if (instance == null) {
            instance = new TransactionService();
        }
        return instance;
    }

    private List<Transaction> transacoes;
    private PersistenceService persistence;

    private TransactionService() {
        
    	this.transacoes = new ArrayList<>();
        this.persistence = new PersistenceService("data/transactions.json");
        
	    // Carrega transações do JSON ao iniciar
        List<Transaction> carregadas = persistence.load();
        if (carregadas != null) transacoes.addAll(carregadas);

    }

    public void setTransactions(List<Transaction> list) {
        this.transacoes = list;
    }

    public List<Transaction> getAll() {
        return transacoes;
    }

    public void addTransaction(Transaction transacao) {
        transacoes.add(transacao);
        persistence.save(transacoes);
    }

    public void updateTransaction(Transaction updated) {
        for (int i = 0; i < transacoes.size(); i++) {
            if (transacoes.get(i).getId().equals(updated.getId())) {
            	transacoes.set(i, updated);
                break;
            }
        }
        persistence.save(transacoes);
    }

    public void removeTransaction(String id) {
    	transacoes.removeIf(t -> t.getId().equals(id));
    	persistence.save(transacoes);
    }

    public List<Transaction> filterByMonth(YearMonth mes) {
        return transacoes.stream()
                .filter(t -> YearMonth.from(t.getData()).equals(mes))
                .collect(Collectors.toList());
    }

    public double getTotal(TransactionType tipo, YearMonth mes) {
        return filterByMonth(mes).stream()
                .filter(t -> t.getTipo() == tipo)
                .mapToDouble(Transaction::getValor)
                .sum();
    }
    
    public List<Transaction> getTransactionsOfCurrentMonth() {
        LocalDate hoje = LocalDate.now();
        int mes = hoje.getMonthValue();
        int ano = hoje.getYear();

        return transacoes.stream()
                .filter(t -> t.getData().getMonthValue() == mes &&
                             t.getData().getYear() == ano)
                .toList();
    }
}
