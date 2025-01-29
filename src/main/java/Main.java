import Controller.SocialMediaController;
import Service.SocialMediaService;
import DAO.SocialMediaDAO;
import io.javalin.Javalin;

/**
 * This class is provided with a main method to allow you to manually run and test your application.
 * This class will not affect your program in any way and you may write whatever code you like here.
 */
public class Main {
    public static void main(String[] args) {
        // Instantiate DAO
        SocialMediaDAO dao = new SocialMediaDAO();
        
        // Instantiate Service with DAO
        SocialMediaService service = new SocialMediaService(dao);
        
        // Instantiate Controller with Service
        SocialMediaController controller = new SocialMediaController(service);
        
        // Start API
        Javalin app = controller.startAPI();
        app.start(8080);
    }
}

