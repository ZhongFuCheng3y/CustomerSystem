package zhongfucheng.web.UI;

import zhongfucheng.domain.Customer;
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
@WebServlet(name = "UpdateCustomerUI",urlPatterns = "/UpdateCustomerUI")
public class UpdateCustomerUI extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        BusinessService businessService = new BusinessService();

        //通过id获取得到用户的详细信息
        Customer customer = businessService.findCustomer(id);
        System.out.println(customer.getId());

        request.setAttribute("customer", customer);

        //跳转到显示用户详细信息的jsp页面上
        request.getRequestDispatcher("/WEB-INF/customerInformation.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.doPost(request, response);


    }
}
