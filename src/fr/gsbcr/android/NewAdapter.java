package fr.gsbcr.android;

import java.text.SimpleDateFormat;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NewAdapter extends ArrayAdapter<CompteRendu> {
	private final Activity context;
	List<CompteRendu> cr;

	public NewAdapter(Activity context,List<CompteRendu> cr) {
		super(context, R.layout.list_row,cr);
		this.context = context;
		this.cr = cr;
	}

	@SuppressLint({ "ViewHolder", "SimpleDateFormat" })
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.list_row, null, true);
		TextView nom = (TextView) rowView.findViewById(R.id.nom);
		TextView date = (TextView) rowView.findViewById(R.id.date);
		nom.setText(cr.get(position).getPraticien().getPraNom());
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String date1 = df.format(cr.get(position).getdDateVisite());
		date.setText(date1);
		return rowView;
	}
}