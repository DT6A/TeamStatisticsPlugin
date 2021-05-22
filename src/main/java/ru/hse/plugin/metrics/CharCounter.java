package ru.hse.plugin.metrics;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.util.PluginConstants;

import java.util.Objects;

public class CharCounter implements Metric {
    private final char character;
    private int numberOfOccurrences;
    // TODO test
    public CharCounter(char character, int numberOfOccurrences) {
        this.character = character;
        this.numberOfOccurrences = numberOfOccurrences;
    }

    public CharCounter(char character) {
        this(character, 0);
    }

    @Override
    public void update(char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        if (charTyped == character) {
            numberOfOccurrences++;
        }
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
        //return "lines";
        return PluginConstants.CHAR_COUNTER + "(" + Character.getNumericValue(character) + ")";
    }

    @Override
    public String toString() {
        return PluginConstants.CHAR_COUNTER + " " + Character.getNumericValue(character) + " " + numberOfOccurrences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CharCounter that = (CharCounter) o;
        return character == that.character;
    }

    @Override
    public int hashCode() {
        return Objects.hash(character);
    }
}
