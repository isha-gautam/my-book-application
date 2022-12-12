package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.comparators.SortbyAuthor;
import com.example.myapplication.comparators.SortbyId;
import com.example.myapplication.comparators.SortbyTitle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class ViewAllBooks extends AppCompatActivity {

    private static final String TAG = "ViewAllBooks";
    private RecyclerView rv;
    private ArrayList<Book> books;
    private ImageButton searchExpandBtn;
    private ImageButton sortExpandBtn;
    private ImageButton filterExpandBtn;
    private LinearLayout expandableSearchView;
    private LinearLayout expandableFilterView;
    private ToggleButtonGroupTableLayout expandableSortView;
    private CardView searchCardView;
    private DatabaseHandler db;
    private BookRecyclerViewAdapter adapter;
    private TextView resultSizeTV;
    private CardView searchToolbarCV;
    private HashMap<String, Integer> filters;
    private HashMap<String, Integer> sorting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_all_books);

        rv = findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHandler(this);
        books = db.getAllBooks();
        if (!books.isEmpty()) {
            adapter = new BookRecyclerViewAdapter(books, new BookRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Book book) {
                    Intent intent = new Intent(getApplicationContext(), BookView.class);
                    intent.putExtra("bookClicked", book);
                    startActivity(intent);
                }
            });

            rv.setAdapter(adapter);

            resultSizeTV = findViewById(R.id.resultSizeTV);
            searchToolbarCV = findViewById(R.id.searchToolbarCV);

            searchCardView = findViewById(R.id.searchCV);
            searchExpandBtn = findViewById(R.id.searchExpandBtn);
            expandableSearchView = findViewById(R.id.expandableSearch);

            filterExpandBtn = findViewById(R.id.filterExpandBtn);
            expandableFilterView = findViewById(R.id.expandableFilter);

            sortExpandBtn = findViewById(R.id.sortExpandBtn);
            expandableSortView = findViewById(R.id.expandableSort);

            filters = new HashMap<>();

            RadioGroup filterRead = findViewById(R.id.FILTER_READ);
            filterRead.check(R.id.filterReadBoth);
            ToggleButtonGroupTableLayout sortBooksRG = findViewById(R.id.expandableSort);

            sortBooksRG.check(R.id.sortIdASC);
            sortBooksRG.setOnCheckedChangeListener(new ToggleButtonGroupTableLayout.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(ToggleButtonGroupTableLayout group, int checkedId) {
                    sortBooks();
                }
            });

        } else {
            Toast.makeText(getApplicationContext(), "No books added yet!", Toast.LENGTH_SHORT).show();
            finish();
        }
        db.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: called");
//        RadioGroup filterRead = findViewById(R.id.FILTER_READ);

        searchBook();
        sortBooks();
