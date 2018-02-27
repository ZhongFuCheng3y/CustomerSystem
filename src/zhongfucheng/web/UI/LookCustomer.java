package zhongfucheng.web.UI;

import zhongfucheng.domain.Customer;
import zhongfucheng.domain.Page;
import zhongfucheng.service.BusinessService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by ozc on 2017/2/28.
 */
@WebServlet(name = "LookCustomer",urlPatterns = "/LookCustomer")
public class LookCustomer extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //获取用户想要看的页数，如果是第一次，那肯定为null
        String currentPageCount = request.getParameter("currentPageCount");

        String servletName = this.getServletName();

        //调用BusinessService的方法，获取得到所有客户信息
        BusinessService businessService = new BusinessService();
        Page page = businessService.getPageData2(currentPageCount, request.getContextPath() + "/" + servletName);

        //把客户信息带过去给jsp页面
        request.setAttribute("page", page);

        //跳转到显示客户界面信息的jsp
        request.getRequestDispatcher("/WEB-INF/lookCustomer.jsp").forward(request, response);


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        this.doPost(request, response);
    }
}
