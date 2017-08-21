package com.niedzwiecki.przemyslguide.ui.custom;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.ui.base.BaseRelativeView;
import com.niedzwiecki.przemyslguide.util.ViewUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class PasswordView extends BaseRelativeView {

    @BindView(R.id.inputPasswordEditText)
    TextInputLayout inputPasswordEditText;
    @BindView(R.id.showPasswordImageView)
    ImageView showPasswordImageView;
    @BindView(R.id.passwordEditText)
    EditText passwordEditText;
    @BindView(R.id.passwordErrorText)
    TextView passwordErrorText;

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
        super(context, attrs, defStyleAttr, defStyleRes);
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
    }

    public String getText() {
        return passwordEditText.getText().toString();
    }

    public void hidePassword() {
        passwordEditText
                .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public void showPassword() {
        passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }

    public void onPasswordEyeIconClicked(boolean eyeButtonFlag) {
        if (eyeButtonFlag) {
            hidePassword();
        } else {
            showPassword();
        }
    }

    @OnClick(R.id.passwordEditText)
    public void onPasswordEditTextClicked() {
        passwordEditText.setFocusableInTouchMode(true);
        passwordEditText.requestFocus();
        passwordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        ViewUtil.showKeyboard(passwordEditText);
    }

    @OnClick(R.id.inputPasswordEditText)
    public void onInputPasswordEditTextClicked() {
        passwordEditText.setFocusableInTouchMode(true);
        passwordEditText.requestFocus();
        passwordEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        ViewUtil.showKeyboard(passwordEditText);
    }

    @OnClick(R.id.showPasswordImageView)
    public void onPasswordIconClicked() {
        int cursorPosition = passwordEditText.getSelectionEnd();
        onPasswordEyeIconClicked(eyeButtonFlag);
        eyeButtonFlag = !eyeButtonFlag;
        passwordEditText.setSelection(cursorPosition);
    }

    public void setErrorText(String errorMessage) {
        passwordErrorText.setText(errorMessage);
    }

    public void setErrorVisibility(int visibility) {
        if (visibility == GONE) {
            visibility = INVISIBLE;
        }

        passwordErrorText.setVisibility(visibility);
    }

    public void setOnChangeTextListener(TextWatcher onChangeTextListener) {
        passwordEditText.addTextChangedListener(onChangeTextListener);
    }

}
