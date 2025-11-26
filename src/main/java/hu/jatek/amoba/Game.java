package hu.jatek.amoba;

import java.util.Scanner;

public class Game {
    private final Board board = new Board();
    private char current = 'X';

    public void start() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            board.print();
            System.out.print("Játékos '" + current + "' lépés (sor oszlop): ");

            int r = sc.nextInt() - 1;
            int c = sc.nextInt() - 1;

            if (!board.place(r, c, current)) {
                System.out.println("Rossz lépés!");
                continue;
            }

            if (board.checkWin(current)) {
                board.print();
                System.out.println("A játékos '" + current + "' nyert!");
                break;
            }

            if (board.isFull()) {
                board.print();
                System.out.println("Döntetlen!");
                break;
            }

            current = (current == 'X') ? 'O' : 'X';
        }

        sc.close();
    }
}