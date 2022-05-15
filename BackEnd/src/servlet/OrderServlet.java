package servlet;

import bo.BOFactory;
import bo.custom.PlaceOrderBO;
import dao.DAOFactory;
import dao.custom.OrderDAO;
import dto.CustomDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;

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

@WebServlet(urlPatterns = "/order")
public class OrderServlet extends HttpServlet {

    PlaceOrderBO placeOrderBO=(PlaceOrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PLACE_ORDER);

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource ds;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String option = req.getParameter("option");
            Connection connection = ds.getConnection();
            PrintWriter writer = resp.getWriter();
            resp.setContentType("application/json");

            switch (option) {
                case "SEARCH":
                    String searchOrderId = req.getParameter("orderId");
                    CustomDTO detailObject = placeOrderBO.getDetailObject(connection, searchOrderId);

                    JsonArrayBuilder arrayBuilderSearch = Json.createArrayBuilder();

                    if (detailObject!=null){
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("id", detailObject.getOrderId());
                        objectBuilder.add("name", detailObject.getCustName());
                        objectBuilder.add("date", detailObject.getDate());
                        objectBuilder.add("discount", detailObject.getDiscount());
                        objectBuilder.add("cost", detailObject.getDiscount());
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
                case "GET_ID":

                    String orderId = placeOrderBO.createOrderId(connection);

                    JsonObjectBuilder objectBuilderId = Json.createObjectBuilder();
                    JsonArrayBuilder arrayBuilderId = Json.createArrayBuilder();
                    objectBuilderId.add("id", orderId);
                    arrayBuilderId.add(objectBuilderId.build());

                    JsonObjectBuilder response1 = Json.createObjectBuilder();
                    response1.add("status", 200);
                    response1.add("message", "Done");
                    response1.add("data", arrayBuilderId.build());
                    writer.print(response1.build());


                    break;

                case "GET_ALL_DETAILS":

                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();

                    ArrayList<CustomDTO> detailArray = placeOrderBO.getDetailArray(connection);
                    for (CustomDTO dto:detailArray) {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("id", dto.getOrderId());
                        objectBuilder.add("name", dto.getCustName());
                        objectBuilder.add("date", dto.getDate());
                        objectBuilder.add("discount", dto.getDiscount());
                        objectBuilder.add("cost", dto.getCost());
                        arrayBuilder.add(objectBuilder.build());
                    }
                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("status", 200);
                    response.add("message", "Done");
                    response.add("data", arrayBuilder.build());
                    writer.print(response.build());

                    break;
                case "COUNT":

                    String count = placeOrderBO.getOrderCount(connection);

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

        System.out.println("doPost ekata awa");
        ArrayList<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();
        for (JsonValue detail : orderDetail) {

            orderDetailDTOS.add(new OrderDetailDTO(orderId,detail.asJsonObject().getString("itemId"), detail.asJsonObject().getInt("qty"),
                    Double.parseDouble(detail.asJsonObject().getString("price"))));
        }

        OrderDTO orderDTO = new OrderDTO(orderDetailDTOS,orderId,customerId,date,Double.parseDouble(cost),Integer.parseInt(discount));
        try {
            Connection connection = ds.getConnection();
            if (placeOrderBO.placeOrder(orderDTO,connection)) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 200);
                objectBuilder.add("message", "Order Placed Successfully");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            }else {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 400);
                objectBuilder.add("message", "Update Order Failed");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String orderId = req.getParameter("orderId");


        try {
            Connection connection = ds.getConnection();
            if (placeOrderBO.deleteOrder(connection,orderId)) {
                PrintWriter writer = resp.getWriter();
                writer.write("Order Deleted");
            }
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            resp.sendError(500, e.getMessage());
            e.printStackTrace();
        }

    }
}
