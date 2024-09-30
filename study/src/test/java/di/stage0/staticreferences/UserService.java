package di.stage0.staticreferences;

import di.User;

class UserService {

    public static User join(User user) {
        /**
         * 의존성이 외부에서 주입되는게 아니라 코드 내부에 붙어있어 테스트 하기 굉장히 어렵다.
         * UserDao가 다른 객체로 변경되려면.. 컴파일 레벨에서 다시 해야한다..
         */
        UserDao.insert(user);
        return UserDao.findById(user.getId());
    }
}
