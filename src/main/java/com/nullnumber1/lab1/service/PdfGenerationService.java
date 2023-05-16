package com.nullnumber1.lab1.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.nullnumber1.lab1.model.PaymentDocument;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfGenerationService {

    public byte[] generatePdf(PaymentDocument paymentDocument) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);

            document.open();
            document.add(new Paragraph("Payment Document ID: " + paymentDocument.getId()));
            document.add(new Paragraph("Payer INN: " + paymentDocument.getPayerInn()));
            document.add(new Paragraph("Payee INN: " + paymentDocument.getPayeeInn()));
            document.add(new Paragraph("Organization OKTMO: " + paymentDocument.getOrganizationOktmo()));
            document.add(new Paragraph("Payment Type: " + paymentDocument.getPaymentType()));
            document.add(new Paragraph("Amount: " + paymentDocument.getAmount()));
            document.add(new Paragraph("Date of Payment: " + paymentDocument.getDateOfPayment()));

        } catch (DocumentException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }

        return out.toByteArray();
    }
}
