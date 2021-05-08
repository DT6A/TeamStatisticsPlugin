package ru.hse.plugin;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class CharCounter implements Metric {
    private final char character;
    private int numberOfOccurrences;

    public static final CharCounter LINE_COUNTER = new CharCounter('\n');

    public CharCounter(char character, int numberOfOccurrences) {
        this.character = character;
        this.numberOfOccurrences = numberOfOccurrences;
    }

    public CharCounter(char character) {
        this(character, 0);
    }

    @Override
    public boolean update(char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        if (charTyped == character) {
            numberOfOccurrences++;
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        numberOfOccurrences = 0;
    }

    @Override
    public String getInfo() {
        return String.valueOf(numberOfOccurrences);
    }

    @Override
    public @NotNull String getName() {
        return PluginConstants.CHAR_COUNTER + "(" + Character.getNumericValue(character) + ")";
    }

    @Override
    public String toString() {
        return PluginConstants.CHAR_COUNTER + " " + Character.getNumericValue(character) + " " + numberOfOccurrences;
    }
}
