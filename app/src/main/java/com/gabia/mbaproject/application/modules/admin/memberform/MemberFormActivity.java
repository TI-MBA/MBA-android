package com.gabia.mbaproject.application.modules.admin.memberform;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.model.Member;

public class MemberFormActivity extends AppCompatActivity {

    public static String MEMBER_KEY = "com.gabia.mbaproject.application.modules.admin.memberform.MEMBER_KEY";
    private Member currentMember;
    private boolean isEditing = false;

    public static Intent createIntent(Context context, Member member) {
        Intent intent = new Intent(context, MemberFormActivity.class);
        intent.putExtra(MEMBER_KEY, member);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_form);

        currentMember = (Member) getIntent().getSerializableExtra(MEMBER_KEY);

        if (currentMember != null) {
            isEditing = true;
        }

    }
}