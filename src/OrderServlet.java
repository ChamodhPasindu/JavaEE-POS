import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
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
            String option=req.getParameter("option");
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Market", "root", "root1234");
            PrintWriter writer=resp.getWriter();
            resp.setContentType("application/json");

            switch (option){
                case "GET_ID":

                    try {
                        ResultSet rst = connection.prepareStatement("SELECT OrderId FROM Orders ORDER BY OrderId DESC LIMIT 1").executeQuery();
                        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder(); //

                        if (rst.next()){
                            int tempId = Integer.
                                    parseInt(rst.getString(1).split("-")[1]);
                            tempId=tempId+1;
                            System.out.println(tempId);
                            if (tempId<=9){
                                String id="OID-00"+tempId;
                                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                                objectBuilder.add("id",id);
                                arrayBuilder.add(objectBuilder.build());

                            }else if(tempId<=99){
                                String id="OID-0"+tempId;
                                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                                objectBuilder.add("id",id);
                                arrayBuilder.add(objectBuilder.build());

                            }else{
                                String id="OID-"+tempId;
                                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                                objectBuilder.add("id",id);
                                arrayBuilder.add(objectBuilder.build());
                            }

                        }else{
                            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                            String id="OID-001";
                            objectBuilder.add("id",id);
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
        super.doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }
}
