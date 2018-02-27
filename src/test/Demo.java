package test;

import org.junit.Test;
import zhongfucheng.dao.CustomerDao;
import zhongfucheng.domain.Customer;

import java.util.Date;
import java.util.List;

/**
 * Created by ozc on 2017/2/26.
 */
public class Demo {

    @Test
    public void add() {

        //为了测试的方便，直接使用构造函数了！
        Customer customer = new Customer("1", "zhongfucheng", "男", new Date(), "1234", "aa@sina.com", "打代码", "我是高贵的用户！", "我是个好人");

        CustomerDao customerDao = new CustomerDao();
        customerDao.addCustomer(customer);

    }

    @Test
    public void find() {

        CustomerDao customerDao = new CustomerDao();
        List<Customer> customers = customerDao.getAll();

        for (Customer customer : customers) {

            System.out.println(customer.getName());
        }
    }
    @Test
    public void update() {

        CustomerDao customerDao = new CustomerDao();

        //我们已经知道了某id，通过id获取得到用户信息（Customer）
        String id = "043f7cce-c6f1-4155-b688-ba386cae1636";
        Customer customer = customerDao.find(id);

        //修改用户信息
        customer.setName("看完博客要点赞");
        customerDao.update(customer);
    }
    @Test
    public void delete() {

        CustomerDao customerDao = new CustomerDao();

        //我们已经知道了某id，通过id删除数据库中的记录
        String id = "043f7cce-c6f1-4155-b688-ba386cae1636";

        customerDao.delete(id);
    }

}
