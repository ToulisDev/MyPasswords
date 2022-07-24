package com.familyprotection.djasdatabase.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.familyprotection.djasdatabase.R;

import java.util.List;

public class PasswordAdapter extends ArrayAdapter<PasswordResponse> {

    public PasswordAdapter(Context context, List<PasswordResponse> passwords){
        super(context, R.layout.list_item, passwords);
    }

    private static class ViewHolder {
        TextView tvSite;
        TextView tvUsername;
        TextView tvPassword;
        TextView tvPasswordId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        PasswordResponse password = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.tvSite = convertView.findViewById(R.id.tv_site);
            viewHolder.tvUsername = convertView.findViewById(R.id.tv_username);
            viewHolder.tvPassword = convertView.findViewById(R.id.tv_pass);
            viewHolder.tvPasswordId = convertView.findViewById(R.id.tv_passId);

            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.tvSite.setText(password.getPasswordsSite());
        viewHolder.tvUsername.setText(password.getPasswordsSite());
        viewHolder.tvPassword.setText(password.getPasswordsPassword());
        viewHolder.tvPasswordId.setText(password.getPasswordsId());
        // Return the completed view to render on screen
        return convertView;
    }
}
