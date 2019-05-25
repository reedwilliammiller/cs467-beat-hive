package com.example.metrognome.editor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.metrognome.R;

public class MeasureEditorAlertDialog extends DialogFragment {
    public static final String KEY_MEASURE_COUNT = "MEASURE_COUNT";
    public static final String KEY_IS_ADD_MEASURE = "IS_ADD_MEASURE";

    private AlertDialog.Builder builder;
    private NumberPicker numberPicker;
    private TextView textView;
    private DialogListener dialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        int measureCount = getArguments().getInt(KEY_MEASURE_COUNT, 0);
        boolean isAddMeasure = getArguments().getBoolean(KEY_IS_ADD_MEASURE, false);

        builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_measure, null);
        numberPicker = dialogView.findViewById(R.id.number_picker_dialog_measure);
        numberPicker.setMinValue(0);
        textView = dialogView.findViewById(R.id.text_view_dialog_measure);

        if (isAddMeasure) {
            textView.setText(R.string.text_add_measure);
            numberPicker.setMaxValue(measureCount);
            builder.setPositiveButton(R.string.text_button_add, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialogListener.onDialogAddMeasure(MeasureEditorAlertDialog.this, numberPicker.getValue());
                }
            });
        } else {
            textView.setText(R.string.text_delete_measure);
            numberPicker.setMaxValue(measureCount - 1);
            builder.setPositiveButton(R.string.text_button_delete, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialogListener.onDialogRemoveMeasure(MeasureEditorAlertDialog.this, numberPicker.getValue());
                }
            });
        }
        builder.setNegativeButton(R.string.text_button_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setView(dialogView);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            dialogListener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement DialogListener");
        }
    }

    public interface DialogListener {
        void onDialogAddMeasure(DialogFragment dialogFragment, int measureIndex);
        void onDialogRemoveMeasure(DialogFragment dialogFragment, int measureIndex);
    }
}
