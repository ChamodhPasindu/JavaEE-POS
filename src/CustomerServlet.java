import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            String option=req.getParameter("option");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Market", "root", "root1234");
            PrintWriter writer=resp.getWriter();
            resp.setContentType("application/json");


            switch (option){
                case "SEARCH":

                    break;

                case "GET_ALL_DETAILS":
                    try {
                        ResultSet rst = connection.prepareStatement("select * from Customer").executeQuery();
                        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder(); //

                        while (rst.next()) {
                            String id = rst.getString(1);
                            String name = rst.getString(2);
                            double salary = rst.getDouble(3);
                            String address = rst.getString(4);

                            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                            objectBuilder.add("id", id);
                            objectBuilder.add("name", name);
                            objectBuilder.add("salary", salary);
                            objectBuilder.add("address", address);
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

                case "GET_ALL_IDS":

                    break;
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

        String cusId=req.getParameter("customerId");
        String cusName=req.getParameter("customerName");
        String cusAddress=req.getParameter("customerAddress");
        String cusSalary=req.getParameter("customerSalary");

        PrintWriter writer=resp.getWriter();
        resp.setContentType("application/json");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Market", "root", "root1234");
            PreparedStatement pstm=connection.prepareStatement("INSERT INTO Customer VALUES (?,?,?,?)");
            pstm.setObject(1,cusId);
            pstm.setObject(2,cusName);
            pstm.setObject(3,cusSalary);
            pstm.setObject(4,cusAddress);

            if (pstm.executeUpdate()>0){
                JsonObjectBuilder response= Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_CREATED);
                response.add("status",200);
                response.add("message","Successfully Added");
                response.add("data","");
                writer.print(response.build());

            }
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
        super.doDelete(req, resp);
    }
}
