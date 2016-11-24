/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servlets;

import ejbs.VendorFacadeBean;
import java.io.IOException;
import java.io.PrintWriter;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import dtos.VendorEJBDTO;

/**
 *
 * @author Milan
 */
@WebServlet(name = "TestVendorFacadeServlet", urlPatterns = {"/TestVendorFacade"})
public class TestVendorFacadeServlet extends HttpServlet {
    @EJB
    private VendorFacadeBean vfb;

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
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet TestVendorFacadeServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet TestVendorFacadeServlet at " + request.getContextPath() + "</h1>");
            VendorEJBDTO vd = new VendorEJBDTO();
            vd.setAddress1("123 Here");
            vd.setCity("Nowhere");
            vd.setEmail("apdept@gadgethut.com");
            vd.setName("Gadget Hut");
            vd.setPhone("555-5555");
            vd.setPostalcode("n1n-1n1");
            vd.setProvince("ON");
            vd.setVendortype("trusted");
            int newID = vfb.addVendor(vd);
            out.println("New vendor row added via Facade - " +newID);
            out.println("</body>");
            out.println("</html>");
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
