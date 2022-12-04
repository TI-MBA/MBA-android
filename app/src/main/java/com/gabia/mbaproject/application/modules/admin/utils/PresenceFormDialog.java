package com.gabia.mbaproject.application.modules.admin.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.modules.admin.rollcall.detail.RollCallDetailActivity;
import com.gabia.mbaproject.model.Member;
import com.gabia.mbaproject.model.PresenceResponse;
import com.gabia.mbaproject.model.enums.Instrument;
import com.gabia.mbaproject.model.enums.PresenceType;

public class PresenceFormDialog {

    private AlertDialog.Builder builder;
    private Member member;
    private SavePresenceListener listener;
    private Activity activity;
    private String title;
    private PresenceResponse presenceResponse;
    private String presenceType = PresenceType.PRESENT.getValue();

    private LinearLayout contentView;
    private ProgressBar loadingBar;
    private Button saveButton;
    private TextView nameText;
    private TextView instrumentText;
    private TextView titleTextView;
    private RadioGroup radioGroup;
    private AppCompatRadioButton presentRadioButton;
    private AppCompatRadioButton observationRadioButton;
    private AppCompatRadioButton absentRadioButton;

    public PresenceFormDialog(Activity activity, Member member, SavePresenceListener listener) {
        this.builder = new AlertDialog.Builder(activity);
        this.activity = activity;
        this.member = member;
        this.listener = listener;
        this.title = "Adicionar Presença";

        bind();
    }

    public PresenceFormDialog(Activity activity, PresenceResponse presenceResponse, SavePresenceListener listener) {
        this.builder = new AlertDialog.Builder(activity);
        this.activity = activity;
        this.member = presenceResponse.getUser();
        this.listener = listener;
        this.title = "Editar Presença";
        this.presenceType = presenceResponse.getType();
        this.presenceResponse = presenceResponse;

        bind();
        setupEdit();
    }

    public void show() {
        builder.show();
    }

    private void bind() {
        View view = activity.getLayoutInflater().inflate(R.layout.alert_add_presence, null);
        contentView = view.findViewById(R.id.alertContentView);
        loadingBar = view.findViewById(R.id.alertProgressBar);
        saveButton = view.findViewById(R.id.alertSaveButton);
        nameText = view.findViewById(R.id.addPresenceNameText);
        instrumentText = view.findViewById(R.id.addPresenceInstrumentText);
        titleTextView = view.findViewById(R.id.addPresenceTitleTextView);
        radioGroup = view.findViewById(R.id.presenceTypeRadioGroup);
        presentRadioButton = view.findViewById(R.id.presentRadioButton);
        observationRadioButton = view.findViewById(R.id.observationRadioButton);
        absentRadioButton = view.findViewById(R.id.absentRadioButton);

        builder.setView(view);

        setupView(view);
    }

    private void setupView(View view) {
        String memberName = "Membro: " + member.getName();
        String memberInstrument = "Ala: " + Instrument.valueOf(member.getInstrument()).getFormattedValue();
        titleTextView.setText(title);
        nameText.setText(memberName);
        instrumentText.setText(memberInstrument);

        presentRadioButton.setOnClickListener(v -> selectRadio(view));
        observationRadioButton.setOnClickListener(v -> selectRadio(view));
        absentRadioButton.setOnClickListener(v -> selectRadio(view));

        saveButton.setOnClickListener(v -> {
            saveAction();
            listener.saveResult();
        });
    }

    private void saveAction() {
        if (presenceResponse == null) {
            // TODO: create new presence and add it
        } else {
            // TODO: Use current presence response and update it
        }
    }

    private void selectRadio(View view) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        AppCompatRadioButton button = view.findViewById(radioId);

        switch (button.getText().toString()) {
            case "Presente":
                presenceType = PresenceType.PRESENT.getValue();
                break;
            case "Observação":
                presenceType = PresenceType.OBSERVATION.getValue();
                break;
            case "Ausente":
                presenceType = PresenceType.ABSENT.getValue();
                break;
        }
    }

    private void setupEdit() {
        switch (presenceType) {
            case "PRESENT":
                presentRadioButton.setChecked(true);
                break;
            case "OBSERVATION":
                observationRadioButton.setChecked(true);
                break;
            case "ABSENT":
                absentRadioButton.setChecked(true);
                break;
        }
    }
}
