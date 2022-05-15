package servlet;

import bo.BOFactory;
import bo.custom.ItemBO;
import dto.ItemDTO;
import util.Builder;

import javax.annotation.Resource;
import javax.json.*;
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

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource ds;

    ItemBO itemBO = (ItemBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.ITEM);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        try {
            String option = req.getParameter("option");
            Connection connection = ds.getConnection();


            switch (option) {
                case "SEARCH":

                    String itemId = req.getParameter("itemId");
                    ItemDTO itemDTO = itemBO.searchItem(connection, itemId);

                    JsonArrayBuilder arrayBuilderSearch = Json.createArrayBuilder();

                    if (itemDTO != null) {

                        JsonObjectBuilder searchItem = Json.createObjectBuilder()
                                .add("id", itemDTO.getId())
                                .add("name", itemDTO.getName())
                                .add("price", itemDTO.getPrice())
                                .add("qty", itemDTO.getQty());
                        arrayBuilderSearch.add(searchItem);

                        Builder.getResponseBuilder(200, "Done", arrayBuilderSearch, writer);
                    } else {
                        Builder.getResponseBuilder(400, "Wrong ID inserted", arrayBuilderSearch, writer);
                    }

                    break;

                case "GET_ALL_DETAILS":

                    ArrayList<ItemDTO> itemDetails = itemBO.getAllItemDetails(connection);
                    JsonArrayBuilder arrayBuilderGetAll = Json.createArrayBuilder();

                    for (ItemDTO dto : itemDetails) {
                        JsonObjectBuilder allItemsDetails = Json.createObjectBuilder()
                                .add("id", dto.getId())
                                .add("name", dto.getName())
                                .add("price", dto.getPrice())
                                .add("qty", dto.getQty());
                        arrayBuilderGetAll.add(allItemsDetails);
                    }

                    Builder.getResponseBuilder(200, "Done", arrayBuilderGetAll, writer);
                    break;

                case "GET_ALL_ID":

                    ArrayList<String> allItemIds = itemBO.getAllItemIds(connection);
                    JsonArrayBuilder arrayBuilderGetId = Json.createArrayBuilder();

                    for (String id : allItemIds) {
                        JsonObjectBuilder allIds = Json.createObjectBuilder()
                                .add("id", id);
                        arrayBuilderGetId.add(allIds);
                    }

                    Builder.getResponseBuilder(200, "Done", arrayBuilderGetId, writer);
                    break;

                case "COUNT":

                    String count = itemBO.getItemCount(connection);
                    JsonArrayBuilder arrayBuilderCount = Json.createArrayBuilder();

                    JsonObjectBuilder itemCount = Json.createObjectBuilder()
                            .add("count", count);
                    arrayBuilderCount.add(itemCount);

                    Builder.getResponseBuilder(200, "Done", arrayBuilderCount, writer);
                    break;
            }
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            Builder.getResponseBuilder(500, e.getLocalizedMessage(), null, writer);
            e.printStackTrace();
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String itemId = jsonObject.getString("id");
        String itemName = jsonObject.getString("name");
        String itemPrice = jsonObject.getString("price");
        String itemQty = jsonObject.getString("qty");

        ItemDTO itemDTO = new ItemDTO(itemId, itemName, Double.parseDouble(itemPrice), Integer.parseInt(itemQty));

        try {
            Connection connection = ds.getConnection();
            if (itemBO.updateItem(connection, itemDTO)) {
                Builder.getResponseBuilder(200, "Successfully Updated", null, writer);
            } else {
                Builder.getResponseBuilder(400, "Update Failed", null, writer);
            }
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            Builder.getResponseBuilder(500, e.getLocalizedMessage(), null, writer);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        String id = req.getParameter("itemId");
        String name = req.getParameter("itemName");
        String price = req.getParameter("itemPrice");
        String qty = req.getParameter("itemQty");

        ItemDTO itemDTO = new ItemDTO(id, name, Double.parseDouble(price), Integer.parseInt(qty));

        try {
            Connection connection = ds.getConnection();
            if (itemBO.addItem(itemDTO, connection)) {
                Builder.getResponseBuilder(200, "Successfully Added", null, writer);
            } else {
                Builder.getResponseBuilder(400, "Item Added Failed", null, writer);
            }
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            Builder.getResponseBuilder(500, e.getLocalizedMessage(), null, writer);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        String itemId = req.getParameter("itemId");

        try {
            Connection connection = ds.getConnection();
            if (itemBO.deleteItem(connection, itemId)) {
                Builder.getResponseBuilder(200, "Item Deleted Successfully", null, writer);
            } else {
                Builder.getResponseBuilder(200, "Item Deleted Fail", null, writer);
            }
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            Builder.getResponseBuilder(500, e.getLocalizedMessage(), null, writer);
            e.printStackTrace();
        }
    }
}
