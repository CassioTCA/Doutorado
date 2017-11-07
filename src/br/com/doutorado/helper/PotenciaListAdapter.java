package br.com.doutorado.helper;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.com.doutorado.R;

public class PotenciaListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<PotenciaObject> potenciaObjects;
	private LayoutInflater inflater;
	
	//TODO
	private PotenciaHelper potenciaHelper;

	public PotenciaListAdapter(Context context, ArrayList<PotenciaObject> potenciaObjects, PotenciaHelper potenciaHelper) {
		this.context = context;
		this.potenciaObjects = potenciaObjects;
		this.potenciaHelper = potenciaHelper;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return potenciaObjects.size();
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
		public TextView txtPotenciaKW;
		public TextView txtPotenciaCV;
	}



	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		final ViewHolder holder;

		// reuse views
		if (rowView == null) {
			rowView = inflater.inflate(R.layout.list_potencia_item, null);
			holder = new ViewHolder();

			holder.txtPotenciaKW = (TextView) rowView.findViewById(R.id.textView1);
			holder.txtPotenciaCV = (TextView) rowView.findViewById(R.id.textView2);

			rowView.setTag(holder);
		} else {
			holder = (ViewHolder) rowView.getTag();
		}

		PotenciaObject object = potenciaObjects.get(position); 

		holder.txtPotenciaKW.setText(object.getPotenciaKW());
		holder.txtPotenciaCV.setText(object.getPotenciaCV());

		return rowView;
	}
} 