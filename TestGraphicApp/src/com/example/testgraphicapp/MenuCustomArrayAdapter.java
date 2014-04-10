package com.example.testgraphicapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuCustomArrayAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;

	public MenuCustomArrayAdapter(Context context, String[] values) {
		super(context, R.layout.list_item, values);
		this.context = context;
		this.values = values;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.list_item, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.itemTextView);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.listImage);
		textView.setText(values[position]);
		// Change icon based on name
		String s = values[position];

		if(s.equals("Мои желания")) {
			imageView.setImageResource(R.drawable.mydesires);
		} else if (s.equals("В округе")) {
			imageView.setImageResource(R.drawable.chat);
		} else if (s.equals("Сообщения")){
			imageView.setImageResource(R.drawable.mail);
		} else imageView.setImageResource(R.drawable.exit);
		
		return rowView;

	}
}