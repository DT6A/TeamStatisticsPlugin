package ru.hse.plugin.util;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.HashSet;

public class PathSet extends HashSet<Path> {
    @Override
    public boolean add(Path path) {
        return super.add(canonical(path));
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof Path) {
            return super.contains(canonical((Path) o));
        } else {
            return false;
        }
    }

    @Override
    public boolean remove(Object o) {
        if (o instanceof Path) {
            return super.contains(canonical((Path) o));
        } else {
            return false;
        }
    }

    @NotNull
    private static Path canonical(Path path) {
        return path.toAbsolutePath().normalize();
    }
}
