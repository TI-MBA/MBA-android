package com.gabia.mbaproject.application.modules.member.rollcall;

import static com.gabia.mbaproject.application.ConstantKeys.USER_ID;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.modules.admin.rollcall.viewmodel.PresenceViewModel;
import com.gabia.mbaproject.application.modules.member.payment.MemberPaymentListDelegate;
import com.gabia.mbaproject.databinding.FragmentRollCallBinding;
import com.gabia.mbaproject.model.MemberPresenceResponse;
import com.gabia.mbaproject.model.PaymentResponse;
import com.gabia.mbaproject.model.PresenceDto;
import com.gabia.mbaproject.utils.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RollCallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RollCallFragment extends Fragment {

    private FragmentRollCallBinding binding;
    private MemberRollCallAdapter adapter;
    private PresenceViewModel presenceViewModel;
    private MemberRollCallDelegate delegate;
    private int userID;

    private RollCallFragment(MemberRollCallDelegate delegate) {
        this.delegate = delegate;
    }

    public static RollCallFragment newInstance(int userID, MemberRollCallDelegate delegate) {
        RollCallFragment fragment = new RollCallFragment(delegate);
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
        binding.setIsLoading(true);

        adapter = new MemberRollCallAdapter();
        presenceViewModel = new ViewModelProvider(this).get(PresenceViewModel .class);
        binding.rollCallRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rollCallRecyclerView.setAdapter(adapter);
        fetchPresence();

        return binding.getRoot();
    }

    private void fetchPresence() {
        presenceViewModel.getPresenceLiveData().observe(getViewLifecycleOwner(), presenceList -> {
            if (presenceList != null) {
                orderByDate(presenceList);
                adapter.setPresenceList(presenceList);
            } else {
                Toast.makeText(getContext(), "Erro ao carregar os pagamentos", Toast.LENGTH_SHORT).show();
            }
            binding.setIsLoading(false);
        });

        presenceViewModel.fetchAllBy(userID, code -> {
            delegate.failToLoadPresences(code);
            binding.setIsLoading(false);
            return null;
        });
    }

    private void orderByDate(List<MemberPresenceResponse> paymentList) {
        paymentList.sort((o1, o2) -> {
            Date firstDate = DateUtils.toDate(DateUtils.isoDateFormat, o1.getRehearsalDate());
            Date secondDate = DateUtils.toDate(DateUtils.isoDateFormat, o2.getRehearsalDate());
            return secondDate.compareTo(firstDate);
        });
    }
}