package br.com.doutorado;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.doutorado.utils.CalcUtils;
import br.com.doutorado.utils.Utils;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

public class ActivityComparaGrafico extends ActionBarActivity implements TextWatcher {
	
	private EditText editNar;
	private EditText editNst;
	private EditText editVidaUtil;
	private EditText editEnergyCost;
	private EditText editPotencia;
	private EditText editPar;
	private EditText editPst;
	private EditText editTaxaInteresse;
	private EditText editTaxaEscalabilidade;
	private EditText editFatorDescontoPst;
	private EditText editFatorDescontoPar;

	private TextView txtResultadoViabilidade;
	private TextView txtResultadoCusto;

	private DialogSeekBar dialogNar;
	private DialogSeekBar dialogNst;
	private DialogSeekBar dialogVidaUtil;
	private DialogSeekBar dialogEnergyCost;
	private DialogSeekBar dialogPotencia;
	private DialogSeekBar dialogPar;
	private DialogSeekBar dialogPst;
	private DialogSeekBarTaxa dialogTaxaInteresse;
	private DialogSeekBarTaxa dialogTaxaEscalabilidade;
	private DialogSeekBar dialogFatorDescontoPst;
	private DialogSeekBar dialogFatorDescontoPar;
	
	private BigDecimal result;
	
	private BigDecimal pontoOperacaoX;
	private BigDecimal pontoOperacaoY;
	
	private BigDecimal nst;
	private BigDecimal nar;
	private BigDecimal n;
	private BigDecimal C;
	private BigDecimal P;
	private BigDecimal Pst;
	private BigDecimal fdPst;
	private BigDecimal Par;
	private BigDecimal fdPar;
	private BigDecimal d;
	private BigDecimal e;

