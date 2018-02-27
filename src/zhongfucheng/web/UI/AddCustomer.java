package zhongfucheng.web.UI;

import java.io.IOException;

/**
 * Created by ozc on 2017/2/27.
 */
@javax.servlet.annotation.WebServlet(name = "AddCustomer",urlPatterns = "/AddCustomer")
public class AddCustomer extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        //直接跳转到显示增加用户页面的jsp
        request.getRequestDispatcher("/WEB-INF/addCustomer.jsp").forward(request, response);

    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {

        this.doPost(request, response);
    }
}
