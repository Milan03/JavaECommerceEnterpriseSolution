/*
 * File:         VendorResource.java
 * Author:       Milan Sobat
 * Last Updated: October 16, 2014
 * Purpose:      REST services for interacting with Vendors.
 */

package resources;

import dtos.VendorDTO;
import dtos.VendorEJBDTO;
import ejbs.VendorFacadeBean;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.naming.NamingException;
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
import models.VendorModel;

/**
 * REST Web Service
 *
 * @author Milan
 */
@Path("vendor")
public class VendorResource {
    @EJB
    private VendorFacadeBean vfb;

    @Context
    private UriInfo context;
    
//    @Resource(lookup = "jdbc/Info5059db")
//    DataSource ds;

    /**
     * Creates a new instance of VendorResource
     */
    public VendorResource() {
    }
    
    @POST
    @Consumes("application/json")
    public Response createVendorFromJson(VendorEJBDTO vendor) {
        int newID = vfb.addVendor(vendor);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(newID).build();
    }
    
    @GET
    @Path("vendors")
    @Produces("application/json")
    public List<VendorEJBDTO> getVendorsJson() {
        List<VendorEJBDTO> vendors = vfb.getVendors();
        return vendors;
    }
    
    @PUT
    @Consumes("application/json")
    public Response updateVendorFromJson(VendorEJBDTO vendor) {
        int numRowsUpdated = vfb.updateVendor(vendor);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(numRowsUpdated).build();
    }
    
    @DELETE
    @Path("/{vendorno}")
    @Consumes("application/json")
    public Response deleteVendorFromJson(@PathParam("vendorno")int vendorno) {
        int rowsDeleted = vfb.deleteVendor(vendorno);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(rowsDeleted).build();
    }
}
