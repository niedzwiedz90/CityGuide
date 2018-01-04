package com.niedzwiecki.przemyslguide.ui.custom;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.databinding.ViewPasswordBinding;
import com.niedzwiecki.przemyslguide.ui.base.BaseRelativeView;
import com.niedzwiecki.przemyslguide.util.ViewUtil;

public class PasswordView extends BaseRelativeView {

    boolean eyeButtonFlag = false;

    public PasswordView(Context context) {
        super(context);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int contentId() {
        return R.layout.view_password;
    }

    @SuppressLint("RtlHardcoded")
    @Override
    public void beforeViews() {
        super.beforeViews();
        int padding = (int) getContext().getResources().getDimension(R.dimen.paddingSmall);
        setPadding(padding, padding, padding, padding);
        setGravity(Gravity.RIGHT | CENTER_VERTICAL);
        dataBidingEnabled = true;
    }

    @Override
    public void afterViews() {
        super.afterViews();
        getViewDataBinding().passwordEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onPasswordEditTextClicked();
            }
        });

        getViewDataBinding().inputPasswordEditText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onInputPasswordEditTextClicked();
            }
        });

        getViewDataBinding().showPasswordImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onPasswordIconClicked();
            }
        });
    }

    @Override
    public ViewPasswordBinding getViewDataBinding() {
        return (ViewPasswordBinding) super.getViewDataBinding();
    }

    public String getText() {
        return getViewDataBinding().passwordEditText.getText().toString();
    }

    public void hidePassword() {
        getViewDataBinding().passwordEditText
                .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public void showPassword() {
        getViewDataBinding().passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }

    public void onPasswordEyeIconClicked(boolean eyeButtonFlag) {
        if (eyeButtonFlag) {
            hidePassword();
        } else {
            showPassword();
        }
    }

    public void onPasswordEditTextClicked() {
        getViewDataBinding().passwordEditText.setFocusableInTouchMode(true);
        getViewDataBinding().passwordEditText.requestFocus();
        getViewDataBinding().passwordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        ViewUtil.showKeyboard(getViewDataBinding().passwordEditText);
    }

    public void onInputPasswordEditTextClicked() {
        getViewDataBinding().passwordEditText.setFocusableInTouchMode(true);
        getViewDataBinding().passwordEditText.requestFocus();
        getViewDataBinding().passwordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        ViewUtil.showKeyboard(getViewDataBinding().passwordEditText);
    }

    public void onPasswordIconClicked() {
        int cursorPosition = getViewDataBinding().passwordEditText.getSelectionEnd();
        onPasswordEyeIconClicked(eyeButtonFlag);
        eyeButtonFlag = !eyeButtonFlag;
        getViewDataBinding().passwordEditText.setSelection(cursorPosition);
    }

    public void setErrorText(String errorMessage) {
        getViewDataBinding().passwordErrorText.setText(errorMessage);
    }

    public void setErrorVisibility(int visibility) {
        if (visibility == GONE) {
            visibility = INVISIBLE;
        }

        getViewDataBinding().passwordErrorText.setVisibility(visibility);
    }

    public void setOnChangeTextListener(TextWatcher onChangeTextListener) {
        getViewDataBinding().passwordEditText.removeTextChangedListener(onChangeTextListener);
        getViewDataBinding().passwordEditText.addTextChangedListener(onChangeTextListener);
    }

}
