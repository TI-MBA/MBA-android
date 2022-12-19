package com.gabia.mbaproject.application.modules.admin.rollcall.memberlist;

import static com.gabia.mbaproject.application.ConstantKeys.REHEARSAL_KEY;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.SelectListener;
import com.gabia.mbaproject.application.modules.admin.MemberViewModel;
import com.gabia.mbaproject.application.modules.admin.rollcall.viewmodel.PresenceViewModel;
import com.gabia.mbaproject.application.modules.admin.utils.PresenceFormDialog;
import com.gabia.mbaproject.databinding.ActivityMemberListBinding;
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack;
import com.gabia.mbaproject.model.Member;
import com.gabia.mbaproject.model.PresenceRequest;
import com.gabia.mbaproject.model.PresenceResponse;
import com.gabia.mbaproject.model.RehearsalResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MemberListActivity extends AppCompatActivity implements SelectListener<Member>, SearchView.OnQueryTextListener {

    ActivityMemberListBinding binding;
    private MemberListAdapter memberAdapter;
    private RehearsalResponse rehearsal;
    private List<Member> memberList = new ArrayList<>();
    private PresenceViewModel presenceViewModel;

    public static Intent createIntent(Context context, RehearsalResponse rehearsal) {
        Intent intent = new Intent(context, MemberListActivity.class);
        intent.putExtra(REHEARSAL_KEY, rehearsal);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_member_list);
        binding.setIsLoading(true);
        memberAdapter = new MemberListAdapter(this);
        presenceViewModel = new ViewModelProvider(this).get(PresenceViewModel.class);
        rehearsal = (RehearsalResponse) getIntent().getSerializableExtra(REHEARSAL_KEY);

        if (rehearsal != null) {
            setupRecyclerview();
            setupSearchView();
        } else {
            Toast.makeText(this, "Ensaio não encontrado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchMembers();
    }

    @Override
    public void didSelect(Member model) {
        new PresenceFormDialog(this, model, rehearsal.getId(), (presence, presenceID) -> createPresence(presence)).show();
    }

    private void setupSearchView() {
        binding.searchView.setOnCloseListener(() -> {
            memberAdapter.setMembers(memberList);
            return true;
        });
        binding.searchView.setOnQueryTextListener(this);
    }

    private void setupRecyclerview() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.membersRecyclerView.setLayoutManager(layoutManager);
        binding.membersRecyclerView.setAdapter(memberAdapter);
        binding.membersRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, layoutManager.getOrientation())
        );
    }

    private void fetchMembers() {
        MemberViewModel viewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        viewModel.getUnrelatedRehearsalMemberListLiveData().observe(this, members -> {
            binding.setIsLoading(false);

            memberList = members;
            memberAdapter.setMembers(members);
        });
        viewModel.fetchUnrelatedWithRehearsal(rehearsal.getId(), code -> {
            runOnUiThread(() -> {
                Toast.makeText(MemberListActivity.this, "Falha ao carregar membros code " + code, Toast.LENGTH_SHORT).show();
                binding.setIsLoading(false);
            });
            return null;
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) { return true;}

    @Override
    public boolean onQueryTextChange(String newText) {
        searchMembers(newText);
        return true;
    }

    private void searchMembers(String query) {
        if (memberAdapter.getMemberList().isEmpty()) {
            memberAdapter.setMembers(memberList);
        } else {
            List<Member> filteredMembers = memberList
                    .stream()
                    .filter(element -> element.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
            memberAdapter.setMembers(filteredMembers);
        }
    }

    public void createPresence(PresenceRequest presence) {
        binding.setIsLoading(true);
        presenceViewModel.create(presence, new BaseCallBack<PresenceResponse>() {
            @Override
            public void onSuccess(PresenceResponse result) { handleSave("Presença adicionada"); }

            @Override
            public void onError(int code) { handleSave("Falha ao adicionar presença code: " + code); }

            private void handleSave(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(MemberListActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                });
            }
        });
    }
}