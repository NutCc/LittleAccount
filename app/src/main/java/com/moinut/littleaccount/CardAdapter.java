package com.moinut.littleaccount;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by c on 2016/3/4.
 */
public class CardAdapter extends BaseAdapter {
    private List<Card> mList;
    private Context mContext;
    public CardAdapter(List<Card> mList,Context context){
        this.mList = mList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Card card = mList.get(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
            holder.money = (TextView) convertView.findViewById(R.id.tv_money);
            holder.name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(card.getName());
        holder.money.setText(new DecimalFormat("￥" + "###,###,###,###,##0.00").format(card.getMoney()));
        holder.time.setText(card.getTime());
        return convertView;
    }


    class ViewHolder {
        TextView money;
        TextView name;
        TextView time;
    }
}
