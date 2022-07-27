package com.familyprotection.djasdatabase.Models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.familyprotection.djasdatabase.R;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.List;

public class PasswordAdapter extends ArrayAdapter<PasswordResponse> {

    public PasswordAdapter(Context context, List<PasswordResponse> passwords){
        super(context, R.layout.list_item, passwords);
    }

    private static class ViewHolder {
        ImageView img_pass;
        TextView tvSite;
        TextView tvUsername;
        TextView tvPassword;
        TextView tvPasswordId;
        TextView tvWebsite;
        TextView tvInsertDate;
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
            viewHolder.img_pass = convertView.findViewById(R.id.img_pass);
            viewHolder.tvSite = convertView.findViewById(R.id.tv_site);
            viewHolder.tvUsername = convertView.findViewById(R.id.tv_username);
            viewHolder.tvPassword = convertView.findViewById(R.id.tv_pass);
            viewHolder.tvPasswordId = convertView.findViewById(R.id.tv_passId);
            viewHolder.tvWebsite = convertView.findViewById(R.id.tv_website);
            viewHolder.tvInsertDate = convertView.findViewById(R.id.tv_insertDate);

            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        String logoName = "___" + password.getPasswordsSite();
        File file = getAppSpecificFile(getContext(), logoName);
        if (file != null) {
            if(file.exists()) {
                String path = file.getPath();
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                viewHolder.img_pass.setImageBitmap(bitmap);
            }else
                viewHolder.img_pass.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.default_logo));
        }else{
            viewHolder.img_pass.setBackground(AppCompatResources.getDrawable(getContext(), R.drawable.default_logo));
        }
        viewHolder.tvSite.setText(password.getPasswordsSite());
        viewHolder.tvUsername.setText(password.getPasswordsSite());
        viewHolder.tvPassword.setText(password.getPasswordsPassword());
        viewHolder.tvPasswordId.setText(password.getPasswordsId());
        viewHolder.tvWebsite.setText(password.getPasswordsWebsite());
        viewHolder.tvInsertDate.setText(password.getPasswordsInsertDate());
        // Return the completed view to render on screen
        return convertView;
    }

    @Nullable
    File getAppSpecificFile(Context context, String filename) {
        return new File(context.getFilesDir(), filename);
    }
}
