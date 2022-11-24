package com.gabia.mbaproject.application.modules.admin.finance.payment;

import static com.gabia.mbaproject.utils.DateUtils.monthAndYear;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.databinding.ActivityPaymentFormBinding;
import com.gabia.mbaproject.infrastructure.utils.BaseCallBack;
import com.gabia.mbaproject.model.PaymentRequest;
import com.gabia.mbaproject.model.PaymentResponse;
import com.gabia.mbaproject.model.PaymentUserRequest;
import com.gabia.mbaproject.model.UpdatePaymentRequest;
import com.gabia.mbaproject.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class PaymentFormActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener {
    public static final String PAYMENT_KEY = "com.gabia.mbaproject.application.modules.adminmodules.finance.payment.PAYMENT_KEY";
    public static final String USER_KEY = "com.gabia.mbaproject.application.modules.adminmodules.finance.payment.USER_ID_KEY";
    private ActivityPaymentFormBinding binding;
    private Date referenceDate;
    private boolean isEditing = false;
    final Calendar myCalendar = Calendar.getInstance();
    private PaymentViewModel viewModel;
    private PaymentResponse currentPayment;
    private int userId;

    public static Intent createIntent(Context context, PaymentResponse payment, int userId) {
        Intent intent = new Intent(context, PaymentFormActivity.class);
        intent.putExtra(PAYMENT_KEY, payment);
        intent.putExtra(USER_KEY, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_form);
        binding.setActivity(this);
        viewModel = new ViewModelProvider(this).get(PaymentViewModel.class);

        currentPayment = (PaymentResponse) getIntent().getSerializableExtra(PAYMENT_KEY);
        userId = getIntent().getIntExtra(USER_KEY, 0);

        if (currentPayment != null) {
            referenceDate = DateUtils.toDate(DateUtils.isoDateFormat, currentPayment.getDate());
            String referenceMonth = DateUtils.toString(monthAndYear, referenceDate);
            binding.referenceMonthText.setText(referenceMonth);
            binding.paymentValueEditText.setText(String.valueOf(currentPayment.getPaymentValue()));
            binding.observationText.setText(currentPayment.getObservation());
            binding.saveButton.setText("Editar pagamento");
            isEditing = true;
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String selectedDate = getMonthText(month) + "/" + year;
        binding.referenceMonthText.setText(selectedDate);
        referenceDate = DateUtils.toDate(monthAndYear, selectedDate);

    }

    public void referenceDateDidPress(View view) {
        new DatePickerDialog(this, this, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void savePaymentDidPress(View view) {
        if (binding.paymentValueEditText.getNumericValue() == 0.0 || referenceDate == null)  {
            Toast.makeText(this, "Data de referência e valor são obrigatorios", Toast.LENGTH_SHORT).show();
        } else {
            String observation = binding.observationText.getText().toString();
            float value = (float) binding.paymentValueEditText.getNumericValue();
            String date = DateUtils.toString(DateUtils.isoDateFormat, referenceDate);

            if (isEditing) {
                requestUpdate(observation, value, date);
            } else {
                requestCreate(observation, value, date);
            }
        }
    }

    private void requestCreate(String observation, float value, String date) {
        PaymentRequest createRequest = new PaymentRequest(observation, value, date, new PaymentUserRequest(userId));
        viewModel.create(createRequest, new BaseCallBack<PaymentResponse>() {
            @Override
            public void onSuccess(PaymentResponse result) {
                runOnUiThread(() -> {
                    Toast.makeText(PaymentFormActivity.this, "Pagamento adicionado", Toast.LENGTH_SHORT).show();
                    PaymentFormActivity.this.onBackPressed();
                });
            }

            @Override
            public void onError(int code) {
                runOnUiThread(() -> {
                    Toast.makeText(PaymentFormActivity.this, "Erro ao criar pagamento code " + code, Toast.LENGTH_SHORT).show();
                    PaymentFormActivity.this.onBackPressed();
                });
            }
        });
    }

    private void requestUpdate(String observation, float value, String date) {
        UpdatePaymentRequest updateRequest = new UpdatePaymentRequest(observation, value, date);
        viewModel.update(currentPayment.getId(), updateRequest, new BaseCallBack<PaymentResponse>() {
            @Override
            public void onSuccess(PaymentResponse result) {
                runOnUiThread(() -> {
                    Toast.makeText(PaymentFormActivity.this, "Pagamento atualizado", Toast.LENGTH_SHORT).show();
                    PaymentFormActivity.this.onBackPressed();
                });
            }

            @Override
            public void onError(int code) {
                runOnUiThread(() -> {
                    Toast.makeText(PaymentFormActivity.this, "Erro ao editar pagamento code " + code, Toast.LENGTH_SHORT).show();
                    PaymentFormActivity.this.onBackPressed();
                });
            }
        });
    }

    private String getMonthText(int month) {
        int monthRealValue = month + 1;
        if (monthRealValue < 10) {
            return "0" + monthRealValue;
        } else {
            return String.valueOf(monthRealValue);
        }
    }
}
