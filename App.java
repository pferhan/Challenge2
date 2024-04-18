import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import git.tools.client.GitSubprocessClient;
import github.tools.client.GitHubApiClient;
import github.tools.responseObjects.*;

public class App extends JFrame{

    private JPanel mainPanel;
    String username, token;

    public App() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(800, 600));
        this.setContentPane(mainPanel);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        getLogin();

        //TODO: Everything

        this.setVisible(true);
    }

    public static void main (String[] args) {
        new App();
    }

    //Get username and access token
    private void getLogin() {
        JPanel loginPanel = new JPanel();
        JTextField userTF = new JTextField();
        JTextField tokenTF = new JTextField();
        JLabel userLabel = new JLabel("Username:", SwingConstants.RIGHT);
        JLabel tokenLabel = new JLabel("Access Token:", SwingConstants.RIGHT);
        JButton button = new JButton("Enter");

        loginPanel.setBackground(java.awt.Color.gray);
        loginPanel.setSize(300, 140);
        loginPanel.setLayout(null);
        loginPanel.setLocation(250, 160);

        loginPanel.add(userLabel);
        userLabel.setBounds(10, 20, 90, 20);
        loginPanel.add(userTF);
        userTF.setBounds(110, 20, 180, 20);
        loginPanel.add(tokenLabel);
        tokenLabel.setBounds(10, 60, 90, 20);
        loginPanel.add(tokenTF);
        tokenTF.setBounds(110, 60, 180, 20);
        loginPanel.add(button);
        button.setBounds(100, 100, 100, 30);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                username = userTF.getText();
                token = tokenTF.getText();

                //TODO: Error checking?

                mainPanel.setVisible(false);
            }
        });

        mainPanel.add(loginPanel);
    }
}