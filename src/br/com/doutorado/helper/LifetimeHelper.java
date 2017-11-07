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

public class LifetimeHelper {
	private Context context;
	private AssetManager assetManager;
	ArrayList<LifetimeObject> lifetimeObjects;
	
	private HelperFinish listener;

	private static final String PATH = "Lifetime/";
	private static final String TABLE = "Lifetime";

	public LifetimeHelper(Context context) {
		this.context = context;
		this.assetManager = context.getAssets();
	}

	public void setListener(HelperFinish listener) {
		this.listener = listener;
	}

	//DIALOGS
	public void startDialogs() {
		try {
			showLifetimeDialog();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	private Dialog tableDialog;
	private void showLifetimeDialog() throws IOException {
		
		tableDialog = new Dialog(context);
		tableDialog.setTitle(R.string.vida_util);

		LayoutInflater inflater = (LayoutInflater)context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
		HorizontalScrollView layoutDialog = (HorizontalScrollView) inflater.inflate(R.layout.dialog_padrao, null);
		
		//HEADER
		LinearLayout header = (LinearLayout) layoutDialog.findViewById(R.id.layout_header);
		
		TextView txtPot = new TextView(context);
		txtPot.setGravity(Gravity.CENTER);
		txtPot.setText(R.string.header_kW);
		txtPot.setWidth((int) context.getResources().getDimension(R.dimen.lifetime_text_width));
		txtPot.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		TextView txt1 = new TextView(context);
		txt1.setGravity(Gravity.CENTER);
		txt1.setText(R.string.header_lifetime);
		txt1.setWidth((int) context.getResources().getDimension(R.dimen.button_width));
		txt1.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_header_size));
		
		header.addView(txtPot);
		header.addView(txt1);
		
		lifetimeObjects = new ArrayList<LifetimeObject>();
		String[] params;

		InputStream inputStream = assetManager.open(PATH + TABLE);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		String line = reader.readLine();
		while (line != null) {
			line = line.replace(" ", "");
			params = line.split(Helper.FIELD_SEPARATOR);
			
			lifetimeObjects.add(new LifetimeObject(params[1], params[0]));

			line = reader.readLine();
		}

		reader.close();
		ListView listItens = (ListView) layoutDialog.findViewById(R.id.listView1);
		listItens.setAdapter(new LifetimeListAdapter(context, lifetimeObjects, this));

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
