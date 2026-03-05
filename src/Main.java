import com.cinema.util.EnvConfig;
import com.cinema.view.auth.LoginFrame;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        System.out.println(EnvConfig.getInstance().get("test"));
        System.out.println(EnvConfig.getInstance().get("password"));
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}