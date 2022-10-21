package com.gabia.mbaproject.application.adminmodules.finance.payment;

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

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.adminmodules.dashboard.AdminDashboardActivity;
import com.gabia.mbaproject.databinding.ActivityPaymentFormBinding;
import com.gabia.mbaproject.model.Payment;
import com.gabia.mbaproject.utils.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class PaymentFormActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener {
    public static final String PAYMENT_KEY = "com.gabia.mbaproject.application.adminmodules.finance.payment.PAYMENT_KEY";
    private ActivityPaymentFormBinding binding;
    private Date referenceDate;
    final Calendar myCalendar = Calendar.getInstance();

    public static Intent createIntent(Context context, Payment payment) {
        Intent intent = new Intent(context, PaymentFormActivity.class);
        intent.putExtra(PAYMENT_KEY, payment);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_form);
        binding.setActivity(this);

        Payment payment = (Payment) getIntent().getSerializableExtra(PAYMENT_KEY);

        if (payment != null) {
            referenceDate = payment.getRelativeDate();
            String referenceMonth = DateUtils.toString(monthAndYear, payment.getRelativeDate());
            binding.referenceMonthText.setText(referenceMonth);
            binding.paymentValueEditText.setText(String.valueOf(payment.getValue()));
            binding.observationText.setText(payment.getObservation());
            binding.saveButton.setText("Editar pagamento");
        }
    }

    public void referenceDateDidPress(View view) {
        new DatePickerDialog(this, this, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void savePaymentDidPress(View view) {
        if (binding.paymentValueEditText.getNumericValue() == 0.0 || referenceDate == null)  {
            Toast.makeText(this, "Data de referência e valor são obrigatorios", Toast.LENGTH_SHORT).show();
        } else {
            // TODO: Create payment
            this.onBackPressed();
        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        String selectedDate = getMonthText(month) + "/" + year;
        binding.referenceMonthText.setText(selectedDate);
        referenceDate = DateUtils.toDate(monthAndYear, selectedDate);
        System.out.println("Data selecionada" + referenceDate);
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
