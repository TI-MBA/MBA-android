package com.gabia.mbaproject.application.modules.admin.memberform;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.databinding.ActivityMemberDetailBinding;
import com.gabia.mbaproject.databinding.ActivityMemberFormBinding;
import com.gabia.mbaproject.model.Member;
import com.gabia.mbaproject.model.enums.Instrument;
import com.gabia.mbaproject.model.enums.Situation;
import com.jaredrummler.materialspinner.MaterialSpinner;

public class MemberFormActivity extends AppCompatActivity {

    public static String MEMBER_KEY = "com.gabia.mbaproject.application.modules.admin.memberform.MEMBER_KEY";
    private Member currentMember;
    private boolean isEditing = false;
    private ActivityMemberFormBinding binding;

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

        currentMember = (Member) getIntent().getSerializableExtra(MEMBER_KEY);

        if (currentMember != null) {
            isEditing = true;
        }

        bind();
    }

    private void bind() {
        setupInstrumentSpinner();
        setupFiscalSpinner();
    }

    private void setupFiscalSpinner() {
        MaterialSpinner fiscalSituationSpinner = binding.fiscalSituationSpinner;
        fiscalSituationSpinner.setItems(
                Situation.UP_TO_DATE.getFormattedValue(),
                Situation.AGREEMENT.getFormattedValue(),
                Situation.EXEMPT.getFormattedValue(),
                Situation.DEBIT.getFormattedValue()
        );
        fiscalSituationSpinner.setOnItemSelectedListener((view, position, id, item) -> {
            System.out.println("selecionou o");
            System.out.println("tal");
        });
    }

    private void setupInstrumentSpinner() {
        MaterialSpinner instrumentSpinner = binding.instrumentSpinner;
        instrumentSpinner.setItems(
                Instrument.AGBE.getFormattedValue(),
                Instrument.AGOGO.getFormattedValue(),
                Instrument.GONGUE.getFormattedValue(),
                Instrument.CAIXA.getFormattedValue(),
                Instrument.ALFAIA.getFormattedValue(),
                Instrument.CANTO.getFormattedValue()
        );

        instrumentSpinner.setOnItemSelectedListener((view, position, id, item) -> {
            System.out.println("selecionou o");
            System.out.println("tal");
        });
    }
}