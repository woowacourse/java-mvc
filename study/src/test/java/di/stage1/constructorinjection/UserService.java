package di.stage1.constructorinjection;

import di.User;

class UserService {

    private final UserDao userDao;

    /**
     * Stage 0과 달리 외부에서 의존성을 주입받으니 결합도가 내려가기는 했지만..
     * 구체 클래스에 의존하고 있어 이 클래스를 테스트하려면 실제 DB 까지 실행해야한다.
     * 이는 DIP 위반이며 더 나아가 OCP를 위반하기에 확장성에서도 분리하다. (ex : DB가 아니라 다른 저장소를 사용하고 싶다면..?
     * @param userDao
     */
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User join(User user) {
        userDao.insert(user);
        return userDao.findById(user.getId());
    }
}
