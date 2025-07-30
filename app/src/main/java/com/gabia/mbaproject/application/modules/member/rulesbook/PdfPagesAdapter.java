package com.gabia.mbaproject.application.modules.member.rulesbook;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gabia.mbaproject.databinding.ItemPdfPageBinding;

public class PdfPagesAdapter extends RecyclerView.Adapter<PdfPagesAdapter.PageViewHolder> {

    private PdfRenderer pdfRenderer;

    public PdfPagesAdapter(PdfRenderer pdfRenderer) {
        this.pdfRenderer = pdfRenderer;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemPdfPageBinding binding = ItemPdfPageBinding.inflate(inflater, parent, false);
        return new PageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        PdfRenderer.Page page = pdfRenderer.openPage(position);

        Bitmap bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(), Bitmap.Config.ARGB_8888);
        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

        holder.binding.setPageBitmap(bitmap);
        holder.binding.executePendingBindings();

        page.close();
    }

    @Override
    public int getItemCount() {
        return pdfRenderer.getPageCount();
    }

    static class PageViewHolder extends RecyclerView.ViewHolder {
        private final ItemPdfPageBinding binding;

        public PageViewHolder(ItemPdfPageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
