package servlet;

import bo.BOFactory;
import bo.custom.CustomerBO;
import dto.CustomerDTO;
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

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    @Resource(name = "java:comp/env/jdbc/pool")
    DataSource ds;

    CustomerBO customerBO = (CustomerBO) BOFactory.getBoFactory().getBO(BOFactory.BOTypes.CUSTOMER);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        try {
            String option = req.getParameter("option");
            Connection connection = ds.getConnection();

            switch (option) {
                case "SEARCH":
                    String cusId = req.getParameter("cusId");
                    CustomerDTO dto = customerBO.searchCustomer(connection, cusId);

                    JsonArrayBuilder arrayBuilderSearch = Json.createArrayBuilder();
                    if (dto != null) {

                        JsonObjectBuilder searchCustomer = Json.createObjectBuilder()
                                .add("id", dto.getId())
                                .add("name", dto.getName())
                                .add("salary", dto.getSalary())
                                .add("address", dto.getAddress());
                        arrayBuilderSearch.add(searchCustomer);

                        Builder.getResponseBuilder(200, "Done", arrayBuilderSearch, writer);
                    } else {
                        Builder.getResponseBuilder(400, "Wrong ID inserted", arrayBuilderSearch, writer);
                    }

                    break;

                case "GET_ALL_DETAILS":

                    ArrayList<CustomerDTO> customerDetails = customerBO.getAllCustomerDetails(connection);
                    JsonArrayBuilder arrayBuilderGetAll = Json.createArrayBuilder();

                    for (CustomerDTO customerDTO : customerDetails) {
                        JsonObjectBuilder allCustomerDetails = Json.createObjectBuilder()
                                .add("id", customerDTO.getId())
                                .add("name", customerDTO.getName())
                                .add("salary", customerDTO.getSalary())
                                .add("address", customerDTO.getAddress());
                        arrayBuilderGetAll.add(allCustomerDetails);
                    }

                    Builder.getResponseBuilder(200, "Done", arrayBuilderGetAll, writer);

                    break;

                case "GET_ALL_ID":

                    ArrayList<String> customerIds = customerBO.getAllCustomerIds(connection);
                    JsonArrayBuilder arrayBuilderGetId = Json.createArrayBuilder();

                    for (String id : customerIds) {
                        JsonObjectBuilder allIds = Json.createObjectBuilder()
                                .add("id", id);
                        arrayBuilderGetId.add(allIds);
                    }

                    Builder.getResponseBuilder(200, "Done", arrayBuilderGetId, writer);

                    break;

                case "COUNT":

                    String count = customerBO.getCustomerCount(connection);
                    JsonArrayBuilder arrayBuilderCount = Json.createArrayBuilder();

                    JsonObjectBuilder customerCount = Json.createObjectBuilder()
                            .add("count", count);
                    arrayBuilderCount.add(customerCount);

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
        String customerID = jsonObject.getString("id");
        String customerName = jsonObject.getString("name");
        String customerAddress = jsonObject.getString("address");
        String customerSalary = jsonObject.getString("salary");

        CustomerDTO dto = new CustomerDTO(customerID, customerName, customerAddress, Double.parseDouble(customerSalary));
        try {
            Connection connection = ds.getConnection();
            if (customerBO.updateCustomer(connection, dto)) {
                Builder.getResponseBuilder(200, "Successfully Updated", null, writer);
            } else {
                Builder.getResponseBuilder(400, "Update Failed", null, writer);
            }
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            Builder.getResponseBuilder(500, e.getLocalizedMessage(), null, writer);
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");

        String cusId = req.getParameter("customerId");
        String cusName = req.getParameter("customerName");
        String cusAddress = req.getParameter("customerAddress");
        String cusSalary = req.getParameter("customerSalary");

        CustomerDTO dto = new CustomerDTO(cusId, cusName, cusAddress, Double.parseDouble(cusSalary));

        try {
            Connection connection = ds.getConnection();
            if (customerBO.addCustomer(dto, connection)) {
                Builder.getResponseBuilder(200, "Successfully Added", null, writer);
            } else {
                Builder.getResponseBuilder(400, "Customer Added Failed", null, writer);
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

        String customerId = req.getParameter("CusId");

        try {
            Connection connection = ds.getConnection();
            if (customerBO.deleteCustomer(connection, customerId)) {
                Builder.getResponseBuilder(200, "Customer Deleted Successfully", null, writer);
            } else {
                Builder.getResponseBuilder(200, "Customer Deleted Fail", null, writer);
            }
            connection.close();

        } catch (SQLException | ClassNotFoundException e) {
            Builder.getResponseBuilder(500, e.getLocalizedMessage(), null, writer);
            e.printStackTrace();
        }
    }
}
