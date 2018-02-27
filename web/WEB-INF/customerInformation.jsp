<%--
  Created by IntelliJ IDEA.
  User: ozc
  Date: 2017/3/2
  Time: 10:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <title>用户详细信息</title>
    <script type="text/javascript" src="${pageContext.request.contextPath}/customer.js">

    </script>
</head>
<body onload="pageInit()">

<form action="${pageContext.request.contextPath}/updateCustomer?id=${customer.id}" method="post" onsubmit="return makeForm()" id="form">
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

</body>
</html>
