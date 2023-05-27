package com.gabia.mbaproject.application.modules.member.rulesbook;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.databinding.FragmentRulesBookBinding;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.github.barteksc.pdfviewer.util.FitPolicy;

public class RulesBookFragment extends Fragment {

    FragmentRulesBookBinding binding;

    private RulesBookFragment() {}

    public static RulesBookFragment newInstance() {
        return new RulesBookFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rules_book, container, false);
        binding.pdfView.fromAsset("cartilha.pdf")
                .enableAnnotationRendering(true)
                .scrollHandle(new DefaultScrollHandle(getContext()))
                .spacing(10)
                .pageFitPolicy(FitPolicy.BOTH)
                .load();
        return binding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
