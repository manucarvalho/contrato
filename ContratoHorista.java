package contrato;

import java.util.Date;

public class ContratoHorista extends Contrato{
	private int horasMes;
	private float valorHora;
	
	public ContratoHorista(Date dataInicio, Colaborador colaborador,
			int horasMes, float valorHora) {
		super(dataInicio, colaborador);
		this.horasMes = horasMes;
		this.valorHora = valorHora;
	}

	public int getHorasMes() {
		return horasMes;
	}

	public void setHorasMes(int horasMes) {
		this.horasMes = horasMes;
	}

	public float getValorHora() {
		return valorHora;
	}

	public void setValorHora(float valorHora) {
		this.valorHora = valorHora;
	}
	
	public float calcVencimento() {
		return horasMes * valorHora;
	}
	
	
}
