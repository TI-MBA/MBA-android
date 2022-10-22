package com.gabia.mbaproject.application.modules.admin.finance;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.SelectListener;
import com.gabia.mbaproject.databinding.ActivityFinanceHomeBinding;
import com.gabia.mbaproject.model.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FinanceHomeActivity extends AppCompatActivity implements SelectListener<Member>, SearchView.OnQueryTextListener {

    private ActivityFinanceHomeBinding binding;
    private MemberAdapter memberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_finance_home);
        memberAdapter = new MemberAdapter(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.membersRecyclerView.setLayoutManager(layoutManager);
        binding.membersRecyclerView.setAdapter(memberAdapter);
        binding.membersRecyclerView.addItemDecoration(
                new DividerItemDecoration(this, layoutManager.getOrientation())
        );

        memberAdapter.setMembers(fetchMembers());

        binding.searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                memberAdapter.setMembers(fetchMembers());
                return true;
            }
        });

        binding.searchView.setOnQueryTextListener(this);
    }

    private List<Member> fetchMembers() {
        List<Member> memberList = new ArrayList<>();
        memberList.add(new Member("Gabriel Rosa do Nascimento"));
        memberList.add(new Member("Carina de Oliveira Monteiro"));
        memberList.add(new Member("Beatriz Vilalta Jimenez"));
        memberList.add(new Member("Gabriel Rosa do Nascimento"));
        memberList.add(new Member("Carina de Oliveira Monteiro"));
        memberList.add(new Member("Beatriz Vilalta Jimenez"));
        memberList.add(new Member("Gabriel Rosa do Nascimento"));
        memberList.add(new Member("Carina de Oliveira Monteiro"));
        memberList.add(new Member("Beatriz Vilalta Jimenez"));
        memberList.add(new Member("Gabriel Rosa do Nascimento"));
        memberList.add(new Member("Carina de Oliveira Monteiro"));
        memberList.add(new Member("Beatriz Vilalta Jimenez"));
        memberList.add(new Member("Gabriel Rosa do Nascimento"));
        memberList.add(new Member("Carina de Oliveira Monteiro"));
        memberList.add(new Member("Beatriz Vilalta Jimenez"));

        return memberList;
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
            memberAdapter.setMembers(fetchMembers());
        } else {
            List<Member> filteredMembers = fetchMembers()
                    .stream()
                    .filter(element -> element.getName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
            memberAdapter.setMembers(filteredMembers);
        }
    }

//    private fun searchLogic(searchString: String?) {
//        if (searchString?.isNotEmpty() == true) {
//
//            val list = mainList.filter {
//                it.contains(searchString, true)
//            }
//            adapter.updateList(ArrayList(list))
//        } else {
//            adapter.updateList(ArrayList(mainList))
//        }
//    }
}
