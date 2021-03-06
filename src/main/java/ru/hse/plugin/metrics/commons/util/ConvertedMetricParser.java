package ru.hse.plugin.metrics.commons.util;

import org.jetbrains.annotations.Nullable;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.metrics.backspaces.DeletedLengthCounter;
import ru.hse.plugin.metrics.backspaces.DeletionCounter;
import ru.hse.plugin.metrics.copypaste.*;
import ru.hse.plugin.metrics.editor.EditorCounter;
import ru.hse.plugin.metrics.editor.MaxOpenedEditors;
import ru.hse.plugin.metrics.git.CommitCounter;
import ru.hse.plugin.metrics.git.SpecificBranchCommitCounter;
import ru.hse.plugin.metrics.project.MaxOpenedProjects;
import ru.hse.plugin.metrics.project.ProjectOpensNumber;
import ru.hse.plugin.metrics.typed.*;

import java.util.Arrays;
import java.util.stream.Collectors;

import static ru.hse.plugin.metrics.commons.util.Names.*;

public final class ConvertedMetricParser {
    private ConvertedMetricParser() { }

    @Nullable
    public static Metric parse(@Nullable String metric) {
        if (metric == null) {
            return null;
        }

        String[] parts = metric.split(" ");

        switch (parts[0]) {
            case WORD_COUNTER:
                validateLength(3, parts, metric);
                return new WordCounter(parts[1], Integer.parseInt(parts[2]));
            case SUBSTRING_COUNTER:
                validateLength(3, parts, metric);
                return new SubstringCounter(parts[1], Integer.parseInt(parts[2]));
            case LINE_COUNTER:
                validateLength(2, parts, metric);
                return new LineCounter(Integer.parseInt(parts[1]));
            case TOTAL_TYPED_COUNTER:
                validateLength(2, parts, metric);
                return new TotalTypedCounter(Integer.parseInt(parts[1]));
            case CHAR_COUNTER:
                validateLength(3, parts, metric);
                return new CharCounter((char) Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            case ALL_CHAR_COUNTER:
                validateLength(10 + ('z' - 'a' + 1) + 1, parts, metric);
                return new AllCharCounter(
                        Arrays.stream(parts)
                                .skip(1)
                                .map(Integer::parseInt)
                                .collect(Collectors.toList())
                );
            case MAX_OPENED_PROJECTS:
                validateLength(2, parts, metric);
                return new MaxOpenedProjects(Integer.parseInt(parts[1]));
            case PROJECT_OPENS_NUMBER:
                validateLength(2, parts, metric);
                return new ProjectOpensNumber(Integer.parseInt(parts[1]));
            case COMMIT_COUNTER:
                validateLength(2, parts, metric);
                return new CommitCounter(Integer.parseInt(parts[1]));
            case SPECIFIC_BRANCH_COMMIT_COUNTER:
                validateLength(3, parts, metric);
                return new SpecificBranchCommitCounter(Integer.parseInt(parts[1]), parts[2]);
            case EDITOR_COUNTER:
                validateLength(2, parts, metric);
                return new EditorCounter(Integer.parseInt(parts[1]));
            case MAX_OPENED_EDITORS:
                validateLength(2, parts, metric);
                return new MaxOpenedEditors(Integer.parseInt(parts[1]));
            case COPY_COUNTER:
                validateLength(2, parts, metric);
                return new CopyCounter(Integer.parseInt(parts[1]));
            case PASTE_COUNTER:
                validateLength(2, parts, metric);
                return new PasteCounter(Integer.parseInt(parts[1]));
            case COPY_LENGTH_COUNTER:
                validateLength(2, parts, metric);
                return new CopyLengthCounter(Integer.parseInt(parts[1]));
            case PASTE_LENGTH_COUNTER:
                validateLength(2, parts, metric);
                return new PasteLengthCounter(Integer.parseInt(parts[1]));
            case MAX_PASTE_LENGTH:
                validateLength(2, parts, metric);
                return new MaxPasteLength(Integer.parseInt(parts[1]));
            case MAX_COPY_LENGTH:
                validateLength(2, parts, metric);
                return new MaxCopyLength(Integer.parseInt(parts[1]));
            case SPECIFIC_LENGTH_PASTE_COUNTER:
                validateLength(3, parts, metric);
                return new SpecificLengthPasteCounter(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            case SPECIFIC_LENGTH_COPY_COUNTER:
                validateLength(3, parts, metric);
                return new SpecificLengthCopyCounter(Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            case DELETED_LENGTH_COUNTER:
                validateLength(2, parts, metric);
                return new DeletedLengthCounter(Integer.parseInt(parts[1]));
            case DELETION_COUNTER:
                validateLength(2, parts, metric);
                return new DeletionCounter(Integer.parseInt(parts[1]));
            default:
                throw new RuntimeException("Parse error, could not parse \"" + metric + "\"");
        }
    }

    public static void validateLength(int length, String[] parts, String metric) {
        if (parts.length != length) {
            throw new RuntimeException("Parse error, could not parse \"" + metric + "\"");
        }
    }
}
