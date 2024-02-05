public class Editor extends User{
    public Editor(String username, String password) {
        super(username, password);
    }

    @Override
    public UserType getUserType() {
        return UserType.EDITOR;
    }
}
