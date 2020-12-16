package com.bharuwa.haritkranti.qrCode;

import com.bharuwa.haritkranti.exceptionHandler.CustomException;
import com.bharuwa.haritkranti.msg91.Msg91Services;

import static com.bharuwa.haritkranti.utils.Constants.FARMER_APP_LINK;

/**
 * @author anuragdhunna
 */
public class HtmlToPdf {

    private static final String PDF_PATH = "./Test.pdf";

    public static void main(String[] args) {

        String appLinkMessage = "प�?रिय किसान, आपका अन�?नदाता �?प में स�?वागत है। \n" +
                "\n" +
                "कृपया नीचे दि�? ग�? लिंक पे क�?लिक करके अपने मोबाइल पर �?प इनस�?टॉल करें " + FARMER_APP_LINK;
        String response = null;
        try {
            response = Msg91Services.sendFarmerAppLinkMsg(appLinkMessage, "8699395100");
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(response);
        }

//
//        try {
//            OutputStream file = new FileOutputStream(new File(PDF_PATH));
//            Document document = new Document();
//            PdfWriter.getInstance(document, file);
//            document.open();
//            HTMLWorker htmlWorker = new HTMLWorker(document);
//            htmlWorker.parse(new StringReader(html));
//            document.close();
//            file.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


    }

