package com.reservatiesysteem.lotte.reservatiesysteem.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.reservatiesysteem.lotte.reservatiesysteem.R;
import com.reservatiesysteem.lotte.reservatiesysteem.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lotte on 2/03/2017.
 */

public class MessageAdapter extends BaseAdapter {
    private List<Message> messages = new ArrayList<>();
    private Context context;

    public MessageAdapter(Context context, int resource, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }
    public void addMessage(Message message){
        messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return messages.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Message message = getItem(position);

        final View v;

        if(convertView == null){
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.view_message_entry, parent, false);
        }
        else {
            v = convertView;
        }

        TextView txtMessageText = (TextView) v.findViewById(R.id.messageText);
        TextView txtMessageUser = (TextView) v.findViewById(R.id.messageUser);

        String[] dateTime = message.getDateTime().split("T");
        final String resDate = dateTime[0];
        final String resTime = dateTime[1];
        String time = resTime.substring(0,5);

        txtMessageText.setText(message.getText());
        String messageUser= message.getUser() == null?"Gepost door jou":("Gepost door"+message.getUser().getFirstname());
        txtMessageUser.setText(messageUser + " op " + resDate + " " + time);
        //txtMessageUser.setText("Gepost door " + message.getUser().getFirstname() + " " + message.getUser().getLastname() + " op " + resDate + " " + time);

        return v;
    }
}
