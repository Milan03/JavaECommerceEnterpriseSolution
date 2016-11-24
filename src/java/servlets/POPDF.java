/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dtos.POLineItemDTO;
import dtos.ProductDTO;
import dtos.PurchaseOrderDTO;
import dtos.VendorDTO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.text.DecimalFormat;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import models.PurchaseOrderModel;
import models.ProductModel;
import models.VendorModel;

/**
 *
 * @author Milan
 */
@WebServlet(name = "POPDF", urlPatterns = {"/POPDF"})
public class POPDF extends HttpServlet {
    
    @Resource(lookup = "jdbc/Info5059db")
    DataSource ds;
    PurchaseOrderModel model = new PurchaseOrderModel();
    VendorModel venModel = new VendorModel();
    ProductModel prodModel = new ProductModel();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try {
            int pono = Integer.parseInt(request.getParameter("po"));
            PurchaseOrderDTO poDTO = model.getPO(pono, ds);
            buildpdf(poDTO, response);
        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }    
    }
    private void buildpdf(PurchaseOrderDTO dto, HttpServletResponse response) {
        Font catFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
        Font subFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD);
        Font smallBold = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        String IMG = getServletContext().getRealPath("/img/logo.png");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Document document = new Document();
        VendorDTO venDTO = venModel.getVendor(dto.getVendorno(), ds);
        DecimalFormat decimal = new DecimalFormat("#0.00");
        ArrayList<ProductDTO> products = prodModel.getProdsForVendor(dto.getVendorno(), ds);
        ArrayList<POLineItemDTO> poLineItems = dto.getItems();

        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            Paragraph preface = new Paragraph();
            // We add one empty line
            Image image1 = Image.getInstance(IMG);
            image1.setAbsolutePosition(55f, 760f);
            preface.add(image1);            
            preface.setAlignment(Element.ALIGN_RIGHT);
            // Lets write a big header
            Paragraph mainHead = new Paragraph(String.format("%55s", "Purchase Order"), catFont);
            preface.add(mainHead);
            preface.setAlignment(Element.ALIGN_LEFT);
            preface.add(new Paragraph(String.format("%82s", "PO#: " +dto.getPONumber()), subFont));
            addEmptyLine(preface, 3);
            preface.add(new Paragraph(String.format("%10s", "Vendor: ", smallBold)));
            preface.add(new Paragraph(String.format("%5s", venDTO.getName(), smallBold)));
            preface.add(new Paragraph(String.format("%5s", venDTO.getAddress1(), smallBold)));
            preface.add(new Paragraph(String.format("%5s", venDTO.getCity(), smallBold)));
            preface.add(new Paragraph(String.format("%5s", venDTO.getProvince(), smallBold)));
            preface.add(new Paragraph(String.format("%5s", venDTO.getPostalCode(), smallBold)));
            addEmptyLine(preface, 1);
            // 3 column table
            PdfPTable table = new PdfPTable(5);
            PdfPCell cell = new PdfPCell(new Paragraph("Product Code", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Product Description", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Quantity Sold", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Price", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            cell = new PdfPCell(new Paragraph("Ext Price", smallBold));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
            for (POLineItemDTO item : poLineItems) {
                cell = new PdfPCell(new Phrase(item.getproductcode()));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                for (ProductDTO prod : products) {
                    if (prod.getProductcode().equals(item.getproductcode())) {
                        cell = new PdfPCell(new Phrase(prod.getProductname()));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table.addCell(cell);
                    }
                }
                cell = new PdfPCell(new Phrase(Integer.toString(item.getQuantity())));
                cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                table.addCell(cell);
                double extPrice = 0.0;
                for (ProductDTO prod : products) {
                    if (prod.getProductcode().equals(item.getproductcode())) {
                        cell = new PdfPCell(new Phrase(Double.toString(prod.getCostprice())));
                        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
                        table.addCell(cell);
                        extPrice = prod.getCostprice() * item.getQuantity();
                    }
                }
                String extPriceStr = decimal.format(extPrice);
                cell = new PdfPCell(new Phrase( extPriceStr ));
                cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(cell);
            } //end for
            double sub = dto.getAmount();
            double tax = dto.getAmount() * 0.13;
            double total = dto.getAmount() * 1.13;
            String taxStr = decimal.format(tax);
            String totalStr = decimal.format(total);
            String subStr = decimal.format(sub);
            cell = new PdfPCell(new Phrase("Total:"));
            cell.setColspan(4);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase( subStr ));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase("Tax:"));
            cell.setColspan(4);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(taxStr));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell = new PdfPCell(new Phrase("Order Total:"));
            cell.setColspan(4);
            cell.setBorder(0);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(cell);
            cell = new PdfPCell(new Phrase(totalStr));
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setBackgroundColor(BaseColor.YELLOW);            
            table.addCell(cell);
            preface.add(table);
            addEmptyLine(preface, 3);
            preface.setAlignment(Element.ALIGN_CENTER);
            preface.add(new Paragraph(String.format("%60s", "Generated on: " +dto.getPODate()), subFont));
            document.add(preface);
            document.close();

            // setting some response headers
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");
            // setting the content type
            response.setContentType("application/pdf");
            // the contentlength
            response.setContentLength(baos.size());
            // write ByteArrayOutputStream to the ServletOutputStream
            OutputStream os = response.getOutputStream();
            baos.writeTo(os);
            os.flush();
            os.close();

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
            
    } //buildpdf

   private void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
