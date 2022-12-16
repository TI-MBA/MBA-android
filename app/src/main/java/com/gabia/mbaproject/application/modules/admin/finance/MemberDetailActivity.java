package com.gabia.mbaproject.application.modules.admin.finance;

import static com.gabia.mbaproject.application.ConstantKeys.MEMBER_KEY;
import static com.gabia.mbaproject.application.ConstantKeys.REQUEST_UPDATE_MEMBER;
import static com.gabia.mbaproject.utils.DateUtils.monthAndYear;
import static com.gabia.mbaproject.utils.FloatUtils.moneyFormat;
import static com.gabia.mbaproject.utils.StringUtils.capitalizeFirstLetter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.ActionsListener;
import com.gabia.mbaproject.application.modules.admin.finance.payment.PaymentAdapter;
import com.gabia.mbaproject.application.modules.admin.finance.payment.PaymentFormActivity;
import com.gabia.mbaproject.application.modules.admin.finance.payment.PaymentViewModel;
import com.gabia.mbaproject.application.modules.admin.memberform.MemberFormActivity;
import com.gabia.mbaproject.application.modules.member.payment.MemberPaymentListViewModel;
import com.gabia.mbaproject.databinding.ActivityMemberDetailBinding;
import com.gabia.mbaproject.infrastructure.remote.providers.ApiDataSourceProvider;
import com.gabia.mbaproject.infrastructure.remote.remotedatasource.AuthRemoteDataSource;
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack;
import com.gabia.mbaproject.model.AuthRequest;
import com.gabia.mbaproject.model.Member;
import com.gabia.mbaproject.model.PaymentResponse;
import com.gabia.mbaproject.model.enums.Situation;
import com.gabia.mbaproject.utils.DateUtils;

import java.util.Date;
import java.util.List;

public class MemberDetailActivity extends AppCompatActivity implements ActionsListener<PaymentResponse>, PopupMenu.OnMenuItemClickListener {

    private ActivityMemberDetailBinding binding;
    private PaymentAdapter adapter;
    private Member currentMember;

    public static Intent createIntent(Context context, Member member) {
        Intent intent = new Intent(context, MemberDetailActivity.class);
        intent.putExtra(MEMBER_KEY, member);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_member_detail);
        binding.setActivity(this);
        adapter = new PaymentAdapter(this);
        binding.paymentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.paymentsRecyclerView.setAdapter(adapter);

        currentMember = (Member) getIntent().getSerializableExtra(MEMBER_KEY);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.resetPassword:
                showResetPasswordAlert();
                return true;
            case R.id.editMember:
                goToEditMember();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentMember != null) {
            fetchPayments(currentMember.getId());
            bind();
        }
    }

    @Override
    public void edit(PaymentResponse item) {
        startActivity(PaymentFormActivity.createIntent(this, item, currentMember.getId()));
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
                .setPositiveButton("Deletar", (dialog, which) -> requestDeletion(item.getId()))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_UPDATE_MEMBER && resultCode == RESULT_OK) {
            assert data != null;
            if (data.getSerializableExtra(MEMBER_KEY) != null) {
                currentMember = (Member) data.getSerializableExtra(MEMBER_KEY);
                bind();
            }
        }
    }

    public void addPaymentDidPress(View view) {
        startActivity(PaymentFormActivity.createIntent(this, null, currentMember.getId()));
    }

    public void memberActionsDidPress(View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.member_actions_menu);
        popup.show();
    }

    private void bind() {
        String instrumentText = "Ala: " + capitalizeFirstLetter(currentMember.getInstrument());
        String associatedText = currentMember.getAssociated() ? "Sócio" : "Membro";
        Situation userSituation = Situation.valueOf(currentMember.getSituation());

        binding.memberNameText.setText(currentMember.getName());
        binding.instrumentText.setText(instrumentText);
        binding.associatedText.setText(associatedText);
        binding.memberSituationTag.setBackgroundTintList(getResources().getColorStateList(userSituation.getSituationColor()));
        binding.memberSituationTag.setText(userSituation.getFormattedValue());
        binding.setActive(currentMember.getActive());
    }

    private void showResetPasswordAlert() {
        String message = "Tem certeza que resetar a senha \n\n" +
                "Nome: " + currentMember.getName() + "\n" +
                "Email: " + currentMember.getEmail() + "\n";
        new AlertDialog.Builder(this, R.style.DeleteDialogTheme)
                .setIcon(R.drawable.ic_edit)
                .setTitle("Resetar senha")
                .setMessage(message)
                .setPositiveButton("Resetar senha", (dialog, which) -> resetPassword())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void resetPassword() {
        AuthRemoteDataSource authRemoteDataSource = new AuthRemoteDataSource(ApiDataSourceProvider.Companion.getAuthApiDataSource());
        authRemoteDataSource.resetPassword(new AuthRequest(currentMember.getEmail(), ""), new BaseCallBack<AuthRequest>() {
            @Override
            public void onSuccess(AuthRequest result) {
                runOnUiThread(() -> Toast.makeText(MemberDetailActivity.this, "Senha resetada com sucesso", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onError(int code) {
                runOnUiThread(() -> Toast.makeText(MemberDetailActivity.this, "Falha ao resetar senha code: " + code, Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void fetchPayments(int memberId) {
        binding.setIsLoading(true);
        MemberPaymentListViewModel viewModel = new ViewModelProvider(this).get(MemberPaymentListViewModel.class);
        viewModel.getPaymentListLiveData().observe(this, paymentList -> {
            if (paymentList != null) {
                orderByDate(paymentList);
                adapter.setPaymentList(paymentList);
            } else {
                Toast.makeText(this, "Erro ao carregar os pagamentos", Toast.LENGTH_SHORT).show();
            }
            binding.setIsLoading(false);
        });
        viewModel.fetchPayments(memberId, code -> {
            runOnUiThread(() -> Toast.makeText(MemberDetailActivity.this, "Erro ao carregar os pagamentos code: " + code , Toast.LENGTH_SHORT).show());
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

    private void requestDeletion(int paymentId) {
        binding.setIsLoading(true);
        PaymentViewModel paymentViewModel = new ViewModelProvider(MemberDetailActivity.this).get(PaymentViewModel.class);
        paymentViewModel.delete(paymentId, code -> {
            runOnUiThread(() -> {
                if (code >= 200 && code <=299) {
                    Toast.makeText(MemberDetailActivity.this, "Deletado com sucesso", Toast.LENGTH_SHORT).show();
                    fetchPayments(currentMember.getId());
                } else {
                    Toast.makeText(MemberDetailActivity.this, "Falha ao deletar code: " + code, Toast.LENGTH_SHORT).show();
                    binding.setIsLoading(false);
                }
            });
            return null;
        });
    }

    private void goToEditMember() {

        startActivityForResult(MemberFormActivity.createIntent(this, currentMember), REQUEST_UPDATE_MEMBER);
    }
}
