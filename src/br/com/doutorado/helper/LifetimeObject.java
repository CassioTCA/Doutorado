package br.com.doutorado.helper;

public class LifetimeObject {
	
	private String value;
	private String potencia;
	
	public LifetimeObject(String value, String potencia) {
		this.value = value;
		this.potencia = potencia;
	}
	public String getPotencia() {
		return potencia;
	}
	public String getValue() {
		return value;
	}
}
