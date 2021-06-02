package ru.hse.plugin.metrics.commons.util;

import org.jetbrains.annotations.Nullable;
import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.metrics.editor.AllCharCounter;
import ru.hse.plugin.metrics.editor.CharCounter;
import ru.hse.plugin.metrics.editor.WordCounter;
import ru.hse.plugin.metrics.git.CommitCounter;
import ru.hse.plugin.metrics.git.SpecificBranchCommitCounter;
import ru.hse.plugin.metrics.project.MaxOpenedProjects;
import ru.hse.plugin.metrics.project.ProjectOpensNumber;

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
                if (parts.length != 3) {
                    throw new RuntimeException("Parse error, could not parse \"" + metric + "\"");
                }
                return new WordCounter(parts[1], Integer.parseInt(parts[2]));
            case CHAR_COUNTER:
                if (parts.length != 3) {
                    throw new RuntimeException("Parse error, could not parse \"" + metric + "\"");
                }
                return new CharCounter((char) Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
            case ALL_CHAR_COUNTER:
                if (parts.length != 10 + ('z' - 'a' + 1) + 1) {
                    throw new RuntimeException("Parse error, could not parse \"" + metric + "\"");
                }
                return new AllCharCounter(
                        Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList())
                );
            case MAX_OPENED_PROJECTS:
                if (parts.length != 2) {
                    throw new RuntimeException("Parse error, could not parse \"" + metric + "\"");
                }
                return new MaxOpenedProjects(Integer.parseInt(parts[1]));
            case PROJECT_OPENS_NUMBER:
                if (parts.length != 2) {
                    throw new RuntimeException("Parse error, could not parse \"" + metric + "\"");
                }
                return new ProjectOpensNumber(Integer.parseInt(parts[1]));
            case COMMIT_COUNTER:
                if (parts.length != 2) {
                    throw new RuntimeException("Parse error, could not parse \"" + metric + "\"");
                }
                return new CommitCounter(Integer.parseInt(parts[1]));
            case SPECIFIC_BRANCH_COMMIT_COUNTER:
                if (parts.length != 3) {
                    throw new RuntimeException("Parse error, could not parse \"" + metric + "\"");
                }
                return new SpecificBranchCommitCounter(Integer.parseInt(parts[1]), parts[2]);
            default:
                throw new RuntimeException("Parse error, could not parse \"" + metric + "\"");
        }
    }
}
