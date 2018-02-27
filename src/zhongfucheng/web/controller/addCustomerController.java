package zhongfucheng.web.controller;

import org.apache.commons.beanutils.BeanUtils;
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
 * Created by ozc on 2017/2/28.
 */
@WebServlet(name = "addCustomerController" ,urlPatterns = "/addCustomerController")
public class addCustomerController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //将表单的数据弄到Bean对象中
        Customer customer = WebUtils.request2Bean(request, Customer.class);

        //生成用户的id
        customer.setId(WebUtils.makeId());
            try {
                //调用BusinessService层的方法，添加客户
                BusinessService businessService = new BusinessService();
                businessService.addCustomer(customer);

                //如果执行到这里，说明成功了，如果被catch了，说明失败了。
                request.setAttribute("message", "添加成功！");

            } catch (Exception e) {
                e.printStackTrace();
            request.setAttribute("message", "添加失败");
        }
        request.getRequestDispatcher("/message.jsp").forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.doPost(request, response);

    }
}
