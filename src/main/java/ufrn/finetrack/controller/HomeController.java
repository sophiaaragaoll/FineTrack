package ufrn.finetrack.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import ufrn.finetrack.model.Transaction;
import ufrn.finetrack.model.TransactionType;
import ufrn.finetrack.service.TransactionService;

public class HomeController {

    @FXML private Label lblTotalReceitas;
    @FXML private Label lblTotalDespesas;
    @FXML private Label lblFluxoLiquido;

    @FXML private ToggleButton toggleSituacao;
    @FXML private PieChart pieGastos;
    
    private final TransactionService service = TransactionService.getInstance();

    @FXML
    public void initialize() {
    	
    	 // Filtra apenas o mês atual
        List<Transaction> transacoes = service.getTransactionsOfCurrentMonth();
        
        double receitas = transacoes.stream()
                .filter(t -> t.getTipo() == TransactionType.RECEITA)
                .mapToDouble(Transaction::getValor)
                .sum();
        
        double despesas = transacoes.stream()
                .filter(t -> t.getTipo() == TransactionType.DESPESA)
                .mapToDouble(Transaction::getValor)
                .sum();
        
        double fluxo = receitas - despesas;

        lblTotalReceitas.setText(String.format("R$ %.2f", receitas));
        lblTotalDespesas.setText(String.format("R$ %.2f", despesas));
        lblFluxoLiquido.setText(String.format("R$ %.2f", fluxo));
        
        atualizarCorToggle(fluxo);
        
     // calcula os gastos em cada categoria de despesa
        Map<String, Double> categorias = transacoes.stream()
                .filter(t -> t.getTipo() == TransactionType.DESPESA)
                .collect(Collectors.groupingBy(
                        Transaction::getCategoria,
                        Collectors.summingDouble(Transaction::getValor)
                ));
        
        pieGastos.getData().clear();
        
        categorias.forEach((cat, total) -> {
            pieGastos.getData().add(new PieChart.Data(cat, total));
        });
    }
    
    private void atualizarCorToggle(double fluxo) {
        toggleSituacao.getStyleClass().removeAll("switch-verde", "switch-vermelho");

        if (fluxo >= 0) {
            toggleSituacao.getStyleClass().add("switch-verde");
            toggleSituacao.setSelected(true);
        } else {
            toggleSituacao.getStyleClass().add("switch-vermelho");
            toggleSituacao.setSelected(true);
        }
    }

    
    @FXML
    private void goToTransactions() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/ufrn/finetrack/view/TransactionsView.fxml")
            );

            Scene scene = new Scene(loader.load(), 1280, 720);
            Stage stage = (Stage) lblTotalReceitas.getScene().getWindow();
            scene.getStylesheets().add(
                getClass().getResource("/ufrn/finetrack/view/style.css").toExternalForm()
            );
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void goToReports() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/ufrn/finetrack/view/ReportsView.fxml")
            );

            Scene scene = new Scene(loader.load(), 1280, 720);
            Stage stage = (Stage) lblTotalReceitas.getScene().getWindow();
            scene.getStylesheets().add(
                getClass().getResource("/ufrn/finetrack/view/style.css").toExternalForm()
            );
            stage.setScene(scene);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
