package com.bharuwa.haritkranti.generatePDFReport;


import com.bharuwa.haritkranti.models.User;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.*;

import java.awt.*;
import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static com.bharuwa.haritkranti.generatePDFReport.GeneratePdfReport.convertDate;

/**
 * @author harman
 */
public class OrganicCertificatePDFReport {

    public static ByteArrayInputStream getCertificate(User user, String khasraList, BigDecimal totalArea, String certificateCode) throws IOException, DocumentException {

        Rectangle rectangle = new Rectangle(550,700);

        Document document = new Document(rectangle);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

//        colors
        BaseColor myColor = WebColors.getRGBColor("#CBC9C9");
        BaseColor blueColor = WebColors.getRGBColor("#0F9BAF");
        BaseColor greenColor = WebColors.getRGBColor("#00963F");
        BaseColor lightGreyColor = WebColors.getRGBColor("#e6e6e6");
        BaseColor greyColor = WebColors.getRGBColor("#F1F1F1");
        BaseColor brownColor = WebColors.getRGBColor("#994d00");


//        font type
        Font regular = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.NORMAL);
        Font regularBold = new Font(Font.FontFamily.TIMES_ROMAN, 14,Font.BOLD);
        Font bold = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font largeBold = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD,brownColor);
        Font small = new Font(Font.FontFamily.TIMES_ROMAN, 12);
        Font sBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
        Font largeFont = new Font(Font.FontFamily.TIMES_ROMAN, 22,Font.BOLD);

//        font Color
        Font blueHeadColor = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, blueColor);
        Font greenHeadColor = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.NORMAL, greenColor);

//        hindi font
        BaseFont unicode = BaseFont.createFont("fonts/ARIALUNI.TTF",
                BaseFont.IDENTITY_H,    BaseFont.EMBEDDED);
        Font hindiFont=new Font(unicode,18,Font.BOLD);
        Font font=new Font(unicode,12,Font.NORMAL);
        Font bigHindiBold=new Font(unicode,22,Font.BOLD,brownColor);

        try {


            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            Paragraph paragraph = new Paragraph();
            paragraph.add("\n");
            document.add(paragraph);

            //Background Image
            PdfContentByte canvas = writer.getDirectContentUnder();
          //  Image image = Image.getInstance("Certificate-bg.jpg");
       //     image.scaleAbsolute(rectangle);
      //      image.setAbsolutePosition(0, 0);
    //        canvas.addImage(image);

            PdfPTable nTable = new PdfPTable(3);
            nTable.setWidthPercentage(100);
            nTable.setWidths(new int[]{5, 5, 5});

            PdfPCell ncell;

            ncell = new PdfPCell(new Phrase("\n\n\nLand Area: "+totalArea+" Acre",small));
            ncell.setBorder(Rectangle.NO_BORDER);
            ncell.setHorizontalAlignment(Element.ALIGN_CENTER);
            nTable.addCell(ncell);

     //       Image stamp = Image.getInstance("Annadata_stamp.png");
    //        stamp.scaleAbsolute(120f, 120f);
   //         ncell = new PdfPCell(stamp);
            ncell.setBorder(Rectangle.NO_BORDER);
            ncell.setHorizontalAlignment(Element.ALIGN_CENTER);
            nTable.addCell(ncell);

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime threeMonthsLater = now.plusDays(90);
            Date date = Date.from( threeMonthsLater.atZone( ZoneId.systemDefault()).toInstant());


            ncell = new PdfPCell(new Phrase("\n\n\nCertificate-No:\n"+certificateCode+""+
                    "\n\nValid up to: "+convertDate(date),small));
            ncell.setBorder(Rectangle.NO_BORDER);
            ncell.setHorizontalAlignment(Element.ALIGN_LEFT);
            nTable.addCell(ncell);
            nTable.setSpacingBefore(10);
            nTable.setSpacingAfter(10);
            document.add(nTable);


            paragraph.clear();
            Phrase ph = new Phrase();
            ph.add(new Chunk("\nWorld's First Digital Technological Parameters Based\n",bold));
            ph.add(new Chunk("\nORGANIC CERTIFICATE ",largeBold));
            paragraph.add(ph);
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(Chunk.NEWLINE);

            paragraph.clear();
            paragraph.add(new Phrase("This is to Certify that",largeFont));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(Chunk.NEWLINE);



            paragraph.clear();
            paragraph.add(new Phrase("Ms./Mr. "+user.getFirstName()+" "+user.getLastName()+",  "+"Son/ Daughter/ Wife, of Mr. "+user.getFatherName(),regularBold));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

            String villageName;
            if(user.getAddressModel().getVillageModel() != null){
                villageName = user.getAddressModel().getVillageModel().getName();
            } else {
                villageName = user.getAddressModel().getVillage();
            }

            paragraph.clear();
            paragraph.add(new Phrase("Resident of "+villageName+", "+"District "+user.getAddressModel().getCity().getName()+", "+ "State "+user.getAddressModel().getState().getName()+", "+"Country "+"India",regular));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(Chunk.NEWLINE);

            paragraph.clear();
            paragraph.add(new Phrase("of his/her farmland has undergone and completed soil inspection and\n"
                    +"other eligibility criteria to be declared and registered as an\n",regular));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(Chunk.NEWLINE);

            paragraph.clear();
            paragraph.add(new Phrase("\"ANNADATA ORGANIC FARMER.\"",greenHeadColor));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(Chunk.NEWLINE);

            PdfPTable qrTable = new PdfPTable(3);
            qrTable.setWidthPercentage(100);
            qrTable.setWidths(new int[]{5, 5, 5});
            qrTable.getDefaultCell().setFixedHeight(150f);

            PdfPCell qrCell;

            qrCell = new PdfPCell(new Phrase("\n\n\n\n"+convertDate(Calendar.getInstance().getTime())+"\n………………………\n" +
                    "Date\n\n\n\n\n",regular));
            qrCell.setBorder(Rectangle.NO_BORDER);
            qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            qrTable.addCell(qrCell);

    //        Image barcode = Image.getInstance("Annadata_QR.png");
    //        Image brCode = Image.getInstance("MyQRCode.png");
    //        brCode.scaleAbsolute(50f, 50f);
  //          barcode.scaleAbsolute(150f, 150f);
  //          qrCell = new PdfPCell(brCode);
  //          qrCell.setCellEvent(new ImageBackgroundEvent(barcode));
            qrCell.setBorder(Rectangle.NO_BORDER);
            qrCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            qrTable.addCell(qrCell);

            qrCell = new PdfPCell(new Phrase("\n\n\n\n\n………………………\n"+
                    "Authorised Signatory\n\n\n\n\n",regular));
            qrCell.setBorder(Rectangle.NO_BORDER);
            qrCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            qrTable.addCell(qrCell);
            qrTable.setSpacingBefore(10);
            qrTable.setSpacingAfter(10);
            document.add(qrTable);


            paragraph.clear();
            paragraph.add(new Phrase("Related Report is enclosed",small));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);
            document.add(Chunk.NEWLINE);


            document.newPage();
  //          canvas.addImage(image);

            Paragraph criteriaParagraph = new Paragraph();
            document.add(Chunk.NEWLINE);
            criteriaParagraph.add(new Phrase("\n   This certificate certifies that following khasra(s) is/are eligible for performing Organic Farming " +khasraList+"\n", regularBold));
            criteriaParagraph.setAlignment(Element.ALIGN_LEFT);
            criteriaParagraph.setIndentationLeft(20);
            document.add(criteriaParagraph);
            document.add(Chunk.NEWLINE);

            criteriaParagraph.clear();
            criteriaParagraph.add(new Phrase("Special Criteria :", regularBold));
            criteriaParagraph.setAlignment(Element.ALIGN_LEFT);
            criteriaParagraph.setIndentationLeft(20);
            document.add(criteriaParagraph);

            PdfPTable criteriaTable = new PdfPTable(3);
            criteriaTable.setWidthPercentage(100);
            criteriaTable.setWidths(new int[]{1, 7, 2});

            PdfPCell criteriacell;

            criteriacell = new PdfPCell(new Phrase("S.No", sBold));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Condition", sBold));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Status", sBold));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