//        searchBook(findViewById(R.id.searchBtn));
    }

    public void expandSearchCard(View view) {
        if (expandableSearchView.getVisibility() == View.VISIBLE) {

            // The transition of the hiddenView is carried out
            //  by the TransitionManager class.
            // Here we use an object of the AutoTransition
            // Class to create a default transition.
//            TransitionManager.beginDelayedTransition(searchCardView,
//                    new AutoTransition().setDuration(300));
//
//            TransitionManager.beginDelayedTransition(rv,
//                    new AutoTransition().setDuration(1100));
//
//            TransitionManager.beginDelayedTransition(searchToolbarCV,
//                    new AutoTransition().setDuration(1100));

            expandableSearchView.setVisibility(View.GONE);
            searchExpandBtn.setImageResource(R.drawable.expand_more);
        }

        // If the CardView is not expanded, set its visibility
        // to visible and change the expand more icon to expand less.
        else {

//            TransitionManager.beginDelayedTransition(searchCardView,
//                    new AutoTransition().setDuration(300));
//
//            TransitionManager.beginDelayedTransition(rv,
//                    new AutoTransition().setDuration(700));
//
//            TransitionManager.beginDelayedTransition(searchToolbarCV,
//                    new AutoTransition().setDuration(700));

            expandableSearchView.setVisibility(View.VISIBLE);
            searchExpandBtn.setImageResource(R.drawable.expand_less);
        }
    }

    public void expandSortCard(View view) {
        expandableFilterView.setVisibility(View.GONE);
        if (expandableSortView.getVisibility() == View.VISIBLE)
            expandableSortView.setVisibility(View.GONE);
        else
            expandableSortView.setVisibility(View.VISIBLE);
    }

    public void expandFilterCard(View view) {
        expandableSortView.setVisibility(View.GONE);
        if (expandableFilterView.getVisibility() == View.VISIBLE)
            expandableFilterView.setVisibility(View.GONE);
        else
            expandableFilterView.setVisibility(View.VISIBLE);
    }

    public void searchBook(View view) {
        Log.d(TAG, "searchBook: HERE");
        searchBook();
//        String title = ((EditText) (findViewById(R.id.queryTitle))).getText().toString();
//        String author = ((EditText) (findViewById(R.id.queryAuthor))).getText().toString();
//
//        ArrayList<Book> searchResults = db.getBooks(title, author);
//        books = searchResults;
//        sortBooks();
//        adapter.updateBooksList(searchResults);
//        if (books.isEmpty())
//            Toast.makeText(getApplicationContext(), "No such book!", Toast.LENGTH_SHORT).show();
//
//        resultSizeTV.setText("Results: " + adapter.getItemCount());
    }

    public void filterRead(View view) {
        RadioGroup filterRead = findViewById(R.id.FILTER_READ);
        int checkedID = filterRead.getCheckedRadioButtonId();
        int read;
        switch (checkedID) {
            case R.id.filterBookRead:
                read = 1;
                break;
            case R.id.filterBookUnread:
                read = 0;
                break;
            default:
                read = -1;
                break;
        }

        filters.put(Constants.KEY_READ, read);
        searchBook();
    }

    public void sortBooks() {
        Log.d(TAG, "sortBooks: HERE");
        ToggleButtonGroupTableLayout sortBooksRG = findViewById(R.id.expandableSort);
        int checkedID = sortBooksRG.getCheckedRadioButtonId();
//        int checkedID = view.getId();
        Log.d(TAG, "sortBooks: Checked id: " + checkedID);
        switch (checkedID) {
            case R.id.sortTitleASC:
                Log.d(TAG, "sortBooks: Case1");
                Collections.sort(books, new SortbyTitle());
                break;
            case R.id.sortTitleDESC:
                Log.d(TAG, "sortBooks: Case2");
                Collections.sort(books, new SortbyTitle());
                Collections.reverse(books);
                break;
            case R.id.sortAuthorASC:
                Log.d(TAG, "sortBooks: Case3");
                Collections.sort(books, new SortbyAuthor());
                break;
            case R.id.sortAuthorDESC:
                Log.d(TAG, "sortBooks: Case4");
                Collections.sort(books, new SortbyAuthor());
                Collections.reverse(books);
                break;
            case R.id.sortIdASC:
                Log.d(TAG, "sortBooks: Case5");
                Collections.sort(books, new SortbyId());
                break;
            case R.id.sortIdDESC:
                Log.d(TAG, "sortBooks: Case6");
                Collections.sort(books, new SortbyId());
                Collections.reverse(books);
                break;
            default:
                Log.d(TAG, "sortBooks: Case7");
                break;
        }

        adapter.updateBooksList(books);
//
//        filters.put(Constants.KEY_READ, read);
//        searchBook(filters);
    }

    private String print(ArrayList<Book> books) {
        StringBuilder sb = new StringBuilder();
        for (Book book : books) {
            sb.append(book.get_title() + " ");
        }
        return sb.toString();
    }

    public void searchBook() {
        Log.d(TAG, "searchBook: HERE");
        String title = ((EditText) (findViewById(R.id.queryTitle))).getText().toString();
        String author = ((EditText) (findViewById(R.id.queryAuthor))).getText().toString();

        ArrayList<Book> searchResults = db.getBooks(title, author, filters);
        books = searchResults;
        sortBooks();
        adapter.updateBooksList(searchResults);
        if (books.isEmpty())
            Toast.makeText(getApplicationContext(), "No such book!", Toast.LENGTH_SHORT).show();

        resultSizeTV.setText("Results: " + adapter.getItemCount());
    }

}