package zhongfucheng.dao;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import zhongfucheng.domain.Customer;
import zhongfucheng.exception.DaoException;
import zhongfucheng.utils.Utils2DB;
import zhongfucheng.utils.WebUtils;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by ozc on 2017/2/26.
 */
public class CustomerDao {

    public void addCustomer(Customer customer) {

        QueryRunner queryRunner = new QueryRunner(Utils2DB.getDataSource());

        String sql = "INSERT INTO customer (id, name, gender, birthday, cellphone,email, preference, type, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";



        //得到用户传递进来的数据
        String id = customer.getId();
        String name = customer.getName();
        String gender = customer.getGender();
        String cellphone = customer.getCellphone();
        String email = customer.getEmail();
        String preference = customer.getPreference();
        String type = customer.getType();
        String description = customer.getDescription();

        //对于日期，要转换一下
        Date date = customer.getBirthday();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthday = simpleDateFormat.format(date);

        try {
            //向数据库插入数据
            queryRunner.update(sql, new Object[]{id, name, gender, birthday, cellphone, email, preference, type, description});

            //插入记录成功！
        } catch (SQLException e) {

            //如果出现了异常，就抛出Dao异常吧（自定义的异常）
            e.printStackTrace();

            throw new DaoException("添加用户出错了！");
        }
    }


    //得到所有的用户
    public List<Customer> getAll() {

        QueryRunner queryRunner = new QueryRunner(Utils2DB.getDataSource());


        String sql = "SELECT * FROM customer";
        try {
            List<Customer> customers = (List<Customer>) queryRunner.query(sql, new BeanListHandler(Customer.class));

            //如果集合大于个数大于0，就返回集合，不大于0，就返回null
            return customers.size() > 0 ? customers : null;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("获取所有的用户出错了！");
        }

    }


    public void update(Customer customer) {

        QueryRunner queryRunner = new QueryRunner(Utils2DB.getDataSource());

        String sql = "UPDATE customer set name=?,gender=?,birthday=?,cellphone=?,email=?,preference=?,type=?,description=?  WHERE id = ?";

        try {
            queryRunner.update(sql, new Object[]{customer.getName(), customer.getGender(), customer.getBirthday(),customer.getCellphone(), customer.getEmail(), customer.getPreference(), customer.getType(), customer.getDescription(), customer.getId()});

            System.out.println("修改成功！");

        } catch (SQLException e) {

            e.printStackTrace();
            throw new DaoException("更新失败");
        }
    }


    public void delete(String id) {
        QueryRunner queryRunner = new QueryRunner(Utils2DB.getDataSource());

        String sql = "DELETE from  customer WHERE id = ?";
        try {
            queryRunner.update(sql, new Object[]{id});
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("删除用户失败了");
        }
    }

    public Customer find(String id) {

        QueryRunner queryRunner = new QueryRunner(Utils2DB.getDataSource());

        String sql = "SELECT * FROM customer WHERE id = ?";

        try {
            Customer customer = (Customer) queryRunner.query(sql, new BeanHandler(Customer.class), new Object[]{id});

            return customer;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("查找用户失败了");
        }

    }



    /*查询总记录数*/
    public Long getTotalRecord() {

        QueryRunner queryRunner = new QueryRunner(Utils2DB.getDataSource());

        String sql = "SELECT count(*) FROM customer";

        try {
            //获取查询的结果
            Long l = (Long) queryRunner.query(sql, new ScalarHandler());
            return l;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询总记录数失败了！");
        }

    }


    /*查询分页数据*/
    //获取分页的数据是需要start和end两个变量的【从哪条开始取，取到哪一条】
    public List<Customer> getPageData(int start, int end) {

        QueryRunner queryRunner = new QueryRunner(Utils2DB.getDataSource());

        String sql = "SELECT * FROM customer LIMIT ?,?";

        try {
            List<Customer> customers = (List<Customer>) queryRunner.query(sql, new BeanListHandler(Customer.class), new Object[]{start, end});

            return customers;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DaoException("获取分页数据失败了！");
        }
    }

}
