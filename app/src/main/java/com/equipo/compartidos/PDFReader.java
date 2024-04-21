package com.equipo.compartidos;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.shockwave.pdfium.PdfDocument;
import com.equipo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class PDFReader extends AppCompatActivity {
    private static final String TAG = PDFReader.class.getSimpleName();
    public static final String SAMPLE_FILE = "HistorialClinico.pdf";
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    private FirebaseStorage firebaseStorage;
    File localFile = null; // Declare the localFile variable outside the try block

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfreader);
        pdfView= (PDFView)findViewById(R.id.pdfView);
        // Storage
        firebaseStorage = FirebaseStorage.getInstance();
        StorageReference myUserImageStorageRef = firebaseStorage.getReference("Users").child("1")
                .child("HistorialClinico.pdf");
        downloadPdf(myUserImageStorageRef);

        //displayFromAsset(SAMPLE_FILE);
    }

    private void downloadPdf(StorageReference pdfRef) {

    }

   /* private void displayPdf(File pdfFile) {
        // Use a third-party library to display the PDF file
        // For example, using MuPDF:
        MuPDFCore core = new MuPDFCore(this, new FileInputStream(pdfFile));
        Page page = core.getPage(0);
        if (page != null) {
            int width = page.getWidth();
            int height = page.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            page.render(canvas, null, null, new RenderListener() {
                @Override
                public void onRender(int pageIndex, float pageWidth, float pageHeight, int width, int height, int[] buffer) {
                    // Do nothing
                }

                @Override
                public void onLayerRender(int pageIndex, int layerIndex, float pageWidth, float pageHeight, int width, int height, int[] buffer) {
                    // Do nothing
                }
            });

            // Display the bitmap in an ImageView or other view
            ImageView pdfView = findViewById(R.id.pdf_view);
            pdfView.setImageBitmap(bitmap);
        }
    }*/



    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;

        pdfView.fromAsset(SAMPLE_FILE)
                .defaultPage(pageNumber)
                .enableSwipe(true)

                .swipeHorizontal(false)
                .onPageChange(this::onPageChanged)
                .enableAnnotationRendering(true)
                .onLoad(this::loadComplete)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
    }



    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }



    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }
}