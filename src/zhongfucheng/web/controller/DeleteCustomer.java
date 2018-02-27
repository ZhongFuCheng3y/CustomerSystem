package zhongfucheng.web.controller;

import zhongfucheng.service.BusinessService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ozc on 2017/3/2.
 */
@WebServlet(name = "DeleteCustomer",urlPatterns = "/DeleteCustomer")
public class DeleteCustomer extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");

        //调用BusinessService层的功能，就可以完成删除操作了
        BusinessService businessService = new BusinessService();

        businessService.deleteCustomer(id);

        //删除完，调转回去提供显示用户界面的Servlet上
        request.getRequestDispatcher("/LookCustomer").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        this.doPost(request, response);
    }
}
