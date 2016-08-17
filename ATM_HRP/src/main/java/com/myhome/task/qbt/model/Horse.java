package com.myhome.task.qbt.model;

public class Horse {

    private String  name;
    private int     odd;
    private DidWin  didWin;

    public Horse(String name, int odd, DidWin didWin) {
        this.name   = name;
        this.odd    = odd;
        this.didWin = didWin;
    }

    public String getName() {
        return name;
    }

    public int getOdd() {
        return odd;
    }

    public void setDidWin(DidWin didWin) {
        this.didWin = didWin;
    }

    public boolean isWinner() {
        return this.didWin == DidWin.WON;
    }

    @Override
    public String toString() {
        return String.format("%s,%d,%s", name, odd, didWin);
    }

    public enum DidWin {
        WON {
            @Override
            public String toString() {
                return "won";
            }
        },
        LOST {
            @Override
            public String toString() {
                return "lost";
            }
        }
    }
}