package com.example.contentprovider;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ContactsAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> contacts;

    public ContactsAdapter(Context context, List<String> contacts) {
        super(context, R.layout.list_item, contacts);
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView textView = rowView.findViewById(R.id.contact_name);

        textView.setText(contacts.get(position));

        // Alternate background color
        if (position % 2 == 0) {
            rowView.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
        } else {
            rowView.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }

        return rowView;
    }
}