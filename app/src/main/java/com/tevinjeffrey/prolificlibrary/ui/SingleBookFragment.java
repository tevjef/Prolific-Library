package com.tevinjeffrey.prolificlibrary.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.tevinjeffrey.prolificlibrary.R;
import com.tevinjeffrey.prolificlibrary.dagger.UiComponent;
import com.tevinjeffrey.prolificlibrary.data.model.Book;
import com.tevinjeffrey.prolificlibrary.ui.util.Utils;

import java.net.UnknownHostException;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SingleBookFragment extends BottomSheetDialogFragment implements SingleBookView {
    public final static String SELECTED_BOOK = "book";

    BottomSheetBehavior behavior;
    View contentView;
    @Bind(R.id.book_name)
    TextView bookName;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @Bind(R.id.author_text)
    TextView authorText;
    @Bind(R.id.author_view)
    FrameLayout authorView;
    @Bind(R.id.publisher_text)
    TextView publisherText;
    @Bind(R.id.publisher_view)
    FrameLayout publisherView;
    @Bind(R.id.last_checkout_text)
    TextView lastCheckoutText;
    @Bind(R.id.last_checkout_date)
    TextView lastCheckoutDate;
    @Bind(R.id.last_checkout_view)
    FrameLayout lastCheckoutView;
    @Bind(R.id.categories_text)
    TextView categoriesText;
    @Bind(R.id.categories_view)
    FrameLayout categoriesView;
    @Bind(R.id.fab_checkout)
    FloatingActionButton fabCheckout;

    @Inject
    SingleBookPresenter singleBookPresenter;
    private Book book;
    DialogInterface.OnCancelListener cancelListener = new DialogInterface.OnCancelListener() {
        @Override
        public void onCancel(DialogInterface dialog) {
            dismissKeyboard();
        }
    };
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };
    private DialogInterface.OnDismissListener dismissListener = new DialogInterface.OnDismissListener() {
        @Override
        public void onDismiss(DialogInterface dialog) {
            dismissKeyboard();
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        UiComponent.Initializer.init(getActivity()).inject(this);
        singleBookPresenter.attachView(this);
        contentView = View.inflate(getContext(), R.layout.bottom_sheet_content_view, null);
        ButterKnife.bind(this, contentView);

        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        behavior = (BottomSheetBehavior) layoutParams.getBehavior();
        behavior.setBottomSheetCallback(mBottomSheetBehaviorCallback);
        behavior.setPeekHeight(Utils.getWindowHeight(getActivity()));
        behavior.setHideable(true);

        book = getArguments().getParcelable(SELECTED_BOOK);
        if (book == null) {
            dismiss();
            return;
        }
        //http://stackoverflow.com/a/32724422/2238427
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(book.getTitle());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle("");
                    isShow = false;
                }
            }
        });
        collapsingToolbar.setExpandedTitleColor(Color.TRANSPARENT);
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);

        toolbar.inflateMenu(R.menu.menu_single_book);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_delete:
                        new MaterialDialog.Builder(getActivity())
                                .title(R.string.are_you_sure)
                                .positiveText(R.string.agree)
                                .negativeText(R.string.cancel)
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        singleBookPresenter.delete(book.getId());
                                    }
                                })
                                .show();

                        break;
                    case R.id.action_share:
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, book.getTitle() + " by " + book.getAuthor());
                        sendIntent.setType("text/plain");
                        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                        break;
                }
                return false;
            }
        });
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setBookDetails(book, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        singleBookPresenter.detachView();
        ButterKnife.unbind(this);
    }

    private void setBookDetails(Book book, boolean eagerly) {
        setBookTitle(book, eagerly);
        setAuthor(book, eagerly);
        setPublisher(book, eagerly);
        setLastCheckout(book, eagerly);
        setCategories(book, eagerly);
        setLastCheckOutDate(book);
    }

    private void setCategories(Book book, boolean eagerly) {
        eagerlySetValue(eagerly, lastCheckoutText);
        categoriesText.setText(book.getCategories() == null ? "No category" : book.getCategories());
    }

    private void setLastCheckOutDate(Book book) {
        if (book.getLastCheckedOutString() == null) {
            lastCheckoutDate.setVisibility(View.GONE);
        } else {
            lastCheckoutDate.setVisibility(View.VISIBLE);
            lastCheckoutDate.setText("Checked out: " + book.getLastCheckedOutString());
        }
    }

    private void setLastCheckout(Book book, boolean eagerly) {
        eagerlySetValue(eagerly, lastCheckoutText);
        lastCheckoutText.setText(book.getLastCheckedOutBy() == null ? "Not yet checked out" : book.getLastCheckedOutBy());
    }

    private void setPublisher(Book book, boolean eagerly) {
        eagerlySetValue(eagerly, publisherText);
        publisherText.setText(book.getPublisher() == null ? "Unknown" : book.getPublisher());
    }

    private void setAuthor(Book book, boolean eagerly) {
        eagerlySetValue(eagerly, authorText);
        authorText.setText(book.getAuthor() == null ? "Unknown" : book.getAuthor());
    }

    private void setBookTitle(Book book, boolean eagerly) {
        eagerlySetValue(eagerly, bookName);
        bookName.setText(book.getTitle() == null ? "Unknown" : book.getTitle());
    }

    private void eagerlySetValue(boolean eagerly, View viewToSet) {
        if (eagerly) {
            viewToSet.setAlpha(.5f);
        } else {
            viewToSet.setAlpha(1f);
        }
    }

    @Override
    public void showError(Throwable e) {
        if (e instanceof UnknownHostException) {
            Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Could not complete request", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setData(Book updatedBook) {
        book = updatedBook;
    }

    @Override
    public void showLoading(boolean isLoading) { /* No loading indicator implemented */ }

    @Override
    public void checkoutSuccess() {
        setBookDetails(book, false);
    }

    @Override
    public void checkoutFail() {
        setBookDetails(book, false);
    }

    @Override
    public void deleteSuccess() {
        dismiss();
    }

    @Override
    public void deleteFail() {
        dismiss();
    }

    @Override
    public void updateFail() {
        setBookDetails(book, false);
    }

    @Override
    public void updateSuccess() {
        setBookDetails(book, false);
    }

    private void dismissKeyboard() {
        if (contentView != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(contentView.getWindowToken(), 0);
        }
    }

    @OnClick({R.id.author_view, R.id.publisher_view, R.id.last_checkout_view, R.id.categories_view, R.id.book_name, R.id.fab_checkout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.author_view:
                TextView textView = (TextView) view.findViewById(R.id.author_text);
                String content = textView.getText() == null? "": textView.getText().toString();
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.edit_author)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .dismissListener(dismissListener)
                        .cancelListener(cancelListener)
                        .input(getResources().getString(R.string.edit_author_hint), content, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                dismissKeyboard();
                                if (TextUtils.isEmpty(input)) return;
                                Book updatedBook = new Book.Builder().author(input.toString()).build();
                                singleBookPresenter.update(book.getId(), updatedBook);
                                setAuthor(updatedBook, true);
                            }
                        }).show();
                break;
            case R.id.publisher_view:
                textView = (TextView) view.findViewById(R.id.publisher_text);
                content = textView.getText() == null ? "" : textView.getText().toString();
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.edit_publisher)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .dismissListener(dismissListener)
                        .cancelListener(cancelListener)
                        .input(getResources().getString(R.string.edit_publisher_hint), content, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                dismissKeyboard();
                                if (TextUtils.isEmpty(input)) return;
                                Book updatedBook = new Book.Builder().publisher(input.toString()).build();
                                singleBookPresenter.update(book.getId(), updatedBook);
                                setPublisher(updatedBook, true);
                            }
                        }).show();
                break;
            case R.id.last_checkout_view:
                break;
            case R.id.categories_view:
                textView = (TextView) view.findViewById(R.id.categories_text);
                content = textView.getText() == null ? "" : textView.getText().toString();
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.edit_categories)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .dismissListener(dismissListener)
                        .cancelListener(cancelListener)
                        .input(getResources().getString(R.string.edit_categories_hint), content, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                dismissKeyboard();
                                if (TextUtils.isEmpty(input)) return;
                                Book updatedBook = new Book.Builder().categories(input.toString()).build();
                                singleBookPresenter.update(book.getId(), updatedBook);
                                setCategories(book, true);
                            }
                        }).show();
                break;
            case R.id.book_name:
                textView = (TextView) view.findViewById(R.id.book_name);
                content = textView.getText() == null ? "" : textView.getText().toString();
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.edit_book_title)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .dismissListener(dismissListener)
                        .cancelListener(cancelListener)
                        .input(getResources().getString(R.string.edit_book_title_hint), content, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                dismissKeyboard();
                                if (TextUtils.isEmpty(input)) return;
                                Book updatedBook = new Book.Builder().title(input.toString()).build();
                                singleBookPresenter.update(book.getId(), updatedBook);
                                setBookTitle(updatedBook, true);
                            }
                        }).show();
                break;
            case R.id.fab_checkout:
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.enter_name)
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .dismissListener(dismissListener)
                        .cancelListener(cancelListener)
                        .input(getResources().getString(R.string.edit_enter_name_hint), "", new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                dismissKeyboard();
                                if (TextUtils.isEmpty(input)) return;
                                Book updatedBook = new Book.Builder().lastCheckedOutBy(input.toString()).build();
                                singleBookPresenter.checkout(book.getId(), updatedBook);
                                setLastCheckout(updatedBook, true);
                            }
                        }).show();
                break;
        }
    }
}
