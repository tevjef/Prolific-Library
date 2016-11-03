package com.tevinjeffrey.prolificlibrary.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.tevinjeffrey.prolificlibrary.R;
import com.tevinjeffrey.prolificlibrary.dagger.UiComponent;
import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.base.BaseActivity;
import com.tevinjeffrey.prolificlibrary.ui.util.ItemClickListener;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BooksActivity extends BaseActivity implements BooksView, ItemClickListener<Book,View> {

    @Inject BooksPresenter booksPresenter;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.booksList) RecyclerView recyclerView;
    @Bind(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.empty_view) LinearLayout emptyView;
    @Bind(R.id.fab) FloatingActionButton fab;
    private List<Book> bookDataSet = new ArrayList<>();
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        ButterKnife.bind(this);

        // Initialize toolbar
        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setNavigationIcon(R.drawable.ic_prolificp);

        if (bookDataSet == null) {
            bookDataSet = new ArrayList<>();
        }

        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(new BooksAdapter(bookDataSet, this));
        }

        showLayout(LayoutType.EMPTY);

        // Initialize data
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                booksPresenter.loadData();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(BooksActivity.this, NewBookActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        booksPresenter.loadData();
    }

    @Override
    public void showError(Throwable e) {
        if (e instanceof UnknownHostException) {
            snackbar = Snackbar.make(fab, "Please check internet connection", Snackbar.LENGTH_INDEFINITE);
        } else {
            snackbar = Snackbar.make(fab, "Could not complete request", Snackbar.LENGTH_INDEFINITE);
        }
        snackbar.show();
    }

    public void showLayout(LayoutType type) {
        switch (type) {
            case EMPTY:
                showRecyclerView(GONE);
                showEmptyLayout(VISIBLE);
                break;
            case NORMAL:
                showEmptyLayout(GONE);
                showRecyclerView(VISIBLE);
                break;
            default:
                throw new RuntimeException("Unknown type: " + type);
        }
    }

    private void showEmptyLayout(int visibility) {
        if (emptyView.getVisibility() != visibility)
            emptyView.setVisibility(visibility);
    }

    private void showRecyclerView(int visibility) {
        if (recyclerView.getVisibility() != visibility)
            recyclerView.setVisibility(visibility);
    }


    @Override
    public void setData(List<Book> books) {
        if (snackbar != null  && snackbar.isShownOrQueued()) {
            snackbar.dismiss();
        }
        bookDataSet.clear();
        bookDataSet.addAll(books);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void addBook(Book book) {
        bookDataSet.add(book);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void updateBook(Book updatedBook) {
        int indexOf = bookDataSet.indexOf(updatedBook);
        bookDataSet.set(indexOf, updatedBook);
        recyclerView.getAdapter().notifyItemChanged(indexOf);
    }

    @Override
    public void deleteBook(int id) {
        int indexOf = bookDataSet.indexOf(new Book.Builder().id(id).build());
        bookDataSet.remove(indexOf);
        recyclerView.getAdapter().notifyItemRemoved(indexOf);
    }

    @Override
    public void deleteAllBooks() {
        bookDataSet.clear();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showLoading(final boolean isLoading) {
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(isLoading);
                }
            }
        });
    }

    @Override
    public void onItemClicked(Book data, View view) {
        BottomSheetDialogFragment bottomSheetDialogFragment = new SingleBookFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SingleBookFragment.SELECTED_BOOK, data);
        bottomSheetDialogFragment.setArguments(bundle);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_books, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_all:
                booksPresenter.deleteAll();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void injectTargets() {
        UiComponent.Initializer.init(this).inject(this);
    }

    @Override
    protected void setupPresenter() {
        booksPresenter.attachView(this);
    }

    @Override
    protected void destroyPresenter() {
        booksPresenter.detachView();
    }
}
