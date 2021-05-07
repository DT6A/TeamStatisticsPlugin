package ru.hse.plugin;

import java.util.List;

public class Storage { // TODO удалить это, но как-нибудь не потерять комментарий с великой мудростью
    // предлагается получать его через getInstance тоже, мне просто кажется, что с серриализацией так поудобнее
    // слово "поудобнее" можно раскрыть: я боюсь пожрать говна с тем что дает идея, а если делать так,
    // то выглядит, что говна скорее всего не пожру. мораль: не жрите говно если можно не жрать говно
//    private StorageData data;

    private static Storage instance = null;

    public static synchronized Storage getInstance() {
        if (instance == null) {
            // make demon
            instance = new Storage();
        }
        return instance;
    }
}
