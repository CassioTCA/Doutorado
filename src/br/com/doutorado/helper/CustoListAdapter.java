package br.com.doutorado.helper;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.doutorado.R;

public class CustoListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<CustoObject> custoObjects;
	private LayoutInflater inflater;
	private CustoHelper custoHelper;

	public CustoListAdapter(Context context, ArrayList<CustoObject> custoObjects, CustoHelper custoHelper) {
		this.context = context;
		this.custoObjects = custoObjects;
		this.custoHelper = custoHelper;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return custoObjects.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder{
		public TextView txtPotencia;
		public TextView txtPotenciaCV;
		public TextView txtPol;
		public TextView txtEff;
		public TextView txtPrecoRS;
		public Button btnPrecoUS;
		public TextView txtMoeda;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final ViewHolder holder;

		// reuse views
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_custo_item, null);
			holder = new ViewHolder();

			holder.btnPrecoUS = (Button) rowView.findViewById(R.id.btn_precoUS);
			holder.btnPrecoUS.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					LinearLayout linearLayout = (LinearLayout)v.getParent();
		            TextView txtPotencia = (TextView)linearLayout.findViewById(R.id.txt_potencia);
		            custoHelper.adapterClick(((Button)v).getText().toString(), txtPotencia.getText().toString());
				}
			});

			holder.txtPotencia = (TextView) rowView.findViewById(R.id.txt_potencia);
			holder.txtPotenciaCV = (TextView) rowView.findViewById(R.id.txt_potenciaCV);
			holder.txtPol = (TextView) rowView.findViewById(R.id.txt_pol);
			holder.txtEff = (TextView) rowView.findViewById(R.id.txt_eff);
			holder.txtPrecoRS = (TextView) rowView.findViewById(R.id.txt_precoRS);
			holder.btnPrecoUS = (Button) rowView.findViewById(R.id.btn_precoUS);
			holder.txtMoeda = (TextView) rowView.findViewById(R.id.txt_moeda);

			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		CustoObject object = custoObjects.get(position); 

		holder.txtPotencia.setText(object.getPotencia());
		holder.txtPotenciaCV.setText(object.getPotenciaCV());
		holder.txtPol.setText(object.getPol());
		holder.txtEff.setText(object.getEff());
		holder.txtPrecoRS.setText(object.getPrecoRS());
		
		holder.btnPrecoUS.setText(object.getPrecoUS());
		if(object.getPrecoUS().equals("x")) {
			holder.btnPrecoUS.setClickable(false);
		}
		
		holder.txtMoeda.setText(object.getMoeda());

		return rowView;
	}
} 