<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <%@ include file="include/header.jspf" %>
        <title>폼 테스트</title>
    </head>
    <body class="bg-primary">
        <div id="layoutAuthentication">
            <div id="layoutAuthentication_content">
                <main>
                    <div class="container">
                        <div class="row justify-content-center">
                            <div class="col-lg-5">
                                <div class="card shadow-lg border-0 rounded-lg mt-5">
                                    <div class="card-header"><h3 class="text-center font-weight-light my-4">어노테이션 기반 컨트롤러 테스트</h3></div>
                                    <div class="card-body">
                                        <form method="post" action="/form">
                                            <div class="form-floating mb-3">
                                                <input class="form-control" id="inputName" name="name" type="text" placeholder="이름을 입력하세요" required />
                                                <label for="inputName">이름</label>
                                            </div>
                                            <div class="form-floating mb-3">
                                                <input class="form-control" id="inputEmail" name="email" type="email" placeholder="이메일을 입력하세요" required />
                                                <label for="inputEmail">이메일</label>
                                            </div>
                                            <div class="form-floating mb-3">
                                                <textarea class="form-control" id="inputMessage" name="message" placeholder="메시지를 입력하세요" style="height: 100px" required></textarea>
                                                <label for="inputMessage">메시지</label>
                                            </div>
                                            <div class="d-flex align-items-center justify-content-between mt-4 mb-0">
                                                <button class="btn btn-primary" type="submit">제출하기</button>
                                            </div>
                                        </form>
                                    </div>
                                    <div class="card-footer text-center py-3">
                                        <div class="small"><a href="/index.jsp">홈으로 돌아가기</a></div>
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
