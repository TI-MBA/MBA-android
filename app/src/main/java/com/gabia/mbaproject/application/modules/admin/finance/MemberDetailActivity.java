package com.gabia.mbaproject.application.modules.admin.finance;

import static com.gabia.mbaproject.utils.DateUtils.monthAndYear;
import static com.gabia.mbaproject.utils.FloatUtils.moneyFormat;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.ActionsListener;
import com.gabia.mbaproject.application.SelectListener;
import com.gabia.mbaproject.application.modules.admin.finance.payment.PaymentAdapter;
import com.gabia.mbaproject.application.modules.admin.finance.payment.PaymentFormActivity;
import com.gabia.mbaproject.databinding.ActivityMemberDetailBinding;
import com.gabia.mbaproject.model.Payment;
import com.gabia.mbaproject.model.PaymentResponse;
import com.gabia.mbaproject.utils.DateUtils;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MemberDetailActivity extends AppCompatActivity implements ActionsListener<PaymentResponse> {

    private ActivityMemberDetailBinding binding;
    private PaymentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_member_detail);
        binding.setActivity(this);
        adapter = new PaymentAdapter(this);
        binding.paymentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.paymentsRecyclerView.setAdapter(adapter);
        fetchPayments();
    }

    public void editMemberDidPress(View view) {
        Toast.makeText(this, "VOU FAZER A TELA DE EDITAR USER", Toast.LENGTH_SHORT).show();
    }

    public void addPaymentDidPress(View view) {
        startActivity(PaymentFormActivity.createIntent(this, null));
    }

    private void fetchPayments() {
//        List<Payment> paymentList = Arrays.asList(
//                new Payment(15.0f, "pagou metade esse mês", new Date()),
//                new Payment(30.50f, "pagou Inteira", new Date()),
//                new Payment(30.50f, "pagou Inteira", new Date()),
//                new Payment(30.50f, "pagou Inteira", new Date()),
//                new Payment(30.50f, "pagou Inteira", new Date()),
//                new Payment(15.0f, "pagou metade esse mês", new Date()),
//                new Payment(30.50f, "pagou Inteira", new Date())
//        );

//        adapter.setPaymentList(paymentList);
    }

    @Override
    public void edit(PaymentResponse item) {
//        startActivity(PaymentFormActivity.createIntent(this, item));
    }

    @Override
    public void delete(PaymentResponse item) {
        String relativeDate = DateUtils.changeFromIso(monthAndYear, item.getDate());
        String message = "Tem certeza que deseja deletar o pagamento \n\n" +
                "Mês de referência: " + relativeDate + "\n" +
                "Valor: " + moneyFormat(item.getPaymentValue()) + "\n";
        new AlertDialog.Builder(this, R.style.DeleteDialogTheme)
                .setIcon(R.drawable.ic_delete_red)
                .setTitle("Deletar pagamento")
                .setMessage(message)
                .setPositiveButton("Deletar", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MemberDetailActivity.this, "Deletado", Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
