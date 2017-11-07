package br.com.doutorado;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.com.doutorado.helper.CustoHelper;
import br.com.doutorado.helper.EffHelper;
import br.com.doutorado.helper.EnergyCostHelper;
import br.com.doutorado.helper.FatorDescontoHelper;
import br.com.doutorado.helper.LifetimeHelper;
import br.com.doutorado.helper.PotenciaHelper;
import br.com.doutorado.helper.TxHelper;
import br.com.doutorado.interfaces.HelperFinish;
import br.com.doutorado.utils.CalcUtils;
import br.com.doutorado.utils.Utils;
import br.com.doutorado.widget.CustomImageView;

import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.LineGraphView;

public class ActivityMain extends ActionBarActivity {

	public static final String NAR = "0";
	public static final String NST = "1";
	public static final String VIDA_UTIL = "2";
	public static final String CUSTO_ENERGIA = "3";
	public static final String POTENCIA = "4";
	public static final String PAR = "5";
	public static final String PST = "6";
	public static final String TAXA_INTERESSE = "7";
	public static final String TAXA_ESCALABILIDADE = "8";
	public static final String FATOR_DESCONTO_PST = "9";
	public static final String FATOR_DESCONTO_PAR = "10";
	public static final String LOP = "11";
	public static final String HOP = "12";

	private boolean isAllFieldsOK = false;

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

	private LinearLayout layoutRootGraph;
	private LinearLayout layoutViabilidade;
	private TextView txtResultadoViabilidade;
	private TextView txtResultadoCusto;

	private BigDecimal pontoOperacaoX = new BigDecimal(0);
	private BigDecimal pontoOperacaoY = new BigDecimal(0);

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

	//Helpers
	private EffHelper tabelaNst;
	private EffHelper tabelaNar;
	private LifetimeHelper tabelaLifetime;
	private EnergyCostHelper tabelaEnergyCost;
	private PotenciaHelper tabelaPotencia;
	private CustoHelper tabelaPst;
	private CustoHelper tabelaPar;
	private TxHelper tabelaTxInt;
	private TxHelper tabelaTxEsc;
	private FatorDescontoHelper tabelaDescontoPst;
	private FatorDescontoHelper tabelaDescontoPar;

	private BigDecimal result;

	private LineGraphView graphView;
	private GraphViewSeries series;
	private GraphViewSeries point;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		
		layoutViabilidade = (LinearLayout) findViewById(R.id.layout_viabilidade);
		layoutRootGraph = (LinearLayout) findViewById(R.id.layoutRootGraph);

		initHelpers();
		initEditTexts();
		initTextViews();
		initGraphView();
		
