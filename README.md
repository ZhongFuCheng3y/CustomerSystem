
# 前言 #
> 为了巩固开发的流程，我们再拿一个客户关系管理系统来练手...！


# 成果图 #

**我们完成的就是下面的项目！**

![](http://i.imgur.com/9mxqkq3.png)


----------

# 搭建配置环境 #

- **配置Tomcat**
- **导入开发包**
- **建立开发用到的程序包**

![](http://i.imgur.com/RERM9ic.png)


- **在数据库创建相对应的表**

```sql

	CREATE TABLE customer (
	
	  id          VARCHAR(40) PRIMARY KEY,
	  name        VARCHAR(20) NOT NULL,
	  gender      VARCHAR(10) NOT NULL,
	  birthday    DATE,
	  cellphone   VARCHAR(30) NOT NULL,
	  email       VARCHAR(30),
	  preference  VARCHAR(200),
	  type        VARCHAR(20),
	  description VARCHAR(255)
	
	);



```

----------


# 开发实体 #

开发实体十分简单，对照着数据库的表就行了！


```java

    private String id;
    private String name ;
    private String gender ;
    private Date birthday ;
    private String cellphone ;
    private String eamil ;
    private String preference ;
    private String type ;
    private String description;


	//....各种setter、getter

```


----------

# 开发获取数据库连接池的Utils #

## 导入配置文件 ##

```xml

	<?xml version="1.0" encoding="UTF-8"?>
	<c3p0-config>
		<default-config>
			<property name="driverClass">com.mysql.jdbc.Driver</property>
			<property name="jdbcUrl">jdbc:mysql://localhost:3306/zhongfucheng</property>
			<property name="user">root</property>
			<property name="password">root</property>
		
			<property name="acquireIncrement">5</property>
			<property name="initialPoolSize">10</property>
			<property name="minPoolSize">5</property>
			<property name="maxPoolSize">20</property>
		</default-config>
		
		<named-config name="mysql">
			<property name="driverClass">com.mysql.jdbc.Driver</property>
			<property name="jdbcUrl">jdbc:mysql://localhost:3306/zhongfucheng</property>
			<property name="user">root</property>
			<property name="password">root</property>
		
			<property name="acquireIncrement">5</property>
			<property name="initialPoolSize">10</property>
			<property name="minPoolSize">5</property>
			<property name="maxPoolSize">20</property>
		</named-config>
		
		
		<named-config name="oracle">
			<property name="driverClass">oracle.jdbc.driver.OracleDriver</property>
			<property name="jdbcUrl">jdbc:oracle:thin:@//localhost:1521/事例名...</property>
			<property name="user">用户名</property>
			<property name="password">密码</property>
		
			<property name="acquireIncrement">5</property>
			<property name="initialPoolSize">10</property>
			<property name="minPoolSize">5</property>
			<property name="maxPoolSize">20</property>
		</named-config>
	</c3p0-config>

```

## 开发提供数据连接池的工具类 ##

```java

	public class Utils2DB {
	
	    private static ComboPooledDataSource comboPooledDataSource = null;
	
	        static {
	
	            //它会自动寻找配置文件，节点为mysql的数据库（默认就是Mysql）
	            comboPooledDataSource = new ComboPooledDataSource();
	        }
	
	
	    public static DataSource getDataSource() {
	        return comboPooledDataSource ;
	    }
	
	    public static Connection connection() {
	        try {
	            return comboPooledDataSource.getConnection();
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw new RuntimeException("数据库初始化失败了！");
	        }
	    }
	}


```

## 开发UUID工具类##

```java

	
	public class WebUtils {
	
	    public static String makeId() {
	        return UUID.randomUUID().toString();
	    }
	}

```


# 开发DAO #

**DAO应该提供增加客户和查询用户的功能**

## 增加用户 ##

```java


    public void addCustomer(Customer customer)  {

        QueryRunner queryRunner = new QueryRunner(Utils2DB.getDataSource());

      
        String sql = "INSERT INTO customer (id,name, gender, birthday, cellphone, preference, type, description) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";


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

```

### 测试增加用户 ###

**写完一个功能，不要急着去写其他的功能，先测试一下！**

```java

    @Test
    public void add() {

        //为了测试的方便，直接使用构造函数了！
        Customer customer = new Customer("1", "zhongfucheng", "男", new Date(), "1234", "aa@sina.com", "打代码", "高贵的用户", "我是个好人");



        CustomerDao customerDao = new CustomerDao();
        customerDao.addCustomer(customer);
        
    }

```

- 好的，没有报错！再看看数据库-----------**只要是中文的数据，都乱码了！**


![](http://i.imgur.com/9eKLvnp.png)


----------


解决的办法，看我另外一篇博文：https://zhongfucheng.bitcron.com/post/jie-jue-cuo-wu/mysqlzhong-wen-luan-ma

----------

## 查询用户 ##

**将所有的客户查询出来就行了！**

```java


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

```

### 测试查询用户 ###

```java

    @Test
    public void find() {

        CustomerDao customerDao = new CustomerDao();
        List<Customer> customers = customerDao.getAll();

        for (Customer customer : customers) {

            System.out.println(customer.getName());
        }
    }

```

![](http://i.imgur.com/gHO46Xg.png)


----------

## 修改用户信息 ##

**修改用户信息首先要知道用户的信息，在web端，只有id能唯一标识用户，我们需要通过id，获取用户全部信息（也就是Customer对象）**

```java

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


```


**修改用户都是外边传递个对象进来，Dao层取出对象的数据，从而对数据库的数据进行修改！**

```java


    public void update(Customer customer) {

        QueryRunner queryRunner = new QueryRunner(Utils2DB.getDataSource());

        String sql = "UPDATE customer set name=?,gender=?,birthday=?,cellphone=?,email=?,preference=?,type=?,description=?  WHERE id = ?";

        try {
            queryRunner.update(sql, new Object[]{customer.getName(), customer.getGender(), customer.getBirthday(),customer.getCellphone(), customer.getEmail(), customer.getPreference(), customer.getType(), customer.getDescription(), customer.getId()});

        } catch (SQLException e) {

            e.printStackTrace();
            throw new DaoException("更新失败");
        }
    }

```







### 测试修改用户 ###

```java

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

```

- 原来该用户的名字是d

![](http://i.imgur.com/8Pmhvtg.png)

- 测试完之后：

![](http://i.imgur.com/qDPN8PF.png)


----------
## 删除用户 ##


- **通过外界传递进来的id，就可以删除数据库表中的记录了**


```java

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

```

### 测试删除用户 ###

```java

    @Test
    public void delete() {

        CustomerDao customerDao = new CustomerDao();

        //我们已经知道了某id，通过id删除数据库中的记录
        String id = "043f7cce-c6f1-4155-b688-ba386cae1636";

        customerDao.delete(id);
    }


```


**数据库已经查询不到id为043f7cce-c6f1-4155-b688-ba386cae1636的记录了！**


# 开发service #


```java


	public class BusinessService {
	
	    CustomerDao customerDao = new CustomerDao();
	
	    public List<Customer> getAll() {
	
	        return customerDao.getAll();
	    }
	
	    public void addCustomer(Customer customer) {
	
	        customerDao.addCustomer(customer);
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
	}

```

# 开发web 的增加和查询#

## 提供UI，增加客户的Servlet ##

```java

        //直接跳转到显示增加用户页面的jsp
        request.getRequestDispatcher("/WEB-INF/addCustomer.jsp").forward(request, response);

```

## 开发显示添加客户页面 ##

```html

<form action="${pageContext.request.contextPath}/addCustomerController">
    <table border="1px">
        <tr>
            <td>用户名：</td>
            <td><input type="text" name="name"></td>
        </tr>
        <tr>
            <td>性别:</td>
            <td>
                <input type="radio" name="gender" value="female">女
                <input type="radio" name="gender" value="male">男
            </td>
        </tr>
        <tr>
            <td>生日：</td>
            <td>
                <select id="year">
                    <option value="1900">1900</option>
                </select>
                <select id="month">
                    <option value="01">01</option>
                </select>
                <select id="day">
                    <option value="01">01</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>电话号码:</td>
            <td><input type="text" name="cellphone"></td>
        </tr>
        <tr>
            <td>邮箱:</td>
            <td><input type="text" name="email"></td>
        </tr>
        <tr>
            <td>爱好：</td>
            <td>
                <input type="checkbox" name="hobbies" value="唱歌">唱歌
                <input type="checkbox" name="hobbies" value="跳舞">跳舞
                <input type="checkbox" name="hobbies" value="打代码">打代码
            </td>
        </tr>
        <tr>
            <td>客户类型</td>
            <td>
                <input type="radio" name="type" value="VIP">VIP
                <input type="radio" name="type" value="普通客户">普通客户
                <input type="radio" name="type" value="黑名单客户">黑名单客户
            </td>
        </tr>
        <tr>
            <td>描述</td>
            <td>
                <textarea name="description" cols="30" rows="10"></textarea>
            </td>
        </tr>
        <tr>
            <td><input type="submit" value="增添客户"></td>
            <td><input type="reset" value="重置"></td>
        </tr>

    </table>
</form>

```


- 效果是这样子的

![](http://i.imgur.com/DtYImw8.png)

我们发现，**在日期的下拉框中，只有一个数据（因为我们在value中只写了一个数据）**


**要想在下拉框中可以选择很多的数据，那么value的值就不能单单只有一个**。当然了，也**不可能在JSP页面中写下面的代码**

```html

                    <option value="1900">1900</option>
                    <option value="1901">1900</option>
                    <option value="1902">1900</option>
                    <option value="1903">1900</option>

```


**我们用javaScript生成下拉框的数据就行了！！**



- **获取年份！**


```javascript

	function makeYear() {
	
	    //得到下拉框的控件
	    var year = document.getElementById("year");
	
	    //要想下拉框有更多的数据，就需要有更多的option控件
	    //js获取得到年份是getFullYear()，单单的getYear()只是获取两位数
	    for (var i=1901; i<= new Date().getFullYear(); i++) {
	
	
	        //生成option控件
	        var option = document.createElement("option");
	
	        //option控件的值和文本内容为循环生成的年分！
	        option.value = i;
	        option.innerText = i;
	
	        //将生成option控件绑定到select控件上
	        year.appendChild(option);
	    }
	
	}

```

- **获取月份和日也类似**

```javascript

	function makeMonth() {
	    var month = document.getElementById("month");
	    for (var i = 2; i <= 12; i++) {
	        var option = document.createElement("option");
	        if (i < 10) {
	            option.value = '0' + i;
	            option.innerText = '0' + i;
	        } else {
	            option.value = i;
	            option.innerText = i;
	        }
	        month.appendChild(option);
	    }
	}
	
	function makeDay()
	{
	    var day = document.getElementById("day");
	    for(var i=2;i<=12;i++)
	    {
	        var option = document.createElement("option");
	        if(i<10)
	        {
	            option.value = '0' + i;
	            option.innerText = '0' + i;
	        }else{
	            option.value = i;
	            option.innerText = i;
	        }
	        day.appendChild(option);
	    }
	}

```


- **在JSP页面中导入javascript文件**

- **注意：javasrcipt文件不能放在WEB-INF下面！！！！否则是获取不到的！！！**

```javascript

    <script type="text/javascript" src="${pageContext.request.contextPath}/customer.js" ></script>

```




- **这三个函数都是在页面加载时就应该被初始化了，所以在body上绑定onload时间即可！！**

```javascript

	function pageInit() {
	    makeYear();
	    makeMonth();
	    makeDay();
	}


	<body onload="pageInit()">

```

- 效果：

![](http://i.imgur.com/qqAWPVO.png)


----------

## JavaScript拼凑数据 ##
**表单的数据非常多，毫无疑问，我们会使用BeanUtils来将数据封装到Bean对象中！**


对于表单的数据，还是有些杂乱的。**表单中日期的年月日是分开的，我们要么在客户端将年月日的数据拼凑起来，要么在服务器端将年月日拼凑起来**！同理，客户的喜好可能不单单有一个，但在Customer对象中，喜好单单用一个String类型来表示的。我们也要把客户的喜好拼凑起来。


**显然，在客户端用javaScript做拼凑是非常方便的！**

```javascript

	function makeBirthday() {
	
	    //获取下拉框的数据，把数据拼凑成日期字符串
	    var year = document.getElementById("year");
	    var month = document.getElementById("month");
	    var day = document.getElementById("day");
	    var birthday = year + "-" + month + "-" + day;
	
	    //想要将拼凑完的字符串提交给服务器，用隐藏域就行了
	    var input = document.createElement("input");
		input.type = "hidden";
	    input.value = birthday;
	    input.name = "birthday";
	
	    //将隐藏域绑定在form下【为了方便，在form中设置id，id名字为form】
	    document.getElementById("form").appendChild(input);
	
	}
	
	function makePreference() {
	
	    //获取喜好的控件
	    var hobbies = document.getElementsByName("hobbies");
	
	    //定义变量，记住用户选中的选项
	    var preference = "";
	
	    //遍历喜好的控件，看用户选上了什么！
	    for (var i = 0; i < hobbies.length; i++) {
	
	        if (hobbies[i].checked == true) {
	            preference += hobbies[i].value + ",";
	        }
	    }
	
	    //刚才拼凑的时候，最后一个逗号是多余的，我们要把它去掉
	    preference = preference.substr(0, preference.length - 1);
	
	    //也是用隐藏域将数据带过去给服务器
	    var input = document.createElement("input");
		input.type = "hidden";
	    input.value = preference;
	    input.name = "preference";
	    
	    //将隐藏域绑定到form表单上
	    document.getElementById("form").appendChild(input);
	    
	}

```

- **当表单提交的时候，触发上面两个函数就行了！所以在form表单上绑定onsumit事件！**

```javascript

	function makeForm() {
	    
	    makeBirthday();
	    makePreference();
	    return true;
	    
	}

	<form action="${pageContext.request.contextPath}/addCustomerController" id="form" onsubmit=" return makeForm()" method="post">

```


## 开发处理表单数据的Servlet ##


- **将表单的数据封装到Bean对象中，要开发工具类**

```java

    public static <T> T request2Bean(HttpServletRequest httpServletRequest, Class<T> aClass) {

        try {
            
            //获取Bean的对象
            T bean = aClass.newInstance();
            
            //获取表单中所有的名字
            Enumeration enumeration = httpServletRequest.getParameterNames();

            //遍历表单提交过来的名字
            while (enumeration.hasMoreElements()) {

                //每个名字
                String name = (String) enumeration.nextElement();

                //获取得到值
                String value = httpServletRequest.getParameter(name);

                //如果用户提交的数据不为空，那么将数据封装到Bean中
                if (!value.equals("") && value != null) {
                    BeanUtils.setProperty(bean, name, value);
                }

              
            }
			  return bean;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("封装数据到Bean中，失败了！");
        }
        
    }

```

- **处理表单数据的Servlet代码：**

```java

        //将表单的数据弄到Bean对象中
        Customer customer = WebUtils.request2Bean(request, Customer.class);


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


```

- 效果：

![](http://i.imgur.com/oYTEbQz.gif)


----------

## 提供查询客户界面的Servlet ##

```java

        //跳转到显示客户界面信息的jsp
        request.getRequestDispatcher("/WEB-INF/lookCustomer.jsp").forward(request, response);

```

## 开发显示客户信息的页面 ##

```jsp

	<c:if test="${empty(list)}">
	    对不起，还没有任何客户的信息！
	</c:if>
	
	<c:if test="${!empty(list)}">
	    <table border="1px">
	        <tr>
	            <td>用户名：</td>
	            <td>密码：</td>
	            <td>性别：</td>
	            <td>生日：</td>
	            <td>电话号码：</td>
	            <td>邮箱：</td>
	            <td>类型：</td>
	            <td>描述：</td>
	        </tr>
	
	        <c:forEach items="${list}" var="customer">
	            <tr>
	                <td>${customer.name}</td>
	                <td>${customer.gender}</td>
	                <td>${customer.birthday}</td>
	                <td>${customer.cellphone}</td>
	                <td>${customer.email}</td>
	                <td>${customer.preference}</td>
	                <td>${customer.type}</td>
	                <td>${customer.description}</td>
	            </tr>
	        </c:forEach>
	    </table>
	</c:if>

```

- 效果：

![](http://i.imgur.com/5Kzquqz.png)


----------

# 将功能拼接在首页上 #

**采用分贞技术，让界面更加好看！**


**index页面：**

```html


  <frameset rows="25%,*">
      <frame src="${pageContext.request.contextPath }/head.jsp" name="head">
      <frame src="${pageContext.request.contextPath }/body.jsp" name="body">
  </frameset>

```

**head页面：**

```html

	<body style="text-align: center;">
	
	<h1>客户管理系统！</h1>
	
	<a href="${pageContext.request.contextPath}/AddCustomer" target="body">增添客户</a>
	<a href="${pageContext.request.contextPath}/LookCustomer" target="body">查看客户</a>
	</body>

```

**body页面：**

```html


	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<html>
	<head>
	    <title>Title</title>
	</head>
	<body>
	</body>
	</html>

```

- 效果：


![效果图](http://img.blog.csdn.net/20170228125604300?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvaG9uXzN5/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)


----------
# 改善显示页面 #

**现在问题来了，如果我们客户信息有非常非常地多，我们不可能把客户信息全部都挤在同一个页面上，如果我们这样做的话，网页的长度就会非常的长！**


于是乎，我们就需要用到了分页的技术**，对于分页技术基础的讲解，在我另一篇博文中有介绍：** https://zhongfucheng.bitcron.com/post/jdbc/jdbcdi-si-pian-shu-ju-ku-lian-jie-chi-dbutilskuang-jia-fen-ye


看完上篇博文，我们知道，首先要做的就是：**明确分页技术中需要用到的4个变量的值！**

## 查询总记录数 ##

**查询总记录数也就是查询数据库表的记录有多少条，这是关于对数据库数据的操作，所以肯定是在dao层做！**

```java

    public Long getTotalRecord() {

        QueryRunner queryRunner = new QueryRunner(Utils2DB.getDataSource());

        String sql = "SELECT * FROM customer";
        
        try {
            //获取查询的结果
            Long l = (Long) queryRunner.query(sql, new ScalarHandler());
            return l;
           
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("查询总记录数失败了！");
        }
        
    }

```


## 查询分页的数据 ##

**获取分页的数据也是查询数据库的记录，这也是关于对数据库的操作，所以也是在Dao层做的！**


```java


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


```




## 分析 ##

**现在我们已经可以知道总记录数了，对于其他3个变量（每页显示记录数【由程序员来指定】，当前是多少页【由用户来指定】，总页数【由总记录数和每页显示记录数来算数来的】）**


现在要分析的是，这些变量应该放在哪里呢？？？**全部放在Dao层？？全部放在Dao层是可以实现功能的，但是，这样MVC结构就被破坏掉了（Dao层只用来对数据进行CRUD操作，4个变量存在Dao层，是不合理的）**


最好的做法是这样的：**创建一个实体Page，将分页用到的信息全部封装在Page中实现！Page就代表着分页的数据**这样就非常符合面向对象的思想了！


## 将数据封装到Page中并在页面上显示分页的数据 ##

**①：创建Page类**

```java


    //保存着分页的数据
    private List<Customer> list;

    //总记录数
    private long totalRecord;

    //每页显示记录数，这里我规定每页显示3条
    private int linesize = 3;

    //总页数
    private int totalPageCount;

    //当前显示的页数
    private long currentPageCount;

	//...各种的setter、getter


```

**②：BusinessService应该提供获取分页数据的服务**

```java

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

            start = (int) ((page.getCurrentPageCount() - 1) * page.getLinesize());

            List<Customer> customers = customerDao.getPageData(start, end);

            page.setList(customers);
        } else {

            //如果不是第一次，就把外界传递进来的页数封装到Page对象中
            page.setCurrentPageCount(Long.parseLong(currentPageCount));

            start = (int) ((page.getCurrentPageCount() - 1) * page.getLinesize());

            List<Customer> customers = customerDao.getPageData(start, end);

            page.setList(customers);

        }
        return page;

    }


```


**③：web层调用BusinessService层的功能，获取得到Page对象**


```java

        //获取用户想要看的页数，如果是第一次，那肯定为null
        String currentPageCount = request.getParameter("currentPageCount");

        //调用BusinessService的方法，获取得到所有客户信息
        BusinessService businessService = new BusinessService();
        Page page  = businessService.getPageData(currentPageCount);

        //把客户信息带过去给jsp页面
        request.setAttribute("page", page);

        //跳转到显示客户界面信息的jsp
        request.getRequestDispatcher("/WEB-INF/lookCustomer.jsp").forward(request, response);

```


**④：在JSP页面中，使用EL表达式获取到Page对象，从而输出数据**

```jsp

        <c:forEach items="${page.list}" var="customer">
            <tr>
                <td>${customer.name}</td>
                <td>${customer.gender}</td>
                <td>${customer.birthday}</td>
                <td>${customer.cellphone}</td>
                <td>${customer.email}</td>
                <td>${customer.preference}</td>
                <td>${customer.type}</td>
                <td>${customer.description}</td>
            </tr>
        </c:forEach>


```


**⑤：在JSP页面中显示页码，同时把码数绑定到超链接去！**

```jsp

    <%--提供页数的界面--%>
    <c:forEach var="pageNum" begin="1" end="${page.totalPageCount}">
        <a href="${pageContext.request.contextPath}/LookCustomer?currentPageCount=${pageNum}">
            ${pageNum}
        </a>
    </c:forEach>


```


- **效果：**

![](http://i.imgur.com/MM5Uk60.gif)

----------

## 让分页的功能更加完善 ##


### 增加上一步和下一步 ###


**一般的分页不仅仅只有页码给你，还有上一步和下一步。我们在JSP页面上也能添加这样的功能，其实这是非常简单的！**

```jsp


    <%--如果当前的页码大于1，才显示上一步--%>
    <c:if test="${page.currentPageCount>1}">

        <%--把传递过去的页码-1就行了--%>
        <a href="${pageContext.request.contextPath}/LookCustomer?currentPageCount=${page.currentPageCount-1}">
            上一步
        </a>
    </c:if>


    <%--如果当前的页码小于总页数，才显示下一步--%>
    <c:if test="${page.currentPageCount<page.totalPageCount}">

        <%--把传递过去的页码-1就行了--%>
        <a href="${pageContext.request.contextPath}/LookCustomer?currentPageCount=${page.currentPageCount+1}">
            下一步
        </a>
    </c:if>


```

- 效果：

![](http://i.imgur.com/N54siOo.gif)


----------
### 显示当前页数，总页数，总记录数 ###

```html

    当前页数是：[${page.currentPageCount}]&nbsp;&nbsp;&nbsp;

    总页数是：${page.totalPageCount}&nbsp;&nbsp;

    总记录数是：${page.totalRecord}

```

- 效果：


![](http://i.imgur.com/NO6Jzh2.png)


----------
### 跳转到某页上 ###

```jsp

    <input type="text" id="currentPageCount">
    <input type="button" value="跳转">

```

- 页面效果：

![](http://i.imgur.com/SwTsYhF.png)


我们现在要做的就是：**怎么样才能输入框输入内容，然后点击跳转按钮，将输入框的数据发送到Servlet上，然后实现跳转到某页上功能**


**明显地，我们肯定要使用JavaScript代码！**


```javascript

<script type="text/javascript">

    /*既然写上了JavaScript代码了，就顺便验证输入框输入的数据是否合法吧*/
    function goPage() {

        /*获取输入框控件*/
        var input = document.getElementById("currentPageCount");

        /*获取输入框的数据*/
        var value = input.value;

        if(value==null || value==""){
            alert("请输入页码");
            return false;
        }

        if(!value.match("\\d+")){
            alert("请输入数字");
            return false;
        }

        if(value<1 || value>${page.totalPageCount}){
            alert("请输入合法数据");
            return false ;
        }

        window.location.href="${pageContext.request.contextPath}/LookCustomer?currentPageCount="+value;
    }

</script>


```

- 效果：

![](http://i.imgur.com/z9nVWQE.gif)


----------

### 记录JSP页面的开始页和结束页 ###


为什么我们要记录JSP页面的开始页和结束页呢？**经过上面层层地优化，我们感觉不出有什么问题了。那是因为数据量太少！**


**我们试着多添加点记录进数据库，再回来看看！**


![](http://i.imgur.com/aUhjiRk.png)


从上面的图我们可以发现**页数有多少，JSP页面就显示多少！**这明显不合理的，如果有100页也显示100页吗？


**我们做一个规定，一次只能显示10页的数据**。那么显示哪10页呢？这又是一个问题了，如果我们在看第11页的数据，应该显示的是第7到第16页的数据（显示11附近的页数），我们在看第2页的数据，应该显示第1到第10页的数据。**用户想要看的页数是不明确的，我们显示附近的页数也是不明确的！**。**我们应该把用户想要看的页数记录下来，然后根据逻辑判断，显示附近的页数**


我们显示页数的代码是这样的：

![](http://i.imgur.com/BX01HdL.png)


**很明显，我们只要控制了begin和end中的数据，就控制显示哪10页了！**


**①在Page类中多定义两个成员变量**

```java

    //记录JSP页面开始的页数和结束的页数
    private int startPage;
    private int endPage;

	//Setter,Getter方法


```

**②开始页数和结束页数受用户想看的页数影响，在BusinessService的getPageData()加入下面的逻辑**

```java

			//第一次访问
			page.setStartPage(1);
            page.setEndPage(10);
		


			//不是第一次访问
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


```

**③：在JSP显示页数时，获取得到开始页和结束页就行了**

```jsp

    <%--提供页数的界面--%>
    <c:forEach var="pageNum" begin="${page.startPage}" end="${page.endPage}">
        <a href="${pageContext.request.contextPath}/LookCustomer?currentPageCount=${pageNum}">
                [${pageNum}]&nbsp;
        </a>
    </c:forEach>


```

- 效果：

![](http://i.imgur.com/DIFXJff.gif)


----------



----------
# 重构优化 #


## 分页重构 ##


**我们再回头看看BusinessService中获取分页数据的代码：**

```java

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

```


太太太太太tm复杂，太太太太tm长了！！！！！我们BusinessService要做的仅仅是调用Dao层的功能，为web层提供数据，**但我们在方法中使用大量了逻辑判断，而且这些逻辑判断都是属于Page类的！**



**明确一下：只有获取数据库总记录数是在BusinessService中做的，其他的数据变量都是应该在Page类中完成！**



**在BusinessService获取了总记录数之后，我们要对其他变量进行初始化（根据总记录数，用户想要看哪一页的数据），算出其他的数据（JSP记录开始页数、结束页数、总页数等等），最好的办法就是通过Page的构造函数来实现初始化！**


- **改良后的BusinessService**
```java


    public Page getPageData2(String currentPageCount) {

        //获取得到总记录数
        Long totalPageCount = customerDao.getTotalRecord();

        if (currentPageCount == null) {

            //如果是第一次，那么就将用户想看的页数设置为1
            Page page = new Page(1, totalPageCount);

            List<Customer> customers = customerDao.getPageData(page.getStartIndex(), page.getLinesize());

            page.setList(customers);
            return page;

        } else {

            //如果不是第一次，就将获取得到的页数传递进去
            Page page = new Page(Integer.parseInt(currentPageCount), totalPageCount);

            List<Customer> customers = customerDao.getPageData(page.getStartIndex(), page.getLinesize());

            page.setList(customers);
            return page;
        }
    }


```

- **改良后的Page类（原来的Page类只有成员变量和setter、getter方法）**

```java


    public Page(int currentPageCount, long totalRecord) {


        //将传递进来的currentPageCount初始化
        this.currentPageCount = currentPageCount;

        //总页数
        totalPageCount = (int) (totalRecord % linesize == 0 ? totalRecord / linesize : totalRecord / linesize + 1);
        
        //总记录数
		this.totalRecord = totalRecord;

        //开始取数据的位置
        startIndex = (currentPageCount - 1) * linesize;


        //如果当前页小于10，那么开始页为1，结束页为10就行了
        if (this.currentPageCount <= 10) {
            this.startPage = 1;
            this.endPage = 10;
        } else {
            startPage = this.currentPageCount - 4;
            endPage = this.currentPageCount + 5;

            //加减后页数越界的情况
            if (startPage < 1) {
                this.startPage = 1;
                this.endPage = 10;
            }
            if (endPage > totalPageCount) {
                this.startPage = this.currentPageCount - 9;
                this.endPage = this.totalPageCount;
            }
        }

    }

```


## 分页显示页面重构 ##

**分页的显示页面都是永恒不变的，我们可以把代码重构成一个jsp，需要用到分页显示页面的地方，就包含进去就行了！**

- **page.jsp**

```jsp


	<%@ page contentType="text/html;charset=UTF-8" language="java" %>
	<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	<%--显示当前页数--%>
	当前页数是：[${page.currentPageCount}]&nbsp;&nbsp;&nbsp;
	
	<%--如果当前的页码大于1，才显示上一步--%>
	<c:if test="${page.currentPageCount>1}">
	
	    <%--把传递过去的页码-1就行了--%>
	    <a href="${pageContext.request.contextPath}/LookCustomer?currentPageCount=${page.currentPageCount-1}">
	        上一步
	    </a>
	</c:if>
	
	<%--提供页数的界面--%>
	<c:forEach var="page" begin="${page.startPage}" end="${page.endPage}">
	    <a href="${pageContext.request.contextPath}/LookCustomer?currentPageCount=${page}">
	        [${page}]&nbsp;
	    </a>
	</c:forEach>
	
	<%--如果当前的页码小于总页数，才显示下一步--%>
	<c:if test="${page.currentPageCount<page.totalPageCount}">
	
	    <%--把传递过去的页码-1就行了--%>
	    <a href="${pageContext.request.contextPath}/LookCustomer?currentPageCount=${page.currentPageCount+1}">
	        下一步
	    </a>&nbsp;&nbsp;
	</c:if>
	
	<input type="text" id="currentPageCount">
	<input type="button" value="跳转" onclick="goPage()">
	
	总页数是：${page.totalPageCount}&nbsp;&nbsp;
	
	总记录数是：${page.totalRecord}
	
	
	<script type="text/javascript">
	
	    /*既然写上了JavaScript代码了，就顺便验证输入框输入的数据是否合法吧*/
	    function goPage() {
	
	        /*获取输入框控件*/
	        var input = document.getElementById("currentPageCount");
	
	        /*获取输入框的数据*/
	        var value = input.value;
	
	        if(value==null || value==""){
	            alert("请输入页码");
	            return false;
	        }
	
	        if(!value.match("\\d+")){
	            alert("请输入数字");
	            return false;
	        }
	
	        if(value<1 || value>${page.totalPageCount}){
	            alert("请输入合法数据");
	            return false ;
	        }
	
	        window.location.href="${pageContext.request.contextPath}/LookCustomer?currentPageCount="+value;
	    }
	
	</script>


```

**用需要用到的地方，导入即可！**

```jsp

    <jsp:include page="page.jsp"></jsp:include>
```


----------


**为了做到更好的通用性，处理分页数据的url应该由Servlet传进去给Page类，让Page类封装起来！要使用的时候，再用Page取出来就行了。**


**下面写法已经固定了，不够灵活！也就是说，下面的url地址不应该写死的**

```java

	${pageContext.request.contextPath}/LookCustomer?currentPageCount=${page.currentPageCount+1}

```

**我们可以这样做：**


- **在Controller上获取Servlet的名称，在传递用户想要看的页数的同时，把Servlet的url也传递进去**

```java

        String servletName = this.getServletName();

        //调用BusinessService的方法，获取得到所有客户信息
        BusinessService businessService = new BusinessService();

		//把Servlet的url也传递进去
        Page page = businessService.getPageData2(currentPageCount, request.getContextPath() + "/" + servletName);


```


- **在Page类上，多增加一个成员变量**


```java

    private String url;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

```

- **在BusinessService接受到web层传递进来的url，set到Page对象上就行了！**

```java

	page.setUrl(url);

```

**我们在jsp页面跳转到处理分页数据的Servlet上，就再不用写死了。直接用Page对象中获取出来就行了！**

```html

    <%--把传递过去的页码-1就行了--%>
    <a href="${page.url}?currentPageCount=${page.currentPageCount-1}">
        上一步
    </a>


```


----------
# 开发web的删除和修改 #

**在查询jsp页面上，增添删除和修改的操作链接！**

![](http://i.imgur.com/PRWKPO1.png)


![](http://i.imgur.com/JgDzm1T.png)



----------



## 开发处理删除操作的Servlet ##


**超链接绑定要删除用户的id，带过去给Controller**

```html
        <a href="${pageContext.request.contextPath}/DeleteCustomer?id=${customer.id}">删除</a>

```

**controller的代码也十分简单：**

```java

        String id = request.getParameter("id");
        
        //调用BusinessService层的功能，就可以完成删除操作了
        BusinessService businessService = new BusinessService();

        businessService.deleteCustomer(id);

```

**删除客户记录也是一件非常重要的事情，应该提供JavaSrcript代码询问是否要真的删除**


**在超链接控件上绑定事件！**

```html

       <a href="${pageContext.request.contextPath}/DeleteCustomer?id=${customer.id}" onclick=" return sureDelete()">删除</a>

```

```javascript

	function sureDelete() {
	    var b = window.confirm("你确定要删除吗？");
	    
	    if(b) {
	        return true;
	    }else {
	        return false;
	    }
	}

```

**测试：**


![](http://i.imgur.com/4moySzR.gif)


----------
## 修改操作 ##

**修改操作的流程是这样的：点击修改超链接，跳转到该用户的详细信息页面，在详细信息页面中修改数据，再提交修改！【跳转到用户详细信息页面时，用户的id还在的，在提交数据的时候，记得把id也给到服务器，【id是不包含在表单中的，要我们自己提交过去】！】**

```html

   <a href="${pageContext.request.contextPath}/UpdateCustomerUI?=${customer.id}">修改</a>

```

### 开发提供用户详细信息的Servlet ###

```java

        String id = request.getParameter("id");
        BusinessService businessService = new BusinessService();
        
        //通过id获取得到用户的详细信息
        Customer customer = businessService.findCustomer(id);

        request.setAttribute("customer", customer);
        //跳转到显示用户详细信息的jsp页面上
        request.getRequestDispatcher("/WEB-INF/customerInformation").forward(request, response);

```

### 开发显示用户信息的JSP【数据回显】 ###



**想要日期能够选择，记得导入JavaScript代码，响应事件！**




**注意：在显示页面上，一定要把id传递过去给处理表单的Servlet，不然服务器是不知道你要修改哪一条数据的！**

```html  
<head>
    <title>用户详细信息</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/customer.js">

    </script>
</head>
<body onload="pageInit()">

<form action="${pageContext.request.contextPath}/updateCustomer?id=${customer.id}" method="post" onsubmit="makeForm()">
    <table border="1px">
        <tr>
            <td>用户名：</td>
            <td><input type="text" name="name" value="${customer.name}"></td>
        </tr>

        <tr>
            <td>性别</td>
            <td><input type="radio" name="gender" value="male" ${customer.gender=='male'?'checked':''}>男
            <input type="radio" name="gender" value="female"${customer.gender=='female'?'checked':''}>女
            </td>
        </tr>
        <tr>
            <td>生日</td>
            <td>
                <select id="year">
                    <option value="${fn:split(customer.birthday,'-')[0]}">${fn:split(customer.birthday,'-')[0]}</option>
                </select>
                <select id="month">
                    <option value="${fn:split(customer.birthday,'-')[1]}">${fn:split(customer.birthday,'-')[1]}</option>
                </select>
                <select id="day">
                    <option value="${fn:split(customer.birthday,'-')[2]}">${fn:split(customer.birthday,'-')[2]}</option>
                </select>
            </td>
        </tr>
        <tr>
            <td>电话号码:</td>
            <td><input type="text" name="cellphone" value="${customer.cellphone}"></td>
        </tr>
        <tr>
            <td>邮箱:</td>
            <td><input type="text" name="email"value="${customer.email}"></td>
        </tr>
        <tr>
            <td>爱好：</td>
            <td>
                <input type="checkbox" name="hobbies" value="唱歌"${fn:contains(customer.preference, '唱歌')==true?'checked':''}>唱歌

                <input type="checkbox" name="hobbies" value="跳舞"${fn:contains(customer.preference, '跳舞')==true?'checked':''}>跳舞
                <input type="checkbox" name="hobbies" value="打代码"${fn:contains(customer.preference, '打代码')==true?'checked':''}>打代码
            </td>
        </tr>
        <tr>
            <td>客户类型</td>
            <td>
                <input type="radio" name="type" value="VIP" ${customer.type=='VIP'?'checked':''}>VIP
                <input type="radio" name="type" value="普通客户"${customer.type=='普通客户'?'checked':''}>普通客户
                <input type="radio" name="type" value="黑名单客户"${customer.type=='黑名单客户'?'checked':''}>黑名单客户
            </td>
        </tr>
        <tr>
            <td>描述</td>
            <td>
                <textarea name="description" cols="30" rows="10">${customer.description}</textarea>
            </td>
        </tr>
        <tr>
            <td><input type="submit" value="确定修改"></td>
            <td><input type="reset" value="重置"></td>
        </tr>
    </table>
</form>

```



效果：



![](http://i.imgur.com/IzNrTH2.gif)



### 处理修改表单数据的Servlet ###

```java 


        //将数据封装到Bean中
        Customer customer = WebUtils.request2Bean(request, Customer.class);

		//将id封装到Customer对象中！！！不要忘了id！！！在表单中获取到的数据是没有id的！！！！！记得！！！！
		customer.setId(request.getParameter("id"));
        
        //调用Service层的方法，实现修改
        BusinessService businessService = new BusinessService();
        businessService.updateCustomer(customer);
        
        //修改成功就跳回查看客户界面
        request.getRequestDispatcher("/LookCustomer").forward(request, response);
```

- 效果：

![](http://i.imgur.com/kAX55UY.gif)

# 总结 #

1. 在dao层中，我们有添加客户、通过id查找用户、删除用户、修改用户信息的方法。
2. 日期我们一般用下拉框来给用户选取，要想下拉框的信息有足够多的数据，我们需要用到JavaScript【DOM编程动态增加和修改数据】
3. javasrcipt文件不能放在WEB-INF目录下面
4. 日期的数据通过下拉框选取，年、月、日是分散的，我们需要把他们拼接，于是我们也用JavaScript来拼接【减低服务器端的压力】
5. 开发工具方法request2Bean，主要用到了BeanUtils框架，这样就不用在Servlet一个一个封装了。
6. 在JSP判断集合是否有元素时，我们可以用EL表达式**${empty(集合)}**。
7. 如果记录数有很多，我们应该使用分页技术，一般地，我们使用Page类来封装分页的数据
8. 要使用分页技术，就必须在数据库用查询总记录数，通过总记录数，就可以算出总页数了【每页显示多少条记录由我们说了算】
9. 在dao层还要编写获取具体的分页数据，从哪里开始，哪里结束，返回一个List集合，再把List集合封装到Page对象上
10. 由于获取分页数据需要当前的页数是多少，（所以在service中要判断当前页数是否存在，如果不存在，那么就设置为1）**【更新，我认为在Controller判断会好一点】**
11. 分页中，我们还支持上一页和下一页的功能，如果页数大于1，才显示上一页，如果页数小于1，才显示下一页。
12. 给出下拉框进行页数跳转，使用JavaScript事件机制，获取页数，再提交给Servlet处理即可
13. 我们还要控制页数的显示，因为不可能有100页，我们就显示100页，这样是不可能的。在Page类中维护两个变量，startPage，endPage。我们规定每次只能显示10页数据，如果第一次访问就显示1-10页。如果当前页数大于10，那么就显示6-15页。如果角标越界了，那么就显示前10页或者后10页
14. 我们把显示分页的页面封装成单独的jsp，使用的Servlet连接也可以用url变量来维护。
15. 前台数据做拼接，最终都是把拼接好的数据用一个隐藏域封装起来，随后让form表单一起提交



----------
> 如果文章有错的地方欢迎指正，大家互相交流。习惯在微信看技术文章的同学，想要获取更多的Java资源的同学，可以**关注微信公众号:Java3y**
