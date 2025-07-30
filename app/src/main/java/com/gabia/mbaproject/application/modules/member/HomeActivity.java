package com.gabia.mbaproject.application.modules.member;

import static com.gabia.mbaproject.utils.StringUtils.capitalizeFirstLetter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.App;
import com.gabia.mbaproject.application.modules.member.editdata.EditPasswordActivity;
import com.gabia.mbaproject.application.modules.member.payment.MemberPaymentListDelegate;
import com.gabia.mbaproject.application.modules.member.payment.MemberPaymentListFragment;
import com.gabia.mbaproject.application.modules.member.rollcall.MemberRollCallDelegate;
import com.gabia.mbaproject.application.modules.member.rollcall.RollCallFragment;
import com.gabia.mbaproject.application.modules.member.rulesbook.RulesBookFragment;
import com.gabia.mbaproject.databinding.ActivityHomeBinding;
import com.gabia.mbaproject.infrastructure.local.UserDefaults;
import com.gabia.mbaproject.model.Member;
import com.gabia.mbaproject.model.enums.Situation;

public class HomeActivity extends AppCompatActivity implements MemberPaymentListDelegate, MemberRollCallDelegate {

    private ActivityHomeBinding binding;
    private Member currentMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setActivity(this);
        currentMember = UserDefaults.getInstance(getApplicationContext()).getCurrentUser();

        if (currentMember == null) {
            Toast.makeText(this, "Sem usuário na sessão", Toast.LENGTH_SHORT).show();
            App.logout(this);
        } else {
            bindUser();
            setupBottomTabs();
        }
    }

    private void bindUser() {
        String instrumentText = "Ala: " + capitalizeFirstLetter(currentMember.getInstrument());
        String associatedText = currentMember.getAssociated() ? "Sócio" : "Membro";
        Situation userSituation = Situation.valueOf(currentMember.getSituation());

        binding.memberNameText.setText(currentMember.getName());
        binding.instrumentText.setText(instrumentText);
        binding.associatedText.setText(associatedText);
        binding.memberSituationTag.setBackgroundTintList(getResources().getColorStateList(userSituation.getSituationColor()));
        binding.memberSituationTag.setText(userSituation.getFormattedValue());
    }

    public void editMemberDidPress(View view) {
        Intent intent = new Intent(getApplicationContext(), EditPasswordActivity.class);
        startActivity(intent);
    }

    public void logoutDidPress(View view) {
        new AlertDialog.Builder(this, R.style.DeleteDialogTheme)
                .setTitle("Sair")
                .setMessage("Tem certeza que deseja sair do app?")

                .setPositiveButton("Sair", (dialog, which) -> {
                    App.logout(this);
                })
                .setNegativeButton("Ficar", null)
                .show();
    }

    private void setupBottomTabs() {
        replaceFragment(MemberPaymentListFragment.newInstance(currentMember.getId(), this));

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.paymentTab:
                    replaceFragment(MemberPaymentListFragment.newInstance(currentMember.getId(), this));
                    break;
                case R.id.rollCallTab:
                    replaceFragment(RollCallFragment.newInstance(currentMember.getId(), this));
                    break;

                case R.id.rulesBookTab:
                    replaceFragment(RulesBookFragment.newInstance());
                    break;
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeContentFrame, fragment);
        fragmentTransaction.commit();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {}

    @Override
    public void failToLoadPayments(int code) {
        runOnUiThread(() -> Toast.makeText(this, "Falha ao carregar pagamentos code " + code, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void failToLoadPresences(int code) {
        runOnUiThread(() -> Toast.makeText(this, "Falha ao carregar presenças code " + code, Toast.LENGTH_SHORT).show());
    }
}