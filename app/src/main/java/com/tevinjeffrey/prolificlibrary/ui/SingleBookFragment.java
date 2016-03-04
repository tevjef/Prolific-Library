package com.tevinjeffrey.prolificlibrary.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tevinjeffrey.prolificlibrary.R;
import com.tevinjeffrey.prolificlibrary.dagger.UiComponent;
import com.tevinjeffrey.prolificlibrary.data.model.Book;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    @Inject
    SingleBookPresenter singleBookPresenter;

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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        singleBookPresenter.detachView();
        ButterKnife.unbind(this);
    }

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
        behavior.setPeekHeight(getWindowHeight());
        behavior.setHideable(true);

        final Book book = getArguments().getParcelable(SELECTED_BOOK);
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

        toolbar.inflateMenu(R.menu.menu_book);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit:
                        Toast.makeText(getContext(), "Edit click", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_delete:
                        Toast.makeText(getContext(), "Delete click", Toast.LENGTH_SHORT).show();
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
        setBookDetails(book);
    }

    @NonNull
    private int getWindowHeight() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels - getStatusBarHeight();
    }

    private void setBookDetails(Book book) {
        setBookTitle(book);
        setAuthor(book);
        setPublisher(book);
        setLastCheckout(book);
        setLastCheckOutDate(book);
        setCategories(book);
    }

    private void setCategories(Book book) {
        categoriesText.setText(book.getCategories() == null? "No category": book.getCategories());
    }

    private void setLastCheckOutDate(Book book) {
        lastCheckoutDate.setText(book.getLastCheckedOut() == null? "":book.getLastCheckedOut());
    }

    private void setLastCheckout(Book book) {
        lastCheckoutText.setText(book.getLastCheckedOutBy() == null? "Not yet checked out": book.getLastCheckedOutBy());
    }

    private void setPublisher(Book book) {
        publisherText.setText(book.getPublisher() == null?"Unknown":book.getPublisher());
    }

    private void setAuthor(Book book) {
        authorText.setText(book.getAuthor() == null?"Unknown":book.getAuthor());
    }

    private void setBookTitle(Book book) {
        bookName.setText(book.getTitle() == null ? "Unknown" : book.getTitle());
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void showError(Throwable e) {

    }

    @Override
    public void setData(Book books) {

    }

    @Override
    public void showLoading(boolean isLoading) {

    }

    @Override
    public void checkoutSuccess() {

    }

    @Override
    public void checkoutFail() {

    }

    @Override
    public void deleteSuccess() {

    }

    @Override
    public void deleteFail() {

    }

/*    public static String getStateAsString(int newState) {
        switch (newState) {
            case BottomSheetBehavior.STATE_COLLAPSED:
                return "Collapsed";
            case BottomSheetBehavior.STATE_DRAGGING:
                return "Dragging";
            case BottomSheetBehavior.STATE_EXPANDED:
                return "Expanded";
            case BottomSheetBehavior.STATE_HIDDEN:
                return "Hidden";
            case BottomSheetBehavior.STATE_SETTLING:
                return "Settling";
        }
        return "undefined";
    }*/

}
