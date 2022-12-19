package com.gabia.mbaproject.application.modules.admin.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.model.CreatePresenceRehearsalRequest;
import com.gabia.mbaproject.model.CreatePresenceUserRequest;
import com.gabia.mbaproject.model.Member;
import com.gabia.mbaproject.model.PresenceRequest;
import com.gabia.mbaproject.model.PresenceResponse;
import com.gabia.mbaproject.model.enums.Instrument;
import com.gabia.mbaproject.model.enums.PresenceType;

public class PresenceFormDialog {

    private AlertDialog.Builder builder;
    private Member member;
    private int rehearsalId;
    private SavePresenceListener listener;
    private Activity activity;
    private String title;
    private int memberId;
    private PresenceResponse presenceResponse;
    private String memberName = "Membro: ";
    String memberInstrument = "Ala: ";
    private int presenceId;
    private String presenceType = PresenceType.PRESENT.getValue();
    private AlertDialog alertDialog;

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

    public PresenceFormDialog(Activity activity, Member member, int rehearsalId, SavePresenceListener listener) {
        this.builder = new AlertDialog.Builder(activity);
        this.activity = activity;
        this.member = member;
        this.memberId = member.getId();
        this.rehearsalId = rehearsalId;
        this.listener = listener;
        this.title = "Adicionar Presença";
        this.memberName += member.getName();
        this.memberInstrument += Instrument.valueOf(member.getInstrument()).getFormattedValue();

        bind();
    }

    public PresenceFormDialog(Activity activity, PresenceResponse presenceResponse, int rehearsalId, SavePresenceListener listener) {
        this.builder = new AlertDialog.Builder(activity);
        this.activity = activity;
        this.listener = listener;
        this.rehearsalId = rehearsalId;
        this.title = "Editar Presença";
        this.presenceResponse = presenceResponse;
        this.presenceType = presenceResponse.getPresenceType();
        this.memberId = presenceResponse.getUserId();
        this.memberName += presenceResponse.getName();
        this.memberInstrument += Instrument.valueOf(presenceResponse.getInstrument()).getFormattedValue();
        this.presenceId = presenceResponse.getPresenceId();

        bind();
        setupEdit();
    }

    public void show() {
        alertDialog = builder.create();
        alertDialog.show();
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
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
        titleTextView.setText(title);
        nameText.setText(memberName);
        instrumentText.setText(memberInstrument);

        presentRadioButton.setOnClickListener(v -> selectRadio(view));
        observationRadioButton.setOnClickListener(v -> selectRadio(view));
        absentRadioButton.setOnClickListener(v -> selectRadio(view));

        saveButton.setOnClickListener(v -> {
            PresenceRequest presenceRequest = new PresenceRequest(
                    presenceType,
                    new CreatePresenceRehearsalRequest(rehearsalId),
                    new CreatePresenceUserRequest(memberId)
            );

            alertDialog.dismiss();
            listener.save(presenceRequest, presenceId);
        });
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
