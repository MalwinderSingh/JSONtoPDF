package com.ms.android.jsontopdf.controller;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfWriter;
import com.ms.android.jsontopdf.ui.fragments.ResponseFragment;
import com.ms.android.jsontopdf.utils.PDFEventHelper;
import com.ms.android.jsontopdf.utils.Utils;

import java.io.OutputStream;

/**
 * Created by Malwinder on 25-10-2015.
 */
public class PDFGeneratorTask extends AsyncTask<OutputStream, Void, Void> {

    private String formattedJSON;
    private Fragment fragment;

    public PDFGeneratorTask(Fragment fragment, String formattedJSON) {
        this.formattedJSON = formattedJSON;
        this.fragment = fragment;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Utils.getInstance().showProgressDialog(fragment.getActivity(), "Please wait ...", "Converting to PDF");
    }

    @Override
    protected Void doInBackground(OutputStream... outputStreams) {
        saveDocument(outputStreams[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Utils.getInstance().dismissDialog();
        ((ResponseFragment) fragment).showFileDialog();
        super.onPostExecute(aVoid);
    }

    private void saveDocument(OutputStream os) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, os).setPageEvent(new PDFEventHelper());
            document.open();
            addDocumentTitle(document);
            addLineBreak(new Phrase("Followed by Line break", Utils.getInstance().boldFont));
            addContent(document, formattedJSON);
            document.close();
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
    }

    private static void addDocumentTitle(Document document) {
        document.addTitle("This is title");
    }

    private static void addContent(Document document, String formattedJSON) throws DocumentException {
        document.add(new Paragraph(formattedJSON));
    }

    private static void addLineBreak(Phrase paragraph) {
        for (int i = 0; i < 3; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}
