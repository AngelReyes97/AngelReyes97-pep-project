package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageservice;

    public SocialMediaController(){
        accountService = new AccountService();
        messageservice = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::registerUser);
        app.post("/login", this::userLogin);
        app.post("/messages", this::newMessage);
        app.get("/messages", this::getMessages);
        app.get("messages/{message_id}", this::getOneMessage);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerUser(Context context) {
        Account newUser = context.bodyAsClass(Account.class);

        if (newUser.getUsername().isBlank() || newUser.getPassword().length() < 4) {
            context.status(400);
            return;
        }

        Account existingUser = accountService.getUserByUsername(newUser.getUsername());

        if (existingUser == null) {
            Account createdUser = accountService.addAccount(newUser);

            // Return the newly created user with account_id in the response
            context.json(createdUser);
            context.status(200); // OK
        }
        else {
            context.status(400); // Bad Request
            return;
        }
    }

    private void userLogin(Context context) {
        Account userLogin = context.bodyAsClass(Account.class);
        Account validUser = accountService.userLogin(userLogin);

        if (validUser != null) {
            context.json(validUser);
            context.status(200);
        }
        else {
            context.status(401);
        }

    }

    private void newMessage(Context context) {
        Message newMessage = context.bodyAsClass(Message.class);
        Message existingUser = messageservice.existingUser(newMessage);

        if (existingUser != null) {
            if(!newMessage.getMessage_text().isBlank() && newMessage.getMessage_text().length() < 255) {
                Message createdMessage = messageservice.createMessage(newMessage);
                context.json(createdMessage);
                context.status(200);
            }
            else {
                context.status(400);
            }
        }
        else {
            context.status(400);
        }
    }

    private void getMessages(Context context) {
        List<Message> messages = messageservice.getAllMessages();

            context.json(messages);
            context.status(200); // OK
    }

    private void getOneMessage(Context context) {
        int messageId = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageservice.getMessageById(messageId);

        if (message != null) {
            context.json(message);
            context.status(200); // OK
        } else {
            context.json(""); // Empty response body
            context.status(200); // OK
        }
        
    }

}