<%@ page import="com.techcourse.domain.User" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <%@ include file="include/header.jspf" %>
    <title>404 Error - SB Admin</title>
</head>
<body>
<div id="layoutError">
    <div id="layoutError_content">
        <main>
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-lg-6">
                        <div class="text-center mt-4">
                            <h1 class="display-1">My Info</h1>
                            <p class="lead">안녕하세요 그린론💚</p>
                            <p>이런 피드백을 남겨주시다니 너무 행복하네유👍</p>
                            <p>제 정보를 알고싶으신가유?</p>
                            <% User user =  (User)session.getAttribute("user"); %>
                            <a href="/api/user?account=${user.account}">
                                <i class="fas fa-arrow-left me-1"></i>
                                내 정보 보기(JSON)
                            </a>
                            <a href="/">
                                <br>return to dashboard
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
    <div id="layoutError_footer">
        <footer class="py-4 bg-light mt-auto">
            <div class="container-fluid px-4">
                <div class="d-flex align-items-center justify-content-between small">
                    <div class="text-muted">Copyright &copy; Your Website 2021</div>
                    <div>
                        <a href="#">Privacy Policy</a>
                        &middot;
                        <a href="#">Terms &amp; Conditions</a>
                    </div>
                </div>
            </div>
        </footer>
    </div>
</div>
<%@ include file="include/footer.jspf" %>
</body>
</html>
