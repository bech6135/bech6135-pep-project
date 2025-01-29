package Controller;

import Service.SocialMediaService;
import DAO.SocialMediaDAO; 
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import java.util.List;

/**
 * The SocialMediaController class manages all API endpoints for user accounts and messages.
 * It defines HTTP routes and delegates business logic to the SocialMediaService.
 */
public class SocialMediaController {
    private final SocialMediaService service;

    // No-args constructor (for tests)
    public SocialMediaController() {
        this.service = new SocialMediaService(new SocialMediaDAO()); // It will recognize SocialMediaDAO
    }

    // Existing constructor (for manual setup in Main.java)
    public SocialMediaController(SocialMediaService service) {
        this.service = service;
    }
    
    /** Starts the Javalin API and defines all endpoints. */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
    
        // User endpoints
        app.post("/register", this::registerUser);
        app.post("/login", this::loginUser);
    
        // Message endpoints
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.patch("/messages/{message_id}", this::updateMessage);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUser);
    
        return app;
    }
    /**
     * Handles user registration by validating input and creating a new account.
     * Responds with 200 on success or 400 on failure.
     */
    private void registerUser(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account createdAccount = service.register(account);
        if (createdAccount != null) {
            ctx.json(createdAccount);
        } else {
            ctx.status(400);
        }
    }

    /**
     * Handles user login by verifying credentials.
     * Responds with 200 on success or 401 on failure.
     */
    private void loginUser(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account authenticatedAccount = service.login(account);
        if (authenticatedAccount != null) {
            ctx.json(authenticatedAccount);
        } else {
            ctx.status(401);
        }
    }

    /**
     * Handles message creation by validating and persisting a new message.
     * Responds with 200 on success or 400 on failure.
     */
    private void createMessage(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        Message createdMessage = service.createMessage(message);
        if (createdMessage != null) {
            ctx.json(createdMessage);
        } else {
            ctx.status(400);
        }
    }

    /**
     * Retrieves all messages from the database.
     * Always responds with status 200.
     */
    private void getAllMessages(Context ctx) {
        List<Message> messages = service.getAllMessages();
        ctx.json(messages);
    }

    /**
     * Retrieves a message by its ID.
     * Responds with the message object or status 200 if not found.
     */
    private void getMessageById(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = service.getMessageById(messageId);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(200);
        }
    }

    /**
     * Deletes a message by its ID.
     * Responds with the deleted message or an empty body with status 200 if not found.
     */
    private void deleteMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = service.deleteMessage(messageId);
        if (deletedMessage != null) {
            ctx.json(deletedMessage);
        } else {
            ctx.status(200);
        }
    }

    /**
     * Updates a message text by its ID.
     * Responds with the updated message on success or 400 on failure.
     */
    private void updateMessage(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = ctx.bodyAsClass(Message.class);
        Message updatedMessage = service.updateMessage(messageId, message.getMessage_text());
        if (updatedMessage != null) {
            ctx.json(updatedMessage);
        } else {
            ctx.status(400);
        }
    }

    /**
     * Retrieves all messages posted by a specific user.
     * Always responds with status 200.
     */
    private void getMessagesByUser(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = service.getMessagesByUser(accountId);
        ctx.json(messages);
    }
}
