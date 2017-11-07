package br.com.doutorado.helper;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import br.com.doutorado.R;

public class PontoOperacaoListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<PontoOperacaoObject> pontoOperacaoObjects;
	private LayoutInflater inflater;
	private PontoOperacaoHelper pontoOperacaoHelper;

	public PontoOperacaoListAdapter(Context context, ArrayList<PontoOperacaoObject> pontoOperacaoObjects, PontoOperacaoHelper pontoOperacaoHelper) {
		this.context = context;
		this.pontoOperacaoObjects = pontoOperacaoObjects;
		this.pontoOperacaoHelper = pontoOperacaoHelper;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return pontoOperacaoObjects.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public static class ViewHolder {
		public TextView txtCountry;
		public Button btnRangePower;
		public TextView txtLoad;
		public TextView txtAnnualOperatingHours;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final ViewHolder holder;

		// reuse views
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_ponto_operacao_item, null);
			holder = new ViewHolder();

			holder.txtCountry = (TextView) rowView.findViewById(R.id.txt_country);
			holder.txtLoad = (TextView) rowView.findViewById(R.id.txt_load);
			holder.txtAnnualOperatingHours = (TextView) rowView.findViewById(R.id.txt_annualOperatingHours);

			holder.btnRangePower = (Button) rowView.findViewById(R.id.btn_powerRange);
			holder.btnRangePower.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					pontoOperacaoHelper.adapterClick(holder.txtLoad.getText().toString(), holder.txtAnnualOperatingHours.getText().toString());
				}
			});

			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		PontoOperacaoObject object = pontoOperacaoObjects.get(position); 

		holder.txtCountry.setText(object.getCountry());
		holder.txtLoad.setText(object.getLoad());
		holder.txtAnnualOperatingHours.setText(object.getAnnualOperatingHours());
		
		
		holder.btnRangePower.setText(object.getPowerRange());
		if(object.getPowerRange().equals("x")) {
			holder.btnRangePower.setClickable(false);
		}

		return rowView;
	}
} 