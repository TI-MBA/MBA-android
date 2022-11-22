package com.gabia.mbaproject.application.modules.admin.finance;

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
import com.gabia.mbaproject.application.modules.admin.finance.payment.MemberListViewModel;
import com.gabia.mbaproject.databinding.ActivityFinanceHomeBinding;
import com.gabia.mbaproject.model.Member;
import com.gabia.mbaproject.model.enums.UserLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FinanceHomeActivity extends AppCompatActivity implements SelectListener<Member>, SearchView.OnQueryTextListener {

    private ActivityFinanceHomeBinding binding;
    private MemberAdapter memberAdapter;
    private List<Member> memberList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_finance_home);
        binding.setIsLoading(true);
        memberAdapter = new MemberAdapter(this);
        setupRecyclerview();
        fetchMembers();
        setupSearchView();
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
        MemberListViewModel viewModel = new ViewModelProvider(this).get(MemberListViewModel.class);
        viewModel.getMemberListLiveData().observe(this, members -> {
            binding.setIsLoading(false);
            List<Member> userLevelMembers = members.stream()
                    .filter(member -> member.getAdminLevel() == UserLevel.ROLE_USER.getValue())
                    .collect(Collectors.toList());
            memberList = userLevelMembers;
            memberAdapter.setMembers(userLevelMembers);
        });
        viewModel.fetchAll(code -> {
            runOnUiThread(() -> Toast.makeText(FinanceHomeActivity.this, "Falha ao carregar membros code " + code, Toast.LENGTH_SHORT).show());
            return null;
        });
    }


    @Override
    public void didSelect(Member model) {
        Intent i = new Intent(getApplicationContext(), MemberDetailActivity.class);
        startActivity(i);
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
