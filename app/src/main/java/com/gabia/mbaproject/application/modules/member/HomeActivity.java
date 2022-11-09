package com.gabia.mbaproject.application.modules.member;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.modules.member.editdata.EditPasswordActivity;
import com.gabia.mbaproject.application.modules.member.payment.PaymentListFragment;
import com.gabia.mbaproject.application.modules.member.rollcall.RollCallFragment;
import com.gabia.mbaproject.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        binding.setActivity(this);
        // TODO: Get this id from API
        replaceFragment(PaymentListFragment.newInstance(12));

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.paymentTab:
                    // TODO: Get this id from API
                    replaceFragment(PaymentListFragment.newInstance(12));
                    break;
                case R.id.rollCallTab:
                    // TODO: Get this id from API
                    replaceFragment(RollCallFragment.newInstance(12));
                    break;
            }
            return true;
        });

    }

    public void editMemberDidPress(View view) {
        Intent intent = new Intent(getApplicationContext(), EditPasswordActivity.class);
        startActivity(intent);
    }

    public void logoutDidPress(View view) {
        new AlertDialog.Builder(this, R.style.DeleteDialogTheme)
                .setTitle("Sair")
                .setMessage("Tem certeza que deseja sair do app?")

                .setPositiveButton("Sair", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("Ficar", null)
                .show();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.homeContentFrame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {}
}