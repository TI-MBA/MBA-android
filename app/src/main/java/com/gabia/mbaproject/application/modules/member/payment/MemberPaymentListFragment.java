package com.gabia.mbaproject.application.modules.member.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.databinding.FragmentPaymentListBinding;
import com.gabia.mbaproject.infrastructure.remote.api.PaymentApiDataSource;
import com.gabia.mbaproject.infrastructure.remote.providers.ApiDataSourceProvider;
import com.gabia.mbaproject.model.Payment;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemberPaymentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberPaymentListFragment extends Fragment {

    private static final String USER_ID = "com.gabia.mbaproject.application.modules.member.payment.MemberPaymentListFragment.USER_ID";

    private FragmentPaymentListBinding binding;
    private int userID;
    private MemberPaymentAdapter adapter;

    public static MemberPaymentListFragment newInstance(int userID) {
        MemberPaymentListFragment fragment = new MemberPaymentListFragment();
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
                R.layout.fragment_payment_list, container,
                false);
        binding.setIsLoading(true);

        adapter = new MemberPaymentAdapter();

        bindView();
        setupViewModel();

        return binding.getRoot();
    }

    private void bindView() {
        binding.paymentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.paymentsRecyclerView.setAdapter(adapter);
    }

    private void setupViewModel() {
        PaymentApiDataSource paymentApiDataSource = ApiDataSourceProvider.Companion.getPaymentApiDataSource();
        MemberPaymentListViewModel viewModel = new ViewModelProvider(this, new MemberPaymentListViewModelFactory(paymentApiDataSource)).get(MemberPaymentListViewModel.class);
        viewModel.getPaymentListLiveData().observe(getViewLifecycleOwner(), paymentList -> {
            if (paymentList != null) {
                binding.setIsLoading(false);
                adapter.setPaymentList(paymentList);
            } else {
                Toast.makeText(getContext(), "Erro ao carregar os pagamentos", Toast.LENGTH_SHORT).show();
                binding.setIsLoading(false);
            }
        });
        viewModel.fetchPayments(userID);
    }
}