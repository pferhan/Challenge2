import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import git.tools.client.GitSubprocessClient;
import github.tools.client.GitHubApiClient;
import github.tools.client.RequestParams;
import github.tools.responseObjects.*;

public class App extends JFrame{

    private JPanel mainPanel;
    String username, token;
    private GitHubApiClient gitHubApiClient;

    public App() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setSize(new Dimension(800, 600));
        this.setContentPane(mainPanel);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        getLogin();
        createRepo();

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

    private void createRepo() {
        //create panel and add text fields
        JPanel createRepoPanel = new JPanel();
        JTextField nameTF = new JTextField();
        JTextField descriptionTF = new JTextField();
        JTextField repositoryPathTF = new JTextField();
        JToggleButton privacyToggle = new JToggleButton("Private");
    
        //add labels
        JLabel nameLabel = new JLabel("Repository Name:", SwingConstants.RIGHT);
        JLabel descriptionLabel = new JLabel("Description:", SwingConstants.RIGHT);
        JLabel privacyLabel = new JLabel("Privacy:", SwingConstants.RIGHT);
        JLabel repositoryPathLabel = new JLabel("RepositoryPath:", SwingConstants.RIGHT);

        //toggle button for public and private
        privacyToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (privacyToggle.isSelected()) {
                    privacyToggle.setText("Public");
                } else {
                    privacyToggle.setText("Private");
                }
            }
        });
    
        JButton submitButton = new JButton("Create Repository");
    
        //format panel
        createRepoPanel.setBackground(java.awt.Color.gray);
        createRepoPanel.setSize(300, 200);
        createRepoPanel.setLayout(null);
        createRepoPanel.setLocation(0, 0);
    
        //format name label
        createRepoPanel.add(nameLabel);
        nameLabel.setBounds(10, 20, 100, 20);
        createRepoPanel.add(nameTF);
        nameTF.setBounds(120, 20, 170, 20);
    
        //format description label
        createRepoPanel.add(descriptionLabel);
        descriptionLabel.setBounds(10, 60, 100, 20);
        createRepoPanel.add(descriptionTF);
        descriptionTF.setBounds(120, 60, 170, 20);
    
        ////format privacy label
        createRepoPanel.add(privacyLabel);
        privacyLabel.setBounds(10, 100, 100, 20);
        createRepoPanel.add(privacyToggle);
        privacyToggle.setBounds(120, 100, 100, 20);
    
        createRepoPanel.add(submitButton);
        submitButton.setBounds(75, 140, 150, 30);
    
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String repoName = nameTF.getText();
                String repoDescription = descriptionTF.getText();
                boolean isPrivate = privacyToggle.isSelected();
                String repoPath = repositoryPathTF.getText();
    
                //Use the values to create the repository
                RequestParams requestParams = new RequestParams();
                requestParams.addParam("name", repoName);                  
                requestParams.addParam("description", repoDescription); 
                requestParams.addParam("private", isPrivate); 
                
                CreateRepoResponse createRepo = gitHubApiClient.createRepo(requestParams);

            }
        });
    
        mainPanel.add(createRepoPanel);
    }

}