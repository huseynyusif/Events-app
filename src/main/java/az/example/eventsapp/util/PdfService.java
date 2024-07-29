package az.example.eventsapp.util;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Component
public class PdfService {

    public byte[] createTicketPdf(String eventName, String userName,String type, String customerCode) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc);

        document.add(new Paragraph("Ticket Purchase Confirmation"));
        document.add(new Paragraph("Event: " + eventName));
        document.add(new Paragraph("Name: " + userName));
        document.add(new Paragraph("Ticket type" + type));
        document.add(new Paragraph("Customer Code: " + customerCode));

        document.close();

        return byteArrayOutputStream.toByteArray();
    }

}
