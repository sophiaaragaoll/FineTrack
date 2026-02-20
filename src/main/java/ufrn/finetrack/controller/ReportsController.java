package ufrn.finetrack.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import ufrn.finetrack.model.ReportData;
import ufrn.finetrack.service.ReportService;
import ufrn.finetrack.service.TransactionService;

import java.io.FileWriter;
import java.io.IOException;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class ReportsController {
	
	@FXML private BorderPane rootPane;
    @FXML private ChoiceBox<YearMonth> choiceMes;
    @FXML private BarChart<String, Number> barCategorias;
    @FXML private NumberAxis barAxisY;

    @FXML private LineChart<String, Number> lineFluxoMensal;
    @FXML private NumberAxis lineAxisY;

    @FXML private Button btnExportar;

    private ReportService reportService;
    private final TransactionService transactionService = TransactionService.getInstance();

    @FXML
    public void initialize() {

        reportService = new ReportService(transactionService);

        configurarChoiceMes();
        configurarGraficos();
    }

    private void configurarChoiceMes() {

        List<YearMonth> meses = transactionService.getAll().stream()
                .map(t -> YearMonth.from(t.getData()))
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        if (meses.isEmpty()) {
            choiceMes.setItems(FXCollections.observableArrayList());
            return;
        }

        choiceMes.setItems(FXCollections.observableArrayList(meses));

        // Seleciona o mês mais recente
        choiceMes.setValue(meses.get(meses.size() - 1));

        choiceMes.valueProperty().addListener((obs, oldVal, novoMes) -> atualizarGraficos());
    }

    private void configurarGraficos() {
    	barCategorias.getData().clear();
        lineFluxoMensal.getData().clear();

        atualizarGraficos();
    }

    private void atualizarGraficos() {
        YearMonth mes = choiceMes.getValue();

        if (mes == null) return;

        ReportData dados = reportService.generateMonthlyReport(mes);

        atualizarGraficoBarras(dados.getGastosPorCategoria());
        atualizarGraficoLinha(dados.getSaldoMensal());
    }

    // gráfico de barras - despesas por categoria
    
    private void atualizarGraficoBarras(Map<String, Double> gastos) {

    	barCategorias.getData().clear();
        barAxisY.setLabel("Valor (R$)");

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Gastos do mês");

        gastos.forEach((categoria, valor) -> {
            serie.getData().add(new XYChart.Data<>(categoria, valor));
        });

        barCategorias.getData().add(serie);
    }

    // gráfico de linha - fluxo mensal
    
    private void atualizarGraficoLinha(Map<YearMonth, Double> fluxoMensal) {

        lineFluxoMensal.getData().clear();
        lineAxisY.setLabel("Saldo Líquido");

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Fluxo líquido");

        fluxoMensal.forEach((ym, valor) -> {
            String label = ym.getMonth().getDisplayName(TextStyle.SHORT, new Locale("pt", "BR"))
                    + "/" + ym.getYear();
            serie.getData().add(new XYChart.Data<>(label, valor));
        });

        lineFluxoMensal.getData().add(serie);
    }

    // exportação
    
    @FXML
    private void handleExport() {
        YearMonth mes = choiceMes.getValue();

        if (mes == null) {
            showAlert("Selecione um mês antes de exportar.");
            return;
        }

        String conteudo = reportService.gerarResumoMensal(mes);

        String nomeArquivo = "Relatorio_" + mes + ".txt";

        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            writer.write(conteudo);
            showAlert("Arquivo exportado com sucesso:\n" + nomeArquivo, Alert.AlertType.INFORMATION);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erro ao exportar o arquivo.");
        }
    }

    // navegação entre telas
    
    @FXML
    private void goToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/ufrn/finetrack/view/HomeView.fxml")
            );

            Scene scene = new Scene(loader.load(), 1280, 720);
            Stage stage = (Stage) rootPane.getScene().getWindow();

            scene.getStylesheets().add(
                getClass().getResource("/ufrn/finetrack/view/style.css").toExternalForm()
            );

            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void goToTransactions() {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/ufrn/finetrack/view/TransactionsView.fxml")
            );

            Scene scene = new Scene(loader.load(), 1280, 720);
            Stage stage = (Stage) rootPane.getScene().getWindow();

            scene.getStylesheets().add(
                getClass().getResource("/ufrn/finetrack/view/style.css").toExternalForm()
            );

            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // utilidades
    private void showAlert(String msg) {
        showAlert(msg, Alert.AlertType.WARNING);
    }

    private void showAlert(String msg, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle("Relatório");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

