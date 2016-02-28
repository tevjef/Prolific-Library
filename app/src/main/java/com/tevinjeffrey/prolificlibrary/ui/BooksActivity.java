package com.tevinjeffrey.prolificlibrary.ui;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tevinjeffrey.prolificlibrary.R;
import com.tevinjeffrey.prolificlibrary.dagger.UiComponent;
import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.base.BaseActivity;
import com.tevinjeffrey.prolificlibrary.ui.util.ItemClickListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BooksActivity extends BaseActivity implements BooksView, ItemClickListener<Book,View> {

    @Inject
    BooksPresenter booksPresenter;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.booksList) RecyclerView booksList;
    @Bind(R.id.swipeRefreshLayout) SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.empty_view) LinearLayout emptyView;
    @Bind(R.id.fab) FloatingActionButton fab;

    List<Book> dataSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        booksList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        booksList.setHasFixedSize(true);

        if (dataSet == null) {
            dataSet = new ArrayList<>(100);
        }

        if (booksList.getAdapter() == null) {
            booksList.setAdapter(new BookAdapter(dataSet, this));
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                booksPresenter.loadData(true);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showError(Throwable e) {
        Snackbar.make(findViewById(android.R.id.content), e.toString(), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setData(List<Book> books) {
        dataSet.addAll(books);
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

    @Override
    public void onItemClicked(Book data, View view) {
        Toast.makeText(this, data.toString(), Toast.LENGTH_SHORT).show();
        BottomSheetDialogFragment bottomSheetDialogFragment = new Bottom();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Book", data);
        bottomSheetDialogFragment.setArguments(bundle);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }
}
