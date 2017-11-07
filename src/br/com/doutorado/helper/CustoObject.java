package br.com.doutorado.helper;

public class CustoObject {
	
	private String potencia;
	private String potenciaCV;
	private String pol;
	private String eff;
	private String precoRS;
	private String precoUS;
	private String moeda;
	
	public CustoObject(String potencia, String potenciaCV, String pol,
			String eff, String precoRS, String precoUS, String moeda) {
		this.potencia = potencia;
		this.potenciaCV = potenciaCV;
		this.pol = pol;
		this.eff = eff;
		this.precoRS = precoRS;
		this.precoUS = precoUS;
		this.moeda = moeda;
	}
	public String getPotencia() {
		return potencia;
	}
	public String getPotenciaCV() {
		return potenciaCV;
	}
	public void setPotenciaCV(String potenciaCV) {
		this.potenciaCV = potenciaCV;
	}
	public String getPol() {
		return pol;
	}
	public void setPol(String pol) {
		this.pol = pol;
	}
	public String getEff() {
		return eff;
	}
	public void setEff(String eff) {
		this.eff = eff;
	}
	public String getPrecoRS() {
		return precoRS;
	}
	public void setPrecoRS(String precoRS) {
		this.precoRS = precoRS;
	}
	public String getPrecoUS() {
		return precoUS;
	}
	public void setPrecoUS(String precoUS) {
		this.precoUS = precoUS;
	}
	public String getMoeda() {
		return moeda;
	}
	public void setMoeda(String moeda) {
		this.moeda = moeda;
	}
}
