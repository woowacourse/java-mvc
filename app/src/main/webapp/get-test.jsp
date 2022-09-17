<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <%@ include file="include/header.jspf" %>
        <title>GET TEST</title>
    </head>
    <body>
        <%=request.getAttribute("id")%>
    </body>
</html>
