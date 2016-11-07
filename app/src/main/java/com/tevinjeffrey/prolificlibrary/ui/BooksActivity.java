package com.tevinjeffrey.prolificlibrary.ui;

import android.content.Intent;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.tevinjeffrey.prolificlibrary.LibraryApplication;
import com.tevinjeffrey.prolificlibrary.R;
import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.base.BaseActivity;
import com.tevinjeffrey.prolificlibrary.ui.util.ItemClickListener;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class BooksActivity extends BaseActivity implements BooksView, ItemClickListener<Book, View> {

    private static final String STATE_BOOK_LIST = "com.tevinjeffrey.prolificlibrary.ui.BooksActivity.bookDataSet";

    @Inject BooksPresenter booksPresenter;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.books_list) RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.empty_view) LinearLayout emptyView;
    @BindView(R.id.fab) FloatingActionButton fab;

    private ArrayList<Book> bookDataSet = new ArrayList<>();
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Restore state book list
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_BOOK_LIST)) {
            bookDataSet = savedInstanceState.getParcelableArrayList(STATE_BOOK_LIST);
        }

        if (bookDataSet == null) {
            bookDataSet = new ArrayList<>();
        }

        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(new BooksAdapter(bookDataSet, this));
        }

        showLayout(LayoutType.NORMAL);

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
            snackbar = Snackbar.make(fab, R.string.check_internet_connection, Snackbar.LENGTH_INDEFINITE);
        } else {
            snackbar = Snackbar.make(fab, R.string.failed_network_request, Snackbar.LENGTH_INDEFINITE);
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
        if (emptyView.getVisibility() != visibility) {
            emptyView.setVisibility(visibility);
        }
    }

    private void showRecyclerView(int visibility) {
        if (recyclerView.getVisibility() != visibility) {
            recyclerView.setVisibility(visibility);
        }
    }


    @Override
    public void setData(List<Book> books) {
        if (snackbar != null && snackbar.isShownOrQueued()) {
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
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(STATE_BOOK_LIST, bookDataSet);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void injectTargets() {
        unbinder = ButterKnife.bind(this);
        LibraryApplication.presentationComponent(this).inject(this);

    }

    @Override
    protected void setupToolbar() {
        // Initialize toolbar
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final AnimatedVectorDrawable prolificDrawable = (AnimatedVectorDrawable) ResourcesCompat.getDrawable(getResources(), R.drawable.prolificp_anim, null);
            toolbar.setNavigationIcon(prolificDrawable);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (!prolificDrawable.isRunning()) {
                            prolificDrawable.start();
                        }
                    }
                }
            });
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_prolificp);
        }

        toolbar.setTitle(getString(R.string.app_name));
    }

    @Override
    protected void setLayoutId() {
        layoutId = R.layout.activity_books;
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
