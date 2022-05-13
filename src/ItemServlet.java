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

@WebServlet(urlPatterns = "/item")
public class ItemServlet extends HttpServlet {

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
                    try {
                        String itemId=req.getParameter("itemId");
                        PreparedStatement pstm = connection.prepareStatement("select * from Item where ItemId=?");
                        pstm.setObject(1,itemId);
                        ResultSet rst = pstm.executeQuery();

                        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder(); //


                        if (rst.next()) {
                            String id = rst.getString(1);
                            String name = rst.getString(2);
                            double price = rst.getDouble(3);
                            String qty = rst.getString(4);

                            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                            objectBuilder.add("id", id);
                            objectBuilder.add("name", name);
                            objectBuilder.add("price", price);
                            objectBuilder.add("qty", qty);
                            arrayBuilder.add(objectBuilder.build());

                            JsonObjectBuilder response = Json.createObjectBuilder();
                            response.add("status", 200);
                            response.add("message", "Done");
                            response.add("data", arrayBuilder.build());
                            writer.print(response.build());

                        }else{
                            JsonObjectBuilder response = Json.createObjectBuilder();
                            response.add("status", 400);
                            response.add("message", "Error");
                            response.add("data", arrayBuilder.build());
                            writer.print(response.build());
                        }

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case "GET_ALL_DETAILS":
                    try {
                        ResultSet rst = connection.prepareStatement("select * from Item").executeQuery();
                        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder(); //

                        while (rst.next()) {
                            String id = rst.getString(1);
                            String name = rst.getString(2);
                            double price = rst.getDouble(3);
                            String qty = rst.getString(4);

                            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                            objectBuilder.add("id", id);
                            objectBuilder.add("name", name);
                            objectBuilder.add("price", price);
                            objectBuilder.add("qty", qty);
                            arrayBuilder.add(objectBuilder.build());
                        }
                        JsonObjectBuilder response = Json.createObjectBuilder();
                        response.add("status", 200);
                        response.add("message", "Done");
                        response.add("data", arrayBuilder.build());
                        writer.print(response.build());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case "GET_ALL_ID":
                    try {
                        ResultSet rst = connection.prepareStatement("select itemId from Item").executeQuery();
                        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder(); //

                        while (rst.next()) {
                            String id = rst.getString(1);

                            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                            objectBuilder.add("id", id);
                            arrayBuilder.add(objectBuilder.build());
                        }
                        JsonObjectBuilder response = Json.createObjectBuilder();
                        response.add("status", 200);
                        response.add("message", "Done");
                        response.add("data", arrayBuilder.build());
                        writer.print(response.build());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;

                case "COUNT":
                    try {
                        ResultSet rst = connection.prepareStatement("select count(itemId) from Item").executeQuery();
                        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder(); //

                        while (rst.next()) {
                            String count = rst.getString(1);

                            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                            objectBuilder.add("count", count);
                            arrayBuilder.add(objectBuilder.build());
                        }
                        JsonObjectBuilder response = Json.createObjectBuilder();
                        response.add("status", 200);
                        response.add("message", "Done");
                        response.add("data", arrayBuilder.build());
                        writer.print(response.build());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;


            }
            connection.close();

        } catch (SQLException e) {
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

        try {
            Connection connection = ds.getConnection();

            PreparedStatement pstm = connection.prepareStatement("Update Item set itemName=?,unitPrice=?,qty=? where itemId=?");
            pstm.setObject(1, itemName);
            pstm.setObject(2, itemPrice);
            pstm.setObject(3, itemQty);
            pstm.setObject(4, itemId);
            if (pstm.executeUpdate() > 0) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 200);
                objectBuilder.add("message", "Successfully Updated");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            } else {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 400);
                objectBuilder.add("message", "Update Failed");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            }
            connection.close();
        } catch (SQLException throwables) {
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

        try {
            Connection connection = ds.getConnection();

            PreparedStatement pstm=connection.prepareStatement("INSERT INTO Item VALUES (?,?,?,?)");
            pstm.setObject(1,id);
            pstm.setObject(2,name);
            pstm.setObject(3,price);
            pstm.setObject(4,qty);

            if (pstm.executeUpdate()>0){
                JsonObjectBuilder response= Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_CREATED);
                response.add("status",200);
                response.add("message","Successfully Added");
                response.add("data","");
                writer.print(response.build());

            }
            connection.close();

        } catch (SQLException e) {
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


            PreparedStatement pstm=connection.prepareStatement("delete from Item where itemId=?");
            pstm.setObject(1,itemId);

            boolean b = pstm.executeUpdate() > 0;
            PrintWriter writer=resp.getWriter();

            if (b){
                writer.write("Item Deleted");
            }
            connection.close();

        } catch (SQLException e) {
            resp.sendError(500, e.getMessage());
            e.printStackTrace();
        }
    }
}
