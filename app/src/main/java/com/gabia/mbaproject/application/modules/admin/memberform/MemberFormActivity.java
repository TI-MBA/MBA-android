package com.gabia.mbaproject.application.modules.admin.memberform;

import static com.gabia.mbaproject.application.ConstantKeys.MEMBER_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.modules.admin.MemberViewModel;
import com.gabia.mbaproject.databinding.ActivityMemberFormBinding;
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack;
import com.gabia.mbaproject.model.CreateMemberRequest;
import com.gabia.mbaproject.model.Member;
import com.gabia.mbaproject.model.MemberRequest;
import com.gabia.mbaproject.model.enums.Instrument;
import com.gabia.mbaproject.model.enums.Situation;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.HashMap;
import java.util.Map;

public class MemberFormActivity extends AppCompatActivity implements BaseCallBack<Member> {

    private Member currentMember;
    private boolean isEditing = false;
    private ActivityMemberFormBinding binding;

    Situation selectedSituation = Situation.UP_TO_DATE;
    Instrument selectedInstrument = Instrument.CAIXA;

    private MaterialSpinner fiscalSituationSpinner;
    private MaterialSpinner instrumentSpinner;
    private EditText nameEditText;
    private EditText emailEditText;
    private SwitchCompat activeMemberSwitch;
    private SwitchCompat associatedSwitch;
    private Button saveButton;

    public static Intent createIntent(Context context, Member member) {
        Intent intent = new Intent(context, MemberFormActivity.class);
        intent.putExtra(MEMBER_KEY, member);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_member_form);
        binding.setActivity(this);
        binding.setIsLoading(false);

        currentMember = (Member) getIntent().getSerializableExtra(MEMBER_KEY);
        isEditing = currentMember != null;
        bind();
    }

    @Override
    public void onSuccess(Member result) {
        runOnUiThread(() -> {
            if (isEditing) {
                Toast.makeText(MemberFormActivity.this, "Membro editado com sucesso", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK,
                        new Intent().putExtra(MEMBER_KEY, result)
                );
                finish();
            } else {
                Toast.makeText(MemberFormActivity.this, "Mebmro criado com sucesso", Toast.LENGTH_SHORT).show();
                MemberFormActivity.this.onBackPressed();
            }

        });
    }

    @Override
    public void onError(int code) {
        runOnUiThread(() -> {
            Toast.makeText(MemberFormActivity.this, "Error code: " + code, Toast.LENGTH_SHORT).show();
            MemberFormActivity.this.onBackPressed();
        });

    }

    public void saveDidPress(View view) {
        if (emailEditText.getText().toString().isEmpty() || nameEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show();
        } else {
            String name = nameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String instrument = selectedInstrument.getValue();
            String situation = selectedSituation.getValue();
            boolean active = activeMemberSwitch.isChecked();
            boolean associated = associatedSwitch.isChecked();
            MemberRequest member = new MemberRequest(
                    email,
                    0,
                    name,
                    instrument,
                    situation,
                    active,
                    associated
            );

            requestAction(member);
        }
    }

    private void requestAction(MemberRequest member) {
        MemberViewModel viewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        binding.setIsLoading(true);
        if (isEditing) {
            viewModel.edit(currentMember.getId(), member, this);
        } else {
            CreateMemberRequest createMemberRequest = new CreateMemberRequest(member);
            viewModel.create(createMemberRequest, this);
        }
    }

    private void bind() {
        nameEditText = binding.nameEditText;
        emailEditText = binding.emailEditText;
        instrumentSpinner = binding.instrumentSpinner;
        fiscalSituationSpinner = binding.fiscalSituationSpinner;
        activeMemberSwitch = binding.activeMemberSwitch;
        associatedSwitch = binding.associatedSwitch;
        saveButton = binding.saveButton;

        setupInstrumentSpinner();
        setupFiscalSpinner();

        if(isEditing) {
            nameEditText.setText(currentMember.getName());
            emailEditText.setText(currentMember.getEmail());
            activeMemberSwitch.setChecked(currentMember.getActive());
            associatedSwitch.setChecked(currentMember.getAssociated());
            saveButton.setText("Editar usuário");
        }
    }

    private void setupFiscalSpinner() {
        Map<String, Situation> situationMap = new HashMap<>();
        situationMap.put("Em dia", Situation.UP_TO_DATE);
        situationMap.put("Débito", Situation.DEBIT);
        situationMap.put("Cota", Situation.EXEMPT);
        situationMap.put("Em acordo", Situation.AGREEMENT);

        fiscalSituationSpinner.setItems(
                Situation.UP_TO_DATE.getFormattedValue(),
                Situation.DEBIT.getFormattedValue(),
                Situation.EXEMPT.getFormattedValue(),
                Situation.AGREEMENT.getFormattedValue()
        );

        fiscalSituationSpinner.setOnItemSelectedListener((view, position, id, item) -> {
            String selectedItem = (String) item;
            this.selectedSituation = situationMap.get(selectedItem);
        });

        if (isEditing) {
            int situationPosition = Situation.valueOf(currentMember.getSituation()).getPosition();
            fiscalSituationSpinner.setSelectedIndex(situationPosition);
            String selectedSituationText = (String) fiscalSituationSpinner.getItems().get(fiscalSituationSpinner.getSelectedIndex());
            this.selectedSituation = situationMap.get(selectedSituationText);
        }
    }

    private void setupInstrumentSpinner() {
        Map<String, Instrument> instrumentMap = new HashMap<>();
        instrumentMap.put("Agbê", Instrument.AGBE);
        instrumentMap.put("Agogô", Instrument.AGOGO);
        instrumentMap.put("Gonguê", Instrument.GONGUE);
        instrumentMap.put("Caixa", Instrument.CAIXA);
        instrumentMap.put("Alfaia", Instrument.ALFAIA);
        instrumentMap.put("Canto", Instrument.CANTO);
        instrumentMap.put("Maestria", Instrument.MAESTRIA);

        instrumentSpinner = binding.instrumentSpinner;
        instrumentSpinner.setItems(
                Instrument.AGBE.getFormattedValue(),
                Instrument.AGOGO.getFormattedValue(),
                Instrument.GONGUE.getFormattedValue(),
                Instrument.CAIXA.getFormattedValue(),
                Instrument.ALFAIA.getFormattedValue(),
                Instrument.CANTO.getFormattedValue(),
                Instrument.MAESTRIA.getFormattedValue()
        );

        instrumentSpinner.setOnItemSelectedListener((view, position, id, item) -> {
            String selectedItem = (String) item;
            this.selectedInstrument = instrumentMap.get(selectedItem);
        });

        if (isEditing) {
            int instrumentPosition = Instrument.valueOf(currentMember.getInstrument()).getPosition();
            instrumentSpinner.setSelectedIndex(instrumentPosition);
            String selectedInstrumentText = (String) instrumentSpinner.getItems().get(instrumentSpinner.getSelectedIndex());
            this.selectedInstrument = instrumentMap.get(selectedInstrumentText);
        }
    }
}