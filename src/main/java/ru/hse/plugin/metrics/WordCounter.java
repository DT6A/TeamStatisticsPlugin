package ru.hse.plugin.metrics;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.component.CounterJComponentWrapper;
import ru.hse.plugin.metrics.component.MetricJComponentWrapper;
import ru.hse.plugin.util.PluginConstants;

import java.util.Locale;
import java.util.Objects;
import java.util.StringTokenizer;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class WordCounter extends Metric {
    private final String word;
    private final int length;
    private final boolean caseSensitive;
    private int numberOfOccurrences;
    private static final String CORRECT_WORD_REGEX = "[a-zA-Z0-9_]{2,}";

    public WordCounter(String word, int numberOfOccurrences) {
        this(word, numberOfOccurrences, true);
    }

    public WordCounter(String word, int numberOfOccurrences, boolean caseSensitive) {
        if (!word.matches(CORRECT_WORD_REGEX)) {
            throw new RuntimeException("Incorrect word for counting");
        }
        if (!caseSensitive) {
            word = word.toLowerCase(Locale.ROOT);
        }
        this.word = word;
        this.length = word.length();
        this.numberOfOccurrences = numberOfOccurrences;
        this.caseSensitive = caseSensitive;
    }

    public WordCounter(String word)  {
        this(word, 0);
    }

    private static boolean isDelimiter(char symbol) {
        return !(Character.isLetterOrDigit(symbol) || symbol == '_');
    }

    @Override
    public void updateCharTyped(char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
        // offset -- количество символов, от начала текста до каретки
        numberOfOccurrences += updateFromText(
                charTyped,
                editor.getDocument().getImmutableCharSequence(),
                editor.getCaretModel().getOffset()
        );

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
    public void mergeAndClear(Metric metric) {
        WordCounter that = cast(metric, getClass());

        if (!this.word.equals(that.word)) {
            throw new RuntimeException("Metrics are expected to be same");
        }

        if (this.caseSensitive != that.caseSensitive) {
            throw new RuntimeException("Metrics are expected to be same");
        }

        this.numberOfOccurrences += that.numberOfOccurrences;

        that.clear();
    }

    @Override
    public MetricJComponentWrapper makeComponent(Metric additional) {
        return new CounterJComponentWrapper() {
            @Override
            protected int count() {
                int counter = WordCounter.this.numberOfOccurrences;

                var that = cast(additional, WordCounter.class);
                counter += that.numberOfOccurrences;

                return counter;
            }
        };
    }

    @NotNull
    @Override
    public String localStatisticString() {
        return "Number of occurrences of \"" + word + "\"";
    }

    @Override
    public String toString() {
        return PluginConstants.WORD_COUNTER + " " + word + " " + numberOfOccurrences;
    }

    private int updateFromText(char charTyped,
                               CharSequence text,
                               int offset) {
        int deltaOccurrences = 0;
        if (isDelimiter(charTyped)) {
            int pos = offset;
            if (matchWordWithText(pos, text)) {
                deltaOccurrences++;
            }
            pos = offset - 2 - length;
            if (pos < -1) {
                return deltaOccurrences;
            }
            if (pos != -1 && !isDelimiter(text.charAt(pos))) {
                return deltaOccurrences;
            }
            pos++;
            if (matchWordWithText(pos, text)) {
                deltaOccurrences++;
            }
            return deltaOccurrences;
        }
        else {
            StringTokenizer tokens = new StringTokenizer(text.subSequence(
                    max(0, offset - length - 1),
                    min(text.length(), offset + length)
            ).toString());
            while (tokens.hasMoreTokens()) {
                String token = tokens.nextToken();
                if (!caseSensitive) {
                    token = token.toLowerCase(Locale.ROOT);
                }
                if (token.equals(word)) {
                    return 1;
                }
            }
            return 0;
        }
    }

    private boolean matchWordWithText(int posInText, CharSequence text) {
        int indexAtWord = 0;
        while (indexAtWord < word.length() && posInText < text.length()) {
            char charText = text.charAt(posInText);
            char charWord = word.charAt(indexAtWord);
            if (!caseSensitive) {
                charText = Character.toLowerCase(charText);
            }
            if (charText != charWord) {
                return false;
            }
            posInText++;
            indexAtWord++;
        }
        if (indexAtWord == word.length()) {
            return posInText == text.length() || isDelimiter(text.charAt(posInText));
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WordCounter that = (WordCounter) o;
        return length == that.length && caseSensitive == that.caseSensitive && word.equals(that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, length, caseSensitive);
    }

}
