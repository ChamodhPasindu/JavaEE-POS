import javax.json.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/order")
public class OrderServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String option = req.getParameter("option");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Market", "root", "root1234");
            PrintWriter writer = resp.getWriter();
            resp.setContentType("application/json");

            switch (option) {
                case "GET_ID":

                    try {
                        ResultSet rst = connection.prepareStatement("SELECT OrderId FROM Orders ORDER BY OrderId DESC LIMIT 1").executeQuery();
                        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder(); //

                        if (rst.next()) {
                            int tempId = Integer.
                                    parseInt(rst.getString(1).split("-")[1]);
                            tempId = tempId + 1;
                            System.out.println(tempId);
                            if (tempId <= 9) {
                                String id = "OID-00" + tempId;
                                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                                objectBuilder.add("id", id);
                                arrayBuilder.add(objectBuilder.build());

                            } else if (tempId <= 99) {
                                String id = "OID-0" + tempId;
                                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                                objectBuilder.add("id", id);
                                arrayBuilder.add(objectBuilder.build());

                            } else {
                                String id = "OID-" + tempId;
                                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                                objectBuilder.add("id", id);
                                arrayBuilder.add(objectBuilder.build());
                            }

                        } else {
                            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                            String id = "OID-001";
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

                case "GET_ALL_DETAILS":

                default:

            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        JsonArray orderDetail = jsonObject.getJsonArray("orderDetail");
        String orderId = jsonObject.getString("orderId");
        String customerId = jsonObject.getString("custId");
        String date = jsonObject.getString("date");
        String cost = jsonObject.getString("cost");
        String discount = jsonObject.getString("discount");

        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        Connection connection = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Market", "root", "root1234");
            connection.setAutoCommit(false);

            PreparedStatement pstm = connection.prepareStatement("INSERT INTO Orders VALUES (?,?,?,?,?)");
            pstm.setObject(1, orderId);
            pstm.setObject(2, date);
            pstm.setObject(3, customerId);
            pstm.setObject(4, cost);
            pstm.setObject(5, discount);

            if (pstm.executeUpdate() > 0) {
                for (JsonValue detail : orderDetail) {
                    PreparedStatement pstm1 = connection.prepareStatement("INSERT INTO OrderDetail VALUES (?,?,?,?)");
                    pstm1.setObject(1, orderId);
                    pstm1.setObject(2, detail.asJsonObject().getString("itemId"));
                    pstm1.setObject(3, detail.asJsonObject().getString("qty"));
                    pstm1.setObject(4, detail.asJsonObject().getString("price"));

                    if (pstm1.executeUpdate() > 0) {
                        PreparedStatement pstm2 = connection.prepareStatement("UPDATE Item SET qty=(qty+" + Integer.parseInt(detail.asJsonObject().
                                getString("qty")) + ") WHERE itemId='" + detail.asJsonObject().getString("itemId") + "'");

                        boolean isSave = pstm2.executeUpdate() > 0;
                        if (!isSave) {
                            connection.rollback();
                            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                            objectBuilder.add("status", 400);
                            objectBuilder.add("message", "Update qty Failed");
                            objectBuilder.add("data", "");
                            writer.print(objectBuilder.build());
                        }
                    } else {
                        connection.rollback();
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("status", 400);
                        objectBuilder.add("message", "Update Order Details Failed");
                        objectBuilder.add("data", "");
                        writer.print(objectBuilder.build());
                    }
                }
                connection.commit();
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 200);
                objectBuilder.add("message", "Order Placed Successfully");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            } else {
                connection.rollback();
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 400);
                objectBuilder.add("message", "Update Order Failed");
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
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