		//??
		preencheEditTexts();
	}
	
	@Override
    protected void onResume() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogLayout = inflater.inflate(R.layout.dialog_dont_show_again, null);
        final CheckBox checkBox = (CheckBox) dialogLayout.findViewById(R.id.checkBox1);
        
        dialog.setView(dialogLayout);
        dialog.setTitle(R.string.mostrar_novamente_titulo);
        dialog.setMessage(R.string.mostrar_novamente_campo_pressionado_texto);
        
        final SharedPreferences settings = getSharedPreferences(Utils.PREFS_NAME, 0);
        
        dialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	
            	
                if (checkBox.isChecked()) {
                	SharedPreferences.Editor editor = settings.edit();
                	editor.putString(Utils.PREF_CAMPO_PRESSIONADO, "not_show_again");
                	editor.commit();
                }
                
                return;
            }
        });
        
        if (settings.getString(Utils.PREF_CAMPO_PRESSIONADO, "").equals("")) {
        	dialog.show();
        }

        super.onResume();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent it;
		
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_about:
			it = new Intent(this, ActivitySobre.class);
			startActivity(it);
			return true;
		case R.id.action_tutorial:
			it = new Intent(this, ActivityTutorial.class);
			startActivity(it);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void showFieldTutorial(View view) {
		String[] texto = view.getTag().toString().split("_");

		AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
		builder1.setTitle(texto[0]);
		
		String tutorial = texto[1];
		
		//??
		CustomImageView btnTutorialPontoOP = (CustomImageView) findViewById(R.id.btnTutorialPontoOP);
		if(view != btnTutorialPontoOP) {
			tutorial += "\n\n";
			tutorial += getResources().getString(R.string.tutorial_pressione_segure);
		}
		
		builder1.setMessage(tutorial);
		builder1.setCancelable(true);
		builder1.setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});

		AlertDialog alert11 = builder1.create();
		alert11.show();
	}

	public void showApplicationTutorial(View view) {
		Intent it = new Intent(this, ActivitySobre.class);
		startActivity(it);
	}

	public void comparaGrafico(View view) {
		calcula(null);
		
		if(isAllFieldsOK) {
			Intent it = new Intent(this, ActivityComparaGrafico.class);
			it.putExtra("result", result.toPlainString());

			it.putExtra(NAR, editNar.getText().toString());
			it.putExtra(NST, editNst.getText().toString());
			it.putExtra(VIDA_UTIL, editVidaUtil.getText().toString());
			it.putExtra(CUSTO_ENERGIA, editEnergyCost.getText().toString());
			it.putExtra(POTENCIA, editPotencia.getText().toString());
			it.putExtra(PAR, editPar.getText().toString());
			it.putExtra(PST, editPst.getText().toString());
			it.putExtra(TAXA_INTERESSE, editTaxaInteresse.getText().toString());
			it.putExtra(TAXA_ESCALABILIDADE, editTaxaEscalabilidade.getText().toString());
			it.putExtra(FATOR_DESCONTO_PST, editFatorDescontoPst.getText().toString());
			it.putExtra(FATOR_DESCONTO_PAR, editFatorDescontoPar.getText().toString());
			it.putExtra(LOP, "" + pontoOperacaoX);
			it.putExtra(HOP, "" + pontoOperacaoY);

			startActivity(it);
		}
	}
	
	public void limpa(View view) {
		editNar.setText("");
		editNst.setText("");
		editVidaUtil.setText("");
		editEnergyCost.setText("");
		editPotencia.setText("");
		editPar.setText("");
		editPst.setText("");
		editTaxaInteresse.setText("");
		editTaxaEscalabilidade.setText("");
		editFatorDescontoPst.setText("");
		editFatorDescontoPar.setText("");
		
		layoutRootGraph.setVisibility(View.INVISIBLE);
		layoutViabilidade.setVisibility(View.GONE);
		
		initHelpers();
		initTextViews();
		initGraphView();
		
		pontoOperacaoX = new BigDecimal(0);
		pontoOperacaoY = new BigDecimal(0);
	}

	public void showDialogPontoOperacao(View view) {
		calcula(null);

		if(isAllFieldsOK) {
			Intent it = new Intent(this, DialogPontoOperacao.class);

			it.putExtra("pointx", pontoOperacaoX);
			it.putExtra("pointy", pontoOperacaoY);
			startActivityForResult(it, 555);
		}
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent intent) {
		super.onActivityResult(arg0, arg1, intent);
		
		String pointx = intent.getStringExtra("pointx");
		String pointy = intent.getStringExtra("pointy");
		
		pontoOperacaoX = new BigDecimal(pointx);
		pontoOperacaoY = new BigDecimal(pointy);

		if(pontoOperacaoX.doubleValue() > 100 || pontoOperacaoX.doubleValue() < 1 || pontoOperacaoY.doubleValue() > 8700 || pontoOperacaoY.doubleValue() < 1) {
			pontoOperacaoX = new BigDecimal(0);
			pontoOperacaoY = new BigDecimal(0);
			//?? plotPoint(0, 0);
			Toast.makeText(ActivityMain.this, R.string.msg_error_ponto_operacao, Toast.LENGTH_LONG).show();
		} else {
			plotPoint(pontoOperacaoX.doubleValue(), pontoOperacaoY.doubleValue());

			layoutViabilidade.setVisibility(View.VISIBLE);
			
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

	public void calcula(View view) {
		if(editFatorDescontoPar.getText().toString().equals("")) {
			editFatorDescontoPar.setText("0");
		}
		if(editFatorDescontoPst.getText().toString().equals("")) {
			editFatorDescontoPst.setText("0");
		}
		
		initGraphView();
		
		layoutViabilidade.setVisibility(View.GONE);
		
		if(isSomeFieldEmpty()) {
			isAllFieldsOK = false;
			Toast toast = Toast.makeText(this, R.string.msg_empty_field, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
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
			isAllFieldsOK = false;
			return;
		}
		
		String wResult = CalcUtils.calcula(nst, nar, n, C, P, Pst, fdPst, Par, fdPar, d, e);

		if(wResult == "error") {
			isAllFieldsOK = false;
			Toast toast = Toast.makeText(this, R.string.msg_calc_error, Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} else {
			layoutRootGraph.setVisibility(View.VISIBLE);
			isAllFieldsOK = true;
			this.result = new BigDecimal(wResult);
			plotSeries(result);
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

	private void initHelpers() {
		tabelaNst = new EffHelper(this);
		tabelaNst.setListener(new HelperFinish() {

			@Override
			public void helperFinished(String... results) {
				String nst = results[0];
				String nar = editNar.getText().toString();

				if(isPotenciaOk(results[1]) == false) {
					Toast.makeText(ActivityMain.this, R.string.msg_potencia_error, Toast.LENGTH_LONG).show();
				} else if(nar.equals("") == false &&  CalcUtils.isBiggerOrEquals(nst, nar)) {
					Toast.makeText(ActivityMain.this, R.string.msg_nst_nar_error, Toast.LENGTH_LONG).show();
				} else {
					editNst.setText(results[0]);
					editPotencia.setText(results[1]);
				}
			}
		});

		tabelaNar = new EffHelper(this);
		tabelaNar.setListener(new HelperFinish() {

			@Override
			public void helperFinished(String... results) {
				String nar = results[0];
				String nst = editNst.getText().toString();

				if(isPotenciaOk(results[1]) == false) {
					Toast.makeText(ActivityMain.this, R.string.msg_potencia_error, Toast.LENGTH_LONG).show();
				} else if(nst.equals("") == false && CalcUtils.isBiggerOrEquals(nst, nar)) {
					Toast.makeText(ActivityMain.this, R.string.msg_nar_nst_error, Toast.LENGTH_LONG).show();
				} else {
					editNar.setText(results[0]);
					editPotencia.setText(results[1]);
				}
			}
		});


		tabelaLifetime = new LifetimeHelper(this);
		tabelaLifetime.setListener(new HelperFinish() {

			@Override
			public void helperFinished(String... results) {
				editVidaUtil.setText(results[0]);

				String[] pots = results[1].split("<=kW<");
				double pot1 = Double.parseDouble(pots[0]);
				double pot2 = Double.parseDouble(pots[1]);

				String wPot = editPotencia.getText().toString();
				if(wPot.equals("") == false) {
					double pot = Double.parseDouble(wPot);
					if(pot < pot1  ||  pot >= pot2) {
						Toast.makeText(ActivityMain.this, R.string.msg_potencia_warning, Toast.LENGTH_LONG).show();
					}
				}

			}
		});

		tabelaEnergyCost = new EnergyCostHelper(this);
		tabelaEnergyCost.setListener(new HelperFinish() {

			@Override
			public void helperFinished(String... results) {
				editEnergyCost.setText(results[0]);
			}
		});

		tabelaPotencia = new PotenciaHelper(this);

		tabelaPst = new CustoHelper(this);
		tabelaPst.setListener(new HelperFinish() {

			@Override
			public void helperFinished(String... results) {
				String pst = results[0];
				String par = editPar.getText().toString();

				if(par.equals("") == false &&  CalcUtils.isBiggerOrEquals(pst, par)) {
					Toast.makeText(ActivityMain.this, R.string.msg_pst_par_error, Toast.LENGTH_LONG).show();
					return;
				} else {
					editPst.setText(results[0]);
				}

				if(isPotenciaOk(results[1]) == false) {
					Toast.makeText(ActivityMain.this, R.string.msg_potencia_warning, Toast.LENGTH_LONG).show();
				}
			}
		});

		tabelaPar = new CustoHelper(this);
		tabelaPar.setListener(new HelperFinish() {

			@Override
			public void helperFinished(String... results) {
				String par = results[0];
				String pst = editPst.getText().toString();

				if(pst.equals("") == false &&  CalcUtils.isBiggerOrEquals(pst, par)) {
					Toast.makeText(ActivityMain.this, R.string.msg_par_pst_error, Toast.LENGTH_LONG).show();
					return;
				} else {
					editPar.setText(results[0]);
				}

				if(isPotenciaOk(results[1]) == false) {
					Toast.makeText(ActivityMain.this, R.string.msg_potencia_warning, Toast.LENGTH_LONG).show();
				}
			}
		});

		tabelaTxInt = new TxHelper(this);
		tabelaTxInt.setListener(new HelperFinish() {

			@Override
			public void helperFinished(String... results) {
				editTaxaInteresse.setText(results[0]);
			}
		});

		tabelaTxEsc = new TxHelper(this);
		tabelaTxEsc.setListener(new HelperFinish() {

			@Override
			public void helperFinished(String... results) {
				editTaxaEscalabilidade.setText(results[0]);
			}
		});

		tabelaDescontoPst = new FatorDescontoHelper(this);
		tabelaDescontoPst.setListener(new HelperFinish() {

			@Override
			public void helperFinished(String... results) {
				editFatorDescontoPst.setText(results[0]);
			}
		});

		tabelaDescontoPar = new FatorDescontoHelper(this);
		tabelaDescontoPar.setListener(new HelperFinish() {

			@Override
			public void helperFinished(String... results) {
				editFatorDescontoPar.setText(results[0]);
			}
		});
	}
	
	//?? Teste
	private void preencheEditTexts() {
		editNst.setText("92");
		editNar.setText("94");
		editVidaUtil.setText("15");
		editEnergyCost.setText("71.8");
		editPotencia.setText("11");
		editPar.setText("1220");
		editPst.setText("1004");
		editTaxaInteresse.setText("3");
		editTaxaEscalabilidade.setText("2");
		editFatorDescontoPst.setText("0");
		editFatorDescontoPar.setText("0");
	}

	private void initEditTexts() {
		editNst = (EditText) findViewById(R.id.editNst);
		editNst.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				tabelaNst.startDialogs();
				return true;
			}
		});

		editNar = (EditText) findViewById(R.id.editNar);
		editNar.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				tabelaNar.startDialogs();
				return true;
			}
		});

		editVidaUtil = (EditText) findViewById(R.id.editVidaUtil);
		editVidaUtil.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				tabelaLifetime.startDialogs();
				return true;
			}
		});

		editEnergyCost = (EditText) findViewById(R.id.editCustoEnergia);
		editEnergyCost.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				tabelaEnergyCost.startDialogs();
				return true;
			}
		});

		editPotencia = (EditText) findViewById(R.id.editPotencia);
		editPotencia.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				tabelaPotencia.startDialogs();
				return true;
			}
		});

		editPar = (EditText) findViewById(R.id.editPar);
		editPar.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				tabelaPar.startDialogs();
				return true;
			}
		});

		editPst = (EditText) findViewById(R.id.editPst);
		editPst.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				tabelaPst.startDialogs();
				return true;
			}
		});

		editTaxaInteresse = (EditText) findViewById(R.id.editTaxaInteresse);
		editTaxaInteresse.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				tabelaTxInt.startDialogs(TxHelper.TX_INT, R.string.taxa_desconto);
				return true;
			}
		});

		editTaxaEscalabilidade = (EditText) findViewById(R.id.editTaxaEscalabilidade);
		editTaxaEscalabilidade.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				tabelaTxEsc.startDialogs(TxHelper.TX_ESC, R.string.taxa_escalabilidade);
				return true;
			}
		});

		editFatorDescontoPst = (EditText) findViewById(R.id.editFatorDescontoPst);
		editFatorDescontoPst.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				tabelaDescontoPst.startDialogs();
				return true;
			}
		});

		editFatorDescontoPar = (EditText) findViewById(R.id.editFatorDescontoPar);
		editFatorDescontoPar.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View arg0) {
				tabelaDescontoPar.startDialogs();
				return true;
			}
		});
	}
	
	private void initTextViews() {
		txtResultadoViabilidade = (TextView) findViewById(R.id.txt_resultado_viabilidade);
		txtResultadoCusto = (TextView) findViewById(R.id.txt_resultado_custo);
	}

	private void initGraphView() {
		LinearLayout layoutGraph = (LinearLayout) findViewById(R.id.layoutGraph);
		
		if(graphView != null) {
			layoutGraph.removeView(graphView);
		}

		graphView = new LineGraphView(this, getResources().getString(R.string.curva_viabilidade));

		// set legend
		graphView.setShowLegend(true);
		graphView.setLegendAlign(LegendAlign.BOTTOM);

		// set view port, start=20, size=80
		graphView.setViewPort(20, 80);
		graphView.setScrollable(false);
		graphView.getGraphViewStyle().setNumHorizontalLabels(1);
		graphView.getGraphViewStyle().setNumVerticalLabels(1);
		graphView.getGraphViewStyle().setTextSize(30);
		graphView.getGraphViewStyle().setLegendWidth((int)getResources().getDimension(R.dimen.legend_width));
		//graphView.setDrawBackground(true);
		graphView.setDrawDataPoints(true);
		graphView.setDataPointsRadius(5);

		GraphViewData[] data = new GraphViewData[2];
		data[0] = new GraphViewData(0, 0);
		data[1] = new GraphViewData(0, 0);

		series = new GraphViewSeries(getResources().getString(R.string.curva_viabilidade), new GraphViewSeriesStyle(Color.rgb(00, 00, 255), 3), data);
		graphView.addSeries(series);

		point = new GraphViewSeries(getResources().getString(R.string.ponto), new GraphViewSeriesStyle(Color.rgb(200, 50, 00), 15), data);
		graphView.addSeries(point);

		layoutGraph.addView(graphView);
	}

	private void plotSeries(BigDecimal result) {
		graphView.getGraphViewStyle().setNumHorizontalLabels(10);
		graphView.getGraphViewStyle().setNumVerticalLabels(10);

		GraphViewData[] data = new GraphViewData[17];

		int j = 0;
		for(int i = 20; i <= 100; i = i + 5) {
			double L = (double) i / 100.0;
			BigDecimal H = result.divide(new BigDecimal(L), 5, RoundingMode.HALF_UP);
			data[j] = new GraphViewData(i, H.doubleValue());
			j++;
		}

		series.resetData(data);
	}

	private void plotPoint(double pointX, double pointY) {
		GraphViewData[] data = new GraphViewData[2];
		data[0] = new GraphViewData(pointX, pointY);
		data[1] = new GraphViewData(pointX + 0.1, pointY + 0.1);

		point.resetData(data);
	}

	// VERIFICADORES
	private boolean isPotenciaOk(String pot) {

		String wPot = editPotencia.getText().toString();
		if(wPot.equals("") == false) {
			if(wPot.equals(pot) == true) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
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
}
