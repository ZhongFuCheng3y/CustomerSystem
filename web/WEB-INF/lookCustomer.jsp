<%--
  Created by IntelliJ IDEA.
  User: ozc
  Date: 2017/2/28
  Time: 11:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>

    <script type="text/javascript">

        function sureDelete() {
            var b = window.confirm("你确定要删除吗？");

            if(b) {
                return true;
            }else {
                return false;
            }
        }
    </script>
</head>
<body>

<c:if test="${empty(page.list)}">
    对不起，还没有任何客户的信息！
</c:if>

<c:if test="${!empty(page.list)}">
    <table border="1px">
        <tr>
            <td>用户名：</td>
            <td>性别：</td>
            <td>生日：</td>
            <td>电话号码：</td>
            <td>邮箱：</td>
            <td>爱好：</td>
            <td>类型：</td>
            <td>描述：</td>
            <td>操作：</td>
        </tr>

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
                <td>
                    <a href="${pageContext.request.contextPath}/UpdateCustomerUI?id=${customer.id}">修改</a>
                    <a href="${pageContext.request.contextPath}/DeleteCustomer?id=${customer.id}" onclick=" return sureDelete()">删除</a>
                </td>
            </tr>
        </c:forEach>
    </table>


    <jsp:include page="/page.jsp"/>

</c:if>




</body>
</html>
