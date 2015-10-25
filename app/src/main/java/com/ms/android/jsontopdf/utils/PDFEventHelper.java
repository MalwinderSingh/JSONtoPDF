package com.ms.android.jsontopdf.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by Malwinder on 24-10-2015.
 */
public class PDFEventHelper extends PdfPageEventHelper {
    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        super.onStartPage(writer, document);
        try {
            document.add(new Paragraph("Header - Page: " + document.getPageNumber(), Utils.getInstance().boldFont));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        super.onEndPage(writer, document);
        try {
            document.add(new Phrase("Footer - Page: " + document.getPageNumber(), Utils.getInstance().boldFont));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
