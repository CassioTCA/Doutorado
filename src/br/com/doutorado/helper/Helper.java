package br.com.doutorado.helper;

public class Helper {
	
	public static String FIELD_SEPARATOR = ":";
	public static String[] FIELD_NULL = {"x", "-"};
	
	public static boolean isNullField(String field) {
		for(String wField : FIELD_NULL) {
			if(field.equals(wField)) {
				return true;
			}
		}
		
		return false;
	}

}
