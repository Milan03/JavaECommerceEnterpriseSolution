/*
 * File:         POResource.java
 * Author:       Milan Sobat
 * Last Updated: October 16, 2014
 * Purpose:      REST services for interacting with Purchase Orders.
 */

package resources;

import dtos.PurchaseOrderDTO;
import dtos.PurchaseOrderEJBDTO;
import ejbs.POFacadeBean;
import java.net.URI;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import models.PurchaseOrderModel;

/**
 * REST Web Service
 *
 * @author Milan
 */
@Path("po")
public class POResource {
    @EJB
    private POFacadeBean pofb;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of POResource
     */
    public POResource() {
    }
    
    @POST
    @Consumes("application/json")
    public Response createPO(PurchaseOrderEJBDTO po) {
        int pono = pofb.addPO(po);
        URI uri = context.getAbsolutePath();
        return Response.created(uri).entity(pono).build();
    }
    
    @GET
    @Path("/{vendorno}")
    @Produces("application/json")
    public List<PurchaseOrderEJBDTO> getPOsForVendorJson(@PathParam("vendorno")int vendorno) {
        System.out.println("Inside getPOsForVendorJson");
        return pofb.getPOsForVendor(vendorno);
    }

}
