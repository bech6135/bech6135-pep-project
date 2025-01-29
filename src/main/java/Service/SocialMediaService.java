package Service;

import DAO.SocialMediaDAO;
import Model.Account;
import Model.Message;
import java.util.List;

/**
 * Service layer responsible for handling business logic before interacting with the DAO.
 */
public class SocialMediaService {
    private final SocialMediaDAO dao;

    /**
     * Constructor for initializing the SocialMediaService with a DAO instance.
     * @param dao The SocialMediaDAO instance for database interaction.
     */
    public SocialMediaService(SocialMediaDAO dao) {
        this.dao = dao;
    }

    /**
     * Registers a new user if the username is unique and password meets requirements.
     * @param account The account details from the request.
     * @return The newly created account or null if registration fails.
     */
    public Account register(Account account) {
        if (account.getUsername().isBlank() || account.getPassword().length() < 4) {
            return null; // Username cannot be blank, and password must be at least 4 characters
        }
        if (dao.getAccountByUsername(account.getUsername()) != null) {
            return null; // Username must be unique
        }
        return dao.createAccount(account);
    }

    /**
     * Authenticates a user by checking username and password.
     * @param account The login credentials.
     * @return The authenticated account or null if login fails.
     */
    public Account login(Account account) {
        Account existingAccount = dao.getAccountByUsername(account.getUsername());
        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount;
        }
        return null; // Invalid username or password
    }

    /**
     * Creates a new message if it meets validation rules.
     * @param message The message details.
     * @return The created message or null if validation fails.
     */
    public Message createMessage(Message message) {
        if (message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            return null; // Message cannot be blank and must be within 255 characters
        }
        if (dao.getAccountById(message.getPosted_by()) == null) {
            return null; // User must exist
        }
        return dao.createMessage(message);
    }

    /**
     * Retrieves all messages.
     * @return A list of messages.
     */
    public List<Message> getAllMessages() {
        return dao.getAllMessages();
    }

    /**
     * Retrieves a message by ID.
     * @param messageId The message ID.
     * @return The message or null if not found.
     */
    public Message getMessageById(int messageId) {
        return dao.getMessageById(messageId);
    }

    /**
     * Deletes a message by ID.
     * @param messageId The message ID.
     * @return The deleted message or null if not found.
     */
    public Message deleteMessage(int messageId) {
        return dao.deleteMessage(messageId);
    }

    /**
     * Updates an existing message if the new text meets validation rules.
     * @param messageId The message ID.
     * @param newMessageText The updated text.
     * @return The updated message or null if update fails.
     */
    public Message updateMessage(int messageId, String newMessageText) {
        if (newMessageText.isBlank() || newMessageText.length() > 255) {
            return null; // Message cannot be blank and must be within 255 characters
        }
        return dao.updateMessage(messageId, newMessageText);
    }

    /**
     * Retrieves all messages posted by a specific user.
     * @param accountId The user ID.
     * @return A list of messages from the user.
     */
    public List<Message> getMessagesByUser(int accountId) {
        return dao.getMessagesByUser(accountId);
    }
}
