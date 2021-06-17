package ru.hse.plugin.metrics.typed;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.metrics.abstracts.CountingMetric;
import ru.hse.plugin.metrics.abstracts.Metric;

import java.util.Locale;
import java.util.Objects;
import java.util.StringTokenizer;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static ru.hse.plugin.metrics.commons.util.Names.WORD_COUNTER;

public class WordCounter extends CountingMetric {
    @NotNull
    private final String word;
    private final int length;
    private final boolean caseSensitive;
    private static final String CORRECT_WORD_REGEX = "[a-zA-Z0-9_]{2,}";

    public WordCounter(@NotNull String word, int numberOfOccurrences) {
        this(word, numberOfOccurrences, true);
    }

    public WordCounter(@NotNull String word, int counter, boolean caseSensitive) {
        super(counter);
        if (!word.matches(CORRECT_WORD_REGEX)) {
            throw new RuntimeException("Incorrect word for counting");
        }
        if (!caseSensitive) {
            word = word.toLowerCase(Locale.ROOT);
        }
        this.word = word;
        this.length = word.length();
        this.caseSensitive = caseSensitive;
    }

    public WordCounter(String word)  {
        this(word, 0);
    }

    private static boolean isDelimiter(char symbol) {
        return !(Character.isLetterOrDigit(symbol) || symbol == '_');
    }

    @Override
    public void updateBeforeCharTyped(char charTyped, @NotNull Editor editor) {
        // offset -- количество символов, от начала текста до каретки
        int update = updateFromText(
                charTyped,
                editor.getDocument().getImmutableCharSequence(),
                editor.getCaretModel().getOffset()
        );
      //  System.out.println(update);
        inc(update);
    }

    @Override
    protected @NotNull String getClassName() {
        return WORD_COUNTER;
    }

    @NotNull
    @Override
    public String getName() {
        return getClassName() + "(" + word + ")";
    }

    @NotNull
    @Override
    public String localStatisticString() {
        return "Number of occurrences of word \"" + word + "\"";
    }

    @Override
    public boolean isSame(@NotNull Metric metric) {
        if (!super.isSame(metric)) {
            return false;
        }

        WordCounter that = (WordCounter) metric;
        return this.caseSensitive == that.caseSensitive && this.word.equals(that.word);
    }

    @Override
    public int hashSame() {
        return Objects.hash(word, length, caseSensitive);
    }

    @Override
    public String toString() {
        return getClassName() + " " + word + " " + getCounter();
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
        if (!super.equals(o)) {
            return false;
        }
        WordCounter that = (WordCounter) o;
        return length == that.length && caseSensitive == that.caseSensitive && word.equals(that.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), word, length, caseSensitive);
    }

    @Override
    @NotNull
    public WordCounter copy() {
        return new WordCounter(word, getCounter());
    }
}
