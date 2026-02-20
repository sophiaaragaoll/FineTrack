package ufrn.finetrack.model;

import java.time.YearMonth;
import java.util.Map;

public class ReportData {

    private Map<String, Double> gastosPorCategoria;
    private Map<YearMonth, Double> saldoMensal;
    private Map<YearMonth, Double> despesaMensal;
    private Map<YearMonth, Double> receitaMensal;

    public ReportData(Map<String, Double> gastosPorCategoria, Map<YearMonth, Double> saldoMensal, Map<YearMonth, Double> receitaMensal, Map<YearMonth, Double> despesaMensal) {
        this.gastosPorCategoria = gastosPorCategoria;
        this.saldoMensal = saldoMensal;
        this.receitaMensal = receitaMensal;
        this.despesaMensal = despesaMensal;
    }

    public Map<String, Double> getGastosPorCategoria() {
        return gastosPorCategoria;
    }

    public Map<YearMonth, Double> getSaldoMensal() {
        return saldoMensal;
    }
    
    public Map<YearMonth, Double> getDespesaMensal() {
        return despesaMensal;
    }
    
    public Map<YearMonth, Double> getReceitaMensal() {
        return receitaMensal;
    }
}
