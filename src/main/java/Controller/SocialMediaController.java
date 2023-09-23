package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Service.AccountService;;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    public SocialMediaController(){
        accountService = new AccountService();
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


}