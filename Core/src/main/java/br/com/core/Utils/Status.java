package br.com.core.Utils;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Status {
    private boolean online;
    private boolean premium;

    public Status(boolean online) {
        this.online = online;
    }

    public boolean isOnline() {
        return this.online;
    }

    public boolean isPremium() {
        return this.premium;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setPremium(boolean premium) {
        this.premium = premium;
    }
}

