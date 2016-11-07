package com.tevinjeffrey.prolificlibrary.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tevinjeffrey.prolificlibrary.LibraryApplication;
import com.tevinjeffrey.prolificlibrary.R;
import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.base.BaseActivity;

import java.net.UnknownHostException;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewBookActivity extends BaseActivity implements NewBookView {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.book_name_edit) EditText bookNameEdit;
    @BindView(R.id.book_author_edit) EditText bookAuthorEdit;
    @BindView(R.id.book_publisher_edit) EditText bookPublisherEdit;
    @BindView(R.id.book_categories_edit) EditText bookCategoriesEdit;
    @BindView(R.id.fab) FloatingActionButton fab;

    @Inject NewBookPresenter newBookPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book.Builder newBook = new Book.Builder()
                        .author(bookAuthorEdit.getText().toString())
                        .categories(bookCategoriesEdit.getText().toString())
                        .publisher(bookPublisherEdit.getText().toString());

                if(TextUtils.isEmpty(bookNameEdit.getText().toString())) {
                    Toast.makeText(NewBookActivity.this, R.string.book_title_required, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    newBook.title(bookNameEdit.getText().toString());
                }
                newBookPresenter.addBook(newBook.build());

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    protected void setupPresenter() {
        newBookPresenter.attachView(this);
    }

    @Override
    protected void destroyPresenter() {
        newBookPresenter.detachView();
    }

    @Override
    protected void injectTargets() {
        unbinder = ButterKnife.bind(this);
        LibraryApplication.presentationComponent(this).inject(this);
    }

    @Override
    protected void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void setLayoutId() {
        layoutId = R.layout.activity_new_book;
    }

    @OnClick({R.id.book_name_edit, R.id.book_author_edit, R.id.book_publisher_edit, R.id.book_categories_edit, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.fab:
                break;
        }
    }

    @Override
    public void showError(Throwable e) {
        if (e instanceof UnknownHostException) {
            Toast.makeText(this, R.string.check_internet_connection, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.failed_network_request, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void addComplete() {
        finish();
    }

}
