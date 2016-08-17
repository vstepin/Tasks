package com.myhome.task.qbt;

import com.myhome.task.qbt.model.ATM;
import com.myhome.task.qbt.model.Horse;

import java.util.*;
import java.util.stream.Collectors;

public class App {

    private static List<Horse> horses = Arrays.asList(
        new Horse("That Darn Gray Cat", 5,  Horse.DidWin.WON),
        new Horse("Fort Utopia",        10, Horse.DidWin.LOST),
        new Horse("Count Sheep",        9,  Horse.DidWin.LOST),
        new Horse("Ms Traitour",        4,  Horse.DidWin.LOST),
        new Horse("Real Princess",      3,  Horse.DidWin.LOST),
        new Horse("Pa Kettle",          5,  Horse.DidWin.LOST),
        new Horse("Gin Stinger",        6,  Horse.DidWin.LOST)
    );

    private static ATM atm = ATM.INSTANCE;

    public static void main( String[] args ) {
        atm.inventory();
        printResults();

        Scanner console = new Scanner(System.in);

        while (!console.hasNext("(?i)q")) {

            String choice = console.nextLine();

            if (choice.matches("(?i)r|((?i)w|\\d+) \\d+")) {
                switch (choice.charAt(0)) {
                    case 'r':
                    case 'R':
                        restocksAction();
                        break;
                    case 'w':
                    case 'W':
                        setWinnerAction(Integer.parseInt(choice.split(" ")[1]));
                        break;
                    default:
                        setBetAction(Integer.parseInt(choice.split(" ")[0]), Integer.parseInt(choice.split(" ")[1]));
                        break;
                }
            } else {
                if (choice.matches("\\d+ \\d*\\.\\d+")) {
                    System.out.println(String.format("Invalid Bet: %s", choice.split(" ")[1]));
                } else {
                    if (!choice.isEmpty()) {
                        System.out.println(String.format("Invalid Command: %s", choice));
                    }
                }
            }
        }

    }

    private static void restocksAction() {
        atm.service();
        atm.inventory();
    }

    private static void setWinnerAction(int horseNumber) {
        if (horseNumber > 0 && horseNumber <= horses.size()) {

            horses.stream()
                  .peek(horse -> horse.setDidWin(Horse.DidWin.LOST))
                  .filter(horse -> horses.indexOf(horse) == horseNumber - 1)
                  .findFirst()
                  .ifPresent(horse -> horse.setDidWin(Horse.DidWin.WON));

            atm.inventory();
            printResults();
        } else {
            System.out.println(String.format("Invalid Horse Number: %d", horseNumber));
        }
    }

    private static void setBetAction(int horseNumber, int bet) {
        if (horseNumber > 0 && horseNumber <= horses.size()) {
            Horse horse = horses.get(horseNumber - 1);

            if (horse.isWinner()) {
                int amount = bet*horse.getOdd();

                if (atm.cash(amount)) {
                    System.out.println(String.format("Payout: %s,$%d", horse.getName(), amount));
                    atm.dispensing();
                    atm.inventory();
                    printResults();
                } else {
                    System.out.println(String.format("Insufficient Funds: %d", amount));
                }
            } else {
                System.out.println(String.format("No Payout: %s", horse.getName()));
                atm.inventory();
                printResults();
            }
        } else {
            System.out.println(String.format("Invalid Horse Number: %d", horseNumber));
        }
    }

    private static void printResults() {
        System.out.println(
            String.format(
                "Horses:\n%s",
                horses.stream()
                      .map(horse -> String.format("%d,%s", horses.indexOf(horse) + 1, horse))
                      .collect(Collectors.joining("\n"))
            )
        );
    }
}