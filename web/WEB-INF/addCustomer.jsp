<%--
  Created by IntelliJ IDEA.
  User: ozc
  Date: 2017/2/27
  Time: 17:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>增加客户</title>

    <script type="text/javascript" src="${pageContext.request.contextPath}/customer.js" ></script>

</head>
<body onload="pageInit()" >
<form action="${pageContext.request.contextPath}/addCustomerController" id="form" onsubmit=" return makeForm()" method="post">
    <table border="1px" >
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
</body>
</html>
