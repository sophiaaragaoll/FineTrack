package ufrn.finetrack.model;

public class FinanceSummary {
	
	private double totalReceitas;
    private double totalDespesas;
    private double saldo;

    public FinanceSummary(double totalReceitas, double totalDespesas) {
        this.totalReceitas = totalReceitas;
        this.totalDespesas = totalDespesas;
        this.saldo = totalReceitas - totalDespesas;
    }

	public double getTotalReceitas() {
		return totalReceitas;
	}

	public double getTotalDespesas() {
		return totalDespesas;
	}

	public double getSaldo() {
		return saldo;
	}
	
}
