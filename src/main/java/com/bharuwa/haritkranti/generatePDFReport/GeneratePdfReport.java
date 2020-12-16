package com.bharuwa.haritkranti.generatePDFReport;

import com.bharuwa.haritkranti.models.crops.Crop;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.*;
import com.bharuwa.haritkranti.exceptionHandler.ResourceNotFoundException;
import com.bharuwa.haritkranti.models.ReportHistory;
import com.bharuwa.haritkranti.models.payments.SoilTest;
import com.bharuwa.haritkranti.models.User;
import com.bharuwa.haritkranti.models.fertilizerModels.FarmingType;
import com.bharuwa.haritkranti.models.requestModels.NPKRecommendation;
import com.bharuwa.haritkranti.models.responseModels.FertilizerResponse;
import com.bharuwa.haritkranti.models.responseModels.MixReqFert;
import com.bharuwa.haritkranti.models.responseModels.OrganicReqFert;
import com.bharuwa.haritkranti.models.responseModels.POMFertCal;
import com.itextpdf.text.pdf.languages.DevanagariLigaturizer;
import com.itextpdf.text.pdf.languages.IndicLigaturizer;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author harman
 */
public class GeneratePdfReport {

    public static ByteArrayInputStream pdfReport(ReportHistory reportHistory, User user, Crop crop, String landImage, String villageImage) throws IOException, DocumentException {

        if (user == null) {
            throw new ResourceNotFoundException("Invalid UserId");
        }

        //        colors
        BaseColor myColor = WebColors.getRGBColor("#CBC9C9");
        BaseColor blueColor = WebColors.getRGBColor("#0F9BAF");
        BaseColor greenColor = WebColors.getRGBColor("#00963F");
        BaseColor lightGreyColor = WebColors.getRGBColor("#e6e6e6");
        BaseColor greyColor = WebColors.getRGBColor("#F1F1F1");

//        font type
        Font regular = new Font(Font.FontFamily.HELVETICA, 11,Font.NORMAL);
        Font bold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font smallBold = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

//        font Color
        Font blueHeadColor = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, blueColor);
        Font greenHeadColor = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, greenColor);

//        hindi font
        BaseFont unicode = BaseFont.createFont("fonts/ARIALUNI.TTF",
                BaseFont.IDENTITY_H ,BaseFont.EMBEDDED);
        Font hindiFont=new Font(unicode,16,Font.BOLD,blueColor);
        Font hindiFontSize14=new Font(unicode,16,Font.BOLD,blueColor);

        Font font=new Font(unicode,12,Font.NORMAL);
        Font hindiBold=new Font(unicode,12,Font.BOLD);

        Rectangle rect = new Rectangle(PageSize.A4);
        rect.setBorder(Rectangle.BOX);
        rect.setBorderColor(blueColor);
        rect.setBorderWidth(2);


        Document document = new Document(rect);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        NPKRecommendation npkRecommendation = reportHistory.getNpkRecommendation();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            PdfContentByte canvas = writer.getDirectContent();
            canvas.rectangle(rect);
            canvas.stroke();

            Paragraph paragraph = new Paragraph();

            try {
                PdfPTable nTable = new PdfPTable(3);
                nTable.setWidthPercentage(100);
                nTable.setWidths(new int[]{2, 6, 2});

                PdfPCell ncell;

                Image image = Image.getInstance("D:/HaritkrantiLogoBySushil.png");
                image.scaleAbsolute(100f, 100f);
                // COMMENTED BY SONU SINGH               
                ncell = new PdfPCell(image);
                ncell.setBorder(Rectangle.NO_BORDER);
                ncell.setHorizontalAlignment(Element.ALIGN_LEFT);
          //      ncell.setBackgroundColor(blueColor);
                nTable.addCell(ncell);

                String strCode;
                if(!StringUtils.isEmpty(reportHistory.getSoilReportNumber())){
                    strCode = reportHistory.getSoilReportNumber();
                } else {
                    strCode = "";
                }

                Paragraph p1 = new Paragraph("Customer Support",bold);
                p1.setAlignment(Element.ALIGN_RIGHT);
                Paragraph p2 = new Paragraph("Ph: +91XXXXXXXXXX");
                p2.setAlignment(Element.ALIGN_RIGHT);
              //  Paragraph p3 = new Paragraph("Report Number: "+strCode);
              //  p3.setAlignment(Element.ALIGN_RIGHT);
                Paragraph p4 = new Paragraph("Report Generated At: "+convertDateNew(reportHistory.getCreationDate()));
                p4.setAlignment(Element.ALIGN_RIGHT);


                ncell = new PdfPCell();
                ncell.addElement(p1);
                ncell.addElement(p2);
              //  ncell.addElement(p3);
                ncell.addElement(p4);
                ncell.setBorder(Rectangle.NO_BORDER);
                ncell.setHorizontalAlignment(Element.ALIGN_LEFT);
                nTable.addCell(ncell);

                String url;
                if(!StringUtils.isEmpty(user.getProfileImage())){
                    url = user.getProfileImage();
                } else {
                    url = "https://s3.ap-southeast-1.amazonaws.com/anndata-pics/1570625365586-icon.png";
                }
                Image profileImage = Image.getInstance(url);
                profileImage.scaleAbsolute(85f, 100f);
                ncell = new PdfPCell(profileImage);
                ncell.setBorder(Rectangle.NO_BORDER);
                ncell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            //    ncell.setBackgroundColor(blueColor);
                nTable.addCell(ncell);

                document.add(nTable);

            } catch(Exception e){
                e.printStackTrace();
            }

            String message = "\nHeartly Welcomes you as a Haritkranti Farmer";

            paragraph.clear();
            paragraph.add(new Phrase(message, hindiFont));
            paragraph.setAlignment(Element.ALIGN_CENTER);
            document.add(paragraph);

            PdfPTable pTable = new PdfPTable(2);
            pTable.setWidthPercentage(100);
            pTable.setWidths(new int[]{6,4});

            PdfPCell pcell;

            pcell = new PdfPCell(new Phrase("Customer Details :" ,bold));
            pcell.setBorder(Rectangle.NO_BORDER);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pTable.addCell(pcell);

            pcell = new PdfPCell(new Phrase("Description :",bold));
            pcell.setBorder(Rectangle.NO_BORDER);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pTable.addCell(pcell);

            Phrase ph = new Phrase();
            ph.add(new Chunk("Name : ",bold));
            ph.add(new Chunk(user.getFirstName()+" "+user.getLastName(),regular));
            pcell = new PdfPCell(ph);
            pcell.setBorder(Rectangle.NO_BORDER);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pTable.addCell(pcell);

            Phrase ph1 = new Phrase();
            ph1.add(new Chunk("Khasra No : ",bold));
            if(!StringUtils.isEmpty(npkRecommendation.getKhasraNo())) {
                ph1.add(new Chunk(npkRecommendation.getKhasraNo(), regular));
            }
            pcell = new PdfPCell(ph1);
            pcell.setBorder(Rectangle.NO_BORDER);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pTable.addCell(pcell);

         /*   Phrase ph2 = new Phrase();
            ph2.add(new Chunk("Father Name : ",bold));
            ph2.add(new Chunk(user.getFatherName(),regular));
            pcell = new PdfPCell(ph2);
            pcell.setBorder(Rectangle.NO_BORDER);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pTable.addCell(pcell);*/
            
            Phrase ph4 = new Phrase();
            ph4.add(new Chunk("Mobile No : ",bold));
            ph4.add(new Chunk(user.getPrimaryPhone(),regular));
            pcell = new PdfPCell(ph4);
            pcell.setBorder(Rectangle.NO_BORDER);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pTable.addCell(pcell);
            
            Phrase ph9 = new Phrase();
            ph9.add(new Chunk("Estimated Yield : ",bold));
            ph9.add(new Chunk(String.valueOf(crop.getCropType()),regular));
            pcell = new PdfPCell(ph9);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);
            
            String villageName;
            if(user.getAddressModel().getVillageModel() != null){
                villageName = user.getAddressModel().getVillageModel().getName();
            } else {
                villageName = user.getAddressModel().getVillage();
            }
            
            Phrase ph8 = new Phrase();
            ph8.add(new Chunk("Village : ",bold));
            ph8.add(new Chunk(villageName,regular));
            pcell = new PdfPCell(ph8);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);
            
            Phrase ph11 = new Phrase();
            ph11.add(new Chunk("Crop : ",bold));
            ph11.add(new Chunk(crop.getCropName(),regular));
            pcell = new PdfPCell(ph11);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);

            Phrase ph10 = new Phrase();
            ph10.add(new Chunk("Tehsil : ",bold));
            ph10.add(new Chunk(user.getAddressModel().getCity().getName(),regular));
            pcell = new PdfPCell(ph10);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);
            
            Phrase ph12 = new Phrase();
            ph12.add(new Chunk("Date of Sowing : ",bold));
            ph12.add(new Chunk(user.getAddressModel().getCity().getName(),regular));
            pcell = new PdfPCell(ph12);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);
            
            Phrase ph14 = new Phrase();
            ph14.add(new Chunk("District : ",bold));
            ph14.add(new Chunk(user.getAddressModel().getCity().getName(),regular));
            pcell = new PdfPCell(ph14);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);
            
            Phrase ph5 = new Phrase();
            ph5.add(new Chunk("Farm-Area : ",bold));
            ph5.add(new Chunk(npkRecommendation.getFieldSize() + " " + npkRecommendation.getFieldSizeType(),regular));
            pcell = new PdfPCell(ph5);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);
            
            Phrase ph15 = new Phrase();
            ph15.add(new Chunk("State : ",bold));
            ph15.add(new Chunk(user.getAddressModel().getState().getName(),regular));
            pcell = new PdfPCell(ph15);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);
            
      /*      Phrase ph13 = new Phrase();
            ph13.add(new Chunk("Crop Type : ",bold));
            ph13.add(new Chunk(String.valueOf(crop.getCropType()),regular));
            pcell = new PdfPCell(ph13);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);*/
            
            Phrase ph7 = new Phrase();
            ph7.add(new Chunk("Farming-Type : ",bold));
            ph7.add(new Chunk(npkRecommendation.getFarmingType().toString(),regular));
            pcell = new PdfPCell(ph7);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);
            
            Phrase ph3 = new Phrase();
            ph3.add(new Chunk("Farm-Type : ",bold));
            ph3.add(new Chunk(npkRecommendation.getYieldType().getType().toString(),regular));
            pcell = new PdfPCell(ph3);
            pcell.setBorder(Rectangle.NO_BORDER);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pTable.addCell(pcell);
            
            String address;
            if(user.getAddressModel().getAddress() != null && !StringUtils.isEmpty(user.getAddressModel().getAddress())){
                address = user.getAddressModel().getAddress();
            } else {
                address = "";
            }
         /*   Phrase ph6 = new Phrase();
            ph6.add(new Chunk("Address : ",bold));
            ph6.add(new Chunk(address,regular));
            pcell = new PdfPCell(ph6);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);*/


//            village Name from old data



