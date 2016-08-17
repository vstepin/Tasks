package com.myhome.task.qbt.model;

import java.util.*;
import java.util.stream.Collectors;

public enum ATM {

    INSTANCE;

    private final static List<Cassette> CASSETTES = Arrays.asList(Cassette.$1, Cassette.$5, Cassette.$10, Cassette.$20, Cassette.$100);

    public void service() {
        CASSETTES.stream().forEach(Cassette::service);
    }

    public void inventory() {
        System.out.println(
            String.format(
                "Inventory:\n%s",
                CASSETTES.stream().map(Cassette::printInventory).collect(Collectors.joining("\n"))
            )
        );
    }

    public void dispensing() {
        System.out.println(
            String.format(
                "Dispensing:\n%s",
                CASSETTES.stream().map(Cassette::printDispensing).collect(Collectors.joining("\n"))
            )
        );
    }

    public boolean cash(int payout) {

        if (payout <= CASSETTES.stream().mapToInt(Cassette::getCash).sum()) {

            int size                    = CASSETTES.size();
            int cassettesDispensing[]   = new int[size];

            while (size-- > 0) {
                Cassette cassette = CASSETTES.get(size);

                int cash        = cassette.getCash();
                int inventory   = cassette.getInventory();

                while (inventory-- > 0) {
                    if (payout < cash) {
                        cash -= cassette.DENOMINATION;
                    } else {
                        payout -= cash;
                        cassettesDispensing[size] = inventory + 1;
                        break;
                    }
                }
            }

            if (payout == 0) {
                CASSETTES.forEach(cassette -> cassette.setInventory(cassette.getInventory() - cassettesDispensing[CASSETTES.indexOf(cassette)]));
                return true;
            }
        }

        return false;
    }

    private enum Cassette {

        $1,
        $5,
        $10,
        $20,
        $100;

        private final static    int MAX_INVENTORY   = 10;
        private final           int DENOMINATION    = Integer.parseInt(this.name().substring(1));
        private                 int inventory;

        Cassette() {
            service();
        }

        public int getInventory() {
            return inventory;
        }

        public void setInventory(int inventory) {
            this.inventory = inventory;
        }

        public int getCash() {
            return inventory*DENOMINATION;
        }

        public void service() {
            setInventory(MAX_INVENTORY);
        }

        public String printDispensing() {
            return String.format("%s,%d", this.name(), MAX_INVENTORY - inventory);
        }

        public String printInventory() {
            return String.format("%s,%d", this.name(), inventory);
        }
    }
}