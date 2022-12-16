package com.gabia.mbaproject.application.modules.admin.rollcall.detail;

import static com.gabia.mbaproject.application.ConstantKeys.REHEARSAL_KEY;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.App;
import com.gabia.mbaproject.application.SelectListener;
import com.gabia.mbaproject.application.modules.admin.MemberViewModel;
import com.gabia.mbaproject.application.modules.admin.rollcall.memberlist.MemberListActivity;
import com.gabia.mbaproject.application.modules.admin.rollcall.viewmodel.PresenceViewModel;
import com.gabia.mbaproject.application.modules.admin.rollcall.viewmodel.RehearsalViewModel;
import com.gabia.mbaproject.application.modules.admin.utils.PresenceFormDialog;
import com.gabia.mbaproject.databinding.ActivityRollcallDetailBinding;
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack;
import com.gabia.mbaproject.model.PresenceRequest;
import com.gabia.mbaproject.model.PresenceResponse;
import com.gabia.mbaproject.model.RehearsalResponse;
import com.gabia.mbaproject.model.enums.PresenceType;
import com.gabia.mbaproject.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import kotlin.Unit;

public class RollCallDetailActivity extends AppCompatActivity implements SelectListener<PresenceResponse>, BaseCallBack<PresenceResponse> {

    private ActivityRollcallDetailBinding binding;
    private RehearsalResponse rehearsal;
    private List<PresenceResponse> presenceList = new ArrayList<>();
    private MemberViewModel memberViewModel;
    private PresenceViewModel presenceViewModel;
    private RehearsalViewModel rehearsalViewModel;
    private PresenceAdapter adapter;

    public static Intent createIntent(Context context, RehearsalResponse rehearsal) {
        Intent intent = new Intent(context, RollCallDetailActivity.class);
        intent.putExtra(REHEARSAL_KEY, rehearsal);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rollcall_detail);
        binding.setActivity(this);
        binding.setIsLoading(true);

        setupViewModels();

        rehearsal = (RehearsalResponse) getIntent().getSerializableExtra(REHEARSAL_KEY);

        if (rehearsal != null) {
            bind();
            observeLiveData();
        }


        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getLayoutPosition();
                PresenceResponse presenceToDelete = adapter.getPresenceAt(position);
                showConfirmationDialog(presenceToDelete, position);
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(RollCallDetailActivity.this, R.color.baque_red))
                        .addActionIcon(R.drawable.ic_delete_white)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            public void showConfirmationDialog(PresenceResponse presence, int position) {
                String message = "Tem certeza que deseja deletar essa presença? \n\n" +
                        "Ensaio - " + DateUtils.changeFromIso(DateUtils.brazilianDate, rehearsal.getDate()) + "\n" +
                        "Nome: " + presence.getName() + "\n" +
                        "Ala: " + presence.getInstrument() + "\n" +
                        "Tipo de presença: " + PresenceType.valueOf(presence.getPresenceType()).getFormattedValue() + "\n";
                new AlertDialog.Builder(RollCallDetailActivity.this)
                        .setIcon(R.drawable.ic_delete_red)
                        .setTitle("Deletar presença")
                        .setMessage(message)
                        .setPositiveButton("Deletar", (dialog, which) -> deletePresence(presence.getPresenceId(), position))
                        .setNegativeButton("Cancelar", (dialog, which) -> adapter.notifyDataSetChanged())
                        .show();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.frequencyRecyclerView);


    }

    @Override
    protected void onResume() {
        super.onResume();

        fetchRelatedMembers();
    }

    @Override
    public void didSelect(PresenceResponse model) {
        new PresenceFormDialog(this, model, rehearsal.getId(), this::updatePresence).show();
    }

    @Override
    public void onSuccess(PresenceResponse result) {
        fetchRelatedMembers();
    }

    @Override
    public void onError(int code) {
        runOnUiThread(() -> Toast.makeText(RollCallDetailActivity.this, "Falha ao editar presença cide: " + code, Toast.LENGTH_SHORT).show());
    }

    public void endRollCallDidPress(View view) {
        rehearsalViewModel.finalizeRehearsal(rehearsal.getId(), () -> {
            runOnUiThread(this::fetchRelatedMembers);
            return null;
        }, code -> {
            runOnUiThread(() -> Toast.makeText(RollCallDetailActivity.this, "Falha ao finalizar ensaio code: " + code, Toast.LENGTH_SHORT).show());
            return null;
        });
    }

    public void addPresenceDidPress(View view) {
        startActivity(MemberListActivity.createIntent(this, rehearsal));
    }

    private void deletePresence(int presenceId, int position) {
        presenceViewModel.delete(presenceId, new BaseCallBack<Unit>() {
            @Override
            public void onSuccess(Unit result) {
                runOnUiThread(() -> {
                    Toast.makeText(RollCallDetailActivity.this, "Deletado com sucesso", Toast.LENGTH_SHORT).show();
                    adapter.getPresenceList().remove(position);
                    adapter.notifyItemRemoved(position);
                });
            }

            @Override
            public void onError(int code) {
                runOnUiThread(() -> {
                    Toast.makeText(RollCallDetailActivity.this, "Falha ao deletar presença code: " + code, Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                });
            }
        });
    }

    private void fetchRelatedMembers() {
        memberViewModel.fetchRelatedWithRehearsal(rehearsal.getId(), code -> {
            runOnUiThread(() -> Toast.makeText(RollCallDetailActivity.this, "Falha ao carregar presenças code: " + code, Toast.LENGTH_SHORT).show());
            binding.setIsLoading(false);
            return null;
        });
    }

    public void updatePresence(PresenceRequest presence, int presenceId) {
        presenceViewModel.update(presenceId, presence, this);
    }

    private void setupViewModels() {
        memberViewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        presenceViewModel = new ViewModelProvider(this).get(PresenceViewModel.class);
        rehearsalViewModel = new ViewModelProvider(this).get(RehearsalViewModel.class);
    }

    private void bind() {
        String formattedDate = DateUtils.changeFromIso(DateUtils.brazilianDate, rehearsal.getDate());
        String title = "Ensaio - " + formattedDate;
        binding.rehearsalTitleText.setText(title);

        adapter = new PresenceAdapter(this);
        binding.frequencyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.frequencyRecyclerView.setAdapter(adapter);
    }

    private void observeLiveData() {
        memberViewModel.getRelatedRehearsalMemberListLiveData().observe(this, presenceList -> {
            adapter.setPresenceList(presenceList);
            binding.setIsLoading(false);
        });
    }
}