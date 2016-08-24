package com.swatboss93.wsclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.swatboss93.wsclient.R;
import com.swatboss93.wsclient.objects.User;

import java.util.List;

/**
 * Created by swatboss93 on 24/08/16.
 */
public class UserAdapter extends BaseAdapter {
    private Context context;
    private List<User> user;

    public UserAdapter(Context context, List<User> user) {
        this.context = context;
        this.user = user;
    }

    public int getCount() {
        return user.size();
    }

    public Object getItem(int position) {
        return user.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int posicao, View convertView, ViewGroup parent) {
        User user = this.user.get(posicao);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_lista, null);
        TextView textNome = (TextView) view.findViewById(R.id.name);
        textNome.setText("Name: " + user.getName());
        TextView textGrupo = (TextView) view.findViewById(R.id.email);
        textGrupo.setText("Email: " + user.getEmail());
        return view;
    }
}