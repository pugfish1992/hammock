package com.pugfish1992.hammock.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.pugfish1992.hammock.R;

/**
 * Created by daichi on 11/5/17.
 */

public class TextInputDialog extends DialogFragment {

    interface TextInputListener {
        void onTextInput(@Nullable String text);
    }

    public static final String KEY_TITLE = "TextInputDialog:title";
    public static final String KEY_MESSAGE = "TextInputDialog:message";
    public static final String KEY_CANCEL_BTN_TITLE = "TextInputDialog:cancelBtnTitle";
    public static final String KEY_OK_BTN_TITLE = "TextInputDialog:okBtnTitle";
    public static final String KEY_EDIT_TEXT_HINT = "TextInputDialog:editTextHint";

    private static final String DEFAULT_CANCEL_BTN_TITLE = "cancel";
    private static final String DEFAULT_OK_BTN_TITLE = "ok";

    private TextInputListener mTextInputListener;

    private String mTitle;
    private String mMessage;
    private String mCancelBtnTitle;
    private String mOkBtnTitle;
    private String mEditTextHint;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mTitle = args.getString(KEY_TITLE);
            mMessage = args.getString(KEY_MESSAGE);
            mCancelBtnTitle = args.getString(KEY_CANCEL_BTN_TITLE, DEFAULT_CANCEL_BTN_TITLE);
            mOkBtnTitle = args.getString(KEY_OK_BTN_TITLE, DEFAULT_OK_BTN_TITLE);
            mEditTextHint = args.getString(KEY_EDIT_TEXT_HINT);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.cmp_text_input_dialog, null);

        TextInputLayout inputLayout = view.findViewById(R.id.ipt_layout);
        inputLayout.setHintEnabled(true);
        inputLayout.setHint(mEditTextHint);
        final EditText editText = view.findViewById(R.id.edit_text);

        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(mTitle)
                .setMessage(mMessage)
                .setView(view)
                .setNegativeButton(mCancelBtnTitle, null)
                .setPositiveButton(mOkBtnTitle, null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button okButton = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String text = editText.getText().toString();
                        mTextInputListener.onTextInput(text.length() != 0 ? text : null);
                        dialogInterface.dismiss();
                    }
                });
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mTextInputListener = (TextInputListener) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(context.getClass().getSimpleName() +
                    " must implements " + TextInputListener.class.getSimpleName());
        }
    }

    /* ------------------------------------- *
     * BUILDER CLASS FOR TEXT-INPUT-DIALOG
     * ------------------------------------- */

    public static class Builder {

        private final Bundle mExtras;

        public Builder() {
            mExtras = new Bundle();
        }

        public Builder title(String title) {
            mExtras.putString(KEY_TITLE, title);
            return this;
        }

        public Builder message(String message) {
            mExtras.putString(KEY_MESSAGE, message);
            return this;
        }

        public Builder cancelButtonTitle(String title) {
            mExtras.putString(KEY_CANCEL_BTN_TITLE, title);
            return this;
        }

        public Builder okButtonTitle(String title) {
            mExtras.putString(KEY_OK_BTN_TITLE, title);
            return this;
        }

        public Builder editTextHint(String hint) {
            mExtras.putString(KEY_EDIT_TEXT_HINT, hint);
            return this;
        }

        public void clear() {
            mExtras.clear();
        }

        public TextInputDialog build() {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setArguments(new Bundle(mExtras));
            return dialog;
        }
    }
}
