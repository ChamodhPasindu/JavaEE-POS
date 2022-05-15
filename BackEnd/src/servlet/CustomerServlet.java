package servlet;

import bo.BOFactory;
import bo.custom.CustomerBO;
import db.DbConnection;
import dto.CustomerDTO;

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

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource ds;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            PrintWriter writer = resp.getWriter();
            String option = req.getParameter("option");
            resp.setContentType("application/json");
            Connection connection = ds.getConnection();


            switch (option) {
                case "SEARCH":
                    String cusId = req.getParameter("cusId");
                    CustomerDTO dto = customerBO.searchCustomer(connection,cusId);

                    JsonArrayBuilder arrayBuilderSearch = Json.createArrayBuilder();

                    if (dto!=null) {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("id", dto.getId());
                        objectBuilder.add("name", dto.getName());
                        objectBuilder.add("salary", dto.getSalary());
                        objectBuilder.add("address", dto.getAddress());
                        arrayBuilderSearch.add(objectBuilder.build());

                        JsonObjectBuilder response = Json.createObjectBuilder();
                        response.add("status", 200);
                        response.add("message", "Done");
                        response.add("data", arrayBuilderSearch.build());
                        writer.print(response.build());
                    }else{
                        JsonObjectBuilder response = Json.createObjectBuilder();
                        response.add("status", 400);
                        response.add("message", "Error");
                        response.add("data", arrayBuilderSearch.build());
                        writer.print(response.build());
                    }

                    break;

                case "GET_ALL_DETAILS":

                    ArrayList<CustomerDTO> customerDetails = customerBO.getAllCustomerDetails(connection);
                    JsonArrayBuilder arrayBuilderGetAll = Json.createArrayBuilder();
                    for (CustomerDTO customerDTO:customerDetails) {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("id", customerDTO.getId());
                        objectBuilder.add("name", customerDTO.getName());
                        objectBuilder.add("salary", customerDTO.getSalary());
                        objectBuilder.add("address", customerDTO.getAddress());
                        arrayBuilderGetAll.add(objectBuilder.build());
                    }
                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("status", 200);
                    response.add("message", "Done");
                    response.add("data", arrayBuilderGetAll.build());
                    writer.print(response.build());

                    break;

                case "GET_ALL_ID":

                    ArrayList<String> customerIds = customerBO.getAllCustomerIds(connection);
                    JsonArrayBuilder arrayBuilderGetId = Json.createArrayBuilder();
                    for (String id :customerIds) {
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

                    String count = customerBO.getCustomerCount(connection);

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
        String customerID = jsonObject.getString("id");
        String customerName = jsonObject.getString("name");
        String customerAddress = jsonObject.getString("address");
        String customerSalary = jsonObject.getString("salary");
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        CustomerDTO dto=new CustomerDTO(customerID,customerName,customerAddress,Double.parseDouble(customerSalary));
        try {
            Connection connection = ds.getConnection();
            if (customerBO.updateCustomer(connection,dto)) {
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
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        String cusId = req.getParameter("customerId");
        String cusName = req.getParameter("customerName");
        String cusAddress = req.getParameter("customerAddress");
        String cusSalary = req.getParameter("customerSalary");

        CustomerDTO dto=new CustomerDTO(cusId,cusName,cusAddress,Double.parseDouble(cusSalary));

        try {
            Connection connection = ds.getConnection();
            if (customerBO.addCustomer(dto,connection)) {
                JsonObjectBuilder response = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_CREATED);
                response.add("status", 200);
                response.add("message", "Successfully Added");
                response.add("data", "");
                writer.print(response.build());
            }
            connection.close();
        } catch (SQLException | ClassNotFoundException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status", 400);
            response.add("message", "Error");
            response.add("data", e.getLocalizedMessage());
            writer.print(response.build());
            resp.setStatus(HttpServletResponse.SC_CREATED);
            e.printStackTrace();

        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String customerId = req.getParameter("CusId");

        try {
            Connection connection = ds.getConnection();
            if (customerBO.deleteCustomer(connection,customerId)) {
                PrintWriter writer = resp.getWriter();
                writer.write("Customer Deleted");
            }
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            resp.sendError(500, e.getMessage());
            e.printStackTrace();
        }
    }
}
