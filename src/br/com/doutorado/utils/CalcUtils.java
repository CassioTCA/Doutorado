package br.com.doutorado.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class CalcUtils {
	
	public static final MathContext MC = new MathContext(5, RoundingMode.HALF_UP);

	private static BigDecimal calcK1(BigDecimal nst, BigDecimal nar, BigDecimal n, BigDecimal Pst, BigDecimal Par, BigDecimal d, BigDecimal e) throws Exception {

		/*BigDecimal p11k1 = e.add(new BigDecimal(1), MC);   //e + 1
		BigDecimal p12k1 = d.subtract(e, MC);   //d - e
		BigDecimal p1k1 = p11k1.divide(p12k1, MC);   // (e + 1) / (d - e)  ->  p11k1 / p12k1

		BigDecimal p21k1 = e.add(new BigDecimal(1), MC);  //e + 1
		BigDecimal p22k1 = d.add(new BigDecimal(1), MC);  //d + 1
		BigDecimal p23k1 = p21k1.divide(p22k1, MC);  //(e + 1) / (d + 1)  ->  p21k1 / p22k1
		BigDecimal p2k1 = p23k1.pow(n.intValue(), MC);  //p23k1^n
		BigDecimal p3k1 = new BigDecimal(1).subtract(p2k1, MC);  //1 - p2k1
		BigDecimal k1 = p1k1.multiply(p3k1, MC); // p1k1 * p3k1*/
		
		
		
		BigDecimal p11k1 = e.add(new BigDecimal(1), MC);   //1 + e
		BigDecimal p12k1 = d.add(new BigDecimal(1), MC);   //1 + d
		BigDecimal p1k1 = p11k1.divide(p12k1, MC);  //(e + 1) / (d + 1)  ->  p11k1 / p12k1
		
		BigDecimal k1 = new BigDecimal(0);
		
		for(int t = 1; t <= n.intValue(); t++) {
			k1 = k1.add(p1k1.pow(t, MC), MC);
		}

		return k1;
	}

	public static BigDecimal calcK2(BigDecimal nst, BigDecimal nar, BigDecimal C, BigDecimal P) {
		BigDecimal p1k2 = new BigDecimal(1).divide(nst, MC);  //(1 / nst)
		BigDecimal p2k2 = new BigDecimal(1).divide(nar, MC);  //(1 / nar)
		BigDecimal p3k2 = p1k2.subtract(p2k2, MC);  //(1 / nst) - (1 / nar)  ->  p1k2 - p2k2
		BigDecimal p4k2 = P.multiply(C, MC); //P * C
		BigDecimal k2 = p4k2.multiply(p3k2, MC);  // P * C * (1/nst - 1/nar)  ->  p4k2 * p3k2

		return k2;
	}

	public static String calcula(BigDecimal nst, BigDecimal nar, BigDecimal n, BigDecimal C, BigDecimal P, BigDecimal Pst, BigDecimal fdPst, BigDecimal Par, BigDecimal fdPar, BigDecimal d, BigDecimal e) {
		try {
			BigDecimal percent = new BigDecimal(100);

			nst = nst.divide(percent, MC);

			nar = nar.divide(percent, MC); 

			fdPst = new BigDecimal("1").subtract(fdPst.multiply(new BigDecimal("0.01"), MC), MC);  //desconto
			Pst = Pst.multiply(fdPst, MC);  //desconto

			fdPar = new BigDecimal("1").subtract(fdPar.multiply(new BigDecimal("0.01"), MC), MC);  //desconto
			Par = Par.multiply(fdPar, MC);  //desconto

			d = d.divide(percent, MC);

			e = e.divide(percent, MC);
			
			C = C.divide(new BigDecimal(1000), MC);

			BigDecimal k1 = calcK1(nst, nar, n, Pst, Par, d, e);  //calcula k1

			BigDecimal k2 = calcK2(nst, nar, C, P);  //calcula k2

			BigDecimal p1H = Par.subtract(Pst, MC);  //Par - Pst
			BigDecimal p2H = k1.multiply(k2, MC);  //k1 * k2
			BigDecimal p3H = p1H.divide(p2H, MC);  //(Par - Pst) / (k1 * k2)

			return p3H.toPlainString();
		} catch (Exception ex) {
			return "error";
		}	
	}

	public static boolean isViavel(BigDecimal nst, BigDecimal nar, BigDecimal n, BigDecimal C, BigDecimal P, BigDecimal Pst, BigDecimal fdPst, BigDecimal Par, BigDecimal fdPar, BigDecimal d, BigDecimal e, BigDecimal Lop, BigDecimal Hop) throws Exception {

		BigDecimal percent = new BigDecimal(100);

		nst = nst.divide(percent);

		nar = nar.divide(percent);

		fdPst = new BigDecimal("1").subtract(fdPst.multiply(new BigDecimal("0.01")));  //desconto
		Pst = Pst.multiply(fdPst);  //desconto

		fdPar = new BigDecimal("1").subtract(fdPar.multiply(new BigDecimal("0.01")));  //desconto
		Par = Par.multiply(fdPar);  //desconto

		d = d.divide(percent);

		e = e.divide(percent);
		
		C = C.divide(new BigDecimal(1000), MC);
		
		Lop = Lop.divide(percent);

		BigDecimal k1 = calcK1(nst, nar, n, Pst, Par, d, e);  //calcula k1

		BigDecimal k2 = calcK2(nst, nar, C, P);  //calcula k2

		BigDecimal result1 = k1.multiply(Lop).multiply(k2).multiply(Hop);  //k1 * lop * k2 * hop

		BigDecimal result2 = Par.subtract(Pst);  //par - pst

		if(result1.doubleValue() >= result2.doubleValue()) {
			//viavel
			return true;
		} else {
			//nao viavel
			return false;
		}
	}

	public static BigDecimal custoProjeto(BigDecimal nst, BigDecimal nar, BigDecimal n, BigDecimal C, BigDecimal P, BigDecimal Pst, BigDecimal fdPst, BigDecimal Par, BigDecimal fdPar, BigDecimal d, BigDecimal e, BigDecimal Lop, BigDecimal Hop) throws Exception {
		BigDecimal percent = new BigDecimal(100);

		nst = nst.divide(percent);

		nar = nar.divide(percent);

		fdPst = new BigDecimal("1").subtract(fdPst.multiply(new BigDecimal("0.01"), MC), MC);  //desconto
		Pst = Pst.multiply(fdPst, MC);  //desconto

		fdPar = new BigDecimal("1").subtract(fdPar.multiply(new BigDecimal("0.01"), MC), MC);  //desconto
		Par = Par.multiply(fdPar, MC);  //desconto

		d = d.divide(percent, MC);

		e = e.divide(percent, MC);
		
		C = C.divide(new BigDecimal(1000), MC);
		
		Lop = Lop.divide(percent, MC);
		
		BigDecimal k1 = calcK1(nst, nar, n, Pst, Par, d, e);  //calcula k1

		BigDecimal k2 = calcK2(nst, nar, C, P);  //calcula k2
		
		BigDecimal diferencaP = Par.subtract(Pst, MC);  //Par - Pst

		
		BigDecimal aux = k2.divide(C, MC);  //(k2 / C)

		BigDecimal dividendo = k1.multiply(aux).multiply(Lop).multiply(Hop);  //k1 * (k2/C) * Lop * Hop
		//?? BigDecimal dividendo = k1.multiply(aux, MC).multiply(Lop, MC).multiply(Hop, MC);  //n * (k2/C) * Lop * Hop

		BigDecimal result = diferencaP.divide(dividendo, MC);
		
		result = result.multiply(new BigDecimal(1000), MC);  //KW -> MW
		
		result = result.setScale(2, BigDecimal.ROUND_HALF_UP);

		return result;
	}
	

	public static boolean isBiggerOrEquals(String v1, String v2) {
		return isBiggerOrEquals(new BigDecimal(v1), new BigDecimal(v2));
	}
	
	public static boolean isBiggerOrEquals(BigDecimal v1, BigDecimal v2) {
		if(v1.compareTo(v2) >= 0) {
			return true;
		} else {
			return false;
		}
	}

}