	private GraphView graphView;
	private GraphViewSeries series1;
	private GraphViewSeries series2;
	private GraphViewSeries point;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compara_grafico);
		
		txtResultadoViabilidade = (TextView) findViewById(R.id.txt_resultado_viabilidade);
		txtResultadoCusto = (TextView) findViewById(R.id.txt_resultado_custo);
		
		pontoOperacaoX = new BigDecimal(getIntent().getExtras().getString(ActivityMain.LOP));
		pontoOperacaoY = new BigDecimal(getIntent().getExtras().getString(ActivityMain.HOP));
		
		initEditTexts(getIntent().getExtras());
		initDialogs();
		initGraphView();
		
		result = new BigDecimal(getIntent().getExtras().getString("result"));
		plotSeries1(result);
		plotPoint(pontoOperacaoX.doubleValue(), pontoOperacaoY.doubleValue());
		
		calcula();
	}
	
	@Override
    protected void onResume() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogLayout = inflater.inflate(R.layout.dialog_dont_show_again, null);
        final CheckBox checkBox = (CheckBox) dialogLayout.findViewById(R.id.checkBox1);
        
        dialog.setView(dialogLayout);
        dialog.setTitle(R.string.mostrar_novamente_titulo);
        dialog.setMessage(R.string.mostrar_novamente_analise_incerteza_texto);
        
        final SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
        
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	
            	
                if (checkBox.isChecked()) {
                	SharedPreferences.Editor editor = settings.edit();
                	editor.putString(Utils.PREF_ANALISE_INCERTEZA, "not_show_again");
                	editor.commit();
                }
                
                return;
            }
        });
        
        if (settings.getString(Utils.PREF_ANALISE_INCERTEZA, "").equals("")) {
        	dialog.show();
        }

        super.onResume();
    }

	public void customImageClick(View view) {
		String[] texto = view.getTag().toString().split("_");

		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setTitle(texto[0]);
		builder1.setMessage(texto[1]);
		builder1.setCancelable(true);
		builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}

	public void calcula() {
		if(isSomeFieldEmpty()) {
			Toast toast = Toast.makeText(this, R.string.msg_empty_field, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			//toast.show();
			return;
		}
		

		nst = new BigDecimal(editNst.getText().toString());
		nar = new BigDecimal(editNar.getText().toString());
		n = new BigDecimal(editVidaUtil.getText().toString()); //anos
		C = new BigDecimal(editEnergyCost.getText().toString());
		P = new BigDecimal(editPotencia.getText().toString());
		Pst = new BigDecimal(editPst.getText().toString());
		fdPst = new BigDecimal(editFatorDescontoPst.getText().toString());
		Par = new BigDecimal(editPar.getText().toString());
		fdPar = new BigDecimal(editFatorDescontoPst.getText().toString());
		d = new BigDecimal(editTaxaInteresse.getText().toString());
		e = new BigDecimal(editTaxaEscalabilidade.getText().toString());
		
		if(!isFieldsOK()) {
			return;
		}
		
		String result = CalcUtils.calcula(nst, nar, n, C, P, Pst, fdPst, Par, fdPar, d, e);
		
		if(result == "error") {
			Toast toast = Toast.makeText(this, R.string.msg_calc_error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} else {
			plotSeries2(new BigDecimal(result));
			teste();
		}
	}
	
	private boolean isFieldsOK() {
		if(CalcUtils.isBiggerOrEquals(nst, nar)) {
			Toast.makeText(this, R.string.msg_nst_nar_error, Toast.LENGTH_LONG).show();
			return false;
		}
		
		if(CalcUtils.isBiggerOrEquals(Pst, Par)) {
			Toast.makeText(this, R.string.msg_pst_par_error, Toast.LENGTH_LONG).show();
			return false;
		}
		
		return true;
	}

	private void initEditTexts(Bundle bundle) {
		editNst = (EditText) findViewById(R.id.editNst);
		editNst.setText(bundle.getString(ActivityMain.NST));
		editNst.addTextChangedListener(this);
		editNst.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				dialogNst.show();
				return true;
			}
		});

		editNar = (EditText) findViewById(R.id.editNar);
		editNar.setText(bundle.getString(ActivityMain.NAR));
		editNar.addTextChangedListener(this);
		editNar.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				dialogNar.show();
				return true;
			}
		});

		editVidaUtil = (EditText) findViewById(R.id.editVidaUtil);
		editVidaUtil.setText(bundle.getString(ActivityMain.VIDA_UTIL));
		editVidaUtil.addTextChangedListener(this);
		editVidaUtil.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				dialogVidaUtil.show();
				return true;
			}
		});

		editEnergyCost = (EditText) findViewById(R.id.editCustoEnergia);
		editEnergyCost.setText(bundle.getString(ActivityMain.CUSTO_ENERGIA));
		editEnergyCost.addTextChangedListener(this);
		editEnergyCost.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				dialogEnergyCost.show();
				return true;
			}
		});

		editPotencia = (EditText) findViewById(R.id.editPotencia);
		editPotencia.setText(bundle.getString(ActivityMain.POTENCIA));
		editPotencia.setEnabled(false);
		/*editPotencia.addTextChangedListener(this);
		editPotencia.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				dialogPotencia.show();
				return true;
			}
		});*/

		editPar = (EditText) findViewById(R.id.editPar);
		editPar.setText(bundle.getString(ActivityMain.PAR));
		editPar.addTextChangedListener(this);
		editPar.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				dialogPar.show();
				return true;
			}
		});

		editPst = (EditText) findViewById(R.id.editPst);
		editPst.setText(bundle.getString(ActivityMain.PST));
		editPst.addTextChangedListener(this);
		editPst.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				dialogPst.show();
				return true;
			}
		});

		editTaxaInteresse = (EditText) findViewById(R.id.editTaxaInteresse);
		editTaxaInteresse.setText(bundle.getString(ActivityMain.TAXA_INTERESSE));
		editTaxaInteresse.addTextChangedListener(this);
		editTaxaInteresse.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				dialogTaxaInteresse.show();
				return true;
			}
		});

		editTaxaEscalabilidade = (EditText) findViewById(R.id.editTaxaEscalabilidade);
		editTaxaEscalabilidade.setText(bundle.getString(ActivityMain.TAXA_ESCALABILIDADE));
		editTaxaEscalabilidade.addTextChangedListener(this);
		editTaxaEscalabilidade.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				dialogTaxaEscalabilidade.show();
				return true;
			}
		});

		editFatorDescontoPst = (EditText) findViewById(R.id.editFatorDescontoPst);
		editFatorDescontoPst.setText(bundle.getString(ActivityMain.FATOR_DESCONTO_PST));
		editFatorDescontoPst.addTextChangedListener(this);
		editFatorDescontoPst.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				dialogFatorDescontoPst.show();
				return true;
			}
		});

		editFatorDescontoPar = (EditText) findViewById(R.id.editFatorDescontoPar);
		editFatorDescontoPar.setText(bundle.getString(ActivityMain.FATOR_DESCONTO_PAR));
		editFatorDescontoPar.addTextChangedListener(this);
		editFatorDescontoPar.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				dialogFatorDescontoPar.show();
				return true;
			}
		});
	}
	
	private void initDialogs() {
		dialogNar = new DialogSeekBar(this, R.string.txt_nar, editNar.getText().toString(), 0, 100);
		dialogNar.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(CalcUtils.isBiggerOrEquals(editNst.getText().toString(), dialogNar.getValue() )) {
					Toast.makeText(ActivityComparaGrafico.this, R.string.msg_nar_nst_error, Toast.LENGTH_LONG).show();
				} else {
					editNar.setText(dialogNar.getValue());
					calcula();
				}
			}
		});
		
		dialogNst = new DialogSeekBar(this, R.string.txt_nst, editNst.getText().toString(), 0, 100);
		dialogNst.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(CalcUtils.isBiggerOrEquals(dialogNst.getValue(), editNar.getText().toString())) {
					Toast.makeText(ActivityComparaGrafico.this, R.string.msg_nst_nar_error, Toast.LENGTH_LONG).show();
				} else {
					editNst.setText(dialogNst.getValue());
					calcula();
				}
			}
		});
		
		dialogVidaUtil = new DialogSeekBar(this, R.string.txt_vida_util, editVidaUtil.getText().toString());
		dialogVidaUtil.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				editVidaUtil.setText(dialogVidaUtil.getValue());
				calcula();
			}
		});
		
		dialogEnergyCost = new DialogSeekBar(this, R.string.txt_custo_energia, editEnergyCost.getText().toString());
		dialogEnergyCost.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				editEnergyCost.setText(dialogEnergyCost.getValue());
				calcula();
			}
		});
		
		dialogPotencia = new DialogSeekBar(this, R.string.txt_potencia, editPotencia.getText().toString());
		dialogPotencia.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				editPotencia.setText(dialogPotencia.getValue());
				calcula();
			}
		});
		
		dialogPar = new DialogSeekBar(this, R.string.txt_par, editPar.getText().toString());
		dialogPar.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				if(CalcUtils.isBiggerOrEquals(editPst.getText().toString(), dialogPar.getValue())) {
					Toast.makeText(ActivityComparaGrafico.this, R.string.msg_par_pst_error, Toast.LENGTH_LONG).show();
				} else {
					editPar.setText(dialogPar.getValue());
					calcula();
				}
			}
		});
		
		dialogPst = new DialogSeekBar(this, R.string.txt_pst, editPst.getText().toString());
		dialogPst.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				
				if(CalcUtils.isBiggerOrEquals(dialogPst.getValue(), editPar.getText().toString())) {
					Toast.makeText(ActivityComparaGrafico.this, R.string.msg_pst_par_error, Toast.LENGTH_LONG).show();
				} else {
					editPst.setText(dialogPst.getValue());
					calcula();
				}
				
			}
		});
		
		dialogTaxaInteresse = new DialogSeekBarTaxa(this, R.string.txt_tx_interesse, editTaxaInteresse.getText().toString());
		dialogTaxaInteresse.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				editTaxaInteresse.setText(dialogTaxaInteresse.getValue());
				calcula();
			}
		});
		
		dialogTaxaEscalabilidade = new DialogSeekBarTaxa(this, R.string.txt_tx_escalabilidade, editTaxaEscalabilidade.getText().toString());
		dialogTaxaEscalabilidade.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				editTaxaEscalabilidade.setText(dialogTaxaEscalabilidade.getValue());
				calcula();
			}
		});
		
		dialogFatorDescontoPst = new DialogSeekBar(this, R.string.txt_fator_desconto, editFatorDescontoPst.getText().toString());
		dialogFatorDescontoPst.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				editFatorDescontoPst.setText(dialogFatorDescontoPst.getValue());
				calcula();
			}
		});
		
		dialogFatorDescontoPar = new DialogSeekBar(this, R.string.txt_fator_desconto, editFatorDescontoPar.getText().toString());
		dialogFatorDescontoPar.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				editFatorDescontoPar.setText(dialogFatorDescontoPar.getValue());
				calcula();
			}
		});
		
	}

	private void initGraphView() {

		graphView = new LineGraphView(this, getResources().getString(R.string.curva_viabilidade));

		// set legend
		graphView.setShowLegend(true);
		graphView.setLegendAlign(LegendAlign.BOTTOM);

		// set view port, start=20, size=80
		graphView.setViewPort(20, 80);
		graphView.setScrollable(false);
		graphView.getGraphViewStyle().setNumHorizontalLabels(8);
		graphView.getGraphViewStyle().setNumVerticalLabels(8);
		graphView.getGraphViewStyle().setTextSize(30);
		graphView.getGraphViewStyle().setLegendWidth((int)getResources().getDimension(R.dimen.legend_width));

		GraphViewData[] data = new GraphViewData[2];
		data[0] = new GraphViewData(0, 0);
		data[1] = new GraphViewData(0, 0);

		series1 = new GraphViewSeries(getResources().getString(R.string.curva_original), new GraphViewSeriesStyle(Color.rgb(00, 00, 255), 3), data);
		graphView.addSeries(series1);

		series2 = new GraphViewSeries(getResources().getString(R.string.curva_incerteza), new GraphViewSeriesStyle(Color.rgb(50, 200, 00), 3), data);
		graphView.addSeries(series2);
		
		point = new GraphViewSeries(getResources().getString(R.string.ponto), new GraphViewSeriesStyle(Color.rgb(200, 50, 00), 15), data);
		graphView.addSeries(point);

		LinearLayout layoutGraph = (LinearLayout) findViewById(R.id.layoutGraph);
		layoutGraph.addView(graphView);
	}

	private void plotSeries1(BigDecimal result) {
		GraphViewData[] data = new GraphViewData[17];

		int j = 0;
		for(int i = 20; i <= 100; i = i + 5) {
			double L = (double) i / 100.0;
			BigDecimal H = result.divide(new BigDecimal(L), 5, RoundingMode.HALF_UP);
			data[j] = new GraphViewData(i, H.doubleValue());
			j++;
		}

		series1.resetData(data);
	}

	private void plotSeries2(BigDecimal result) {
		GraphViewData[] data = new GraphViewData[17];

		int j = 0;
		for(int i = 20; i <= 100; i = i + 5) {
			double L = (double) i / 100.0;
			BigDecimal H = result.divide(new BigDecimal(L), 5, RoundingMode.HALF_UP);
			data[j] = new GraphViewData(i, H.doubleValue());
			j++;
		}

		series2.resetData(data);
	}
	
	private void plotPoint(double pointX, double pointY) {
		GraphViewData[] data = new GraphViewData[2];
		data[0] = new GraphViewData(pointX, pointY);
		data[1] = new GraphViewData(pointX + 0.1, pointY + 0.1);

		point.resetData(data);
	}
	
	private void teste() {
		if(pontoOperacaoX.doubleValue() > 100 || pontoOperacaoX.doubleValue() < 1 || pontoOperacaoY.doubleValue() > 8700 || pontoOperacaoY.doubleValue() < 1) {
			pontoOperacaoX = new BigDecimal(0);
			pontoOperacaoY = new BigDecimal(0);
			//?? plotPoint(0, 0);
			Toast.makeText(ActivityComparaGrafico.this, R.string.msg_error_ponto_operacao, Toast.LENGTH_LONG).show();
		} else {
			BigDecimal Lop = new BigDecimal(pontoOperacaoX.doubleValue());
			BigDecimal Hop = new BigDecimal(pontoOperacaoY.doubleValue());

			//viabilidade
			try {
				String resultViabilidade = " ";
				
				if(CalcUtils.isViavel(nst, nar, n, C, P, Pst, fdPst, Par, fdPar, d, e, Lop, Hop)) {
					resultViabilidade += getResources().getString(R.string.viavel);
					txtResultadoViabilidade.setText(resultViabilidade);
				} else {
					resultViabilidade += getResources().getString(R.string.nao_viavel);
					txtResultadoViabilidade.setText(resultViabilidade);
				}
			} catch (Exception ex) {
				txtResultadoViabilidade.setText(" Error");
			}
			
			try {
				//custo
				txtResultadoCusto.setText(" " + CalcUtils.custoProjeto(nst, nar, n, C, P, Pst, fdPst, Par, fdPar, d, e, Lop, Hop).toPlainString() + " U$/MWh");
			} catch (Exception ex) {
				txtResultadoCusto.setText(" Error");
			}
		}
	}

	private boolean isSomeFieldEmpty() {
		String f1 = editNst.getText().toString();
		String f2 = editNar.getText().toString();
		String f3 = editVidaUtil.getText().toString();
		String f4 = editEnergyCost.getText().toString();
		String f5 = editPotencia.getText().toString();
		String f6 = editPar.getText().toString();
		String f7 = editPst.getText().toString();
		String f8 = editTaxaInteresse.getText().toString();
		String f9 = editTaxaEscalabilidade.getText().toString();

		if(f1.equals("") || f2.equals("") || f3.equals("") 
				|| f4.equals("") || f5.equals("") || f6.equals("") 
				|| f7.equals("") || f8.equals("") || f9.equals("")) {

			return true;
		}

		//fator desconto nao precisa

		return false;
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		calcula();
	}
	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
}
