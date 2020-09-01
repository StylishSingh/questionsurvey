package com.library.basecontroller;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.library.R;

public abstract class BaseActivity extends AppCompatActivity {

    protected ProgressDialog mProgressDialog;
    private InputMethodManager imm;

    protected abstract int containerId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(this, null, null, true);
            mProgressDialog.setContentView(R.layout.progress_bar);
            mProgressDialog.setCancelable(false);
            if (mProgressDialog.getWindow() != null)
                mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } else mProgressDialog.show();
    }


    public void hideProgressDialog() {
        if (isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public boolean isShowing() {
        return mProgressDialog != null && mProgressDialog.isShowing();
    }

    public void replaceFragment(Class fragmentClass, String extraTag, boolean addToBackStack, Bundle bundle, Fragment fragmentForResult) {
        if (containerId() == 0) throw new NullPointerException("ContainerId cannot be null");
        String finalTag = fragmentClass.getSimpleName() + extraTag;
        boolean isPopBackStack = getSupportFragmentManager().popBackStackImmediate(finalTag, 0);
        if (!isPopBackStack) {
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(finalTag);
            if (fragment == null) try {
                fragment = (Fragment) fragmentClass.newInstance();
                if (fragment instanceof BaseFragment && bundle != null) {
                    ((BaseFragment) fragment).setBundle(bundle);
                }

                if (fragmentForResult != null) {
                    fragment.setTargetFragment(fragmentForResult, 1232);
                }
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(containerId(), fragment, finalTag);
                if (addToBackStack) fragmentTransaction.addToBackStack(finalTag);
                fragmentTransaction.commit();
                getSupportFragmentManager().executePendingTransactions();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void navigateToWithBundle(int container, Fragment fragment, boolean isBackStack, Bundle bundle) {
        fragment.setArguments(bundle);
        FragmentTransaction fts = getSupportFragmentManager().beginTransaction();
        fts.replace(container, fragment, fragment.getClass().getSimpleName());
        if (isBackStack)
            fts.addToBackStack(fragment.getClass().getSimpleName());
        fts.commit();
    }

    public void showDialogFragment(BaseDialogFragment baseDialogFragment, String extraTag, Bundle bundle, Fragment fragmentForResult) {
        baseDialogFragment.setBundle(bundle);
        baseDialogFragment.setTargetFragment(fragmentForResult, 4434);
        baseDialogFragment.show(getSupportFragmentManager(), baseDialogFragment.getClass().getSimpleName() + extraTag);
    }

    public void clearBackStack() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        //this will clear the back stack and displays no animation on the screen
        int backStackCount = fragmentManager.getBackStackEntryCount();
        for (int i = 0; i < backStackCount; i++) {
            // Get the back stack fragment id.
            int backStackId = fragmentManager.getBackStackEntryAt(i).getId();
            fragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            //super.onBackPressed();
            finish();
        }
    }

    public BaseFragment getCurrentFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(containerId());
        if (fragment == null) return null;
        else return (BaseFragment) fragment;
    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
