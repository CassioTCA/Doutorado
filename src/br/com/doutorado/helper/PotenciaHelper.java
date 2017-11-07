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

public class PotenciaHelper {
	private Context context;
	private AssetManager assetManager;
	ArrayList<PotenciaObject> potenciaObjects;
	
	private HelperFinish listener;

	private static final String PATH = "Potencia/";
	private static final String TABLE = "Potencia";

	public PotenciaHelper(Context context) {
		this.context = context;
		this.assetManager = context.getAssets();
	}

	public void setListener(HelperFinish listener) {
		this.listener = listener;
	}

	//DIALOGS
	public void startDialogs() {
		try {
			showPotenciaDialog();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private Dialog tableDialog;
	private void showPotenciaDialog() throws IOException {
		
		tableDialog = new Dialog(context);
		tableDialog.setTitle(R.string.potencia);

		LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		HorizontalScrollView layoutDialog = (HorizontalScrollView) inflater.inflate(R.layout.dialog_padrao, null);
		
		//HEADER
		LinearLayout header = (LinearLayout) layoutDialog.findViewById(R.id.layout_header);
		
		TextView txt1 = new TextView(context);
		txt1.setGravity(Gravity.CENTER);
		txt1.setText(R.string.header_kW);
		txt1.setWidth((int) context.getResources().getDimension(R.dimen.potencia_text_width));
		txt1.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		TextView txt2 = new TextView(context);
		txt2.setGravity(Gravity.CENTER);
		txt2.setText(R.string.header_cv_hp);
		txt2.setWidth((int) context.getResources().getDimension(R.dimen.potencia_text_width));
		txt2.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		header.addView(txt1);
		header.addView(txt2);
		
		potenciaObjects = new ArrayList<PotenciaObject>();
		String[] params;

		InputStream inputStream = assetManager.open(PATH + TABLE);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		String line = reader.readLine();
		while (line != null) {
			line = line.replace(" ", "");
			params = line.split(Helper.FIELD_SEPARATOR);
			
			potenciaObjects.add(new PotenciaObject(params[0], params[1]));

			line = reader.readLine();
		}

		reader.close();
		ListView listItens = (ListView) layoutDialog.findViewById(R.id.listView1);
		listItens.setAdapter(new PotenciaListAdapter(context, potenciaObjects, this));

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
