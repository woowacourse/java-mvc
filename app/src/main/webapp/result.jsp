<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <%@ include file="include/header.jspf" %>
        <title>제출 결과</title>
    </head>
    <body class="bg-primary">
        <div id="layoutAuthentication">
            <div id="layoutAuthentication_content">
                <main>
                    <div class="container">
                        <div class="row justify-content-center">
                            <div class="col-lg-6">
                                <div class="card shadow-lg border-0 rounded-lg mt-5">
                                    <div class="card-header"><h3 class="text-center font-weight-light my-4">제출 결과</h3></div>
                                    <div class="card-body">
                                        <div class="alert alert-success" role="alert">
                                            <h4 class="alert-heading">폼이 성공적으로 제출되었습니다!</h4>
                                            <p>어노테이션 기반 컨트롤러가 정상적으로 작동하고 있습니다.</p>
                                        </div>
                                        
                                        <div class="card mb-3">
                                            <div class="card-header">
                                                <h5 class="mb-0">제출된 정보</h5>
                                            </div>
                                            <div class="card-body">
                                                <div class="row mb-3">
                                                    <div class="col-sm-3">
                                                        <strong>이름:</strong>
                                                    </div>
                                                    <div class="col-sm-9">
                                                        ${name}
                                                    </div>
                                                </div>
                                                <div class="row mb-3">
                                                    <div class="col-sm-3">
                                                        <strong>이메일:</strong>
                                                    </div>
                                                    <div class="col-sm-9">
                                                        ${email}
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-3">
                                                        <strong>메시지:</strong>
                                                    </div>
                                                    <div class="col-sm-9">
                                                        ${message}
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        
                                        <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
                                            <a href="/form" class="btn btn-primary">다시 제출하기</a>
                                            <a href="/index.jsp" class="btn btn-secondary">홈으로</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </main>
            </div>
            <div id="layoutAuthentication_footer">
                <footer class="py-4 bg-light mt-auto">
                    <div class="container-fluid px-4">
                        <div class="d-flex align-items-center justify-content-between small">
                            <div class="text-muted">Copyright &copy; Your Website 2021</div>
                            <div>
                                <a href="/index.jsp">Home</a>
                                &middot;
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