    private static final String html = "\n" +
            "<html>\n" +
            "<head>\n" +
            "<meta charset=\"utf-8\">\n" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "<title>CERTIFICATE</title>\n" +
            "</style>\n" +
            "</head>\n" +
            "<body yahoo=\"yahoo\">\n" +
            "<table width=\"100%\"  cellspacing=\"0\" cellpadding=\"0\">\n" +
            "  <tbody>\n" +
            "    <tr>\n" +
            "      <td><table width=\"1200\"  align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "          <tbody>\n" +
            "            \n" +
            "            <tr> \n" +
            "              <td><table width=\"96%\"  align=\"left\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "                  <tr> \n" +
            "                    <td align=\"center\" style=\"font-size: 40px; font-weight: 300; line-height: 2.5em;  font-family: sans-serif;\"><strong>PATANJALI ORGANIC CROP CERTIFICATE</strong></td>\n" +
            "                  </tr>\n" +
            "                  <tr> \n" +
            "                    <td align=\"center\" style=\"font-size: 16px; font-weight:300;  font-family: sans-serif;\">The world first Institution that gives <strong>ORGANIC CERTIFICATE</strong> on the basis of Testing of <strong>CROP and SOIL TESTING</strong> Report</td>\n" +
            "                  </tr>\n" +
            "                  <tr>\n" +
            "                    <td style=\"font-size: 0; line-height: 0;\" height=\"20\"><table width=\"100%\" align=\"left\"  cellpadding=\"0\" cellspacing=\"0\">\n" +
            "                        <tr> \n" +
            "                          <td style=\"font-size: 0; line-height: 0;\" height=\"10\">&nbsp;</td>\n" +
            "                        </tr>\n" +
            "                      </table></td>\n" +
            "                  </tr>\n" +
            "                  <tr > \n" +
            "                    <td  align=\"center\"  style=\"font-size: 18px; font-style: normal; font-weight: 100;  line-height: 1.8; padding:10px 20px 0px 20px; font-family: sans-serif;\"><strong>CERTIFIED ORGANIC CRO</strong>P on the basis of report of the <strong>EVALUATION COMMITTEE</strong>, conducted evaluation of Certification process and has found the detail mentioned and reports are in accordance with the Accreditation Criteria. Accordingly the National Accreditation Body has <strong>APPROVED</strong> the certificate as per details given:</td>\n" +
            "                  </tr>\n" +
            "                </table></td>\n" +
            "            </tr>\n" +
            "\t\t\t  \n" +
            "\t\t\t  \n" +
            "\t\t\t   <tr >\n" +
            "              <td align=\"center\" >\n" +
            "\t\t\t\t  <div style=\"width: 80%\">\n" +
            "\t\t\t\t\t  <table class=\"footer\" width=\"50%\"  align=\"left\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "                  <tr>\n" +
            "                    <td><p align=\"left\"  style=\"font-size: 18px; font-weight:300; line-height: 2.5em;  font-family: sans-serif;\"><strong>Certificate No:</strong>……………………………………</p>\n" +
            "\t\t\t\t\t\t<p align=\"left\"  style=\"font-size:18px; font-weight:300; line-height: 2.5em;  font-family: sans-serif;\"><strong>Valid up to:</strong>…………………………………………</p>\n" +
            "                    </td>\n" +
            "                  </tr>\n" +
            "                </table>\n" +
            "                <table class=\"footer\" width=\"50%\"  align=\"left\" cellpadding=\"0\" cellspacing=\"0\">\n" +
            "                  <tr>\n" +
            "                  <td><p align=\"left\"   style=\"font-size: 18px; font-weight:300; line-height: 2.5em;  font-family: sans-serif;\"><strong>Total Certified Area: </strong>…………………………………………</p>\n" +
            "\t\t\t\t\t  <p align=\"left\"   style=\"font-size: 18px; font-weight:300; line-height: 2.5em;  font-family: sans-serif;\"><strong>Scope of Accreditation: </strong>……………………………………</p>\n" +
            "                    </td>\n" +
            "                  </tr>\n" +
            "                </table>\n" +
            "\t\t\t\t   </div></td>\n" +
            "            </tr>\n" +
            "            <tr> \n" +
            "              <td style=\"font-size: 0; line-height: 0;\" height=\"10\"><table width=\"100%\" align=\"left\"  cellpadding=\"0\" cellspacing=\"0\">\n" +
            "                  <tr>\n" +
            "                    <td style=\"font-size: 0; line-height: 0;\" height=\"6\">&nbsp;</td>\n" +
            "                  </tr>\n" +
            "                </table></td>\n" +
            "            </tr>\n" +
            "\t\t\t  \n" +
            "            <tr>\n" +
            "              <td><table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"84%\" style=\"margin-left:12.5%\" class=\"catalog\">\n" +
            "                  <tr>\n" +
            "                    <td ><table class =\"responsive-table\" width=\"48%\"  cellspacing=\"0\" cellpadding=\"0\" align=\"left\" style=\"margin: 10px 0px 10px 0px;\">\n" +
            "                        <tbody>\n" +
            "                          <tr>\n" +
            "                            <td>\n" +
            "                              <table class=\"table.responsiveContent\" width=\"100%\"  cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
            "                                <tbody align=\"center\" style=\"font-size: 18px; font-weight:300; line-height: 1em;  font-family: sans-serif;\">\n" +
            "                                  <tr>\n" +
            "                                    <td><p >………………………………………<br><strong>Signature of issuing Authority</strong></p>\n" +
            "\t\t\t\t\t\t\t\t\t\t\n" +
            "                                     </td>\n" +
            "                                  </tr>\n" +
            "                                </tbody>\n" +
            "                              </table></td>\n" +
            "                          </tr>\n" +
            "                        </tbody>\n" +
            "                      </table>\n" +
            "                      <table class =\"responsive-table\" width=\"48%\"  cellspacing=\"0\" cellpadding=\"0\" align=\"left\" style=\"margin: 10px 0px 10px 0px;\">\n" +
            "                        <tbody>\n" +
            "                          <tr>\n" +
            "                            <td>\n" +
            "                               <table class=\"table.responsiveContent\" width=\"100%\"  cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
            "                                <tbody align=\"center\" style=\"font-size: 18px; font-weight:300; line-height: 1em;  font-family: sans-serif;\">\n" +
            "                                  <tr>\n" +
            "                                    <td><p>………………………………………<br><strong>Issu Date</strong></p>\n" +
            "\t\t\t\t\t\t\t\t\t\t\n" +
            "                                     </td>\n" +
            "                                  </tr>\n" +
            "                                </tbody>\n" +
            "                              </table></td>\n" +
            "                          </tr>\n" +
            "                        </tbody>\n" +
            "                      </table></td>\n" +
            "                  </tr>\n" +
            "\t\t\t\t  \n" +
            "\t\t\t\t  \n" +
            "                </table></td>\n" +
            "            </tr>\n" +
            "            <tr> \n" +
            "              <td style=\"font-size: 0; line-height: 0;\" height=\"20\"><table width=\"96%\" align=\"left\"  cellpadding=\"0\" cellspacing=\"0\">\n" +
            "                  <tr>\n" +
            "                    <td style=\"font-size: 0; line-height: 0;\" height=\"20\">&nbsp;</td>\n" +
            "                  </tr>\n" +
            "                </table></td>\n" +
            "            </tr>\n" +
            "\t\t\t  \n" +
            "          <tr> \n" +
            "                    <td align=\"center\" style=\"font-size: 18px; font-weight:300;  font-family: sans-serif;\">This  certificate is issued in accordance with Public <strong>Notice No:</strong> ……………………….</td>\n" +
            "                  </tr>\n" +
            "\t\t\t  \n" +
            "\t\t\t  <tr>\n" +
            "              <td><table cellpadding=\"0\" cellspacing=\"0\" align=\"center\" width=\"84%\" style=\"margin-left:12.5%\" class=\"catalog\">\n" +
            "                  <tr>\n" +
            "                    <td ><table class =\"responsive-table\" width=\"48%\"  cellspacing=\"0\" cellpadding=\"0\" align=\"left\" style=\"margin: 10px 0px 10px 0px;\">\n" +
            "                        <tbody>\n" +
            "                          <tr>\n" +
            "                            <td>\n" +
            "                              <table class=\"table.responsiveContent\" width=\"100%\"  cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
            "                                <tbody align=\"center\" style=\"font-size: 18px; font-weight:300; line-height: 1em;  font-family: sans-serif;\">\n" +
            "                                  <tr>\n" +
            "                                    <td><p ><strong>Dated:</strong>…………………………</p>\n" +
            "\t\t\t\t\t\t\t\t\t\t\n" +
            "                                     </td>\n" +
            "                                  </tr>\n" +
            "                                </tbody>\n" +
            "                              </table></td>\n" +
            "                          </tr>\n" +
            "                        </tbody>\n" +
            "                      </table>\n" +
            "                      <table class =\"responsive-table\" width=\"35%\"  cellspacing=\"0\" cellpadding=\"0\" align=\"left\" style=\"margin: 10px 0px 10px 0px;\">\n" +
            "                        <tbody>\n" +
            "                          <tr>\n" +
            "                            <td>\n" +
            "                               <table class=\"table.responsiveContent\" width=\"100%\"  cellspacing=\"0\" cellpadding=\"0\" align=\"center\">\n" +
            "                                <tbody align=\"center\" style=\"font-size: 18px; font-weight:300; line-height: 1em;  font-family: sans-serif;\">\n" +
            "                                  <tr>\n" +
            "                                    <td><p><strong>Issued by:</strong>…………………………</p>\n" +
            "\t\t\t\t\t\t\t\t\t\t\n" +
            "                                     </td>\n" +
            "                                  </tr>\n" +
            "                                </tbody>\n" +
            "                              </table></td>\n" +
            "                          </tr>\n" +
            "                        </tbody>\n" +
            "                      </table></td>\n" +
            "                  </tr>\n" +
            "\t\t\t\t  \n" +
            "\t\t\t\t  \n" +
            "                </table></td>\n" +
            "            </tr>\n" +
            "  </tbody>\n" +
            "</table>\n" +
            "\t\t  </td>\n" +
            "\t\t</tr>\n" +
            "\t  </tbody>\n" +
            "\t</table>\n" +
            "</body>\n" +
            "</html>\n";
}
