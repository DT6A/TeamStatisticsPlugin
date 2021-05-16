package ru.hse.plugin.metrics;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;
import ru.hse.plugin.util.PluginConstants;

import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import static java.lang.Math.*;

public class WordCounter implements Metric {
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
    public void update(char charTyped, @NotNull Project project, @NotNull Editor editor, @NotNull PsiFile file) {
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

}
