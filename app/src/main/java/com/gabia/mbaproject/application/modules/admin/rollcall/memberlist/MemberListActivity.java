package com.gabia.mbaproject.application.modules.admin.rollcall.memberlist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.SelectListener;
import com.gabia.mbaproject.application.modules.admin.MemberViewModel;
import com.gabia.mbaproject.application.modules.admin.utils.PresenceFormDialog;
import com.gabia.mbaproject.application.modules.admin.utils.SavePresenceListener;
import com.gabia.mbaproject.databinding.ActivityMemberListBinding;
import com.gabia.mbaproject.model.Member;
import com.gabia.mbaproject.model.PaymentResponse;
import com.gabia.mbaproject.model.PresenceResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MemberListActivity extends AppCompatActivity implements SelectListener<Member>, SearchView.OnQueryTextListener {

    ActivityMemberListBinding binding;
    private MemberListAdapter memberAdapter;
    private List<Member> memberList = new ArrayList<>();

    public static Intent createIntent(Context context) {
        return new Intent(context, MemberListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_member_list);
        binding.setIsLoading(true);
        memberAdapter = new MemberListAdapter(this);

        setupRecyclerview();
        setupSearchView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchMembers();
    }

    @Override
    public void didSelect(Member model) {
        new PresenceFormDialog(this, model, () -> {
            // TODO: Update it to fetch again the list
        }).show();
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

    // TODO: Update this fetch to fetch members not listed on rehearsal
    private void fetchMembers() {
        MemberViewModel viewModel = new ViewModelProvider(this).get(MemberViewModel.class);
        viewModel.getMemberListLiveData().observe(this, members -> {
            binding.setIsLoading(false);
            memberList = members;
            memberAdapter.setMembers(members);
        });
        viewModel.fetchAll(code -> {
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
}