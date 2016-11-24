/*
 * File:         ProductResource.java
 * Author:       Milan Sobat
 * Last Updated: October 16, 2014
 * Purpose:      REST services for interacting with Products.
 */

package resources;

import dtos.ProductDTO;
import dtos.ProductEJBDTO;
import ejbs.ProductFacadeBean;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.ProductModel;

/**
 * REST Web Service
 *
 * @author Milan
 */
@Path("product")
public class ProductResource {
    @EJB
    private ProductFacadeBean pfb;

    @Context
    private UriInfo context;
    
    
    /**
     * Creates a new instance of ProductResource
     */
    public ProductResource() {
    }
    
    @POST
    @Consumes("application/json")
    public Response createProductFromJson(ProductEJBDTO prod) {
        String newCode = pfb.addProduct(prod);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(newCode).build();
    }
    
    @GET
    @Path("products")
    @Produces("application/json")
    public List<ProductEJBDTO> getProducts() {
        List<ProductEJBDTO> products = pfb.getProducts();
        return products;
    }
    
    @GET
    @Path("/{vendorno}")
    @Produces("application/json")
    public List<ProductEJBDTO> getProductsForVendorJson(@PathParam("vendorno")int vendorno) {
        return pfb.getAllProductsForVendor(vendorno);
    }
    
    @DELETE
    @Path("/{productcode}")
    @Consumes("application/json")
    public Response deleteVendorFromJson(@PathParam("productcode")String prodCode) {
        int rowsDeleted = pfb.deleteProduct(prodCode);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(rowsDeleted).build();
    }
    
    @PUT
    @Consumes("application/json")
    public Response updateProductFromJson(ProductEJBDTO product) {
        int numRowsUpdated = pfb.updateProduct(product);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(numRowsUpdated).build();
    }
    
}
