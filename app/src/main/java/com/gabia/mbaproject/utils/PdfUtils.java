package com.gabia.mbaproject.utils;
import android.content.Context;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PdfUtils {

    public static PdfRenderer openPdfRendererFromAssets(Context context, String assetFileName) throws IOException {
        // 1. Copy asset to a file in cache directory
        File file = new File(context.getCacheDir(), assetFileName);

        if (!file.exists()) {
            try (InputStream asset = context.getAssets().open(assetFileName);
                 FileOutputStream output = new FileOutputStream(file)) {

                byte[] buffer = new byte[1024];
                int read;
                while ((read = asset.read(buffer)) != -1) {
                    output.write(buffer, 0, read);
                }
                output.flush();
            }
        }

        // 2. Open ParcelFileDescriptor on that file
        ParcelFileDescriptor parcelFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);

        // 3. Create and return PdfRenderer
        return new PdfRenderer(parcelFileDescriptor);
    }
}
