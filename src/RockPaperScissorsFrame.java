import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Random;

public class RockPaperScissorsFrame extends JFrame {
    private JLabel titleLabel;
    private JTextArea resultsArea;
    private JButton rockButton, paperButton, scissorsButton, quitButton, startGameButton;
    private JTextField playerWinsField, computerWinsField, tiesField;
    private int playerWins = 0, computerWins = 0, ties = 0;

    public RockPaperScissorsFrame() {
        setTitle("Rock Paper Scissors");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        initTopPanel();
        initGamePanel();
        initStatsPanel();
        initBottomPanel();

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setSize((int)(screenSize.width * 0.75), (int)(screenSize.height * 0.75));
        setLocationRelativeTo(null); // Center the frame
    }

    private void initTopPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        titleLabel = new JLabel("Rock Paper Scissors", SwingConstants.CENTER);
        titleLabel.setFont(new Font("MS Gothic", Font.BOLD, 48));
        titleLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        titleLabel.setVerticalTextPosition(SwingConstants.BOTTOM);

        topPanel.add(titleLabel, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
    }

    private void initGamePanel() {
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new FlowLayout());

        rockButton = new JButton("Rock");
        paperButton = new JButton("Paper");
        scissorsButton = new JButton("Scissors");

        rockButton.setFont(new Font("Cracked", Font.PLAIN, 24));
        paperButton.setFont(new Font("Cracked", Font.PLAIN, 24));
        scissorsButton.setFont(new Font("Cracked", Font.PLAIN, 24));

        rockButton.addActionListener(this::playerChoice);
        paperButton.addActionListener(this::playerChoice);
        scissorsButton.addActionListener(this::playerChoice);

        gamePanel.add(rockButton);
        gamePanel.add(paperButton);
        gamePanel.add(scissorsButton);

        resultsArea = new JTextArea(10, 40);
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        resultsArea.setEditable(false);
        gamePanel.add(scrollPane);

        add(gamePanel, BorderLayout.CENTER);
    }

    private void initStatsPanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new GridLayout(3, 2));

        statsPanel.add(new JLabel("Player Wins:"));
        playerWinsField = new JTextField("0");
        playerWinsField.setEditable(false);
        statsPanel.add(playerWinsField);

        statsPanel.add(new JLabel("Computer Wins:"));
        computerWinsField = new JTextField("0");
        computerWinsField.setEditable(false);
        statsPanel.add(computerWinsField);

        statsPanel.add(new JLabel("Ties:"));
        tiesField = new JTextField("0");
        tiesField.setEditable(false);
        statsPanel.add(tiesField);

        add(statsPanel, BorderLayout.EAST);
    }

    private void initBottomPanel() {
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());

        startGameButton = new JButton("Start Game!");
        startGameButton.setFont(new Font("Cracked", Font.PLAIN, 24));
        startGameButton.addActionListener(this::startGame);

        quitButton = new JButton("Quit");
        quitButton.setFont(new Font("Cracked", Font.PLAIN, 24));
        quitButton.addActionListener(e -> System.exit(0));

        bottomPanel.add(startGameButton);
        bottomPanel.add(quitButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private String determineWinner(String playerChoice, String computerChoice) {
        if (playerChoice.equals(computerChoice)) {
            return "It's a tie!";
        }
        switch (playerChoice) {
            case "Rock":
                return computerChoice.equals("Scissors") ? "Rock breaks scissors (Player wins)" : "Paper covers rock (Computer wins)";
            case "Paper":
                return computerChoice.equals("Rock") ? "Paper covers rock (Player wins)" : "Scissors cuts paper (Computer wins)";
            case "Scissors":
                return computerChoice.equals("Paper") ? "Scissors cuts paper (Player wins)" : "Rock breaks scissors (Computer wins)";
            default:
                return "Invalid choice";
        }
    }

    private String computerChoice() {
        Strategy strategy = new RandomStrategy(); // You can replace this with different strategies
        return strategy.determineMove();
    }

    private void playerChoice(ActionEvent e) {
        JButton buttonClicked = (JButton) e.getSource();
        String playerChoice = buttonClicked.getText(); // Get the player's choice (Rock, Paper, Scissors)
        String computerMove = computerChoice(); // Get the computer's random choice

        String result = determineWinner(playerChoice, computerMove); // Determine who wins
        resultsArea.append("Player chose: " + playerChoice + "\nComputer chose: " + computerMove + "\n" + result + "\n\n");

        updateStats(result); // Update game stats
    }

    private void updateStats(String result) {
        if (result.contains("Player wins")) {
            playerWins++;
        } else if (result.contains("Computer wins")) {
            computerWins++;
        } else {
            ties++;
        }
        playerWinsField.setText(String.valueOf(playerWins));
        computerWinsField.setText(String.valueOf(computerWins));
        tiesField.setText(String.valueOf(ties));
    }

    private void startGame(ActionEvent e) {
        resultsArea.setText(""); // Clear results for new session
        playerWins = 0;
        computerWins = 0;
        ties = 0;
        playerWinsField.setText("0");
        computerWinsField.setText("0");
        tiesField.setText("0");
    }

    public interface Strategy {
        String determineMove();
    }

    public class RandomStrategy implements Strategy {
        private final String[] choices = {"Rock", "Paper", "Scissors"};

    @Override
    public String determineMove() {
        Random random = new Random();
        return choices[random.nextInt(choices.length)];
    }
    }
}
