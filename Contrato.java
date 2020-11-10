package contrato;

import java.util.Date;

public abstract class Contrato {
	private static int idGeral = 0;
	private int id;
	private Date dataInicio;
	private Date dataEncerramento;
	private Colaborador colaborador;
	private boolean ativo;
	
	public Contrato(Date dataInicio, Colaborador colaborador) {
		idGeral++;
		this.id = idGeral;
		this.dataInicio = dataInicio;
		this.colaborador = colaborador;
		colaborador.ativar();
		this.ativo = true;
	}

	public int getId() {
		return id;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataEncerramento() {
		return dataEncerramento;
	}

	public void setDataEncerramento(Date dataEncerramento) {
		this.dataEncerramento = dataEncerramento;
	}	

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public boolean isAtivo() {
		return ativo;
	}

	public void encerrarContrato() {
		ativo = !ativo;
		colaborador.desativar();
	}
	
}