//            Phrase ph13 = new Phrase();
//            ph13.add(new Chunk("State : ",bold));
//            ph13.add(new Chunk(user.getAddressModel().getState().getName(),regular));
//            pcell = new PdfPCell(ph13);
//            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
//            pcell.setBorder(Rectangle.NO_BORDER);
//            pTable.addCell(pcell);


            pTable.setSpacingBefore(20);
            document.add(pTable);
            pTable.setSpacingAfter(30);
            document.add(Chunk.NEWLINE);


            if(npkRecommendation.getFarmingType().equals(FarmingType.Type.Organic)) {
                paragraph.clear();
                paragraph.add(new Phrase("1. The Organic cultivated land of the farmer, and his field and village is shown through GPS and GEO fencing.", font));
                paragraph.setAlignment(Element.ALIGN_LEFT);
                document.add(paragraph);
            }else {
                paragraph.clear();
                paragraph.add(new Phrase("Your Land and Your Village\n", hindiFontSize14));
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);
            }

            PdfPTable geoTable = new PdfPTable(2);
            geoTable.setWidthPercentage(100);
            geoTable.setWidths(new int[]{6, 4});

            PdfPCell geoCell;
            Phrase villagePhrase = new Phrase();
            villagePhrase.add(new Chunk("Geo-fencing of Village",hindiBold));
            villagePhrase.add(new Chunk("\n"));
            geoCell = new PdfPCell(villagePhrase);
            geoCell.setBorder(Rectangle.NO_BORDER);
            geoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            geoTable.addCell(geoCell);

            Phrase fieldPhrase = new Phrase();
            fieldPhrase.add(new Chunk("Geo-fencing of Field\n",hindiBold));
            fieldPhrase.add(new Chunk("\n"));
            geoCell = new PdfPCell(fieldPhrase);
            geoCell.setBorder(Rectangle.NO_BORDER);
            geoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            geoTable.addCell(geoCell);

            if(StringUtils.isEmpty(villageImage)) {
                villageImage = "https://s3.ap-southeast-1.amazonaws.com/anndata-pics/1574418029341-no-image-available-icon-6.jpg";
            }

            if(StringUtils.isEmpty(landImage)) {
                landImage = "https://s3.ap-southeast-1.amazonaws.com/anndata-pics/1574418029341-no-image-available-icon-6.jpg";
            }

            String url1 = villageImage;
            Image villagePic = Image.getInstance(url1);
            villagePic.scaleAbsolute(310f, 180f);
            geoCell = new PdfPCell(villagePic);
            geoCell.setBorder(Rectangle.NO_BORDER);
            geoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            geoTable.addCell(geoCell);

            String url2 = landImage;
            Image fieldImage = Image.getInstance(url2);
            fieldImage.scaleAbsolute(200f, 180f);
            geoCell = new PdfPCell(fieldImage);
            geoCell.setBorder(Rectangle.NO_BORDER);
            geoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            geoTable.addCell(geoCell);

            geoTable.setSpacingBefore(10);
            document.add(geoTable);
            geoTable.setSpacingAfter(30);



            if(npkRecommendation.getSoilTest() != null) {

                if(npkRecommendation.getFarmingType().equals(FarmingType.Type.Organic)) {
                    paragraph.clear();
                    paragraph.add(new Phrase("\n\n2. Soil testing has been done by the farmer before organic farming, his report is attached-\n", font));
                    paragraph.setAlignment(Element.ALIGN_LEFT);
                    document.add(paragraph);
                }
                else {
                    paragraph.clear();
                  //  paragraph.add(new Phrase("\n\nYour soil testing report are as follows-\n\n", font));
                  //  paragraph.setAlignment(Element.ALIGN_LEFT);
                    document.add(paragraph);

                    Chunk blueText = new Chunk("\nSoil Test Report ( "+convertDate(Calendar.getInstance().getTime())+" )\n", blueHeadColor);

                    paragraph.clear();
                    paragraph.add(blueText);
                    paragraph.setAlignment(Element.ALIGN_CENTER);
                    document.add(paragraph);

                    paragraph.clear();
               //     paragraph.add("As per the testing of your soil with Dharti Ka Doctor(Soil Testing Kit)\n" +
               //             "The nutrients present in your Farm are as followed:");
                    paragraph.setAlignment(Element.ALIGN_CENTER);
                    document.add(paragraph);
                }


                SoilTest soilTest = npkRecommendation.getSoilTest();

                Map<String, String> map = new LinkedHashMap<>();
                map.put("Nitrogen", soilTest.getnValue()+" kg");
                map.put("Phosphrous", soilTest.getpValue()+" kg");
                map.put("Potash", soilTest.getkValue()+" kg");
                map.put("Organic Carbon", soilTest.getOrganicCarbon()+" %");
                map.put("pH", soilTest.getpHValue()+" ");

                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setWidths(new int[]{1, 5, 5});

                PdfPCell hcell;
                hcell = new PdfPCell(new Phrase("Sr.", bold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                hcell = new PdfPCell(new Phrase("Soil-Parameters", bold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                hcell = new PdfPCell(new Phrase("Values", bold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                Integer sr = 0;

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    sr++;
                    PdfPCell cell;

                    BaseColor color = greyColor;
                    if (sr % 2 == 0){
                        color = lightGreyColor;
                    }

                    cell = new PdfPCell(new Phrase(sr.toString()));
                    cell.setPaddingLeft(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(color);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(entry.getKey(),font));
                    cell.setPaddingLeft(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(color);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(entry.getValue()),font));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                }
                table.setSpacingBefore(10);
                document.add(table);
            }

            document.newPage();
            canvas.stroke();

            if(!reportHistory.getFertilizerResponse().isEmpty() && npkRecommendation.getFarmingType().equals(FarmingType.Type.Chemical)) {

                paragraph.clear();
                paragraph.add(new Phrase(" You have decided for chemical farming in your farm. For this, use the following fertilizer.\n", font));
                paragraph.setAlignment(Element.ALIGN_LEFT);
                document.add(paragraph);

                Chunk greenText = new Chunk("Fertilizer Recommendation for "+npkRecommendation.getFieldSize() +npkRecommendation.getFieldSizeType(), greenHeadColor);

                document.add(Chunk.NEWLINE);
                paragraph.clear();
                paragraph.add(greenText);
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);

                paragraph.clear();
     //           paragraph.add("As per your choice to perform Chemical\n"+
     //                   "in your farm, the recommendations are as follows:");
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);

                List<FertilizerResponse> fertilizerResponses = reportHistory.getFertilizerResponse();

                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                table.setWidths(new int[]{2, 5, 5, 5});

                PdfPCell hcell;
                hcell = new PdfPCell(new Phrase("S.NO", bold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                hcell = new PdfPCell(new Phrase("Fertilizer-Name", bold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                hcell = new PdfPCell(new Phrase("Fertilizer-Type", bold));
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                hcell = new PdfPCell(new Phrase("Requirement", bold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                Integer sr = 0;

                for (FertilizerResponse response : fertilizerResponses) {

                    int requirment = (int) response.getRequirement();

                    sr++;
                    PdfPCell cell;

                    BaseColor color = greyColor;
                    if (sr % 2 == 0){
                        color = lightGreyColor;
                    }

                    cell = new PdfPCell(new Phrase(sr.toString()));
                    cell.setPaddingLeft(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(color);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(response.getFertilizerName()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(response.getFertylizerType()).trim()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(requirment+" "+response.getUnit()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                }
                table.setSpacingBefore(10);
                document.add(table);

            }
            if(reportHistory.getOrganicReqFert() != null && npkRecommendation.getFarmingType().equals(FarmingType.Type.Organic)) {

                paragraph.clear();
                paragraph.add(new Phrase("3. According to the soil testing, the above fields have been recommended to use the necessary fertilizers required for organic farming (this is displayed as the estimated manure applied by the farmer.)", font));
                paragraph.setAlignment(Element.ALIGN_LEFT);
                document.add(paragraph);

                OrganicReqFert organicReqFert = reportHistory.getOrganicReqFert();
                List<POMFertCal> pomFertCals = organicReqFert.getPomFertCals();

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                table.setWidths(new int[]{2,5, 5, 5, 5});

                PdfPCell hcell;
                hcell = new PdfPCell(new Phrase("Sr.", bold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                hcell = new PdfPCell(new Phrase("Fertilizer Name", bold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                hcell = new PdfPCell(new Phrase("Fertilizer Type", bold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                hcell = new PdfPCell(new Phrase("Category Type", bold));
                hcell.setBackgroundColor(myColor);
                hcell.setPaddingLeft(5);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                hcell = new PdfPCell(new Phrase("Requirement", bold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                Integer sr = 0;

                for (POMFertCal pomFertCal : pomFertCals) {

                    String  requirment =  pomFertCal.getRequiredFert().toBigInteger().toString();

                    sr++;
                    PdfPCell cell;

                    BaseColor color = greyColor;
                    if (sr % 2 == 0){
                        color = lightGreyColor;
                    }

                    cell = new PdfPCell(new Phrase(sr.toString()));
                    cell.setPaddingLeft(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(color);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(pomFertCal.getFertilizerName()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(pomFertCal.getFertilizerType())));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(pomFertCal.getCategoryType())));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(requirment+" "+pomFertCal.getUnit()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                }
                table.setSpacingBefore(10);
                document.add(table);
                document.add(Chunk.NEWLINE);

            }

            if(reportHistory.getMixReqFert() != null && npkRecommendation.getFarmingType().equals(FarmingType.Type.INM_MIX)) {

                paragraph.clear();
                paragraph.add(new Phrase(" You have decided to perform INM_MIX farming in your farm. For this, use the following fertilizer / chemicals:\n", font));
                paragraph.setAlignment(Element.ALIGN_LEFT);
                document.add(paragraph);

                Chunk greenText = new Chunk("Fertilizer Recommendation for "+npkRecommendation.getFieldSize() +npkRecommendation.getFieldSizeType(), greenHeadColor);

                document.add(Chunk.NEWLINE);
                paragraph.clear();
                paragraph.add(greenText);
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);

                paragraph.clear();
                paragraph.add("As per your choice to perform INM_MIX farming, use the following\n\n"+
                        "CHEMICAL Fertilizers:");
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);

                Set<FertilizerResponse> fertilizerResponses = reportHistory.getMixReqFert().getFertilizerResponseList();

                PdfPTable chemicalTable = new PdfPTable(4);
                chemicalTable.setWidthPercentage(100);
                chemicalTable.setWidths(new int[]{2, 5, 5, 5});

                PdfPCell hcell1;
                hcell1 = new PdfPCell(new Phrase("Sr.", bold));
                hcell1.setPaddingLeft(5);
                hcell1.setBackgroundColor(myColor);
                hcell1.setBorder(Rectangle.NO_BORDER);
                hcell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                chemicalTable.addCell(hcell1);

                hcell1 = new PdfPCell(new Phrase("Fertilizer-Name", bold));
                hcell1.setPaddingLeft(5);
                hcell1.setBackgroundColor(myColor);
                hcell1.setBorder(Rectangle.NO_BORDER);
                hcell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                chemicalTable.addCell(hcell1);

                hcell1 = new PdfPCell(new Phrase("Fertilizer-Type", bold));
                hcell1.setPaddingLeft(5);
                hcell1.setBackgroundColor(myColor);
                hcell1.setBorder(Rectangle.NO_BORDER);
                hcell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                chemicalTable.addCell(hcell1);

                hcell1 = new PdfPCell(new Phrase("Requirement", bold));
                hcell1.setPaddingLeft(5);
                hcell1.setBackgroundColor(myColor);
                hcell1.setBorder(Rectangle.NO_BORDER);
                hcell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                chemicalTable.addCell(hcell1);

                int sr1 = 0;

                for (FertilizerResponse response : fertilizerResponses) {

                    int requirment = 0;
                    requirment = (int) response.getRequirement();

                    sr1++;
                    PdfPCell cell;

                    BaseColor color = greyColor;
                    if (sr1 % 2 == 0){
                        color = lightGreyColor;
                    }

                    cell = new PdfPCell(new Phrase(Integer.toString(sr1)));
                    cell.setPaddingLeft(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(color);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    chemicalTable.addCell(cell);

                    cell = new PdfPCell(new Phrase(response.getFertilizerName()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    chemicalTable.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(response.getFertylizerType()).trim()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    chemicalTable.addCell(cell);

                    cell = new PdfPCell(new Phrase(requirment+" "+response.getUnit()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    chemicalTable.addCell(cell);
                }
                chemicalTable.setSpacingBefore(10);
                document.add(chemicalTable);


                document.add(Chunk.NEWLINE);
                paragraph.clear();
                paragraph.add("ORGANIC Fertilizers:");
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);

                MixReqFert mixReqFert = reportHistory.getMixReqFert();
                List<POMFertCal> pomFertCals = mixReqFert.getOrganicReqFert().getPomFertCals();


                PdfPTable organicTable = new PdfPTable(5);
                organicTable.setWidthPercentage(100);
                organicTable.setWidths(new int[]{2,5, 5, 5, 5});


                PdfPCell hcell;
                hcell = new PdfPCell(new Phrase("Sr.", bold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                organicTable.addCell(hcell);

                hcell = new PdfPCell(new Phrase("Fertilizer-Name", bold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                organicTable.addCell(hcell);

                hcell = new PdfPCell(new Phrase("Fertilizer-Type", bold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                organicTable.addCell(hcell);

                hcell = new PdfPCell(new Phrase("Category-Type", bold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                organicTable.addCell(hcell);

                hcell = new PdfPCell(new Phrase("Requirement", bold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                organicTable.addCell(hcell);

                Integer sr = 0;

                for (POMFertCal pomFertCal : pomFertCals) {

                    String  requirment =  pomFertCal.getRequiredFert().toBigInteger().toString();

                    sr++;
                    PdfPCell cell;

                    BaseColor color = greyColor;
                    if (sr % 2 == 0){
                        color = lightGreyColor;
                    }

                    cell = new PdfPCell(new Phrase(sr.toString()));
                    cell.setPaddingLeft(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(color);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    organicTable.addCell(cell);

                    cell = new PdfPCell(new Phrase(pomFertCal.getFertilizerName()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    organicTable.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(pomFertCal.getFertilizerType())));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    organicTable.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(pomFertCal.getCategoryType())));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    organicTable.addCell(cell);

                    cell = new PdfPCell(new Phrase(requirment+" "+pomFertCal.getUnit()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    organicTable.addCell(cell);
                }
                organicTable.setSpacingBefore(10);
                document.add(organicTable);

            }

            if(npkRecommendation.getFarmingType().equals(FarmingType.Type.Organic)) {
                PdfPTable newTable = new PdfPTable(1);
                newTable.setWidthPercentage(100);

                PdfPCell newCell;
                newCell = new PdfPCell(new Phrase());
                newCell.setBorder(Rectangle.NO_BORDER);
                newCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                newTable.addCell(newCell);

                Phrase phrase1 = new Phrase();
                phrase1.add(new Chunk("Other Special Criteria:\n" +
                        "\n" +
                        "The prevailing Organic standard â€” \n\n" ,hindiBold));
                phrase1.add(new Chunk("1.     According to PGS standard, the cluster (Group Farming) has been given Organic recognition where the minimum of 50 acres of land is available at a single place.\n\n" +
                        "         (A) If the organic field is opened from 2 sides (i.e., it is covered with a forest, river, barren land etc.), then if there is more than 10 acres of farmland, it is recognized as a special organic produce.\n\n" +
                        "         (B) If the organic field is open from 3 sides (i.e., it is covered with a forest, river, barren land etc.), then if there is more than 5 acres of farmland, it is recognized as a special organic production.\n\n" +
                        "         (C) If the organic farm is open all around (i.e., it is covered with forest, river, barren land etc.), then if there is more than 1 acre farmland, it is recognized as special organic produce.\n\n",font));
                phrase1.add(new Chunk("2.     Third Party-\n",font));
                phrase1.add(new Chunk("     As Third Party Inspection, every produce of the farmer's farm has been recognized by our representative (agent) as \"Organic Farming\" only after the on-site terrestrial inspection.\n\n",font));
                phrase1.add(new Chunk("3.     The seed is not treated by GMO or any chemical.\n\n",font));
                phrase1.add(new Chunk("4.     Care has been taken to have a limited amount of metal industry in the surrounding area or heavy metals and other harmful elements in the ground and water (Lead (Pb), Arsenic (As) etc.\n\n",font));
                phrase1.add(new Chunk("5.     Care should be taken to ensure that there is no flooded land or chemically treated land in the vicinity.\n\n",font));
                phrase1.add(new Chunk("Note:",hindiBold));
                phrase1.add(new Chunk(" This certificate certifies that the land is free from chemical residues and other heavy metals water pollution etc. Only one crop grown by the fanner has been certified as \"Annadata Organic Farming\". It originates from the \"Annadata Organic Solution System\" by analyzing all the above parameters in an automatic method.\n\n",font));
                phrase1.add(new Chunk(" * After the preparation of above organic fanning according to the prescribed standards, your Organic Certificate automatically generated in your Anndata App.",font));

                newCell = new PdfPCell(phrase1);
                newCell.setBorder(Rectangle.NO_BORDER);
                newCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                newTable.addCell(newCell);
                document.add(newTable);

            } else {
                paragraph.clear();
                paragraph.add(new Phrase("\n We believe that with our cooperation and advice, your cost of farming will decrease and income will increase. We wish you a happy life and happy family.\n" +
                        "\n" +
                        " \nThank you" +
                        "\n", font));
                paragraph.setAlignment(Element.ALIGN_LEFT);
                document.add(paragraph);
            }

            document.close();

        } catch (DocumentException ex) {

            Logger.getLogger(GeneratePdfReport.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private static String convertDateNew(Date dateStr){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
        return formatter.format(dateStr);
    }

    public static String convertDate(Date dateStr){
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.format(dateStr);

    }





    public static ByteArrayInputStream pdfReportHindi(ReportHistory reportHistory, User user, Crop crop, String landImage, String villageImage) throws IOException, DocumentException {

        if (user == null) {
            throw new ResourceNotFoundException("Invalid UserId");
        }

        //        colors
        BaseColor myColor = WebColors.getRGBColor("#CBC9C9");
        BaseColor blueColor = WebColors.getRGBColor("#0F9BAF");
        BaseColor greenColor = WebColors.getRGBColor("#00963F");
        BaseColor lightGreyColor = WebColors.getRGBColor("#e6e6e6");
        BaseColor greyColor = WebColors.getRGBColor("#F1F1F1");

//        font type
        Font regular = new Font(Font.FontFamily.HELVETICA, 11,Font.NORMAL);
        Font bold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font smallBold = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

//        font Color
        Font blueHeadColor = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, blueColor);
        Font greenHeadColor = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, greenColor);

//        hindi font
        BaseFont unicode = BaseFont.createFont("fonts/ARIALUNI.TTF",
                BaseFont.IDENTITY_H ,BaseFont.EMBEDDED);
        Font hindiFont=new Font(unicode,16,Font.BOLD,blueColor);
        Font hindiFontSize14=new Font(unicode,16,Font.BOLD,blueColor);

        Font font=new Font(unicode,12,Font.NORMAL);
        Font hindiBold=new Font(unicode,12,Font.BOLD);

        Rectangle rect = new Rectangle(PageSize.A4);
        rect.setBorder(Rectangle.BOX);
        rect.setBorderColor(blueColor);
        rect.setBorderWidth(2);

        IndicLigaturizer hindi = new DevanagariLigaturizer();

        Document document = new Document(rect);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        NPKRecommendation npkRecommendation = reportHistory.getNpkRecommendation();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            PdfContentByte canvas = writer.getDirectContent();
            canvas.rectangle(rect);
            canvas.stroke();

            Paragraph paragraph = new Paragraph();

            try {
                PdfPTable nTable = new PdfPTable(3);
                nTable.setWidthPercentage(100);
                nTable.setWidths(new int[]{2, 6, 2});

                PdfPCell ncell;

                Image image = Image.getInstance("Annadata logo.png");
                image.scaleAbsolute(100f, 100f);
                // logo added
                ncell = new PdfPCell(image);
                ncell.setBorder(Rectangle.NO_BORDER);
                ncell.setHorizontalAlignment(Element.ALIGN_LEFT);
                nTable.addCell(ncell);

                // Soil Test Report Number
                String strCode;
                if(!StringUtils.isEmpty(reportHistory.getSoilReportNumber())){
                    strCode = reportHistory.getSoilReportNumber();
                } else {
                    strCode = "";
                }

                Paragraph p1 = new Paragraph(hindi.process("à¤—à¥?à¤°à¤¾à¤¹à¤• à¤¸à¤¹à¤¾à¤¯à¤¤à¤¾"),font);
                p1.setAlignment(Element.ALIGN_RIGHT);
                Paragraph p2 = new Paragraph("Ph: +91XXXXXXXXXX\n\n");
                p2.setAlignment(Element.ALIGN_RIGHT);
                Paragraph p3 = new Paragraph(hindi.process("à¤°à¤¿à¤ªà¥‹à¤°à¥?à¤Ÿ à¤¨à¤‚à¤¬à¤° : ")+strCode,font);
                p3.setAlignment(Element.ALIGN_RIGHT);
                Paragraph p4 = new Paragraph(hindi.process("à¤°à¤¿à¤ªà¥‹à¤°à¥?à¤Ÿ à¤œà¤¨à¤°à¥‡à¤Ÿ : ")+convertDateNew(reportHistory.getCreationDate()),font);
                p4.setAlignment(Element.ALIGN_RIGHT);


                ncell = new PdfPCell();
                ncell.addElement(p1);
                ncell.addElement(p2);
                ncell.addElement(p3);
                ncell.addElement(p4);
                ncell.setBorder(Rectangle.NO_BORDER);
                ncell.setHorizontalAlignment(Element.ALIGN_LEFT);
                nTable.addCell(ncell);

                String url;
                if(!StringUtils.isEmpty(user.getProfileImage())){
                    url = user.getProfileImage();
                } else {
                    url = "https://s3.ap-southeast-1.amazonaws.com/anndata-pics/1570625365586-icon.png";
                }
                Image profileImage = Image.getInstance(url);
                profileImage.scaleAbsolute(85f, 100f);
                ncell = new PdfPCell(profileImage);
                ncell.setBorder(Rectangle.NO_BORDER);
                ncell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                nTable.addCell(ncell);

                document.add(nTable);

            } catch(Exception e){
                e.printStackTrace();
            }

            Image headImage = Image.getInstance("AnnadataHeading.png");
            headImage.scaleAbsolute(400f, 30f);

            PdfPTable headingTable = new PdfPTable(1);
            headingTable.setWidthPercentage(100);
            PdfPCell headingcell;

            headingcell = new PdfPCell(headImage);
            headingcell.setBorder(Rectangle.NO_BORDER);
            headingcell.setHorizontalAlignment(Element.ALIGN_CENTER);
            headingTable.addCell(headingcell);
            headingTable.setSpacingBefore(20);
            document.add(headingTable);

//            String message = "\nà¤…à¤¨à¥?à¤¨à¤¦à¤¾à¤¤à¤¾ à¤•à¥ƒà¤·à¤• à¤•à¥‡ à¤°à¥‚à¤ª à¤†à¤ªà¤•à¤¾ à¤¹à¤¾à¤°à¥?à¤¦à¤¿à¤• à¤¸à¥?à¤µà¤¾à¤—à¤¤ à¤¹à¥ˆà¤‚ ";


//            String result = hindi.process(message);
//
//            paragraph.clear();
//            paragraph.add(new Phrase(result, hindiFont));
//            paragraph.add(image);
//            paragraph.setAlignment(Element.ALIGN_CENTER);
//            document.add(paragraph);

            PdfPTable pTable = new PdfPTable(2);
            pTable.setWidthPercentage(100);
            pTable.setWidths(new int[]{6, 4});

            PdfPCell pcell;

            pcell = new PdfPCell(new Phrase(hindi.process("à¤—à¥?à¤°à¤¾à¤¹à¤• à¤µà¤¿à¤µà¤°à¤£ :") ,hindiBold));
            pcell.setBorder(Rectangle.NO_BORDER);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pTable.addCell(pcell);

            pcell = new PdfPCell(new Phrase(hindi.process("à¤µà¤¿à¤µà¤°à¤£ :"),hindiBold));
            pcell.setBorder(Rectangle.NO_BORDER);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pTable.addCell(pcell);

            Phrase ph = new Phrase();
            ph.add(new Chunk(hindi.process("à¤¨à¤¾à¤® : "),font));
            ph.add(new Chunk(user.getFirstName()+" "+user.getLastName(),regular));
            pcell = new PdfPCell(ph);
            pcell.setBorder(Rectangle.NO_BORDER);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pTable.addCell(pcell);

            Phrase ph1 = new Phrase();
            ph1.add(new Chunk(hindi.process("à¤–à¤¸à¤°à¤¾ à¤¨à¤‚à¤¬à¤° : "),font));
            if(!StringUtils.isEmpty(npkRecommendation.getKhasraNo())) {
                ph1.add(new Chunk(npkRecommendation.getKhasraNo(), regular));
            }
            pcell = new PdfPCell(ph1);
            pcell.setBorder(Rectangle.NO_BORDER);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pTable.addCell(pcell);

            Phrase ph2 = new Phrase();
            ph2.add(new Chunk(hindi.process("à¤ªà¤¿à¤¤à¤¾ à¤•à¤¾ à¤¨à¤¾à¤® : "),font));
            ph2.add(new Chunk(user.getFatherName(),regular));
            pcell = new PdfPCell(ph2);
            pcell.setBorder(Rectangle.NO_BORDER);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pTable.addCell(pcell);

            Phrase ph3 = new Phrase();
            ph3.add(new Chunk(hindi.process("à¤­à¥‚à¤®à¤¿ à¤ªà¥?à¤°à¤•à¤¾à¤° : "),font));
            ph3.add(new Chunk(npkRecommendation.getYieldType().getType().toString(),regular));
            pcell = new PdfPCell(ph3);
            pcell.setBorder(Rectangle.NO_BORDER);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pTable.addCell(pcell);

            Phrase ph4 = new Phrase();
            ph4.add(new Chunk(hindi.process("à¤®à¥‹à¤¬à¤¾à¤‡à¤² à¤¨à¤‚à¤¬à¤° : "),font));
            ph4.add(new Chunk(user.getPrimaryPhone(),regular));
            pcell = new PdfPCell(ph4);
            pcell.setBorder(Rectangle.NO_BORDER);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pTable.addCell(pcell);

            Phrase ph5 = new Phrase();
            ph5.add(new Chunk(hindi.process("à¤–à¥‡à¤¤à¥€ à¤•à¥?à¤·à¥‡à¤¤à¥?à¤° : "),font));
            ph5.add(new Chunk(npkRecommendation.getFieldSize() + " " + npkRecommendation.getFieldSizeType(),regular));
            pcell = new PdfPCell(ph5);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);

            String address;
            if(user.getAddressModel().getAddress() != null && !StringUtils.isEmpty(user.getAddressModel().getAddress())){
                address = user.getAddressModel().getAddress();
            } else {
                address = "";
            }
            Phrase ph6 = new Phrase();
            ph6.add(new Chunk(hindi.process("à¤ªà¤¤à¤¾ : "),font));
            ph6.add(new Chunk(address,regular));
            pcell = new PdfPCell(ph6);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);

            Phrase ph7 = new Phrase();
            ph7.add(new Chunk(hindi.process("à¤–à¥‡à¤¤à¥€ à¤ªà¥?à¤°à¤•à¤¾à¤° : "),font));
            ph7.add(new Chunk(npkRecommendation.getFarmingType().toString(),regular));
            pcell = new PdfPCell(ph7);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);

//            village Name from old data
            String villageName;
            if(user.getAddressModel().getVillageModel() != null){
                villageName = user.getAddressModel().getVillageModel().getName();
            } else {
                villageName = user.getAddressModel().getVillage();
            }

            Phrase ph8 = new Phrase();
            ph8.add(new Chunk(hindi.process("à¤—à¤¾à¤‚à¤µ : "),font));
            ph8.add(new Chunk(villageName,regular));
            pcell = new PdfPCell(ph8);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);

            Phrase ph9 = new Phrase();
            ph9.add(new Chunk(hindi.process("à¤«à¤¸à¤² à¤ªà¥?à¤°à¤•à¤¾à¤° : "),font));
            ph9.add(new Chunk(String.valueOf(crop.getCropType()),regular));
            pcell = new PdfPCell(ph9);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);


            Phrase ph10 = new Phrase();
            ph10.add(new Chunk(hindi.process("à¤œà¤¿à¤²à¤¾ : "),font));
            ph10.add(new Chunk(user.getAddressModel().getCity().getName(),regular));
            pcell = new PdfPCell(ph10);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);

            Phrase ph11 = new Phrase();
            ph11.add(new Chunk(hindi.process("à¤«à¤¸à¤² : "),font));
            ph11.add(new Chunk(crop.getCropName(),regular));
            pcell = new PdfPCell(ph11);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);

            Phrase ph12 = new Phrase();
            ph12.add(new Chunk(hindi.process("à¤°à¤¾à¤œà¥?à¤¯ : "),font));
            ph12.add(new Chunk(user.getAddressModel().getState().getName(),regular));
            pcell = new PdfPCell(ph12);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);

            Phrase ph13 = new Phrase();
//            ph13.add(new Chunk("State : ",bold));
//            ph13.add(new Chunk(user.getAddressModel().getState().getName(),regular));
            pcell = new PdfPCell(ph13);
            pcell.setHorizontalAlignment(Element.ALIGN_LEFT);
            pcell.setBorder(Rectangle.NO_BORDER);
            pTable.addCell(pcell);


            pTable.setSpacingBefore(20);
            document.add(pTable);
            pTable.setSpacingAfter(30);
            document.add(Chunk.NEWLINE);

            PdfPTable point1Table = new PdfPTable(1);
            point1Table.setWidthPercentage(100);
            PdfPCell point1Cell;
            if(npkRecommendation.getFarmingType().equals(FarmingType.Type.Organic)) {

                Image point1Image = Image.getInstance("Point1.png");
                point1Image.scaleAbsolute(530f, 40f);

                point1Cell = new PdfPCell(point1Image);
                point1Cell.setBorder(Rectangle.NO_BORDER);
                point1Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                point1Table.addCell(point1Cell);
                point1Table.setSpacingBefore(20);
                document.add(point1Table);
//                paragraph.clear();
//                paragraph.add(new Phrase(hindi.process("1- à¤œà¤¿à¤¸ à¤­à¥‚à¤®à¤¿ à¤ªà¤° à¤•à¤¿à¤¸à¤¾à¤¨ à¤¨à¥‡ à¤œà¥ˆà¤µà¤¿à¤• à¤‰à¤¤à¥?à¤ªà¤¾à¤¦ à¤•à¥‹ à¤‰à¤ªà¤œà¤¾à¤¯à¤¾ à¤¹à¥ˆ à¤‰à¤¸à¤•à¥‡ à¤—à¤¾à¤?à¤µ à¤µ à¤–à¥‡à¤¤ à¤•à¥‹ GPS à¤”à¤° GEO à¤«à¥‡à¤‚à¤¸à¤¿à¤‚à¤— à¤•à¥‡ à¤¦à¥?à¤µà¤¾à¤°à¤¾ à¤¦à¤¶à¤¯à¤¿à¤¾ à¤—à¤¯à¤¾ à¤¹à¥ˆà¥¤"), font));
//                paragraph.setAlignment(Element.ALIGN_LEFT);
//                document.add(paragraph);
            }else {
                Image point1ChemicalMixImage = Image.getInstance("Point1ChemicalMix.png");
                point1ChemicalMixImage.scaleAbsolute(200f, 25f);

                point1Cell = new PdfPCell(point1ChemicalMixImage);
                point1Cell.setBorder(Rectangle.NO_BORDER);
                point1Cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                point1Table.addCell(point1Cell);
                point1Table.setSpacingBefore(20);
                document.add(point1Table);


//                paragraph.clear();
//                paragraph.add(new Phrase(hindi.process("à¤†à¤ªà¤•à¤¾ à¤—à¤¾à¤?à¤µ à¤µ à¤†à¤ªà¤•à¥€ à¤œà¤®à¥€à¤¨\n"), hindiFontSize14));
//                paragraph.setAlignment(Element.ALIGN_CENTER);
//                document.add(paragraph);
            }


            PdfPTable geoTable = new PdfPTable(2);
            geoTable.setWidthPercentage(100);
            geoTable.setWidths(new int[]{6, 4});


            PdfPCell geoCell;
            Phrase villagePhrase = new Phrase();
            villagePhrase.add(new Chunk(hindi.process("à¤—à¤¾à¤?à¤µ à¤•à¤¾ à¤œà¤¿à¤¯à¥‹à¤«à¥‡à¤‚à¤¸à¤¿à¤‚à¤—"),hindiBold));
            villagePhrase.add(new Chunk("\n"));
            geoCell = new PdfPCell(villagePhrase);
            geoCell.setBorder(Rectangle.NO_BORDER);
            geoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            geoTable.addCell(geoCell);

            Phrase fieldPhrase = new Phrase();
            fieldPhrase.add(new Chunk(hindi.process("à¤–à¥‡à¤¤ à¤•à¤¾ à¤œà¤¿à¤¯à¥‹à¤«à¥‡à¤‚à¤¸à¤¿à¤‚à¤—\n"),hindiBold));
            fieldPhrase.add(new Chunk("\n"));
            geoCell = new PdfPCell(fieldPhrase);
            geoCell.setBorder(Rectangle.NO_BORDER);
            geoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            geoTable.addCell(geoCell);

            if(StringUtils.isEmpty(villageImage)) {
                villageImage = "https://s3.ap-southeast-1.amazonaws.com/anndata-pics/1574418029341-no-image-available-icon-6.jpg";
            }

            if(StringUtils.isEmpty(landImage)) {
                landImage = "https://s3.ap-southeast-1.amazonaws.com/anndata-pics/1574418029341-no-image-available-icon-6.jpg";
            }

            String url1 = villageImage;
            Image villagePic = Image.getInstance(url1);
            villagePic.scaleAbsolute(310f, 180f);
            geoCell = new PdfPCell(villagePic);
            geoCell.setBorder(Rectangle.NO_BORDER);
            geoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            geoTable.addCell(geoCell);

            String url2 = landImage;
            Image fieldImage = Image.getInstance(url2);
            fieldImage.scaleAbsolute(200f, 180f);
            geoCell = new PdfPCell(fieldImage);
            geoCell.setBorder(Rectangle.NO_BORDER);
            geoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            geoTable.addCell(geoCell);

            geoTable.setSpacingBefore(10);
            document.add(geoTable);
            geoTable.setSpacingAfter(30);



            if(npkRecommendation.getSoilTest() != null) {

//
                PdfPTable soilTestTable = new PdfPTable(1);
                soilTestTable.setWidthPercentage(100);
                PdfPCell soilTestCell;

                if(npkRecommendation.getFarmingType().equals(FarmingType.Type.Organic)) {

                    Image point2Image = Image.getInstance("Point2.png");
                    point2Image.scaleAbsolute(500f, 20f);

                    soilTestCell = new PdfPCell(point2Image);
                    soilTestCell.setBorder(Rectangle.NO_BORDER);
                    soilTestCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    soilTestTable.addCell(soilTestCell);
                    document.add(soilTestTable);

//                    paragraph.clear();
//                    paragraph.add(new Phrase(hindi.process("\n\n2- à¤œà¥ˆà¤µà¤¿à¤• à¤•à¥ƒà¤·à¤¿ à¤‰à¤¤à¥?à¤ªà¤¾à¤¦ à¤¸à¥‡ à¤ªà¥‚à¤°à¥?à¤µ à¤•à¤¿à¤¸à¤¾à¤¨ à¤¦à¥?à¤µà¤¾à¤°à¤¾ à¤®à¥ƒà¤¦à¤¾ à¤ªà¤°à¥€à¤•à¥?à¤·à¤£ à¤•à¤°à¤¾à¤¯à¤¾ à¤—à¤¯à¤¾ à¤¹à¥ˆ, à¤‰à¤¸à¤•à¥€ à¤°à¤¿à¤ªà¥‹à¤°à¥?à¤Ÿ à¤¸à¤‚à¤²à¤—à¥?à¤¨ à¤¹à¥ˆ-"), font));
//                    paragraph.setAlignment(Element.ALIGN_LEFT);
//                    document.add(paragraph);
                }
                else {


                    Image soilTest = Image.getInstance("SoilTest.png");
                    soilTest.scaleAbsolute(230f, 20f);

                    soilTestCell = new PdfPCell(soilTest);
                    soilTestCell.setBorder(Rectangle.NO_BORDER);
                    soilTestCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    soilTestTable.addCell(soilTestCell);
                    document.add(soilTestTable);

//                    paragraph.clear();
//                    paragraph.add(new Phrase(hindi.process("\n\nà¤†à¤ªà¤•à¥€ à¤®à¥ƒà¤¦à¤¾ à¤ªà¤°à¥€à¤•à¥?à¤·à¤£ à¤°à¤¿à¤ªà¥‹à¤°à¥?à¤Ÿ à¤¨à¤¿à¤®à¥?à¤¨ à¤ªà¥?à¤°à¤•à¤¾à¤° à¤¹à¥ˆ-\n\n"), font));
//                    paragraph.setAlignment(Element.ALIGN_LEFT);
//                    document.add(paragraph);


                    PdfPTable soilTestTable1 = new PdfPTable(2);
                    soilTestTable1.setWidthPercentage(100);
                    geoTable.setWidths(new int[]{6, 4});
                    PdfPCell soilTestCell1;

                    Image soilTest1 = Image.getInstance("SoilTest1.png");
                    soilTest1.scaleAbsolute(120f, 20f);

                    soilTestCell1 = new PdfPCell(soilTest1);
                    soilTestCell1.setBorder(Rectangle.NO_BORDER);
                    soilTestCell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                    soilTestTable1.addCell(soilTestCell1);

                    soilTestCell1 = new PdfPCell(new Phrase(" ( "+convertDate(Calendar.getInstance().getTime())+" )",regular));
                    soilTestCell1.setBorder(Rectangle.NO_BORDER);
                    soilTestCell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                    soilTestTable1.addCell(soilTestCell1);
                    document.add(soilTestTable1);

//                    Chunk blueText = new Chunk(hindi.process("à¤®à¥ƒà¤¦à¤¾ à¤ªà¤°à¥€à¤•à¥?à¤·à¤£ à¤°à¤¿à¤ªà¥‹à¤°à¥?à¤Ÿ ( "+convertDate(Calendar.getInstance().getTime())+" )"), hindiFont);
//
//                    paragraph.clear();
//                    paragraph.add(blueText);
//                    paragraph.setAlignment(Element.ALIGN_CENTER);
//                    document.add(paragraph);

                    PdfPTable soilTestTable2 = new PdfPTable(1);
                    soilTestTable2.setWidthPercentage(100);
                    PdfPCell soilTestCell2;

                    Image soilTest2 = Image.getInstance("SoilTest2.png");
                    soilTest2.scaleAbsolute(400f, 20f);

                    soilTestCell2 = new PdfPCell(soilTest2);
                    soilTestCell2.setBorder(Rectangle.NO_BORDER);
                    soilTestCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    soilTestTable2.addCell(soilTestCell2);

                    Image soilTest3 = Image.getInstance("SoilTest3.png");
                    soilTest3.scaleAbsolute(200f, 20f);

                    soilTestCell2 = new PdfPCell(soilTest3);
                    soilTestCell2.setBorder(Rectangle.NO_BORDER);
                    soilTestCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                    soilTestTable2.addCell(soilTestCell2);
                    document.add(soilTestTable2);

//                    paragraph.clear();
//                    paragraph.add(new Phrase(hindi.process("à¤§à¤°à¤¤à¥€ à¤•à¤¾ à¤¡à¥‰à¤•à¥?à¤Ÿà¤° à¤®à¤¿à¤Ÿà¥?à¤Ÿà¥€ à¤ªà¤°à¥€à¤•à¥?à¤·à¤£ à¤•à¤¿à¤Ÿ à¤•à¥‡ à¤¸à¤¾à¤¥ à¤…à¤ªà¤¨à¥€ à¤®à¤¿à¤Ÿà¥?à¤Ÿà¥€ à¤•à¥‡ à¤ªà¤°à¥€à¤•à¥?à¤·à¤£ à¤•à¥‡ à¤…à¤¨à¥?à¤¸à¤¾à¤°,\n" +
//                            " à¤†à¤ªà¤•à¥‡ à¤–à¥‡à¤¤ à¤®à¥‡à¤‚ à¤®à¥Œà¤œà¥‚à¤¦ à¤ªà¥‹à¤·à¤• à¤¤à¤¤à¥?à¤µ à¤‡à¤¸ à¤ªà¥?à¤°à¤•à¤¾à¤° à¤¹à¥ˆà¤‚\n"),font));
//                    paragraph.setAlignment(Element.ALIGN_CENTER);
//                    document.add(paragraph);
                }


                SoilTest soilTest = npkRecommendation.getSoilTest();

                Map<String, String> map = new LinkedHashMap<>();
                map.put(hindi.process("à¤¨à¤¾à¤‡à¤Ÿà¥?à¤°à¥‹à¤œà¤¨"), soilTest.getnValue()+hindi.process(" à¤•à¤¿à¤—à¥?à¤°à¤¾"));
                map.put(hindi.process("à¤«à¤¾à¤¸à¥?à¤«à¥‹à¤°à¤¸"), soilTest.getpValue()+hindi.process(" à¤•à¤¿à¤—à¥?à¤°à¤¾"));
                map.put(hindi.process("à¤ªà¥‹à¤Ÿà¤¾à¤¶"), soilTest.getkValue()+hindi.process(" à¤•à¤¿à¤—à¥?à¤°à¤¾"));
                map.put(hindi.process("à¤‘à¤°à¥?à¤—à¥‡à¤¨à¤¿à¤• à¤•à¤¾à¤°à¥?à¤¬à¤¨"), soilTest.getOrganicCarbon()+" %");
                map.put(hindi.process("à¤ªà¥€à¤?à¤š"), soilTest.getpHValue()+" ");


                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setWidths(new int[]{1, 5, 5});

                PdfPCell hcell;
                hcell = new PdfPCell(new Phrase(hindi.process("à¤…à¤¨à¥?."), hindiBold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                hcell = new PdfPCell(new Phrase(hindi.process("à¤®à¥ƒà¤¦à¤¾-à¤ªà¥ˆà¤°à¤¾à¤®à¥€à¤Ÿà¤°"), hindiBold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                hcell = new PdfPCell(new Phrase(hindi.process("à¤®à¤¾à¤¨"), hindiBold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                Integer sr = 0;

                for (Map.Entry<String, String> entry : map.entrySet()) {
                    sr++;
                    PdfPCell cell;

                    BaseColor color = greyColor;
                    if (sr % 2 == 0){
                        color = lightGreyColor;
                    }

                    cell = new PdfPCell(new Phrase(sr.toString()));
                    cell.setPaddingLeft(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(color);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(entry.getKey(),font));
                    cell.setPaddingLeft(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(color);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(entry.getValue()),font));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                }
                table.setSpacingBefore(10);
                document.add(table);
            }

            document.newPage();
            canvas.stroke();

            PdfPTable recommendationHeadingTable = new PdfPTable(1);
            recommendationHeadingTable.setWidthPercentage(100);
            PdfPCell recommendationHeadingCell;

            Image chemicalImage = Image.getInstance("chemical.png");
            chemicalImage.scaleAbsolute(530f, 20f);

            Image fertilizerName = Image.getInstance("24.png");
            fertilizerName.scaleAbsolute(80f, 15f);

            Image fertilizerType = Image.getInstance("25.png");
            fertilizerType.scaleAbsolute(80f, 15f);

            Image requirement = Image.getInstance("26.png");
            requirement.scaleAbsolute(80f, 15f);

            if(!reportHistory.getFertilizerResponse().isEmpty() && npkRecommendation.getFarmingType().equals(FarmingType.Type.Chemical)) {

                recommendationHeadingCell = new PdfPCell(chemicalImage);
                recommendationHeadingCell.setBorder(Rectangle.NO_BORDER);
                recommendationHeadingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                recommendationHeadingTable.addCell(recommendationHeadingCell);
                recommendationHeadingTable.setSpacingBefore(20);

//                Image fertilizerReco = Image.getInstance("chemical.png");
//                fertilizerReco.scaleAbsolute(530f, 20f);
//
//                recommendationHeadingCell = new PdfPCell(fertilizerReco);
//                recommendationHeadingCell.setBorder(Rectangle.NO_BORDER);
//                recommendationHeadingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
//                recommendationHeadingTable.addCell(recommendationHeadingCell);
//                recommendationHeadingTable.setSpacingBefore(20);
                document.add(recommendationHeadingTable);

//                paragraph.clear();
//                paragraph.add(new Phrase(hindi.process("à¤†à¤ªà¤¨à¥‡ à¤…à¤ªà¤¨à¥‡ à¤–à¥‡à¤¤ à¤®à¥‡à¤‚ à¤°à¤¾à¤¸à¤¾à¤¯à¤¨à¤¿à¤• à¤–à¥‡à¤¤à¥€ à¤•à¤°à¤¨à¥‡ à¤•à¤¾ à¤µà¤¿à¤šà¤¾à¤° à¤•à¤¿à¤¯à¤¾ à¤¹à¥ˆà¥¤ à¤‡à¤¸à¤•à¥‡ à¤²à¤¿à¤? à¤†à¤ª à¤¨à¤¿à¤®à¥?à¤¨ à¤–à¤¾à¤¦/à¤°à¤¸à¤¾à¤¯à¤¨ à¤ªà¥?à¤°à¤¯à¥‹à¤— à¤•à¤°à¥‡à¤‚"), font));
//                paragraph.setAlignment(Element.ALIGN_LEFT);
//                document.add(paragraph);

                Chunk greenText = new Chunk(hindi.process("à¤‰à¤°à¥?à¤µà¤°à¤• à¤•à¥€ à¤ªà¥?à¤¨: à¤ªà¥‚à¤°à¥?à¤¤à¤¿"+npkRecommendation.getFieldSize() +npkRecommendation.getFieldSizeType()), greenHeadColor);

                document.add(Chunk.NEWLINE);
                paragraph.clear();
                paragraph.add(greenText);
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);

                paragraph.clear();
                paragraph.add(new Phrase(hindi.process("à¤°à¤¾à¤¸à¤¾à¤¯à¤¨à¤¿à¤• à¤ªà¥?à¤°à¤¦à¤°à¥?à¤¶à¤¨ à¤•à¤°à¤¨à¥‡ à¤•à¥‡ à¤²à¤¿à¤? à¤…à¤ªà¤¨à¥€ à¤ªà¤¸à¤‚à¤¦ à¤•à¥‡ à¤…à¤¨à¥?à¤¸à¤¾à¤°\n" +
                        "à¤†à¤ªà¤•à¥‡ à¤–à¥‡à¤¤ à¤®à¥‡à¤‚, à¤¸à¥?à¤?à¤¾à¤µ à¤‡à¤¸ à¤ªà¥?à¤°à¤•à¤¾à¤° à¤¹à¥ˆà¤‚:"),font));
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);

                List<FertilizerResponse> fertilizerResponses = reportHistory.getFertilizerResponse();

                PdfPTable table = new PdfPTable(4);
                table.setWidthPercentage(100);
                table.setWidths(new int[]{2, 5, 5, 5});

                PdfPCell hcell;
//                hcell = new PdfPCell(new Phrase(hindi.process("à¤•à¥?à¤°à¤®à¤¾à¤‚à¤•"), hindiBold));
                hcell = new PdfPCell(new Phrase(hindi.process("à¤•à¥?à¤°à¤®à¤¾à¤‚à¤•"), hindiBold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

//                hcell = new PdfPCell(new Phrase(hindi.process("à¤‰à¤°à¥?à¤µà¤°à¤• à¤•à¤¾ à¤¨à¤¾à¤®"), hindiBold));
                hcell = new PdfPCell(fertilizerName);
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

//                hcell = new PdfPCell(new Phrase(hindi.process("à¤‰à¤°à¥?à¤µà¤°à¤• à¤ªà¥?à¤°à¤•à¤¾à¤°"), hindiBold));
                hcell = new PdfPCell(fertilizerType);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

//                hcell = new PdfPCell(new Phrase(hindi.process("à¤†à¤µà¤¶à¥?à¤¯à¤•à¤¤à¤¾"), hindiBold));
                hcell = new PdfPCell(requirement);
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                Integer sr = 0;

                for (FertilizerResponse response : fertilizerResponses) {

                    int requirment = (int) response.getRequirement();

                    sr++;
                    PdfPCell cell;

                    BaseColor color = greyColor;
                    if (sr % 2 == 0){
                        color = lightGreyColor;
                    }

                    cell = new PdfPCell(new Phrase(sr.toString()));
                    cell.setPaddingLeft(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(color);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(response.getFertilizerName()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(response.getFertylizerType()).trim()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(requirment+" "+response.getUnit()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                }
                table.setSpacingBefore(10);
                document.add(table);

            }
            if(reportHistory.getOrganicReqFert() != null && npkRecommendation.getFarmingType().equals(FarmingType.Type.Organic)) {

                Image point3Image = Image.getInstance("Point3.png");
                point3Image.scaleAbsolute(530f, 40f);

                recommendationHeadingCell = new PdfPCell(point3Image);
                recommendationHeadingCell.setBorder(Rectangle.NO_BORDER);
                recommendationHeadingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                recommendationHeadingTable.addCell(recommendationHeadingCell);
                recommendationHeadingTable.setSpacingBefore(20);
                document.add(recommendationHeadingTable);

//                paragraph.clear();
//                paragraph.add(new Phrase(hindi.process("3- à¤®à¥ƒà¤¦à¤¾ à¤ªà¤°à¥€à¤•à¥?à¤·à¤£ à¤•à¥‡ à¤…à¤¨à¥?à¤¸à¤¾à¤° à¤‰à¤ªà¤°à¥‹à¤•à¥?à¤¤ à¤–à¥‡à¤¤ à¤®à¥‡à¤‚ à¤œà¥ˆà¤µà¤¿à¤• à¤•à¥ƒà¤·à¤¿ à¤•à¥‡ à¤²à¤¿à¤? à¤†à¤µà¤¶à¥?à¤¯à¤• à¤…à¤¨à¥?à¤®à¥‹à¤¦à¤¿à¤¤ à¤‰à¤°à¥?à¤µà¤°à¤•à¥‹à¤‚ à¤µ à¤–à¤¾à¤¦ à¤ªà¥?à¤°à¤¯à¥‹à¤— à¤•à¥‡ à¤²à¤¿à¤? à¤¨à¤¿à¤°à¥?à¤¦à¥‡à¤¶à¤¿à¤¤ à¤•à¤¿à¤¯à¤¾ à¤—à¤¯à¤¾ à¤¹à¥ˆ (à¤‰à¤¸à¥‡ à¤•à¤¿à¤¸à¤¾à¤¨ à¤¦à¥?à¤µà¤¾à¤°à¤¾ à¤¡à¤¾à¤²à¥‡ à¤—à¤? à¤…à¤¨à¥?à¤®à¤¾à¤¨à¤¿à¤¤ à¤–à¤¾à¤¦ à¤•à¥‡ à¤°à¥‚à¤ª à¤®à¥‡à¤‚ à¤ªà¥?à¤°à¤¦à¤°à¥?à¤¶à¤¿à¤¤ à¤•à¤¿à¤¯à¤¾ à¤—à¤¯à¤¾ à¤¹à¥ˆà¥¤)"), font));
//                paragraph.setAlignment(Element.ALIGN_LEFT);
//                document.add(paragraph);

                OrganicReqFert organicReqFert = reportHistory.getOrganicReqFert();
                List<POMFertCal> pomFertCals = organicReqFert.getPomFertCals();

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);
                table.setWidths(new int[]{2,5, 5, 5, 5});

                PdfPCell hcell;
                hcell = new PdfPCell(new Phrase(hindi.process("à¤•à¥?à¤°à¤®à¤¾à¤‚à¤•"), hindiBold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

//                hcell = new PdfPCell(new Phrase(hindi.process("à¤‰à¤°à¥?à¤µà¤°à¤• à¤•à¤¾ à¤¨à¤¾à¤®"), hindiBold));
                hcell = new PdfPCell(fertilizerName);
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

//                hcell = new PdfPCell(new Phrase(hindi.process("à¤‰à¤°à¥?à¤µà¤°à¤• à¤ªà¥?à¤°à¤•à¤¾à¤°"), hindiBold));
                hcell = new PdfPCell(fertilizerType);
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                hcell = new PdfPCell(new Phrase(hindi.process("à¤‰à¤°à¥?à¤µà¤°à¤• à¤µà¤°à¥?à¤— à¤ªà¥?à¤°à¤•à¤¾à¤°"), hindiBold));
                hcell.setBackgroundColor(myColor);
                hcell.setPaddingLeft(5);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

//                hcell = new PdfPCell(new Phrase(hindi.process("à¤†à¤µà¤¶à¥?à¤¯à¤•à¤¤à¤¾"), hindiBold));
                hcell = new PdfPCell(requirement);
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(hcell);

                Integer sr = 0;

                for (POMFertCal pomFertCal : pomFertCals) {

                    String  requirment =  pomFertCal.getRequiredFert().toBigInteger().toString();

                    sr++;
                    PdfPCell cell;

                    BaseColor color = greyColor;
                    if (sr % 2 == 0){
                        color = lightGreyColor;
                    }

                    cell = new PdfPCell(new Phrase(sr.toString()));
                    cell.setPaddingLeft(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(color);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(pomFertCal.getFertilizerName()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(pomFertCal.getFertilizerType())));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(pomFertCal.getCategoryType())));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);

                    cell = new PdfPCell(new Phrase(requirment+" "+pomFertCal.getUnit()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    table.addCell(cell);
                }
                table.setSpacingBefore(10);
                document.add(table);
                document.add(Chunk.NEWLINE);

            }

            if(reportHistory.getMixReqFert() != null && npkRecommendation.getFarmingType().equals(FarmingType.Type.INM_MIX)) {

                recommendationHeadingCell = new PdfPCell(chemicalImage);
                recommendationHeadingCell.setBorder(Rectangle.NO_BORDER);
                recommendationHeadingCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                recommendationHeadingTable.addCell(recommendationHeadingCell);
                recommendationHeadingTable.setSpacingBefore(20);
                document.add(recommendationHeadingTable);

//                paragraph.clear();
//                paragraph.add(new Phrase(hindi.process("à¤†à¤ªà¤¨à¥‡ à¤…à¤ªà¤¨à¥‡ à¤–à¥‡à¤¤ à¤®à¥‡à¤‚ à¤°à¤¾à¤¸à¤¾à¤¯à¤¨à¤¿à¤• à¤–à¥‡à¤¤à¥€ à¤•à¤°à¤¨à¥‡ à¤•à¤¾ à¤µà¤¿à¤šà¤¾à¤° à¤•à¤¿à¤¯à¤¾ à¤¹à¥ˆà¥¤ à¤‡à¤¸à¤•à¥‡ à¤²à¤¿à¤? à¤†à¤ª à¤¨à¤¿à¤®à¥?à¤¨ à¤–à¤¾à¤¦/à¤°à¤¸à¤¾à¤¯à¤¨ à¤ªà¥?à¤°à¤¯à¥‹à¤— à¤•à¤°à¥‡à¤‚"), font));
//                paragraph.setAlignment(Element.ALIGN_LEFT);
//                document.add(paragraph);

                Chunk greenText = new Chunk(hindi.process("à¤‰à¤°à¥?à¤µà¤°à¤• à¤•à¥€ à¤ªà¥?à¤¨: à¤ªà¥‚à¤°à¥?à¤¤à¤¿ "+npkRecommendation.getFieldSize() +npkRecommendation.getFieldSizeType()), greenHeadColor);

                document.add(Chunk.NEWLINE);
                paragraph.clear();
                paragraph.add(greenText);
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);

                paragraph.clear();
                paragraph.add(new Phrase(hindi.process("à¤®à¤¿à¤¶à¥?à¤°à¤¿à¤¤ à¤–à¥‡à¤¤à¥€ à¤ªà¥?à¤°à¤¦à¤°à¥?à¤¶à¤¨ à¤•à¤°à¤¨à¥‡ à¤•à¥‡ à¤²à¤¿à¤?,à¤¨à¤¿à¤®à¥?à¤¨ à¤‰à¤°à¥?à¤µà¤°à¤• à¤•à¤¾ à¤‰à¤ªà¤¯à¥‹à¤— à¤•à¤°à¥‡à¤‚ \n" +
                        "à¤°à¤¾à¤¸à¤¾à¤¯à¤¨à¤¿à¤• à¤‰à¤°à¥?à¤µà¤°à¤•:"),font));
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);

                Set<FertilizerResponse> fertilizerResponses = reportHistory.getMixReqFert().getFertilizerResponseList();

                PdfPTable chemicalTable = new PdfPTable(4);
                chemicalTable.setWidthPercentage(100);
                chemicalTable.setWidths(new int[]{2, 5, 5, 5});

                PdfPCell hcell1;
                hcell1 = new PdfPCell(new Phrase(hindi.process("à¤•à¥?à¤°à¤®à¤¾à¤‚à¤•"), hindiBold));
                hcell1.setPaddingLeft(5);
                hcell1.setBackgroundColor(myColor);
                hcell1.setBorder(Rectangle.NO_BORDER);
                hcell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                chemicalTable.addCell(hcell1);

//                hcell1 = new PdfPCell(new Phrase(hindi.process("à¤‰à¤°à¥?à¤µà¤°à¤• à¤•à¤¾ à¤¨à¤¾à¤®"), hindiBold));
                hcell1 = new PdfPCell(fertilizerName);
                hcell1.setPaddingLeft(5);
                hcell1.setBackgroundColor(myColor);
                hcell1.setBorder(Rectangle.NO_BORDER);
                hcell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                chemicalTable.addCell(hcell1);

//                hcell1 = new PdfPCell(new Phrase(hindi.process("à¤‰à¤°à¥?à¤µà¤°à¤• à¤ªà¥?à¤°à¤•à¤¾à¤°"), hindiBold));
                hcell1 = new PdfPCell(fertilizerType);
                hcell1.setPaddingLeft(5);
                hcell1.setBackgroundColor(myColor);
                hcell1.setBorder(Rectangle.NO_BORDER);
                hcell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                chemicalTable.addCell(hcell1);

//                hcell1 = new PdfPCell(new Phrase(hindi.process("à¤†à¤µà¤¶à¥?à¤¯à¤•à¤¤à¤¾"), hindiBold));
                hcell1 = new PdfPCell(requirement);
                hcell1.setPaddingLeft(5);
                hcell1.setBackgroundColor(myColor);
                hcell1.setBorder(Rectangle.NO_BORDER);
                hcell1.setHorizontalAlignment(Element.ALIGN_LEFT);
                chemicalTable.addCell(hcell1);

                int sr1 = 0;

                for (FertilizerResponse response : fertilizerResponses) {

                    int requirment = 0;
                    requirment = (int) response.getRequirement();

                    sr1++;
                    PdfPCell cell;

                    BaseColor color = greyColor;
                    if (sr1 % 2 == 0){
                        color = lightGreyColor;
                    }

                    cell = new PdfPCell(new Phrase(Integer.toString(sr1)));
                    cell.setPaddingLeft(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(color);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    chemicalTable.addCell(cell);

                    cell = new PdfPCell(new Phrase(response.getFertilizerName()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    chemicalTable.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(response.getFertylizerType()).trim()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    chemicalTable.addCell(cell);

                    cell = new PdfPCell(new Phrase(requirment+" "+response.getUnit()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    chemicalTable.addCell(cell);
                }
                chemicalTable.setSpacingBefore(10);
                document.add(chemicalTable);


                document.add(Chunk.NEWLINE);
                paragraph.clear();
                paragraph.add(new Phrase(hindi.process("à¤œà¥ˆà¤µà¤¿à¤• à¤‰à¤°à¥?à¤µà¤°à¤•:"),hindiFont));
                paragraph.setAlignment(Element.ALIGN_CENTER);
                document.add(paragraph);

                MixReqFert mixReqFert = reportHistory.getMixReqFert();
                List<POMFertCal> pomFertCals = mixReqFert.getOrganicReqFert().getPomFertCals();


                PdfPTable organicTable = new PdfPTable(5);
                organicTable.setWidthPercentage(100);
                organicTable.setWidths(new int[]{2,5, 5, 5, 5});


                PdfPCell hcell;
                hcell = new PdfPCell(new Phrase(hindi.process("à¤•à¥?à¤°à¤®à¤¾à¤‚à¤•"), hindiBold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                organicTable.addCell(hcell);

//                hcell = new PdfPCell(new Phrase(hindi.process("à¤‰à¤°à¥?à¤µà¤°à¤• à¤•à¤¾ à¤¨à¤¾à¤®"), hindiBold));
                hcell = new PdfPCell(fertilizerName);
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                organicTable.addCell(hcell);

//                hcell = new PdfPCell(new Phrase(hindi.process("à¤‰à¤°à¥?à¤µà¤°à¤• à¤ªà¥?à¤°à¤•à¤¾à¤°"), hindiBold));
                hcell = new PdfPCell(fertilizerType);
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                organicTable.addCell(hcell);

                hcell = new PdfPCell(new Phrase(hindi.process("à¤‰à¤°à¥?à¤µà¤°à¤• à¤µà¤°à¥?à¤— à¤ªà¥?à¤°à¤•à¤¾à¤°"), hindiBold));
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                organicTable.addCell(hcell);

//                hcell = new PdfPCell(new Phrase(hindi.process("à¤†à¤µà¤¶à¥?à¤¯à¤•à¤¤à¤¾"), hindiBold));
                hcell = new PdfPCell(requirement);
                hcell.setPaddingLeft(5);
                hcell.setBackgroundColor(myColor);
                hcell.setBorder(Rectangle.NO_BORDER);
                hcell.setHorizontalAlignment(Element.ALIGN_LEFT);
                organicTable.addCell(hcell);

                Integer sr = 0;

                for (POMFertCal pomFertCal : pomFertCals) {

                    String  requirment =  pomFertCal.getRequiredFert().toBigInteger().toString();

                    sr++;
                    PdfPCell cell;

                    BaseColor color = greyColor;
                    if (sr % 2 == 0){
                        color = lightGreyColor;
                    }

                    cell = new PdfPCell(new Phrase(sr.toString()));
                    cell.setPaddingLeft(5);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setBackgroundColor(color);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    organicTable.addCell(cell);

                    cell = new PdfPCell(new Phrase(pomFertCal.getFertilizerName()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    organicTable.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(pomFertCal.getFertilizerType())));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    organicTable.addCell(cell);

                    cell = new PdfPCell(new Phrase(String.valueOf(pomFertCal.getCategoryType())));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    organicTable.addCell(cell);

                    cell = new PdfPCell(new Phrase(requirment+" "+pomFertCal.getUnit()));
                    cell.setPaddingLeft(5);
                    cell.setBackgroundColor(color);
                    cell.setBorder(Rectangle.NO_BORDER);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                    organicTable.addCell(cell);
                }
                organicTable.setSpacingBefore(10);
                document.add(organicTable);

            }

            PdfPTable instructionTable = new PdfPTable(1);
            instructionTable.setWidthPercentage(100);
            PdfPCell instructionCell;

            if(npkRecommendation.getFarmingType().equals(FarmingType.Type.Organic)) {

                Image mixInstructionsImage = Image.getInstance("MixInstructions.png");
                mixInstructionsImage.scaleAbsolute(530f, 480f);

                instructionCell = new PdfPCell(mixInstructionsImage);
                instructionCell.setBorder(Rectangle.NO_BORDER);
                instructionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                instructionTable.addCell(instructionCell);
                instructionTable.setSpacingBefore(20);
                document.add(instructionTable);


//                PdfPTable newTable = new PdfPTable(1);
//                newTable.setWidthPercentage(100);
//
//                PdfPCell newCell;
//                newCell = new PdfPCell(new Phrase());
//                newCell.setBorder(Rectangle.NO_BORDER);
//                newCell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                newTable.addCell(newCell);

//                Phrase phrase1 = new Phrase();
//                phrase1.add(new Chunk(hindi.process("à¤…à¤¨à¥?à¤¯ à¤µà¤¿à¤¶à¥‡à¤· à¤®à¤¾à¤ªà¤¦à¤£à¥?à¤¡:\n" +
//                        "\n" +
//                        "à¤ªà¥?à¤°à¤šà¤²à¤¿à¤¤ à¤œà¥ˆà¤µà¤¿à¤• à¤®à¤¾à¤¨à¤•-\n\n" ),hindiBold));
//                phrase1.add(new Chunk(hindi.process("(I) PGS à¤…à¤°à¥?à¤¹à¤¤à¤¾ à¤•à¥‡ à¤…à¤¨à¥?à¤¸à¤¾à¤° Cluster (à¤¸à¤¾à¤®à¥‚à¤¹à¤¿à¤• à¤–à¥‡à¤¤à¥€) à¤•à¥‹ à¤¹à¥€ à¤œà¥ˆà¤µà¤¿à¤• à¤®à¤¾à¤¨à¥?à¤¯à¤¤à¤¾ à¤¦à¥€ à¤—à¤¯à¥€ à¤¹à¥ˆ à¤œà¤¿à¤¸à¤•à¥‡ à¤¤à¤¹à¤¤ à¤¸à¤¾à¤®à¤¾à¤¨à¥?à¤¯à¤¤à¤ƒ à¤•à¤® à¤¸à¥‡ à¤•à¤® 50 à¤?à¤•à¤¡à¤¼ à¤­à¥‚à¤®à¤¿ à¤?à¤• à¤¸à¤¾à¤¥ à¤¹à¥‹à¤¨à¤¾ à¤œà¤°à¥‚à¤°à¥€ à¤¹à¥ˆà¥¤\n\n" +
//                        "         (à¤•)   à¤¯à¤¦à¤¿ à¤œà¥ˆà¤µà¤¿à¤• à¤–à¥‡à¤¤ à¤¦à¥‹ à¤¤à¤°à¤« à¤¸à¥‡ à¤–à¥?à¤²à¤¾ (à¤¯à¤¾à¤¨à¤¿ à¤•à¥‹à¤ˆ à¤œà¤‚à¤—à¤², à¤¨à¤¦à¥€, à¤¬à¤‚à¤œà¤° à¤­à¥‚à¤®à¤¿ à¤†à¤¦à¤¿ à¤•à¥‡ à¤¸à¤¾à¤¥ à¤²à¤—à¤¤à¥€ à¤¹à¥‹à¥¤) à¤¤à¤¬ 10 à¤?à¤•à¤¡à¤¼ à¤¸à¥‡ à¤…à¤§à¤¿à¤• à¤–à¥‡à¤¤ à¤¹à¥‹à¤¨à¥‡ à¤ªà¤° à¤µà¤¿à¤¶à¥‡à¤· à¤œà¥ˆà¤µà¤¿à¤• à¤‰à¤¤à¥?à¤ªà¤¾à¤¦à¤¨ à¤•à¥‡ à¤°à¥‚à¤ª à¤®à¥‡à¤‚ à¤®à¤¾à¤¨à¥?à¤¯à¤¤à¤¾ à¤¦à¥€ à¤—à¤¯à¥€ à¤¹à¥ˆà¥¤\n\n" +
//                        "         (à¤–)   à¤¯à¤¦à¤¿ à¤œà¥ˆà¤µà¤¿à¤• à¤–à¥‡à¤¤ à¤¤à¥€à¤¨ à¤¤à¤°à¤« à¤¸à¥‡ à¤–à¥?à¤²à¤¾ (à¤¯à¤¾à¤¨à¤¿ à¤•à¥‹à¤ˆ à¤œà¤‚à¤—à¤², à¤¨à¤¦à¥€, à¤¬à¤‚à¤œà¤° à¤­à¥‚à¤®à¤¿ à¤†à¤¦à¤¿ à¤•à¥‡ à¤¸à¤¾à¤¥ à¤²à¤—à¤¤à¥€ à¤¹à¥‹à¥¤) à¤¤à¤¬ 5 à¤?à¤•à¤¡à¤¼ à¤¸à¥‡ à¤…à¤§à¤¿à¤• à¤–à¥‡à¤¤ à¤¹à¥‹à¤¨à¥‡ à¤ªà¤° à¤µà¤¿à¤¶à¥‡à¤· à¤œà¥ˆà¤µà¤¿à¤• à¤‰à¤¤à¥?à¤ªà¤¾à¤¦à¤¨ à¤•à¥‡ à¤°à¥‚à¤ª à¤®à¥‡à¤‚ à¤®à¤¾à¤¨à¥?à¤¯à¤¤à¤¾ à¤¦à¥€ à¤—à¤¯à¥€ à¤¹à¥ˆà¥¤\n\n" +
//                        "         (à¤—)   à¤¯à¤¦à¤¿ à¤œà¥ˆà¤µà¤¿à¤• à¤–à¥‡à¤¤ à¤šà¤¾à¤°à¥‹à¤‚ à¤¤à¤°à¤« à¤¸à¥‡ à¤–à¥?à¤²à¤¾ (à¤¯à¤¾à¤¨à¤¿ à¤•à¥‹à¤ˆ à¤œà¤‚à¤—à¤², à¤¨à¤¦à¥€, à¤¬à¤‚à¤œà¤° à¤­à¥‚à¤®à¤¿ à¤†à¤¦à¤¿ à¤•à¥‡ à¤¸à¤¾à¤¥ à¤²à¤—à¤¤à¥€ à¤¹à¥‹à¥¤) à¤¤à¤¬ 1 à¤?à¤•à¤¡à¤¼ à¤¸à¥‡ à¤…à¤§à¤¿à¤• à¤–à¥‡à¤¤ à¤¹à¥‹à¤¨à¥‡ à¤ªà¤° à¤µà¤¿à¤¶à¥‡à¤· à¤œà¥ˆà¤µà¤¿à¤• à¤‰à¤¤à¥?à¤ªà¤¾à¤¦à¤¨ à¤•à¥‡ à¤°à¥‚à¤ª à¤®à¥‡à¤‚ à¤®à¤¾à¤¨à¥?à¤¯à¤¤à¤¾ à¤¦à¥€ à¤—à¤¯à¥€ à¤¹à¥ˆà¥¤\n\n"),font));
//                phrase1.add(new Chunk(hindi.process("(II) à¤¥à¤°à¥?à¤¡ à¤ªà¤¾à¤°à¥?à¤Ÿà¥€-\n\n"),font));
//                phrase1.add(new Chunk(hindi.process("     à¤¥à¤°à¥?à¤¡ à¤ªà¤¾à¤°à¥?à¤Ÿà¥€ à¤¨à¤¿à¤°à¥€à¤•à¥?à¤·à¤£ à¤•à¥‡ à¤°à¥‚à¤ª à¤®à¥‡à¤‚ à¤¹à¤®à¤¾à¤°à¥‡ à¤ªà¥?à¤°à¤¤à¤¿à¤¨à¤¿à¤§à¤¿ (Agent) à¤¦à¥?à¤µà¤¾à¤°à¤¾ à¤•à¤¿à¤¸à¤¾à¤¨ à¤•à¥‡ à¤–à¥‡à¤¤ à¤•à¥€ à¤ªà¥?à¤°à¤¤à¥?à¤¯à¥‡à¤• à¤‰à¤ªà¤œ à¤•à¥‹ à¤‰à¤—à¤¾à¤¤à¥‡ à¤¸à¤®à¤¯ à¤¸à¥?à¤¥à¤²à¥€à¤¯ à¤¨à¤¿à¤°à¥€à¤•à¥?à¤·à¤£ à¤•à¥‡ à¤‰à¤ªà¤°à¤¾à¤¨à¥?à¤¤ à¤¹à¥€ â€˜â€˜à¤œà¥ˆà¤µà¤¿à¤• à¤•à¥ƒà¤·à¤¿â€™â€™ à¤•à¥‡ à¤°à¥‚à¤ª à¤®à¥‡à¤‚ à¤®à¤¾à¤¨à¥?à¤¯à¤¤à¤¾ à¤¦à¥€ à¤—à¤¯à¥€ à¤¹à¥ˆà¥¤\n\n"),font));
//                phrase1.add(new Chunk(hindi.process("(III) à¤¬à¥€à¤œ, GMO à¤¯à¤¾ à¤•à¤¿à¤¸à¥€ à¤•à¥ˆà¤®à¤¿à¤•à¤² à¤•à¥‡ à¤¦à¥?à¤µà¤¾à¤°à¤¾ à¤‰à¤ªà¤šà¤¾à¤°à¤¿à¤¤ à¤¨à¤¹à¥€à¤‚ à¤¹à¥ˆà¥¤\n\n"),font));
//                phrase1.add(new Chunk(hindi.process("(IV) à¤†à¤¸à¤ªà¤¾à¤¸ à¤•à¥‡ à¤•à¥?à¤·à¥‡à¤¤à¥?à¤° à¤®à¥‡à¤‚ à¤­à¤¾à¤°à¥€ à¤§à¤¾à¤¤à¥? à¤‰à¤¦à¥?à¤¯à¥‹à¤— à¤¯à¤¾ à¤œà¤®à¥€à¤¨ à¤µ à¤œà¤² à¤®à¥‡à¤‚ à¤­à¤¾à¤°à¥€ à¤§à¤¾à¤¤à¥? à¤µ à¤…à¤¨à¥?à¤¯ à¤¹à¤¾à¤¨à¤¿à¤•à¤¾à¤°à¤• à¤¤à¤¤à¥?à¤µ (à¤²à¥ˆà¤¡, à¤†à¤°à¥?à¤¸à¥‡à¤¨à¤¿à¤•) à¤†à¤¦à¤¿ à¤•à¥€ à¤®à¤¾à¤¤à¥?à¤° à¤¸à¥€à¤®à¤¿à¤¤ à¤®à¤¾à¤¤à¥?à¤° à¤®à¥‡à¤‚ à¤¹à¥€ à¤¹à¥‹, à¤¯à¤¹ à¤§à¥?à¤¯à¤¾à¤¨ à¤°à¤–à¤¾ à¤—à¤¯à¤¾ à¤¹à¥ˆà¥¤\n\n"),font));
//                phrase1.add(new Chunk(hindi.process("(V) à¤¬à¤¹à¥?à¤¤ à¤¬à¤¾à¤¢à¤¼ à¤µà¤¾à¤²à¤¾ à¤¯à¤¾ à¤†à¤¸à¤ªà¤¾à¤¸ à¤®à¥‡à¤‚ à¤­à¥€ à¤•à¥‡à¤®à¥€à¤•à¤² à¤•à¤¾ à¤…à¤§à¤¿à¤• à¤ªà¥?à¤°à¤¯à¥‹à¤— à¤µà¤¾à¤²à¥€ à¤œà¤®à¥€à¤¨ à¤¨ à¤¹à¥‹, à¤‡à¤¸à¤•à¤¾ à¤§à¥?à¤¯à¤¾à¤¨ à¤°à¤–à¤¾ à¤—à¤¯à¤¾ à¤¹à¥ˆà¥¤\n\n"),font));
//                phrase1.add(new Chunk(hindi.process("à¤¨à¥‹à¤Ÿ:"),hindiBold));
//                phrase1.add(new Chunk(hindi.process("   à¤¯à¤¹ à¤ªà¥?à¤°à¤®à¤¾à¤£-à¤ªà¤¤à¥?à¤° à¤ªà¥?à¤°à¤®à¤¾à¤£à¤¿à¤¤ à¤•à¤°à¤¤à¤¾ à¤¹à¥ˆ à¤•à¤¿ à¤­à¥‚à¤®à¤¿ à¤°à¤¾à¤¸à¤¾à¤¯à¤¨à¤¿à¤• à¤…à¤µà¤¶à¥‡à¤· (Residues)à¤”à¤° à¤…à¤¨à¥?à¤¯ à¤­à¤¾à¤°à¥€ à¤§à¤¾à¤¤à¥?à¤“à¤‚/à¤œà¤² à¤ªà¥?à¤°à¤¦à¥‚à¤·à¤£ à¤†à¤¦à¤¿ à¤¸à¥‡ à¤®à¥?à¤•à¥?à¤¤ à¤¹à¥ˆà¥¤ à¤•à¤¿à¤¸à¤¾à¤¨ à¤¦à¥?à¤µà¤¾à¤°à¤¾ à¤‰à¤ªà¤œà¤¾à¤¯à¥‡ à¤—à¤¯à¥‡ à¤®à¤¾à¤¤à¥?à¤° à¤?à¤• à¤«à¤¸à¤² à¤•à¥‹ à¤¹à¥€ â€˜â€˜à¤…à¤¨à¥?à¤¨à¤¦à¤¾à¤¤à¤¾ à¤œà¥ˆà¤µà¤¿à¤• à¤•à¥ƒà¤·à¤¿â€™â€™ à¤•à¥‡ à¤°à¥‚à¤ª à¤®à¥‡à¤‚ à¤ªà¥?à¤°à¤®à¤¾à¤£-à¤ªà¤¤à¥?à¤° à¤¦à¤¿à¤¯à¤¾ à¤—à¤¯à¤¾ à¤¹à¥ˆà¥¤ à¤¯à¤¹ â€œAnnadata Organic Solution Systemâ€?à¤¸à¥‡ à¤‰à¤ªà¤°à¥‹à¤•à¥?à¤¤ à¤¸à¤­à¥€   à¤®à¤¾à¤ªà¤¦à¤£à¥?à¤¡à¥‹à¤‚ à¤•à¤¾ à¤¸à¥?à¤µà¤šà¤¾à¤²à¤¿à¤¤ (Automatic) à¤µà¤¿à¤§à¤¿ à¤¸à¥‡ à¤¨à¤¿à¤°à¥€à¤•à¥?à¤·à¤£ (Analysis) à¤¸à¥‡ à¤¨à¤¿à¤°à¥?à¤—à¤¤ à¤¹à¥ˆà¥¤\n\n"),font));
//                phrase1.add(new Chunk(hindi.process("* à¤‰à¤ªà¤°à¥‹à¤•à¥?à¤¤ à¤œà¥ˆà¤µà¤¿à¤• à¤•à¥ƒà¤·à¤¿ à¤¤à¤¯ à¤®à¤¾à¤¨à¤• à¤…à¤¨à¥?à¤¸à¤¾à¤° à¤«à¤¸à¤² à¤¤à¥ˆà¤¯à¤¾à¤° à¤¹à¥‹à¤¨à¥‡ à¤ªà¤° à¤†à¤ª à¤•à¥‡ à¤…à¤¨à¥?à¤¨à¤¦à¤¾à¤¤à¤¾ App à¤ªà¤° à¤¸à¥?à¤µà¤¤ à¤¹à¥€ à¤œà¥ˆà¤µà¤¿à¤• à¤«à¤¸à¤² à¤•à¤¾ à¤ªà¥?à¤°à¤®à¤¾à¤£-à¤ªà¤¤à¥?à¤° (Organic Certificate) à¤ªà¥?à¤°à¤¾à¤ªà¥?à¤¤ à¤¹à¥‹ à¤œà¤¾à¤¯à¥‡à¤—à¤¾à¥¤"),font));
//
//                newCell = new PdfPCell(phrase1);
//                newCell.setBorder(Rectangle.NO_BORDER);
//                newCell.setHorizontalAlignment(Element.ALIGN_LEFT);
//                newTable.addCell(newCell);
//                document.add(newTable);

            } else {

                Image thankYouImage = Image.getInstance("Thankyou.png");
                thankYouImage.scaleAbsolute(530f, 80f);

                instructionCell = new PdfPCell(thankYouImage);
                instructionCell.setBorder(Rectangle.NO_BORDER);
                instructionCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                instructionTable.addCell(instructionCell);
                instructionTable.setSpacingBefore(20);
                document.add(instructionTable);

//                paragraph.clear();
//                paragraph.add(new Phrase(hindi.process("\n\n   à¤¹à¤®à¥‡à¤‚ à¤µà¤¿à¤¶à¥?à¤µà¤¾à¤¸ à¤¹à¥ˆ à¤•à¤¿ à¤¹à¤®à¤¾à¤°à¥‡ à¤¸à¤¹à¤¯à¥‹à¤— à¤µ à¤¸à¤²à¤¾à¤¹ à¤¸à¥‡ à¤†à¤ªà¤•à¥€ à¤–à¥‡à¤¤à¥€ à¤•à¥€ à¤²à¤¾à¤—à¤¤ à¤®à¥‡à¤‚ à¤•à¤®à¥€ à¤µ à¤†à¤¯ à¤®à¥‡à¤‚ à¤µà¥ƒà¤¦à¥?à¤§à¤¿ à¤¹à¥‹à¤—à¥€à¥¤ à¤¹à¤® à¤†à¤ªà¤•à¥‡ à¤–à¥?à¤¶à¤¹à¤¾à¤² à¤ªà¤°à¤¿à¤µà¤¾à¤° à¤µ à¤œà¥€à¤µà¤¨ à¤•à¥€ à¤•à¤¾à¤®à¤¨à¤¾ à¤•à¤°à¤¤à¥‡ à¤¹à¥ˆà¤‚à¥¤\n" +
//                        "\n" +
//                        " \n" +
//                        "à¤§à¤¨à¥?à¤¯à¤µà¤¾à¤¦ \n"), font));
//                paragraph.setAlignment(Element.ALIGN_LEFT);
//                document.add(paragraph);
            }

            document.close();

        } catch (DocumentException ex) {

            Logger.getLogger(GeneratePdfReport.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
