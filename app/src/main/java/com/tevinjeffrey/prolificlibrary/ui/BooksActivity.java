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

    @Inject
    BooksPresenter booksPresenter;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.booksList) RecyclerView booksList;
    @Bind(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.empty_view) LinearLayout emptyView;
    @Bind(R.id.fab) FloatingActionButton fab;

    List<Book> dataSet = new ArrayList<>();
    Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle(getString(R.string.app_name));
        toolbar.setNavigationIcon(R.drawable.ic_prolificp);

        booksList.setLayoutManager(new LinearLayoutManager(this));
        booksList.setHasFixedSize(true);

        if (dataSet == null) {
            dataSet = new ArrayList<>();
        }

        if (booksList.getAdapter() == null) {
            booksList.setAdapter(new BooksAdapter(dataSet, this));
        }

        showLayout(LayoutType.EMPTY);

        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                booksPresenter.loadData(true);
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
        booksPresenter.loadData(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_books, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_all:
                booksPresenter.deleteAll();
                break;
        }
        return super.onOptionsItemSelected(item);
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
        if (booksList.getVisibility() != visibility)
            booksList.setVisibility(visibility);
    }


    @Override
    public void setData(List<Book> books) {
        if (snackbar != null  && snackbar.isShownOrQueued()) {
            snackbar.dismiss();
        }
        dataSet.clear();
        dataSet.addAll(books);
        booksList.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void addBook(Book book) {
        dataSet.add(book);
        booksList.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void updateBook(Book updatedBook) {
        int indexOf = dataSet.indexOf(updatedBook);
        dataSet.set(indexOf, updatedBook);
        booksList.getAdapter().notifyItemChanged(indexOf);
    }

    @Override
    public void deleteBook(int id) {
        int indexOf = dataSet.indexOf(new Book.Builder().id(id).build());
        dataSet.remove(indexOf);
        booksList.getAdapter().notifyItemRemoved(indexOf);
    }

    @Override
    public void deleteAllBooks() {
        dataSet.clear();
        booksList.getAdapter().notifyDataSetChanged();
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
