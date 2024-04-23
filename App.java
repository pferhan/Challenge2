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
    private String username, token, repoPath;
    private Boolean isRunning, initComplete;

    GitSubprocessClient gitSubprocessClient;

    public App() {
        mainPanel = new JPanel();
        isRunning = false;
        initComplete = false;

        mainPanel.setLayout(null);
        mainPanel.setBackground(java.awt.Color.pink);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(800, 600));
        this.setContentPane(mainPanel);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        start();
    }

    public static void main (String[] args) {
        new App();
    }

    public void start() {
        getLogin();
        run();
    }

    public void run() {
        isRunning = true;

        //File path request
        JLabel pathLabel = new JLabel("Repository File Path:", SwingConstants.RIGHT);
        JTextField pathTF = new JTextField();
        pathLabel.setBounds(50, 100, 160, 30);
        pathTF.setBounds(230, 100, 420, 30);
        mainPanel.add(pathLabel);
        mainPanel.add(pathTF);
        JButton pathButton = new JButton("OK");
        pathButton.setBounds(670, 100, 60, 30);
        mainPanel.add(pathButton);
        pathButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repoPath = pathTF.getText();
                System.out.println(repoPath);
            }
        });

        repaint();

        while (isRunning) {

            //Check completion of repo initialization
            if (!initComplete) {

                //Check if repo path entered by user
                if (repoPath != null) {

                    //Turn local project into github repo
                    gitSubprocessClient = new GitSubprocessClient(repoPath);
                    String gitInit = gitSubprocessClient.gitInit();

                    //Initialization of repo complete. Print to terminal
                    initComplete = true;
                    System.out.println(gitInit);
                }
            }

            //Sleep
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } 
    }

    //Get username and access token
    private void getLogin() {
        //Username and access token panel
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

                loginPanel.setVisible(false);
            }
        });

        this.add(loginPanel);
        repaint();

        //Wait to load main panel until login obtained
        while (username == null || token == null) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}