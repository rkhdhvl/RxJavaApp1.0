package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Entry;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private final List<Entry> entries = new ArrayList<>();

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // The recyclerview is already attached to the parent hence pass the third parameter as false
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_layout,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, int position) {
      Entry entry = entries.get(position);
      holder.setTxtName(entries.get(position).getEntryName());
      holder.setTxtPrice(entries.get(position).getEntryPrice());
      holder.setTxtDate(entries.get(position).getEntryDate());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public void addEntry(Entry value)
    {
        entries.add(value);
        // since we are inserting the item at the last location, pass the index of the last
        notifyItemInserted(entries.size()-1);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_txt_name)
        TextView txtName;
        @BindView(R.id.item_txt_price)
        TextView txtPrice;
        @BindView(R.id.item_txt_date)
        TextView txtDate;

        private final NumberFormat ENTRY_PRICE_FORMAT = new DecimalFormat("$#0.00");

        public void setTxtName(String name) {
            this.txtName.setText(name);
        }

        public void setTxtPrice(BigDecimal price) {
            // Using double since it has more precise value & its good for showing the price
            this.txtPrice.setText(ENTRY_PRICE_FORMAT.format(price.doubleValue()));
        }

        public void setTxtDate(Date date) {
            this.txtDate.setText(android.text.format.DateFormat.format("yyyy-MM-dd hh:mm",date));
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }



}
