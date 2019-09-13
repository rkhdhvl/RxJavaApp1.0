package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.Quotes;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.QuotesViewHolder>  {

    private final List<Quotes> quotesList;
    private Context context;

    public QuotesAdapter(Context context,List<Quotes> quotesList)
    {
       this.context = context;
       this.quotesList = quotesList;
    }

    @NonNull
    @Override
    public QuotesAdapter.QuotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item_layout,parent,false);
        QuotesViewHolder viewHolder = new QuotesViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QuotesAdapter.QuotesViewHolder holder, int position) {
      Quotes quotes = quotesList.get(position);
      holder.setAuthor(quotes.getAuthor());
      holder.setQuote(quotes.getEn());
      holder.setRating(quotes.getRating());
    }

    @Override
    public int getItemCount() {
        return quotesList.size();
    }


    public class QuotesViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_txt_name)
        TextView quote;
        @BindView(R.id.item_txt_price)
        TextView author;
        @BindView(R.id.item_txt_date)
        TextView rating;

        private final NumberFormat RATING_FORMAT = new DecimalFormat("#0.00");


        public void setQuote(String full_quote) {
            this.quote.setText(full_quote);
        }

        public void setAuthor(String quote_author) {
            this.author.setText(quote_author);
        }

        public void setRating(Double quote_rating) {
            this.rating.setText(RATING_FORMAT.format(quote_rating.doubleValue()));
        }


        public QuotesViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
