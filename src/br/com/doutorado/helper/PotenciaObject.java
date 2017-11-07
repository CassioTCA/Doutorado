package br.com.doutorado.helper;

public class PotenciaObject {
	
	private String potenciaKW;
	private String potenciaCV;
	
	public PotenciaObject(String potenciaKW, String potenciaCV) {
		this.potenciaKW = potenciaKW;
		this.potenciaCV = potenciaCV;
	}

	public String getPotenciaKW() {
		return potenciaKW;
	}
	public void setPotenciaKW(String potenciaKW) {
		this.potenciaKW = potenciaKW;
	}
	public String getPotenciaCV() {
		return potenciaCV;
	}
	public void setPotenciaCV(String potenciaCV) {
		this.potenciaCV = potenciaCV;
	}
	
	
}
