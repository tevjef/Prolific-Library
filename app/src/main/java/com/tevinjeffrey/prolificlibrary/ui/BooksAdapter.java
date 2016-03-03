package com.tevinjeffrey.prolificlibrary.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tevinjeffrey.prolificlibrary.R;
import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.util.ItemClickListener;

import java.util.List;

import butterknife.ButterKnife;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BooksViewHolder>{

    private final List<Book> dataSet;
    private final ItemClickListener<Book, View> itemClickListener;

    public BooksAdapter(List<Book> dataSet, @NonNull ItemClickListener<Book, View> listener) {
        this.dataSet = dataSet;
        this.itemClickListener = listener;
    }

    @Override
    public BooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View listItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_item, parent, false);
        final BooksViewHolder booksViewHolder = new BooksViewHolder(listItem);
        booksViewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPos = booksViewHolder.getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    itemClickListener.onItemClicked(dataSet.get(adapterPos), v);
                }
            }
        });
        return booksViewHolder;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public void onBindViewHolder(BooksViewHolder holder, int position) {
        holder.bindView(dataSet.get(position));
    }

    public static class BooksViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView author;
        View parent;
        public BooksViewHolder(View itemView) {
            super(itemView);
            parent = itemView;
            title = ButterKnife.findById(itemView, R.id.book_title);
            author = ButterKnife.findById(itemView, R.id.book_author);
        }

        public void setTitle(String title) {
            this.title.setText(title);
        }
        public void setAuthor(String author) {
            this.author.setText(author);
        }
        public void bindView(Book book) {
            setTitle(book.getTitle());
            setAuthor(book.getAuthor());
        }

        public void setOnClickListener(View.OnClickListener listener) {
            parent.setOnClickListener(listener);
        }
    }
}
