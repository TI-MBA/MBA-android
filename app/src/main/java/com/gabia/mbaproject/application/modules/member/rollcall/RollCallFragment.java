package com.gabia.mbaproject.application.modules.member.rollcall;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.modules.admin.dashboard.AdminDashboardAdapter;
import com.gabia.mbaproject.databinding.FragmentRollCallBinding;
import com.gabia.mbaproject.model.PresenceDto;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RollCallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RollCallFragment extends Fragment {

    private static final String USER_ID = "com.gabia.mbaproject.application.modules.member.rollcall.RollCallFragment.USER_ID";

    private FragmentRollCallBinding binding;
    private MemberRollCallAdapter adapter;
    private int userID;

    public static RollCallFragment newInstance(int userID) {
        RollCallFragment fragment = new RollCallFragment();
        Bundle args = new Bundle();
        args.putInt(USER_ID, userID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userID = getArguments().getInt(USER_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_roll_call, container,
                false);

        adapter = new MemberRollCallAdapter();
        binding.rollCallRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rollCallRecyclerView.setAdapter(adapter);
        fetchPresence();

        return binding.getRoot();
    }

    private void fetchPresence() {
        List<PresenceDto> presenceList = new ArrayList<>();
        presenceList.add(new PresenceDto(false, "Ensaio - 22/10/2022"));
        presenceList.add(new PresenceDto(true, "Ensaio - 15/10/2022"));
        presenceList.add(new PresenceDto(true, "Ensaio - 8/10/2022"));
        presenceList.add(new PresenceDto(true, "Ensaio - 1/10/2022"));
        presenceList.add(new PresenceDto(true, "Ensaio - 24/09/2022"));
        presenceList.add(new PresenceDto(false, "Ensaio - 24/09/2022"));
        presenceList.add(new PresenceDto(true, "Ensaio - 24/09/2022"));
        presenceList.add(new PresenceDto(false, "Ensaio - 24/09/2022"));
        presenceList.add(new PresenceDto(true, "Ensaio - 24/09/2022"));
        presenceList.add(new PresenceDto(true, "Ensaio - 24/09/2022"));

        adapter.setPresenceList(presenceList);
    }
}