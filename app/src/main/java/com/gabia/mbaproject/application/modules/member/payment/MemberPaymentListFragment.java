package com.gabia.mbaproject.application.modules.member.payment;

import static com.gabia.mbaproject.application.ConstantKeys.USER_ID;

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
import com.gabia.mbaproject.application.modules.admin.finance.FinanceHomeActivity;
import com.gabia.mbaproject.databinding.FragmentPaymentListBinding;
import com.gabia.mbaproject.infrastructure.remote.api.PaymentApiDataSource;
import com.gabia.mbaproject.infrastructure.remote.providers.ApiDataSourceProvider;
import com.gabia.mbaproject.model.Payment;
import com.gabia.mbaproject.model.PaymentResponse;
import com.gabia.mbaproject.utils.DateUtils;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemberPaymentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemberPaymentListFragment extends Fragment {

    private FragmentPaymentListBinding binding;
    private int userID;
    private MemberPaymentAdapter adapter;
    private MemberPaymentListDelegate delegate;

    private MemberPaymentListFragment(MemberPaymentListDelegate delegate) {
        this.delegate = delegate;
    }

    public static MemberPaymentListFragment newInstance(int userID, MemberPaymentListDelegate delegate) {
        MemberPaymentListFragment fragment = new MemberPaymentListFragment(delegate);
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
        MemberPaymentListViewModel viewModel = new ViewModelProvider(this).get(MemberPaymentListViewModel.class);
        viewModel.getPaymentListLiveData().observe(getViewLifecycleOwner(), paymentList -> {
            if (paymentList != null) {
                orderByDate(paymentList);
                adapter.setPaymentList(paymentList);
            } else {
                Toast.makeText(getContext(), "Erro ao carregar os pagamentos", Toast.LENGTH_SHORT).show();
            }
            binding.setIsLoading(false);
        });
        viewModel.fetchPayments(userID, code -> {
            delegate.failToLoadPayments(code);
            binding.setIsLoading(false);
            return null;
        });
    }

    private void orderByDate(List<PaymentResponse> paymentList) {
        paymentList.sort((o1, o2) -> {
            Date firstDate = DateUtils.toDate(DateUtils.isoDateFormat, o1.getDate());
            Date secondDate = DateUtils.toDate(DateUtils.isoDateFormat, o2.getDate());
            return secondDate.compareTo(firstDate);
        });
    }
}