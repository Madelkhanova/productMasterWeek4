package medium;

public class UserDataCloudDataSource implements DataSource<UserData> {

    @Override
    public UserData getData() {
        return new UserData(78, "Aruzhan", "madelxano@mail.ru");
    }
}
