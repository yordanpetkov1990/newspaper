public class Writer extends User{
    public Writer(String username, String password) {
        super(username, password);
    }

    @Override
    public UserType getUserType() {
        return UserType.WRITER;
    }
}
