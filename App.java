import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;

public class App extends JFrame{

    private JPanel panel;

    public App() {

        panel = new JPanel();

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(800, 600));
        this.setContentPane(panel);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        //TODO: Everything

        this.setVisible(true);
    }

    public static void main (String[] args) {
        new App();
    }
}