package com.tevinjeffrey.prolificlibrary.ui.base;

import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

//Responsible for attaching and detaching the view to the presenter
public abstract class BasePresenter<V extends BaseView>  {

    @Nullable
    //I admit this was a bit premature. The WeakReference holds the view to avoid leaking a
    // reference to it.
    private WeakReference<V> mBaseView;

    public void onPause() {}

    public void onResume() {}

    @Nullable
    public V getView() {
        if (mBaseView != null)
            return mBaseView.get();
        return null;
    }

    public V attachView(V view) {
        mBaseView = new WeakReference<>(view);
        return mBaseView.get();
    }

    public void detachView() {
        if (mBaseView != null) {
            mBaseView.clear();
        }
    }

    @Override
    public String toString() {
        return "BasePresenter";
    }
}
