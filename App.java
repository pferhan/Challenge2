import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import git.tools.client.GitSubprocessClient;
import github.tools.client.GitHubApiClient;
import github.tools.client.RequestParams;
import github.tools.responseObjects.*;

public class App extends JFrame{
    
    private Image logo;
    private JPanel mainPanel;
    private String username, token, repoPath, name, description;
    private Boolean isRunning, initComplete, isPrivate;
    GitHubApiClient gitHubApiClient;
    GitSubprocessClient gitSubprocessClient;

    public App() {
        mainPanel = new MainPanel();
        isRunning = false;
        initComplete = false;

        mainPanel.setLayout(null);
        mainPanel.setBackground(java.awt.Color.pink);

        //Load logo image
        try {
            logo = ImageIO.read(getClass().getResource("SmallerLogo.png"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

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


            //request, name, description, and privacy
            JLabel nameLabel = new JLabel("Repository Name:", SwingConstants.RIGHT);
            JTextField nameTF = new JTextField();
            nameLabel.setBounds(50, 200, 160, 30);
            nameTF.setBounds(230, 200, 420, 30);
            mainPanel.add(nameLabel);
            mainPanel.add(nameTF);

            JLabel descriptionLabel = new JLabel("Repository Description:", SwingConstants.RIGHT);
            JTextField descriptionTF = new JTextField();
            descriptionLabel.setBounds(50, 300, 160, 30);
            descriptionTF.setBounds(230, 300, 420, 30);
            mainPanel.add(descriptionLabel);
            mainPanel.add(descriptionTF);

           
            JToggleButton privacyButton = new JToggleButton("Click here");
            JLabel privacyLabel = new JLabel("Click to select Public or Private:", SwingConstants.RIGHT);
            privacyButton.setBounds(330, 400, 200, 30);
            privacyLabel.setBounds(50, 400, 260, 30);
            mainPanel.add(privacyButton);
            mainPanel.add(privacyLabel);
             privacyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (privacyButton.isSelected()) {
                    privacyButton.setText("Public");
                    isPrivate = false;
                } else {
                    privacyButton.setText("Private");
                    isPrivate = true;
                }
            }
        });
            
            
            JButton createButton = new JButton("create");
            createButton.setBounds(370, 500, 100, 30);
            mainPanel.add(createButton);
            createButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Git add all
                    String gitAddAll = gitSubprocessClient.gitAddAll();
                    System.out.println(gitAddAll);

                    //Git commit
                    String commit = gitSubprocessClient.gitCommit("Initial Commit");
                    System.out.println(commit);

                    name = nameTF.getText();
                    description = descriptionTF.getText();
                    
                    RequestParams requestParams = new RequestParams();
                    requestParams.addParam("name", name);                   // name of repo
                    requestParams.addParam("description", description); // repo description
                    requestParams.addParam("private", isPrivate);                    // if repo is private or not

                    CreateRepoResponse createRepo = gitHubApiClient.createRepo(requestParams);
                    System.out.println("repo created");

                    //Git push
                    String push = gitSubprocessClient.gitPush("master");
                }
            });


            repaint();

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
                gitHubApiClient = new GitHubApiClient(username, token);
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

    private class MainPanel extends JPanel {
    
        public MainPanel() {

        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            g.drawImage(logo, 0, 0, null);
        }
        
    }
}