<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="include/header.jspf" %>
    <title>get-test</title>
</head>
<body>
<h1>Welcome! This is get-test Page. </h1>

<form method="post" action="/post-test">
    <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
        <button class="btn btn-primary" type="submit">post-test button</button>
    </div>
<%@ include file="include/footer.jspf" %>
</body>
</html>