package p1;

import java.util.*;

class Player {
    String name;
    int position;

    public Player(String name) {
        this.name = name;
        this.position = 1; // Game starts at position 1
    }
}

class Dice {
    private final Random random;
    private final int sides;

    public Dice(int sides) {
        this.sides = sides;
        this.random = new Random();
    }

    public int roll() {
        return random.nextInt(sides) + 1;
    }
}

class Board {
    private final int size;
    private final Map<Integer, Integer> snakes;
    private final Map<Integer, Integer> ladders;

    public Board(int size) {
        this.size = size;
        this.snakes = new HashMap<>();
        this.ladders = new HashMap<>();
        initializeSnakesAndLadders();
    }

    private void initializeSnakesAndLadders() {
        // Fixed snakes
        snakes.put(98, 2);
        snakes.put(50, 5);
        snakes.put(77, 25);
        snakes.put(93, 66);

        // Fixed ladders
        ladders.put(3, 40);
        ladders.put(10, 60);
        ladders.put(20, 70);
        ladders.put(45, 90);
    }

    public int getNewPosition(int position) {
        if (snakes.containsKey(position)) {
            System.out.println("Oops! Bitten by a snake at " + position + " → Moving to " + snakes.get(position));
            return snakes.get(position);
        } else if (ladders.containsKey(position)) {
            System.out.println("Yay! Climbed a ladder at " + position + " → Moving to " + ladders.get(position));
            return ladders.get(position);
        }
        return position;
    }

    public int getBoardSize() {
        return size * size;
    }
}

class Game {
    private final Board board;
    private final Dice dice;
    private final Queue<Player> players;

    public Game(int boardSize, int diceSides, List<Player> playerList) {
        this.board = new Board(boardSize);
        this.dice = new Dice(diceSides);
        this.players = new LinkedList<>(playerList);
    }

    public void play() {
        while (true) {
            Player player = players.poll();
            int roll = dice.roll();
            int newPosition = player.position + roll;

            System.out.println(player.name + " rolled a " + roll);

            if (newPosition > board.getBoardSize()) {
                System.out.println(player.name + " rolled too high! Staying at " + player.position);
                players.offer(player);
            } else {
                player.position = board.getNewPosition(newPosition);
                System.out.println(player.name + " moved to " + player.position);

                if (player.position == board.getBoardSize()) {
                    System.out.println("--> " + player.name + " wins! <--");
                    return;
                }
                players.offer(player);
            }
        }
    }
}

public class SnakesAndLadders {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Taking user input for board size and dice sides
        System.out.print("Enter board size (N x N): (Prefer 10)");
        int boardSize = scanner.nextInt();

        System.out.print("Enter number of sides on the dice: (Prefer 6)");
        int diceSides = scanner.nextInt();

        // Taking user input for players
        System.out.print("Enter number of players: ");
        int numPlayers = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        List<Player> players = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++) {
            System.out.print("Enter player " + (i + 1) + " name: ");
            String playerName = scanner.nextLine();
            players.add(new Player(playerName));
        }

        System.out.println("\nSnakes are at: {98 - 2, 50 - 5, 77 - 25, 93 - 66}");
        System.out.println("Ladders are at: {3 - 40, 10 - 60, 20 - 70, 45 - 90}");

        Game game = new Game(boardSize, diceSides, players);

        System.out.println("\nGame starts now!\n");
        game.play();

        scanner.close();
    }
}
