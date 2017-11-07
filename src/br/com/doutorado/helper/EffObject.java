package br.com.doutorado.helper;

public class EffObject {
	private String potencia;
	private String[] values;
	
	public EffObject(String potencia, String... values) {
		this.potencia = potencia;
		this.values = values;
	}
	
	public String getPotencia() {
		return potencia;
	}
	
	public String[] getValues() {
		return values;
	}
}
