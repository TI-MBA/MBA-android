package com.gabia.mbaproject.application.modules.member.payment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.modules.admin.finance.payment.PaymentAdapter;
import com.gabia.mbaproject.databinding.FragmentPaymentListBinding;
import com.gabia.mbaproject.infrastructure.remote.providers.ApiDataSourceProvider;
import com.gabia.mbaproject.infrastructure.remote.remotedatasource.PaymentRemoteDataSource;
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack;
import com.gabia.mbaproject.model.Payment;
import com.gabia.mbaproject.model.PaymentResponse;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PaymentListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PaymentListFragment extends Fragment {

    private static final String USER_ID = "com.gabia.mbaproject.application.modules.member.payment.PaymentListFragment.USER_ID";

    private FragmentPaymentListBinding binding;
    private int userID;
    private MemberPaymentAdapter adapter;

    public static PaymentListFragment newInstance(int userID) {
        PaymentListFragment fragment = new PaymentListFragment();
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

        adapter = new MemberPaymentAdapter();
        binding.paymentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.paymentsRecyclerView.setAdapter(adapter);
        fetchPayments();

        return binding.getRoot();
    }

    private void fetchPayments() {
        PaymentRemoteDataSource paymentRemoteDataSource = new PaymentRemoteDataSource(ApiDataSourceProvider.Companion.getPaymentApiDataSource());

        
        List<Payment> paymentList = Arrays.asList(
                new Payment(15.0f, "pagou metade esse mês", new Date()),
                new Payment(30.50f, "pagou Inteira", new Date()),
                new Payment(30.50f, "pagou Inteira", new Date()),
                new Payment(30.50f, "pagou Inteira", new Date()),
                new Payment(30.50f, "pagou Inteira", new Date()),
                new Payment(15.0f, "pagou metade esse mês", new Date()),
                new Payment(30.50f, "pagou Inteira", new Date())
        );

        adapter.setPaymentList(paymentList);
    }
}