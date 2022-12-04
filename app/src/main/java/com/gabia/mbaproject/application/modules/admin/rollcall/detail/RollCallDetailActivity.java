package com.gabia.mbaproject.application.modules.admin.rollcall.detail;

import static com.gabia.mbaproject.application.ConstantKeys.REHEARSAL_KEY;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.application.SelectListener;
import com.gabia.mbaproject.application.modules.admin.finance.payment.PaymentAdapter;
import com.gabia.mbaproject.application.modules.admin.rollcall.memberlist.MemberListActivity;
import com.gabia.mbaproject.application.modules.admin.utils.PresenceFormDialog;
import com.gabia.mbaproject.application.modules.admin.utils.SavePresenceListener;
import com.gabia.mbaproject.databinding.ActivityRollcallDetailBinding;
import com.gabia.mbaproject.model.Member;
import com.gabia.mbaproject.model.PresenceResponse;
import com.gabia.mbaproject.model.RehearsalResponse;
import com.gabia.mbaproject.model.enums.Instrument;
import com.gabia.mbaproject.model.enums.PresenceType;
import com.gabia.mbaproject.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class RollCallDetailActivity extends AppCompatActivity implements SelectListener<PresenceResponse> {

    private ActivityRollcallDetailBinding binding;
    private RehearsalResponse rehearsal;
    private List<PresenceResponse> presenceList = new ArrayList<>();
    private PresenceAdapter adapter;

    public static Intent createIntent(Context context, RehearsalResponse rehearsal) {
        Intent intent = new Intent(context, RollCallDetailActivity.class);
        intent.putExtra(REHEARSAL_KEY, rehearsal);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rollcall_detail);
        binding.setActivity(this);

        rehearsal = (RehearsalResponse) getIntent().getSerializableExtra(REHEARSAL_KEY);

        if (rehearsal != null) {
            bind();
            fetchMembers();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.setIsLoading(false);
    }

    public void endRollCallDidPress(View view) {
        Toast.makeText(this, "FINALIZOU A CHAMADA", Toast.LENGTH_SHORT).show();
    }

    public void addPresenceDidPress(View view) {
        startActivity(MemberListActivity.createIntent(this));
    }

    private void bind() {
        String formattedDate = DateUtils.changeFromIso(DateUtils.brazilianDate, rehearsal.getDate());
        String title = "Ensaio - " + formattedDate;
        binding.rehearsalTitleText.setText(title);

        adapter = new PresenceAdapter(this);
        binding.frequencyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.frequencyRecyclerView.setAdapter(adapter);
    }

    private void fetchMembers() {
        presenceList.add(new PresenceResponse(0, PresenceType.PRESENT.getValue(), "", new Member(0, "", "", 0, "Pedroni", Instrument.AGBE.getValue(), "", "", "", true, true)));
        presenceList.add(new PresenceResponse(0, PresenceType.ABSENT.getValue(), "", new Member(0, "", "", 0, "Gabi", Instrument.AGOGO.getValue(), "", "", "", true, true)));
        presenceList.add(new PresenceResponse(0, PresenceType.OBSERVATION.getValue(), "", new Member(0, "", "", 0, "Beatriz Vilalta", Instrument.GONGUE.getValue(), "", "", "", true, true)));
        presenceList.add(new PresenceResponse(0, PresenceType.PRESENT.getValue(), "", new Member(0, "", "", 0, "Carina Monteiro", Instrument.CAIXA.getValue(), "", "", "", true, true)));
        presenceList.add(new PresenceResponse(0, PresenceType.OBSERVATION.getValue(), "", new Member(0, "", "", 0, "Gabriel Rosa", Instrument.ALFAIA.getValue(), "", "", "", true, true)));
        presenceList.add(new PresenceResponse(0, PresenceType.PRESENT.getValue(), "", new Member(0, "", "", 0, "Romulo", Instrument.CANTO.getValue(), "", "", "", true, true)));

        adapter.setPresenceList(presenceList);
    }

    @Override
    public void didSelect(PresenceResponse model) {
        new PresenceFormDialog(this, model, () -> {
            // TODO: fetch again the list to update
        }).show();
    }
}