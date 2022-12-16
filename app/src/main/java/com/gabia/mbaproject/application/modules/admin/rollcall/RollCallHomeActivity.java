package com.gabia.mbaproject.application.modules.admin.rollcall;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.SelectListener;
import com.gabia.mbaproject.application.modules.admin.rollcall.viewmodel.RehearsalViewModel;
import com.gabia.mbaproject.application.modules.admin.rollcall.detail.RollCallDetailActivity;
import com.gabia.mbaproject.databinding.ActivityRollCallHomeBinding;
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack;
import com.gabia.mbaproject.model.RehearsalRequest;
import com.gabia.mbaproject.model.RehearsalResponse;
import com.gabia.mbaproject.utils.DateUtils;

import java.util.Date;

public class RollCallHomeActivity extends AppCompatActivity implements SelectListener<RehearsalResponse> {

    private ActivityRollCallHomeBinding binding;
    private RehearsalAdapter adapter;
    RehearsalViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_roll_call_home);
        binding.setActivity(this);
        binding.setIsLoading(true);
        adapter = new RehearsalAdapter(this);
        viewModel = new ViewModelProvider(this).get(RehearsalViewModel.class);
        setupRecyclerView();
        fetchRehearsalList();
    }

    public void addRehearsalDidPress(View view) {
        String message = "Tem certeza que deseja adicionar um ensaio para hoje? \n\n" +
                "Data: " + DateUtils.toString(DateUtils.brazilianDate, new Date()) + "\n";
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_rehearsal)
                .setTitle("Criar ensaio")
                .setMessage(message)
                .setPositiveButton("Criar", (dialog, which) -> createRehearsal())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void didSelect(RehearsalResponse model) {
        startActivity(RollCallDetailActivity.createIntent(this, model));
    }

    private void createRehearsal() {
        String today = DateUtils.toString(DateUtils.brazilianDate, new Date());
        boolean canCreate = checkCanCreateRehearsal(today);

        if (canCreate) {
            viewModel.create(new RehearsalRequest(today), new BaseCallBack<RehearsalResponse>() {
                @Override
                public void onSuccess(RehearsalResponse result) {
                    runOnUiThread(() -> {
                        Toast.makeText(RollCallHomeActivity.this, "Ensaio criado com sucesso", Toast.LENGTH_SHORT).show();
                        viewModel.fetchAll(integer -> null);
                    });
                }

                @Override
                public void onError(int code) {
                    runOnUiThread(() -> {
                        Toast.makeText(RollCallHomeActivity.this, "Falha ao criar ensaio code: " + code, Toast.LENGTH_SHORT).show();
                        System.out.println("aaa");
                    });
                }
            });
        } else {
            Toast.makeText(this, "Já possui ensaio criado pra hoje", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkCanCreateRehearsal(String today) {
        boolean canCreate = true;
        for (RehearsalResponse currentRehearsal : adapter.getRehearsalList()) {
            String currentDate = DateUtils.changeFromIso(DateUtils.brazilianDate, currentRehearsal.getDate());
            if (currentDate.equals(today)) {
                canCreate = false;
            }
        }
        return canCreate;
    }

    private void fetchRehearsalList() {
        viewModel.getRehearsalLiveData().observe(this, rehearsalResponses -> {
            if (rehearsalResponses != null) {
                adapter.setRehearsal(rehearsalResponses);
            } else {
                runOnUiThread(() -> {
                    Toast.makeText(RollCallHomeActivity.this, "Erro ao carregar os ensaios", Toast.LENGTH_SHORT).show();
                });
            }
            binding.setIsLoading(false);
        });
        viewModel.fetchAll(code -> {
            runOnUiThread(() -> {
                Toast.makeText(RollCallHomeActivity.this, "Erro ao carregar os ensaios code: " + code, Toast.LENGTH_SHORT).show();
                binding.setIsLoading(false);
            });

            return null;
        });
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.rehearsalRecyclerView.setLayoutManager(layoutManager);
        binding.rehearsalRecyclerView.setAdapter(adapter);
    }
}