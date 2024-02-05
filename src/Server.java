import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Server {
    private static List<User> userList = Collections.synchronizedList(new ArrayList<>());
    private static List<String> writingsList = Collections.synchronizedList(new ArrayList<>());
    private static List<String> writingsListToBePublished = Collections.synchronizedList(new ArrayList<>());
    private final Object userLock;
    private final Object writingLock;
    private final Object writingTObePublishedLock;
    private ServerSocket serverSocket;
    private int port;

    public Server(int port) {
        userList.add(new Administrator("yordan","12345"));
        this.port = port;
        userLock = new Object();
        writingLock = new Object();
        writingTObePublishedLock = new Object();
    }
    public void run(){
        try{
            serverSocket = new ServerSocket(port);
            while (true){
                Socket clientSocket = serverSocket.accept();
                Thread clientThread = new Thread(() -> {
                    try{
                        PrintWriter pf = new PrintWriter(clientSocket.getOutputStream(),true);
                        BufferedReader bf = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        pf.println("Please enter username");
                        String username = bf.readLine();
                        pf.println("Please enter password");
                        String password = bf.readLine();
                        User user = login(username,password);
                        UserType userType = user.getUserType();
                        switch (userType){
                            case EDITOR:
                                pf.println("Welcome editor");
                                synchronized (writingTObePublishedLock){
                                    pf.println(writingsListToBePublished.toString());
                                    pf.println("Please enter index");
                                    String index = bf.readLine();
                                    String writing = writingsListToBePublished.get(Integer.valueOf(index));
                                    synchronized (writingLock){
                                        writingsList.add(writing);
                                    }
                                    writingsListToBePublished.remove(writing);

                                }
                                break;
                            case WRITER:
                                pf.println("Welcome Writer");
                                pf.println("Please enter writing");
                                String writing = bf.readLine();
                                synchronized (writingTObePublishedLock){
                                    writingsListToBePublished.add(writing);
                                }

                                break;
                            case Administrator:
                                synchronized (userLock) {

                                    pf.println("Welcome admin");
                                    pf.println("Please enter username");
                                    String usernameToRegister = bf.readLine();
                                    pf.println("Please enter password");
                                    String passwordToRegister = bf.readLine();
                                    pf.println("Please enter role");
                                    UserType userType1 = UserType.valueOf(bf.readLine());
                                    ;
                                    synchronized (userLock) {
                                        switch (userType1) {
                                            case EDITOR:
                                                userList.add(new Editor(usernameToRegister, passwordToRegister));
                                                break;
                                            case WRITER:
                                                userList.add(new Writer(usernameToRegister, passwordToRegister));
                                                break;
                                            case Administrator:
                                                userList.add(new Administrator(usernameToRegister, passwordToRegister));
                                                break;
                                        }
                                    }
                                    break;
                                }
                        }
                        pf.println(userList.toString());
                        pf.println(writingsList.toString());
                    }catch (IOException e){
                        System.out.println(e.getMessage());
                    }

                });
                clientThread.start();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }

    }

    private User login(String username, String password) {
        synchronized (userLock){
            for (User user : userList) {
                if(user.getPassword().equals(password) && user.getUsername().equals(username)){
                    return user;
                }
            }
        }
        return null;
    }
}
