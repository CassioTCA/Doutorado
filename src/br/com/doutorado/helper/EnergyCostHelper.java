package br.com.doutorado.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
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

public class EnergyCostHelper {
	private static final String ENERGY_COST_PATH = "EnergyCost/energycost.txt";
	
	private Context context;
	private AssetManager assetManager;
	ArrayList<EnergyCostObject> energyCostObjects;
	
	private HelperFinish listener;

	public EnergyCostHelper(Context context) {
		this.context = context;
		this.assetManager = context.getAssets();
	}

	public void setListener(HelperFinish listener) {
		this.listener = listener;
	}

	//DIALOGS
	public void startDialogs() {
		try {
			showTxDialog();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private Dialog tableDialog;
	private void showTxDialog() throws IOException {
		
		tableDialog = new Dialog(context);
		tableDialog.setTitle(R.string.custo_energia);

		LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		HorizontalScrollView layoutDialog = (HorizontalScrollView) inflater.inflate(R.layout.dialog_padrao, null);
		
		//HEADER
		LinearLayout header = (LinearLayout) layoutDialog.findViewById(R.id.layout_header);
		
		TextView txt1 = new TextView(context);
		txt1.setGravity(Gravity.CENTER);
		txt1.setText(R.string.header_country);
		txt1.setWidth((int) context.getResources().getDimension(R.dimen.country_text_size));
		txt1.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		TextView txt2 = new TextView(context);
		txt2.setGravity(Gravity.CENTER);
		txt2.setText(R.string.header_electricity_industry);
		txt2.setWidth((int) context.getResources().getDimension(R.dimen.button_width));
		txt2.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		TextView txt3 = new TextView(context);
		txt3.setGravity(Gravity.CENTER);
		txt3.setText(R.string.header_electricity_households);
		txt3.setWidth((int) context.getResources().getDimension(R.dimen.text_width));
		txt3.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		TextView txt4 = new TextView(context);
		txt4.setGravity(Gravity.CENTER);
		txt4.setText(R.string.header_currency);
		txt4.setWidth((int) context.getResources().getDimension(R.dimen.text_width));
		txt4.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		header.addView(txt1);
		header.addView(txt2);
		header.addView(txt3);
		header.addView(txt4);
		
		energyCostObjects = new ArrayList<EnergyCostObject>();
		String[] params;

		InputStream inputStream = assetManager.open(ENERGY_COST_PATH);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		String line = reader.readLine();
		while (line != null) {
			params = line.split(Helper.FIELD_SEPARATOR);
			energyCostObjects.add(new EnergyCostObject(params[0], params[1], params[2], params[3]));
			line = reader.readLine();
		}

		reader.close();

		ListView listItens = (ListView) layoutDialog.findViewById(R.id.listView1);
		listItens.setAdapter(new EnergyCostListAdapter(context, energyCostObjects, this));

		tableDialog.setContentView(layoutDialog);

		tableDialog.show();	
	}
	
	private void finishHelper(String... results) {
		tableDialog.dismiss();
		if(listener != null) {
			listener.helperFinished(results);
		}
	}
	
	public void adapterClick(String result) {
		String[] results = new String[]{result};
		finishHelper(results);
	}
}
