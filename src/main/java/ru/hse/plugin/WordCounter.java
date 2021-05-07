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
    private int numberOfOccurrences;

    public WordCounter(String word) {
        this.word = word;
    }

    @Override
    public boolean update(@NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        int offset = editor.getCaretModel().getOffset();
        Document document = editor.getDocument();
        String substring = document.getText(new TextRange(offset - word.length(), offset));
        if (word.equals(substring)) {
            numberOfOccurrences++;
            return true;
        }
        return false;
    }

    @Override
    public String getInfo() {
        return Integer.toString(numberOfOccurrences);
    }
}