//            criteria 1
            criteriacell = new PdfPCell(new Phrase("1.", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Flood in this year"));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Permitted", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

//            criteria 2
            criteriacell = new PdfPCell(new Phrase("2.", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Flood in alternate years/Flooded area"));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Not Permitted", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

//            criteria 3
            criteriacell = new PdfPCell(new Phrase("3.", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Land open on all sides & more than 1 acre"));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Permitted", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

//            criteria 4
            criteriacell = new PdfPCell(new Phrase("4.", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Land open on 3 sides along with buffer zone on 1 side & more than 3 acre"));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Permitted", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

//            criteria 5
            criteriacell = new PdfPCell(new Phrase("5.", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Land open on 2 sides along with buffer zone on 2 sides & more than 5 acre"));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Permitted", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

//            criteria 6
            criteriacell = new PdfPCell(new Phrase("6.", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Land open on 1 side along with buffer zone on 3 sides & more than 10 acre"));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Permitted", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

//            criteria 7
            criteriacell = new PdfPCell(new Phrase("7.", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Nearby heavy metal industries/industrial area"));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Not Permitted", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

//            criteria 8
            criteriacell = new PdfPCell(new Phrase("8.", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Boundary wall on four sides but not water logged area"));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Permitted", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

//            criteria 9
            criteriacell = new PdfPCell(new Phrase("9.", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Nearby sewage plant or sewage water affected field"));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Not Permitted", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

//            criteria 10
            criteriacell = new PdfPCell(new Phrase("10.", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("50 acre cluster with buffer zone"));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Permitted", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

//            criteria 11
            criteriacell = new PdfPCell(new Phrase("11.", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Virgin land (New cultivated land/ forest land/ pasture land)"));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_LEFT);
            criteriaTable.addCell(criteriacell);

            criteriacell = new PdfPCell(new Phrase("Permitted", small));
            criteriacell.setBorder(Rectangle.NO_BORDER);
            criteriacell.setHorizontalAlignment(Element.ALIGN_CENTER);
            criteriaTable.addCell(criteriacell);

            criteriaTable.setSpacingBefore(10);
            criteriaTable.setSpacingAfter(10);
            document.add(criteriaTable);
            
            document.close();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

}


















