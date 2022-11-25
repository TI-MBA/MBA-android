package com.gabia.mbaproject.application.modules.admin.finance;

import android.os.Bundle;
import android.view.View;
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
import com.gabia.mbaproject.application.modules.admin.memberform.MemberFormActivity;
import com.gabia.mbaproject.databinding.ActivityFinanceHomeBinding;
import com.gabia.mbaproject.model.Member;

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
        binding.setActivity(this);
        binding.setIsLoading(true);
        memberAdapter = new MemberAdapter(this);
        setupRecyclerview();
        setupSearchView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchMembers();
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
        viewModel.getMemberListLiveData().observe(this, members -> {
            binding.setIsLoading(false);
            memberList = members;
            memberAdapter.setMembers(members);
        });
        viewModel.fetchAll(code -> {
            runOnUiThread(() -> {
                Toast.makeText(FinanceHomeActivity.this, "Falha ao carregar membros code " + code, Toast.LENGTH_SHORT).show();
                binding.setIsLoading(false);
            });
            return null;
        });
    }

    public void addMemberDidPress(View view) {
        startActivity(MemberFormActivity.createIntent(this, null));
    }


    @Override
    public void didSelect(Member model) {
        startActivity(MemberDetailActivity.createIntent(this, model));
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
