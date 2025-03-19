package br.com.core;

public enum Visibility {
    desativado(0),
    ativado(1);


    private int level;

    private Visibility(int level) {
        this.level = level;
    }
}
