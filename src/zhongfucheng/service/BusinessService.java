package zhongfucheng.service;

import zhongfucheng.dao.CustomerDao;
import zhongfucheng.domain.Customer;
import zhongfucheng.domain.Page;

import java.util.List;

/**
 * Created by ozc on 2017/2/27.
 */
public class BusinessService {

    CustomerDao customerDao = new CustomerDao();


    //既然Page对象代表是分页数据，那么返回Page对象即可！
    //web层应该传入想要看哪一页数据的参数！
    public Page getPageData(String currentPageCount) {

        Page page = new Page();

        //获取数据库中有多少条记录，并封装到Page对象中
        Long totalRecord = customerDao.getTotalRecord();
        page.setTotalRecord(totalRecord);

        //算出总页数，并封装到Page对象中
        int totalPagecount = (int) (totalRecord % page.getLinesize() == 0 ? totalRecord / page.getLinesize() : totalRecord / page.getLinesize() + 1);
        page.setTotalPageCount(totalPagecount);

        int start ;
        int end = page.getLinesize();

        //现在又分两种情况了，如果传递进来的参数是null的，那么说明外界是第一次查询的
        if (currentPageCount == null) {

            //第一次查询，就应该设置当前页数是第一页
            page.setCurrentPageCount(1);

            page.setStartPage(1);
            page.setEndPage(10);

            start = (int) ((page.getCurrentPageCount() - 1) * page.getLinesize());

            List<Customer> customers = customerDao.getPageData(start, end);

            page.setList(customers);
        } else {

            //如果不是第一次，就把外界传递进来的页数封装到Page对象中
            page.setCurrentPageCount(Long.parseLong(currentPageCount));

            start = (int) ((page.getCurrentPageCount() - 1) * page.getLinesize());

            if (page.getCurrentPageCount() <= 10) {
                page.setStartPage(1);
                page.setEndPage(10);
            } else {
                page.setStartPage((int) (page.getCurrentPageCount() - 4));
                page.setEndPage((int) (page.getCurrentPageCount() + 5));

                //如果因为加减角标越界了，那么就设置最前10页，或者最后10页
                if (page.getStartPage() < 1) {
                    page.setStartPage(1);
                    page.setEndPage(10);
                }
                if (page.getEndPage() > page.getTotalPageCount()) {
                    page.setEndPage(page.getTotalPageCount());
                    page.setStartPage(page.getTotalPageCount() - 9);
                }
            }


            List<Customer> customers = customerDao.getPageData(start, end);

            page.setList(customers);

        }
        return page;
    }

    public Page getPageData2(String currentPageCount,String url) {

        //获取得到总记录数
        Long totalPageCount = customerDao.getTotalRecord();

        if (currentPageCount == null) {

            //如果是第一次，那么就将用户想看的页数设置为1
            Page page = new Page(1, totalPageCount);

            List<Customer> customers = customerDao.getPageData(page.getStartIndex(), page.getLinesize());

            page.setUrl(url);
            page.setList(customers);
            return page;

        } else {

            //如果不是第一次，就将获取得到的页数传递进去
            Page page = new Page(Integer.parseInt(currentPageCount), totalPageCount);

            List<Customer> customers = customerDao.getPageData(page.getStartIndex(), page.getLinesize());

            page.setUrl(url);
            page.setList(customers);
            return page;
        }
    }

    public void deleteCustomer(String id) {
        customerDao.delete(id);

    }

    public void updateCustomer(Customer customer) {
        customerDao.update(customer);
    }

    public Customer findCustomer(String id) {
        return customerDao.find(id);
    }


    public List<Customer> getAll() {

        return customerDao.getAll();
    }

    public void addCustomer(Customer customer) {

        customerDao.addCustomer(customer);

    }
}
