package ru.hse.plugin;

import com.intellij.openapi.editor.CaretModel;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.LogicalPosition;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class WordCounter implements Metric {
    private final String word;
    private final int length;
    private int numberOfOccurrences;

    public WordCounter(String word, int numberOfOccurrences) {
        // TODO no spaces in word
        this.word = word;
        this.length = word.length();
        this.numberOfOccurrences = numberOfOccurrences;
    }

    public WordCounter(String word) {
        this(word, 0);
//        this.word = word;
//        length = word.length();
    }

    @Override
    public boolean update(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        int offset = editor.getCaretModel().getOffset();
        Document document = editor.getDocument();
        // TODO +2 или +1....
        // TODO зачем перенос? в 120 символов же вмещается
        String substring = document.getText(new TextRange(offset - length - 2,
                offset + length + 2));
        for (int start = 0; start < substring.length() - length - 1; start++) {
            if (substring.charAt(start) == ' ' &&
                    substring.charAt(start + length + 1) == ' ') {
                if (substring.substring(start + 1, start + length).equals(word)) {
                    numberOfOccurrences++;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void clear() {
        numberOfOccurrences = 0;
    }

    @Override
    public String getInfo() {
        return Integer.toString(numberOfOccurrences);
    }

    @Override
    public String toString() {
        return PluginConstants.WORD_COUNTER + " " + word + " " + numberOfOccurrences;
    }
}
