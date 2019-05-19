package com.example.metrognome.editor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.metrognome.R;
import com.example.metrognome.time.Measure;
import com.example.metrognome.time.TimeSignature;

public class TimeSignatureDialogFragment extends DialogFragment implements AdapterView.OnItemSelectedListener {
    Measure measure;
    Spinner topSignatureSpinner;
    Spinner bottomSignatureSpinner;
    private TimeSignature timeSignature;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View alertView = inflater.inflate(R.layout.alert_dialog_time_signature, null);

        topSignatureSpinner = alertView.findViewById(R.id.spinner_top_signature);
        final ArrayAdapter<Integer> topAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, TimeSignature.TOP_SIGNATURES);
        topAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        topSignatureSpinner.setAdapter(topAdapter);
        topSignatureSpinner.setOnItemSelectedListener(this);
        topSignatureSpinner.setSelection(measure.getTimeSignature().getTopSignature() - 1);

        bottomSignatureSpinner = alertView.findViewById(R.id.spinner_bottom_signature);
        final ArrayAdapter<Integer> bottomAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, TimeSignature.BOTTOM_SIGNATURES);
        bottomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bottomSignatureSpinner.setAdapter(bottomAdapter);
        bottomSignatureSpinner.setOnItemSelectedListener(this);
        bottomSignatureSpinner.setSelection((measure.getTimeSignature().getBottomSignature() + 1)  / 2);

        builder.setView(alertView)
                .setTitle(R.string.time_signature_dialog_title)
                .setMessage(R.string.time_signature_dialog_message)
                .setPositiveButton(R.string.time_signature_dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (timeSignature != null) {
                            measure.setTimeSignature(timeSignature);
                        }
                    }
                })
                .setNegativeButton(R.string.time_signature_dialog_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing if they cancel
                    }
                });

        return builder.create();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Integer value = (Integer)adapterView.getItemAtPosition(i);
        if (timeSignature == null) {
            timeSignature = measure.getTimeSignature();
        }
        if (view.getParent().equals(topSignatureSpinner)) {
            timeSignature = new TimeSignature(value, timeSignature.getBottomSignature());
        } else if (view.getParent().equals(bottomSignatureSpinner)) {
            timeSignature = new TimeSignature(timeSignature.getTopSignature(), value);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // do nothing
    }
}
