package com.example.clock0.Controller.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.clock0.databinding.FragmentTimerDialogBinding;


@SuppressWarnings("ALL")
public class TimerDialogFragment extends DialogFragment {

  private FragmentTimerDialogBinding dialog;
  private OkButtonClickedListener timeListener;

  public TimerDialogFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OkButtonClickedListener) {
            timeListener = (OkButtonClickedListener) context;
        }
        else {
           throw new ClassCastException("your activity does not implements OnFragmentClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        timeListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        dialog = FragmentTimerDialogBinding.inflate(inflater, container, false);
        View view = dialog.getRoot();

        dialog.pickerHours.setMaxValue(23);
        dialog.pickerHours.setMinValue(0);
        dialog.pickerHours.setValue(0);

        dialog.pickerMinutes.setMaxValue(59);
        dialog.pickerMinutes.setMinValue(0);
        dialog.pickerMinutes.setValue(1);

        dialog.pickerSeconds.setMaxValue(59);
        dialog.pickerSeconds.setMinValue(0);
        dialog.pickerSeconds.setValue(0);

        dialog.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             long fullTime =
                     (dialog.pickerHours.getValue()*60*60*1_000) +
                     (dialog.pickerMinutes.getValue()*60*1_000) +
                     (dialog.pickerSeconds.getValue()*1_000) ;

                timeListener.okButtonClickedListener(fullTime);


             getDialog().dismiss();
            }
        });

        dialog.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        return view;
    }


    public interface OkButtonClickedListener {
        void okButtonClickedListener(long fullTime);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dialog = null;
    }
}