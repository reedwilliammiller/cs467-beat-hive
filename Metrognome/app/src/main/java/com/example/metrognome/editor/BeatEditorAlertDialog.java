package com.example.metrognome.editor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.metrognome.R;

public class BeatEditorAlertDialog extends DialogFragment {
    public static final String KEY_BEAT_INDEX = "BEAT_INDEX";

    private AlertDialog.Builder builder;
    private DialogListener dialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Activity activity = getActivity();
        final int beatIndex = getArguments().getInt(KEY_BEAT_INDEX, 0);

        builder = new AlertDialog.Builder(activity);
        builder.setTitle(getString(R.string.text_dialog_beat_title));
        builder.setPositiveButton(getString(R.string.text_dialog_beat_add), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogListener.onDialogAddBeat(BeatEditorAlertDialog.this, beatIndex);
            }
        });

        builder.setNegativeButton(getString(R.string.text_dialog_beat_remove), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogListener.onDialogRemoveBeat(BeatEditorAlertDialog.this, beatIndex);
            }
        });

        builder.setNeutralButton(getString(R.string.text_button_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogListener.onDialogCancel(BeatEditorAlertDialog.this, beatIndex);
                dialog.cancel();
            }
        });

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
        void onDialogAddBeat(DialogFragment dialogFragment, int beatIndex);
        void onDialogRemoveBeat(DialogFragment dialogFragment, int beatIndex);
        void onDialogCancel(DialogFragment dialogFragment, int beatIndex);
    }
}
