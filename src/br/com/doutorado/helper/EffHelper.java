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

public class EffHelper {
	private Context context;
	private AssetManager assetManager;

	private HelperFinish listener;

	private static final String EFF_PATH = "Eff";

	private String tableName;
	private String padraoName;

	ArrayList<EffObject> effObjects;

	public EffHelper(Context context) {
		this.context = context;
		this.assetManager = context.getAssets();
	}

	public void setListener(HelperFinish listener) {
		this.listener = listener;
	}

	//DIALOGS
	public void startDialogs() {
		try {
			showPadroesDialog();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	private void showPadroesDialog() throws IOException {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.padrao);

		final String[] padroes = assetManager.list("Eff");


		builder.setItems(padroes, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				padraoName = padroes[which];
				try {
					initDialogPadrao();
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		});

		AlertDialog alert = builder.create();
		alert.show();
	}

	private void initDialogPadrao() throws IOException {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(padraoName);

		final String[] tables = assetManager.list(EFF_PATH + "/" + padraoName);

		builder.setItems(tables, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				tableName = tables[which];
				try {
					showTableDialog();
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
	private void showTableDialog() throws IOException {
		initTable();
		
		tableDialog = new Dialog(context);
		tableDialog.setTitle(tableName);
		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		HorizontalScrollView layoutDialog = (HorizontalScrollView) inflater.inflate(R.layout.dialog_padrao, null);
		
		//HEADER
		LinearLayout header = (LinearLayout) layoutDialog.findViewById(R.id.layout_header);
		
		TextView txtPot = new TextView(context);
		txtPot.setGravity(Gravity.CENTER);
		txtPot.setText(R.string.header_potencia);
		txtPot.setWidth((int) context.getResources().getDimension(R.dimen.text_width));
		txtPot.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		TextView txt1 = new TextView(context);
		txt1.setGravity(Gravity.CENTER);
		txt1.setText(R.string.header_pol2);
		txt1.setWidth((int) context.getResources().getDimension(R.dimen.button_width));
		txt1.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		TextView txt2 = new TextView(context);
		txt2.setGravity(Gravity.CENTER);
		txt2.setText(R.string.header_pol4);
		txt2.setWidth((int) context.getResources().getDimension(R.dimen.button_width));
		txt2.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		TextView txt3 = new TextView(context);
		txt3.setGravity(Gravity.CENTER);
		txt3.setText(R.string.header_pol6);
		txt3.setWidth((int) context.getResources().getDimension(R.dimen.button_width));
		txt3.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		header.addView(txtPot);
		header.addView(txt1);
		header.addView(txt2);
		header.addView(txt3);
		
		if(effObjects.get(0).getValues().length == 4) {
			TextView txt4 = new TextView(context);
			txt4.setGravity(Gravity.CENTER);
			txt4.setText(R.string.header_pol8);
			txt4.setWidth((int) context.getResources().getDimension(R.dimen.button_width));
			txt4.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
			header.addView(txt4);
		}
		
		
		ListView listItens = (ListView) layoutDialog.findViewById(R.id.listView1);
		listItens.setAdapter(new EffListAdapter(context, effObjects, this));

		tableDialog.setContentView(layoutDialog);

		tableDialog.show();	
	}

	private ArrayList<EffObject> initTable() throws IOException {
		effObjects = new ArrayList<EffObject>();

		String[] params = null;
		String[] values = null;
		String pot;

		InputStream inputStream = assetManager.open(EFF_PATH + "/" + padraoName + "/" + tableName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		String line = reader.readLine();
		while (line != null) {
			params = line.split(Helper.FIELD_SEPARATOR);
			pot = params[0];
			
			if(params.length == 4) {
				values = new String[]{params[1], params[2], params[3]};
			} else if(params.length == 5) {
				values = new String[]{params[1], params[2], params[3], params[4]};
			}

			effObjects.add(new EffObject(pot, values));
			
			line = reader.readLine();
		}

		reader.close();

		return effObjects;
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