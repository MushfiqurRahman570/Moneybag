package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.historyViewHolder> {

    Context context;
    ArrayList<userModel>list;

    public HistoryAdapter(Context context, ArrayList<userModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public historyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent, false);
        return new historyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull historyViewHolder holder, int position) {

        userModel userModel = list.get(position);
        holder.amount.setText(userModel.getAddMoney());
        holder.amount.setText(userModel.getSendMoney());
        holder.transaction.setText(userModel.getuId());
        holder.type.setText(userModel.getType());
        holder.dT.setText(userModel.getTimeStamp());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class historyViewHolder extends RecyclerView.ViewHolder{

        TextView amount,transaction,type,dT;

        public historyViewHolder(@NonNull View itemView) {
            super(itemView);

            amount = itemView.findViewById(R.id.Tamount);
            transaction = itemView.findViewById(R.id.TnxId);
            type = itemView.findViewById(R.id.Ttype);
            dT = itemView.findViewById(R.id.dT);
        }
    }
}
