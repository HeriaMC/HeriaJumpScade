package fr.heriamc.games.jumpscade;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Pair<K, V> {

    private K key;
    private V value;

    /*
        NEED TO BE MOVED IN GAME API
     */
    public Pair() {
        this.key = null;
        this.value = null;
    }

    public void setKeyAndValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public void setBothNull() {
        this.key = null;
        this.value = null;
    }

    public boolean keyIsNull() {
        return key == null;
    }

    public boolean valueIsNull() {
        return value == null;
    }

    public boolean bothIsNull() {
        return key == null && value == null;
    }

}