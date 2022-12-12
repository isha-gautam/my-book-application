package com.example.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    private final ArrayList<Book> books;
    private final OnItemClickListener listener;
    private static final String TAG = "BookRecyclerViewAdapter";

    public BookRecyclerViewAdapter(ArrayList<Book> books, OnItemClickListener listener) {
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); //since in this case adapter is in separate class

        View itemView = li.inflate(R.layout.list_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Book book = books.get(position);
        holder.tvName.setText(book.get_title());
        holder.tvAuthor.setText(book.get_author());

        holder.bind(books.get(position), listener);
    }

    public void updateBooksList(ArrayList<Book> newList) {
        Log.d(TAG, "updateBooksList: books: " + books);
        Log.d(TAG, "updateBooksList: newList: " + books);
        books.clear();
        Log.d(TAG, "updateBooksList: books: " + books);
        books.addAll(newList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.bookTitle);
            tvAuthor = itemView.findViewById(R.id.bookAuthor);

        }

        private void bind(final Book item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

}

