package br.com.doutorado.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import br.com.doutorado.R;
import br.com.doutorado.interfaces.HelperFinish;

public class CustoHelper {
	private Context context;
	private AssetManager assetManager;
	ArrayList<CustoObject> custoObjects;
	
	private HelperFinish listener;

	private static final String CUSTO_PATH = "Custo";

	public CustoHelper(Context context) {
		this.context = context;
		this.assetManager = context.getAssets();
	}

	public void setListener(HelperFinish listener) {
		this.listener = listener;
	}

	//DIALOGS
	public void startDialogs() {
		try {
			showTipoCustoDialog();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private void showTipoCustoDialog() throws IOException {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.tabelas);

		final String[] tipos = assetManager.list(CUSTO_PATH);

		builder.setItems(tipos, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					showTablesDialog(CUSTO_PATH + "/" + tipos[which]);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		});

		AlertDialog alert = builder.create();
		alert.show();	
	}
	
	private void showTablesDialog(final String path) throws IOException {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.tabela);

		final String[] tables = assetManager.list(path);


		builder.setItems(tables, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				try {
					showCustoDialog(path + "/" + tables[which]);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		});

		AlertDialog alert = builder.create();
		alert.show();	
	}
	
	private Dialog tableDialog;
	private void showCustoDialog(String path) throws IOException {
		
		tableDialog = new Dialog(context);
		tableDialog.setTitle(R.string.tabela);

		LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		HorizontalScrollView layoutDialog = (HorizontalScrollView) inflater.inflate(R.layout.dialog_padrao, null);
		
		//HEADER
		LinearLayout header = (LinearLayout) layoutDialog.findViewById(R.id.layout_header);
		
		TextView txt1 = new TextView(context);
		txt1.setGravity(Gravity.CENTER);
		txt1.setText(R.string.header_kW);
		txt1.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		TextView txt2 = new TextView(context);
		txt2.setGravity(Gravity.CENTER);
		txt2.setText(R.string.header_cv);
		txt2.setWidth((int) context.getResources().getDimension(R.dimen.text_width));
		txt2.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		TextView txt3 = new TextView(context);
		txt3.setGravity(Gravity.CENTER);
		txt3.setText(R.string.header_poles);
		txt3.setWidth((int) context.getResources().getDimension(R.dimen.text_width));
		txt3.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		TextView txt4 = new TextView(context);
		txt4.setGravity(Gravity.CENTER);
		txt4.setText(R.string.header_eff);
		txt4.setWidth((int) context.getResources().getDimension(R.dimen.text_width));
		txt4.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		TextView txt5 = new TextView(context);
		txt5.setGravity(Gravity.CENTER);
		txt5.setText(R.string.header_price_rs);
		txt5.setWidth((int) context.getResources().getDimension(R.dimen.text_width));
		txt5.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		TextView txt6 = new TextView(context);
		txt6.setGravity(Gravity.CENTER);
		txt6.setText(R.string.header_price_us);
		txt6.setWidth((int) context.getResources().getDimension(R.dimen.button_width));
		txt6.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		TextView txt7 = new TextView(context);
		txt7.setGravity(Gravity.CENTER);
		txt7.setText(R.string.header_currency);
		txt7.setWidth((int) context.getResources().getDimension(R.dimen.button_width));
		txt7.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		header.addView(txt1);
		header.addView(txt2);
		header.addView(txt3);
		header.addView(txt4);
		header.addView(txt5);
		header.addView(txt6);
		header.addView(txt7);
		
		custoObjects = new ArrayList<CustoObject>();
		String[] params;

		InputStream inputStream = assetManager.open(path);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		String line = reader.readLine();
		while (line != null) {
			params = line.split(":");
			
			custoObjects.add(new CustoObject(params[0], params[1], params[2], params[3], params[4], params[5], params[6]));

			line = reader.readLine();
		}

		reader.close();
		ListView listItens = (ListView) layoutDialog.findViewById(R.id.listView1);
		listItens.setAdapter(new CustoListAdapter(context, custoObjects, this));

		tableDialog.setContentView(layoutDialog);

		tableDialog.show();	
	}
	
	private void finishHelper(String... results) {
		tableDialog.dismiss();
		if(listener != null) {
			listener.helperFinished(results);
		}
	}
	
	public void adapterClick(String result, String pot) {
		String[] results = new String[]{result, pot};
		finishHelper(results);
	}

}
