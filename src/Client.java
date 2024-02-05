import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try(Socket socket = new Socket("localhost",5555);
            BufferedReader bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter pf = new PrintWriter(socket.getOutputStream(),true)){
            Scanner scanner = new Scanner(System.in);
            String input1 = bf.readLine();
            System.out.println(input1);
            String username = scanner.nextLine();
            pf.println(username);
            String input2 = bf.readLine();
            System.out.println(input2);
            String password = scanner.nextLine();
            pf.println(password);
            String welcomeMessage = bf.readLine();
            System.out.println(welcomeMessage);
            switch (welcomeMessage){
                case "Welcome admin":
                    System.out.println(bf.readLine());
                    pf.println(scanner.nextLine());
                    System.out.println(bf.readLine());
                    pf.println(scanner.nextLine());
                    System.out.println(bf.readLine());
                    pf.println(scanner.nextLine());
                    break;
                case "Welcome editor":
                    System.out.println(bf.readLine());
                    System.out.println(bf.readLine());
                    pf.println(scanner.nextLine());
                    break;
                case "Welcome Writer":
                    System.out.println(bf.readLine());
                    pf.println(scanner.nextLine());
                    break;
            }
            System.out.println(bf.readLine());
            System.out.println(bf.readLine());




        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
