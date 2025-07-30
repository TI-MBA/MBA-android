package com.gabia.mbaproject.application.modules.member.rulesbook;

import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gabia.mbaproject.R;
import com.gabia.mbaproject.databinding.FragmentRulesBookBinding;
import com.gabia.mbaproject.utils.PdfUtils;

import java.io.IOException;

public class RulesBookFragment extends Fragment {

    FragmentRulesBookBinding binding;
    private PdfRenderer pdfRenderer;

    private RulesBookFragment() {}

    public static RulesBookFragment newInstance() {
        return new RulesBookFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rules_book, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            pdfRenderer = PdfUtils.openPdfRendererFromAssets(requireContext(), "cartilha.pdf");

            binding.pdfRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            PdfPagesAdapter adapter = new PdfPagesAdapter(pdfRenderer);
            binding.pdfRecyclerView.setAdapter(adapter);

        } catch (IOException e) {
            Toast.makeText(getContext(), "Failed to open PDF", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (pdfRenderer != null) pdfRenderer.close();
        binding = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
