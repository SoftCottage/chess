import client.ServerFacade;
import ui.ChessClient;

public class Main {
    public static void main(String[] args) {

        // Default server URL (your server will run on this)
        String serverUrl = "http://localhost:8080";

        // If run with a command-line argument, override default
        if (args.length == 1) {
            serverUrl = args[0];
        }

        ServerFacade facade = new ServerFacade(serverUrl);

        ChessClient client = new ChessClient(facade);

        System.out.println("♕ 240 Chess Client");
        client.run();  // Start the REPL loop
    }
}
