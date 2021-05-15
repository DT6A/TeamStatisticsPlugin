package ru.hse.plugin;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.StringTokenizer;
import java.util.regex.Pattern;

import static java.lang.Math.min;
import static java.lang.Math.max;

public class WordCounter implements Metric {
    private final String word;
    private final int length;
    private int numberOfOccurrences;
    private static final Pattern CORRECT_WORD = Pattern.compile("[^a-zA-Z0-9_]{2,}");

    public WordCounter(String word, int numberOfOccurrences) {
//        if (!CORRECT_WORD.matcher(word).matches()) { // TODO тут бага, надо удалять папку билд, чтобы это тестить
//            throw new RuntimeException("Incorrect word for counting");
//        }
        this.word = word;
        this.length = word.length();
        this.numberOfOccurrences = numberOfOccurrences;
    }

    public WordCounter(String word)  {
        this(word, 0);
    }

    @Override
    public void update(char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        if (!word.contains(String.valueOf(charTyped))) {
            return;
        }
        int offset = editor.getCaretModel().getOffset();
        Document document = editor.getDocument();
        /**
         * TODO +2 или +1....
         *
         * TODO хочется регистро неразличимо + substring каждый раз брать долго, кажется
         * TODO Ищем слова 'Cock' и 'coq', дописываю "... Coc coq ..." -> "... Cock coq ..."
         *
         * Перед исправлением смотри {@link TypedHandler#charTyped} (а как ссылки ставить)
         */

        StringTokenizer tokens = new StringTokenizer(document.getText(new TextRange(max(0, offset - length - 2),
                min(document.getTextLength(), offset + length + 2))));
        while (tokens.hasMoreTokens()) {
            if (tokens.nextToken().equals(word)) {
                numberOfOccurrences++;
                return;
            }
        }
    }

    @Override
    public void clear() {
        numberOfOccurrences = 0;
    }

    @Override
    public String getInfo() {
        return Integer.toString(numberOfOccurrences);
    }

    @NotNull
    @Override
    public String getName() {
        return PluginConstants.WORD_COUNTER + "(" + word + ")";
    }

    @Override
    public String toString() {
        return PluginConstants.WORD_COUNTER + " " + word + " " + numberOfOccurrences;
    }
}
