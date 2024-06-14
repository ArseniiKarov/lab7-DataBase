package manager;

import data.Worker;
import data.generators.WorkerGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
public class Console {
    private Request request;

    public void start(InputStream inputStream) throws Exception {
        while (true) {
            Scanner scanner = new Scanner(inputStream);
            Server client = new Server();
            String input;
            boolean status_of_ath = false;
            System.out.println("Welcome to application!");
            System.out.println(
                    "if you a new user, then type 'create' to create a new account\n" +
                            "if you already have an account - type 'log_in' to authorized"
            );
            while (!status_of_ath) {
                input = scanner.nextLine();

                if (input.equals("create")) {
                    System.out.println("create username");
                    String username = scanner.nextLine();
                    System.out.println("create password");
                    String password = scanner.nextLine();
                    request = new Request("create", null, password, username);

                    if (client.sendEcho(request).equals("User created successfully")) {
                        status_of_ath = true;
                        System.out.println("Great! Now you have log in!");
                    } else {
                        System.out.println("user with that data is already exist");
                    }

                } else if (input.equals("log_in")) {
                    System.out.println("input username");
                    String username = scanner.nextLine();
                    System.out.println("input password");
                    String password = scanner.nextLine();
                    request = new Request("log_in", null, password, username);

                    if (client.sendEcho(request).equals("true")) {
                        status_of_ath = true;
                        System.out.println("Great! Now you have log in!");
                    } else {
                        System.out.println("wrong username or a password");
                    }
                } else {
                    System.out.println("you can't use this application without authorization");
                }
            }
            while (true) {
                try {
                    client = new Server();
                    input = scanner.nextLine();
                    request.setMessage(input);

                    if (input.equals("remove_greater") || input.equals("remove_lower")) {
                        Worker worker = WorkerGenerator.createWorker(0L);
                        request.setWorker(worker);
                        String answer = client.sendEcho(request);
                        System.out.println(answer);

                    } else if (input.split(" ")[0].equals("update")) {
                        String anwser = client.sendEcho(request);

                        if (anwser.equals("wrong id")) {
                            System.out.println(anwser);
                            continue;
                        } else {
                            System.out.println("New Worker");
                            request.setWorker(WorkerGenerator.createWorker(0L));

                        }

                    } else if (input.equals("exit")) {

                        if (input.split(" ")[0].equals("execute_script")) {
                            ExecuteScript.execute(request.getMessage());
                        } else {
                            String echo = client.sendEcho(request);
                            System.out.println("Echo from server: \n" + echo);
                            client.close();
                        }

                    } else {
                        String echo = client.sendEcho(request);
                        System.out.println("info from server: " + echo);
                        client.close();
                    }
                } catch (SocketException e) {
                    System.out.println("SocketException: \n" + e.getMessage());
                } catch (UnknownHostException e) {
                    System.out.println("UnknownHostException: \n" + e.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }


            }
        }
    }
}
