package controllers;

import api.CreateReceiptRequest;
import api.CreateTagRequest;
import api.ReceiptResponse;
import api.TagResponse;
import dao.ReceiptDao;
import generated.tables.records.ReceiptTagRecord;
import generated.tables.records.ReceiptsRecord;
import io.dropwizard.jersey.sessions.Session;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("")
@Produces(MediaType.APPLICATION_JSON)
public class TagController {
    final ReceiptDao receipts;

    public TagController(ReceiptDao receipts) {
        this.receipts = receipts;
    }
    // You can specify additional @Path steps; they will be relative
    // to the @Path defined at the class level
    @GET
    @Path("/netid")
    public String netId() {
        return "mb2589" ;
    }
    @PUT
    @Path("/tags/{tag}")
    public void toggleTag(@PathParam("tag") String tagName,int id) {
        receipts.insert_tag(tagName,id);
        return ;
    }
    @GET
    @Path("/tags/{tag}")
    public List<ReceiptResponse> getTaggedReceipts(@PathParam("tag") String tagName) {
        List<ReceiptsRecord> receiptTagRecords = receipts.getAllTags(tagName);
        return receiptTagRecords.stream().map(ReceiptResponse::new).collect(toList());
    }
    @GET
    @Path("/tags/")
    public List<TagResponse> getAllTags() {
        List<ReceiptTagRecord> TagRecords = receipts.getAllTagsId();
        return TagRecords.stream().map(TagResponse::new).collect(toList());
    }
}
