package servlet;

import bo.BOFactory;
import bo.custom.ItemBO;
import dao.custom.ItemDAO;
import dto.CustomerDTO;
import dto.ItemDTO;

import javax.annotation.Resource;
import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {

    ItemBO itemBO=(ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource ds;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Connection connection = ds.getConnection();
            String option=req.getParameter("option");
            PrintWriter writer=resp.getWriter();
            resp.setContentType("application/json");


            switch (option){
                case "SEARCH":

                    String itemId=req.getParameter("itemId");
                    JsonArrayBuilder arrayBuilderSearch = Json.createArrayBuilder();
                    ItemDTO itemDTO = itemBO.searchItem(connection, itemId);

                    if (itemDTO!=null) {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("id", itemDTO.getId());
                        objectBuilder.add("name", itemDTO.getName());
                        objectBuilder.add("price", itemDTO.getPrice());
                        objectBuilder.add("qty", itemDTO.getQty());
                        arrayBuilderSearch.add(objectBuilder.build());

                        JsonObjectBuilder response = Json.createObjectBuilder();
                        response.add("status", 200);
                        response.add("message", "Done");
                        response.add("data", arrayBuilderSearch.build());
                        writer.print(response.build());
                    }else {
                        JsonObjectBuilder response = Json.createObjectBuilder();
                        response.add("status", 400);
                        response.add("message", "Error");
                        response.add("data", arrayBuilderSearch.build());
                        writer.print(response.build());
                    }


                    break;

                case "GET_ALL_DETAILS":
                    ArrayList<ItemDTO> itemDetails = itemBO.getAllItemDetails(connection);
                    JsonArrayBuilder arrayBuilderGetAll = Json.createArrayBuilder();
                    for (ItemDTO dto:itemDetails) {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("id", dto.getId());
                        objectBuilder.add("name", dto.getName());
                        objectBuilder.add("price", dto.getPrice());
                        objectBuilder.add("qty", dto.getQty());
                        arrayBuilderGetAll.add(objectBuilder.build());
                    }
                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("status", 200);
                    response.add("message", "Done");
                    response.add("data", arrayBuilderGetAll.build());
                    writer.print(response.build());
                    break;

                case "GET_ALL_ID":

                    ArrayList<String> allItemIds = itemBO.getAllItemIds(connection);
                    JsonArrayBuilder arrayBuilderGetId = Json.createArrayBuilder();
                    for (String id :allItemIds) {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("id", id);
                        arrayBuilderGetId.add(objectBuilder.build());
                    }
                    JsonObjectBuilder response1 = Json.createObjectBuilder();
                    response1.add("status", 200);
                    response1.add("message", "Done");
                    response1.add("data", arrayBuilderGetId.build());
                    writer.print(response1.build());
                    break;

                case "COUNT":

                    String count = itemBO.getItemCount(connection);

                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    JsonArrayBuilder arrayBuilderCount = Json.createArrayBuilder();
                    objectBuilder.add("count", count);
                    arrayBuilderCount.add(objectBuilder.build());

                    JsonObjectBuilder response2 = Json.createObjectBuilder();
                    response2.add("status", 200);
                    response2.add("message", "Done");
                    response2.add("data", arrayBuilderCount.build());
                    writer.print(response2.build());
                    break;


            }
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String itemId = jsonObject.getString("id");
        String itemName = jsonObject.getString("name");
        String itemPrice = jsonObject.getString("price");
        String itemQty = jsonObject.getString("qty");
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        ItemDTO itemDTO = new ItemDTO(itemId,itemName,Double.parseDouble(itemPrice),Integer.parseInt(itemQty));

        try {
            Connection connection = ds.getConnection();
            if (itemBO.updateItem(connection,itemDTO)) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 200);
                objectBuilder.add("message", "Successfully Updated");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            }else {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 400);
                objectBuilder.add("message", "Update Failed");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            }

            connection.close();
        } catch (SQLException | ClassNotFoundException throwables) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("status", 500);
            objectBuilder.add("message", "Update Failed");
            objectBuilder.add("data", throwables.getLocalizedMessage());
            writer.print(objectBuilder.build());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id=req.getParameter("itemId");
        String name=req.getParameter("itemName");
        String price=req.getParameter("itemPrice");
        String qty=req.getParameter("itemQty");

        PrintWriter writer=resp.getWriter();
        resp.setContentType("application/json");

        ItemDTO itemDTO = new ItemDTO(id,name,Double.parseDouble(price),Integer.parseInt(qty));

        try {
            Connection connection = ds.getConnection();
            if (itemBO.addItem(itemDTO,connection)) {
                JsonObjectBuilder response= Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_CREATED);
                response.add("status",200);
                response.add("message","Successfully Added");
                response.add("data","");
                writer.print(response.build());
            }
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            JsonObjectBuilder response=Json.createObjectBuilder();
            response.add("status",400);
            response.add("message","Error");
            response.add("data",e.getLocalizedMessage());
            writer.print(response.build());
            resp.setStatus(HttpServletResponse.SC_CREATED);
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String itemId=req.getParameter("itemId");

        try {
            Connection connection = ds.getConnection();
            PrintWriter writer=resp.getWriter();

            if (itemBO.deleteItem(connection,itemId)) {
                writer.write("Item Deleted");
            }

            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            resp.sendError(500, e.getMessage());
            e.printStackTrace();
        }
    }
}
