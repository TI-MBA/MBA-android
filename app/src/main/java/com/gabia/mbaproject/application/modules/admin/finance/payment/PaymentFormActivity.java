package com.gabia.mbaproject.application.modules.admin.finance.payment;

import static com.gabia.mbaproject.application.ConstantKeys.MEMBER_KEY;
import static com.gabia.mbaproject.application.ConstantKeys.PAYMENT_KEY;
import static com.gabia.mbaproject.utils.DateUtils.brazilianDate;
import static com.gabia.mbaproject.utils.DateUtils.isoDateFormat;
import static com.gabia.mbaproject.utils.DateUtils.monthAndYear;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
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
import com.kal.rackmonthpicker.RackMonthPicker;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PaymentFormActivity extends AppCompatActivity {
    private ActivityPaymentFormBinding binding;
    private Date referenceDate;
    private Date paymentDate;
    private boolean isEditing = false;
    final Calendar myCalendar = Calendar.getInstance();
    private PaymentViewModel viewModel;
    private PaymentResponse currentPayment;
    private int userId;

    public static Intent createIntent(Context context, PaymentResponse payment, int userId) {
        Intent intent = new Intent(context, PaymentFormActivity.class);
        intent.putExtra(PAYMENT_KEY, payment);
        intent.putExtra(MEMBER_KEY, userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_form);
        binding.setActivity(this);
        binding.setIsLoading(false);
        viewModel = new ViewModelProvider(this).get(PaymentViewModel.class);

        currentPayment = (PaymentResponse) getIntent().getSerializableExtra(PAYMENT_KEY);
        userId = getIntent().getIntExtra(MEMBER_KEY, 0);

        if (currentPayment != null) {
            referenceDate = DateUtils.toDate(DateUtils.isoDateFormat, currentPayment.getDate());
            paymentDate = DateUtils.toDate(DateUtils.isoDateFormat, currentPayment.getPaymentDate());
            String referenceMonth = DateUtils.toString(monthAndYear, referenceDate);
            String paymentStringDate = DateUtils.toString(brazilianDate, paymentDate);
            binding.referenceMonthText.setText(referenceMonth);
            binding.paymentDateText.setText(paymentStringDate);
            binding.paymentValueEditText.setText(String.valueOf(currentPayment.getPaymentValue()));
            binding.observationText.setText(currentPayment.getObservation());
            binding.saveButton.setText("Editar pagamento");
            isEditing = true;
        }
    }

    public void referenceDateDidPress(View view) {
        new RackMonthPicker(this)
                .setLocale(Locale.getDefault())
                .setPositiveButton(this::referenceMonthDidPress)
                .setNegativeButton(AppCompatDialog::dismiss)
                .show();
    }

    public void paymentDateDidPress(View view) {
        new DatePickerDialog(
                this,
                R.style.PickerFullBlue,
                this::paymentDataSelected,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    private void paymentDataSelected(DatePicker datePicker, int year, int month, int day) {
        String selectedDate = addInitialZero(day) + "/" + addInitialZero(month + 1) + "/" + year;
        binding.paymentDateText.setText(selectedDate);
        paymentDate = DateUtils.toDate(brazilianDate, selectedDate);
    }

    private void referenceMonthDidPress(int month, int startDate, int endDate, int year, String monthLabel) {
        String selectedDate = addInitialZero(month) + "/" + year;
        binding.referenceMonthText.setText(selectedDate);
        referenceDate = DateUtils.toDate(monthAndYear, selectedDate);
    }

    public void savePaymentDidPress(View view) {
        binding.setIsLoading(true);
        if (referenceDate == null || paymentDate == null)  {
            Toast.makeText(this, "todos os campos são obrigatorios", Toast.LENGTH_SHORT).show();
        } else {
            String observation = binding.observationText.getText().toString();
            float value = (float) binding.paymentValueEditText.getNumericValue();
            String referenceDateString = DateUtils.toString(isoDateFormat, referenceDate);
            String paymentDateString = DateUtils.toString(isoDateFormat, paymentDate);

            if (isEditing) {
                requestUpdate(observation, value, referenceDateString, paymentDateString);
            } else {
                requestCreate(observation, value, referenceDateString, paymentDateString);
            }
        }
    }

    private void requestCreate(String observation, float value, String referenceDate, String paymentDate) {
        PaymentRequest createRequest = new PaymentRequest(observation, value, referenceDate, paymentDate, new PaymentUserRequest(userId));
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

    private void requestUpdate(String observation, float value, String referenceDate, String paymentDate) {
        UpdatePaymentRequest updateRequest = new UpdatePaymentRequest(observation, value, referenceDate, paymentDate);
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

    private String addInitialZero(int text) {
        if (text < 10) {
            return "0" + text;
        } else {
            return String.valueOf(text);
        }
    }
}
