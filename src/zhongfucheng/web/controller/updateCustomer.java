package zhongfucheng.web.controller;

import zhongfucheng.domain.Customer;
import zhongfucheng.service.BusinessService;
import zhongfucheng.utils.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ozc on 2017/3/2.
 */
@WebServlet(name = "updateCustomer",urlPatterns = "/updateCustomer")
public class updateCustomer extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //将数据封装到Bean中
        Customer customer = WebUtils.request2Bean(request, Customer.class);
        customer.setId(request.getParameter("id"));

        //调用Service层的方法，实现修改
        BusinessService businessService = new BusinessService();
        businessService.updateCustomer(customer);

        //修改成功就跳回查看客户界面
        request.getRequestDispatcher("/LookCustomer").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        this.doPost(request, response);
    }
}
