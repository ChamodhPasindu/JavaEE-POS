package servlet;

import bo.BOFactory;
import bo.custom.PlaceOrderBO;
import dto.CustomDTO;
import dto.OrderDTO;
import dto.OrderDetailDTO;
import util.Builder;

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

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource ds;

    PlaceOrderBO placeOrderBO = (PlaceOrderBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.PLACE_ORDER);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        try {
            String option = req.getParameter("option");
            Connection connection = ds.getConnection();

            switch (option) {
                case "SEARCH":
                    String searchOrderId = req.getParameter("orderId");
                    CustomDTO detailObject = placeOrderBO.getDetailObject(connection, searchOrderId);

                    JsonArrayBuilder arrayBuilderSearch = Json.createArrayBuilder();

                    if (detailObject != null) {
                        JsonObjectBuilder searchOrder = Json.createObjectBuilder()
                                .add("id", detailObject.getOrderId())
                                .add("name", detailObject.getCustName())
                                .add("date", detailObject.getDate())
                                .add("discount", detailObject.getDiscount())
                                .add("cost", detailObject.getCost());
                        arrayBuilderSearch.add(searchOrder);

                        Builder.getResponseBuilder(200, "Done", arrayBuilderSearch, writer);
                    } else {
                        Builder.getResponseBuilder(400, "Wrong ID inserted", null, writer);
                    }

                    break;
                case "GET_ID":

                    String orderId = placeOrderBO.createOrderId(connection);
                    JsonArrayBuilder arrayBuilderId = Json.createArrayBuilder();

                    JsonObjectBuilder generatedOrderId = Json.createObjectBuilder()
                            .add("id", orderId);
                    arrayBuilderId.add(generatedOrderId);

                    Builder.getResponseBuilder(200, "Done", arrayBuilderId, writer);

                    break;

                case "GET_ALL_DETAILS":

                    ArrayList<CustomDTO> detailArray = placeOrderBO.getDetailArray(connection);
                    JsonArrayBuilder arrayBuilderGetDetail = Json.createArrayBuilder();

                    for (CustomDTO dto : detailArray) {
                        JsonObjectBuilder allOrderDetails = Json.createObjectBuilder()
                                .add("id", dto.getOrderId())
                                .add("name", dto.getCustName())
                                .add("date", dto.getDate())
                                .add("discount", dto.getDiscount())
                                .add("cost", dto.getCost());
                        arrayBuilderGetDetail.add(allOrderDetails);
                    }

                    Builder.getResponseBuilder(200, "Done", arrayBuilderGetDetail, writer);

                    break;
                case "COUNT":

                    String count = placeOrderBO.getOrderCount(connection);
                    JsonArrayBuilder arrayBuilderCount = Json.createArrayBuilder();

                    JsonObjectBuilder orderCount = Json.createObjectBuilder()
                            .add("count", count);
                    arrayBuilderCount.add(orderCount);

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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        JsonArray orderDetail = jsonObject.getJsonArray("orderDetail");
        String orderId = jsonObject.getString("orderId");
        String customerId = jsonObject.getString("custId");
        String date = jsonObject.getString("date");
        String cost = jsonObject.getString("cost");
        String discount = jsonObject.getString("discount");

        ArrayList<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();
        for (JsonValue detail : orderDetail) {

            orderDetailDTOS.add(new OrderDetailDTO(orderId, detail.asJsonObject().getString("itemId"),
                    detail.asJsonObject().getInt("qty"), Double.parseDouble(detail.asJsonObject().getString("price"))));
        }

        OrderDTO orderDTO = new OrderDTO(orderDetailDTOS, orderId, customerId, date, Double.parseDouble(cost),
                Integer.parseInt(discount));
        try {

            Connection connection = ds.getConnection();
            if (placeOrderBO.placeOrder(orderDTO, connection)) {
                Builder.getResponseBuilder(200, "Order Placed Successfully", null, writer);
            } else {
                Builder.getResponseBuilder(400, "Update Order Failed", null, writer);
            }

        } catch (SQLException | ClassNotFoundException e) {
            Builder.getResponseBuilder(500, e.getLocalizedMessage(), null, writer);
            e.printStackTrace();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        String orderId = req.getParameter("orderId");

        try {
            Connection connection = ds.getConnection();
            if (placeOrderBO.deleteOrder(connection, orderId)) {
                Builder.getResponseBuilder(200, "Order Deleted Successfully", null, writer);
            } else {
                Builder.getResponseBuilder(400, "Delete Failed", null, writer);
            }
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            Builder.getResponseBuilder(500, e.getLocalizedMessage(), null, writer);
            e.printStackTrace();
        }

    }
}
