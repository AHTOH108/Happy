package com.iandp.happy.dialogs;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.iandp.happy.R;

/**
 * Date creation: 17.03.2016.
 */
public class EditTextDialog extends AppCompatDialogFragment {

    private static final String ARG_TEXT = "text";
    private static final String TAG_PARENT_FRAGMENT = "tagParentFragment";

    private TextInputLayout mTextInputLayout;
    private EditText mEditText;

    public interface OnConfirmEditListener {
        void onConfirmEditListener(String string);
    }

    private OnConfirmEditListener mOnConfirmListener;

    public static EditTextDialog newInstance(String text, String tagFragment) {
        EditTextDialog fragment = new EditTextDialog();

        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        args.putString(TAG_PARENT_FRAGMENT, tagFragment);
        fragment.setArguments(args);

        return fragment;
    }

    public String getTagFragment() {
        return getArguments().getString(TAG_PARENT_FRAGMENT);
    }

    public String getTextOnEdit() {
        String str = getArguments().getParcelable(ARG_TEXT);
        if (str == null) str = "";
        return str;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (getTagFragment().length() > 0) {
            mOnConfirmListener = (OnConfirmEditListener) getFragmentManager().findFragmentByTag(getTagFragment());
        } else {
            mOnConfirmListener = (OnConfirmEditListener) activity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mOnConfirmListener = null;
    }

    @NonNull
    @Override
    public AppCompatDialog onCreateDialog(Bundle savedInstanceState) {
        String str = getTextOnEdit();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_edit_text, null);
        setupView(view, str);

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setView(view)
                .setPositiveButton(getString(R.string.apply), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveAndClose();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        closeDialog();
                    }
                });

        return adb.create();
    }

    private void setupView(View view, String text) {
        mTextInputLayout = (TextInputLayout) view.findViewById(R.id.textInputLayout);
        mEditText = (EditText) view.findViewById(R.id.editText);

        showNameCategory(text);
        mEditText.requestFocus();
        openKeyboard();
    }

    private void showNameCategory(String name) {
        if (name == null) name = "";
        mEditText.setText(name);

    }

    private void openKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mTextInputLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        imm.toggleSoftInput(0, 0);
    }

    private void saveAndClose() {
        mTextInputLayout.setError(null);
        String text = mEditText.getText().toString();
        if (!TextUtils.isEmpty(text)) {

            mOnConfirmListener.onConfirmEditListener(text);
            closeDialog();
        } else {
//            textInputLayoutCategory.setError(getString(R.string.error_edit_text));
//            textInputLayoutCategory.requestFocus();
            //TODO: не работает обработка ошибок. Окно само закрывается
            closeDialog();
        }
    }

    private void closeDialog() {
        closeKeyboard();
        this.dismiss();
    }
}
