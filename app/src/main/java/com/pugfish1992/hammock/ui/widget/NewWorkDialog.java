package com.pugfish1992.hammock.ui.widget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;

import com.pugfish1992.hammock.R;
import com.pugfish1992.hammock.model.Overview;
import com.pugfish1992.hammock.model.OverviewCreator;
import com.pugfish1992.hammock.model.Work;
import com.pugfish1992.hammock.model.WorkCreator;

/**
 * Created by daichi on 11/4/17.
 */

public class NewWorkDialog extends DialogFragment {

    private OnCreateNewWorkListener mListener;

    public interface OnCreateNewWorkListener {
        void onNewWorkCreated(@NonNull Work work);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater()
                .inflate(R.layout.widget_new_work_dialog, null);

        final TextInputLayout inputNameLayout = view.findViewById(R.id.input_name_layout);
        final TextInputEditText nameEditor = view.findViewById(R.id.edit_name);
        final TextInputEditText summaryEditor = view.findViewById(R.id.edit_summary);

        final Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle("NEW WORK")
                .setView(view)
                .setPositiveButton("SAVE", null)
                .setNegativeButton("CENCEL", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = nameEditor.getText().toString();
                        String summary = summaryEditor.getText().toString();
                        if (name.length() != 0) {
                            Overview overview = new OverviewCreator()
                                    .title(name)
                                    .summary((summary.length() != 0) ? summary : null)
                                    .save();

                            Work work = new WorkCreator()
                                    .overview(overview)
                                    .save();

                            mListener.onNewWorkCreated(work);
                            dialog.dismiss();

                        } else {
                            inputNameLayout.setError("Input a title.");
                        }
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
            mListener = (OnCreateNewWorkListener) context;
        } catch (ClassCastException e) {
            throw new RuntimeException(
                    context.toString() + " must implement " +
                            OnCreateNewWorkListener.class.getSimpleName());
        }
    }
}
