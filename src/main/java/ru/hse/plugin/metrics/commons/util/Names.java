package ru.hse.plugin.metrics.commons.util;

import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.metrics.copypaste.*;
import ru.hse.plugin.metrics.editor.EditorCounter;
import ru.hse.plugin.metrics.editor.MaxOpenedEditors;
import ru.hse.plugin.metrics.git.CommitCounter;
import ru.hse.plugin.metrics.project.MaxOpenedProjects;
import ru.hse.plugin.metrics.project.ProjectOpensNumber;
import ru.hse.plugin.metrics.typed.AllCharCounter;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class Names {
    private Names() { }

    public static final String EMPTY_USER_INFO = "EmptyUserInfo";

    public static final String WORD_COUNTER = "WordCounter";
    public static final String CHAR_COUNTER = "CharCounter";
    public static final String ALL_CHAR_COUNTER = "AllCharCounter";

    public static final String MAX_OPENED_PROJECTS = "MaxOpenedProjects";
    public static final String PROJECT_OPENS_NUMBER = "ProjectOpensNumber";

    public static final String COMMIT_COUNTER = "CommitCounter";
    public static final String SPECIFIC_BRANCH_COMMIT_COUNTER = "SpecificBranchCommitCounter";

    public static final String EDITOR_COUNTER = "EditorCounter";
    public static final String MAX_OPENED_EDITORS = "MaxOpenedEditors";

    public static final String COPY_COUNTER = "CopyCounter";
    public static final String PASTE_COUNTER = "PasteCounter";
    public static final String COPY_LENGTH_COUNTER = "CopyLengthCounter";
    public static final String PASTE_LENGTH_COUNTER = "PasteLengthCounter";
    public static final String MAX_PASTE_LENGTH = "MaxPasteLength";
    public static final String MAX_COPY_LENGTH = "MaxCopyLength";
    public static final String SPECIFIC_LENGTH_PASTE_COUNTER = "SpecificLengthPasteCounter";
    public static final String SPECIFIC_LENGTH_COPY_COUNTER = "SpecificLengthCopyCounter";
    public static final String LINE_COUNTER = "LineCounter";

    public static final Map<String, Supplier<Metric>> NON_PARAMETRIZED_METRICS_CONSTRUCTORS;

    static {

        NON_PARAMETRIZED_METRICS_CONSTRUCTORS = new HashMap<>();
        NON_PARAMETRIZED_METRICS_CONSTRUCTORS.put(COPY_COUNTER, CopyCounter::new);
        NON_PARAMETRIZED_METRICS_CONSTRUCTORS.put(COPY_LENGTH_COUNTER, CopyLengthCounter::new);
        NON_PARAMETRIZED_METRICS_CONSTRUCTORS.put(MAX_COPY_LENGTH, MaxCopyLength::new);
        NON_PARAMETRIZED_METRICS_CONSTRUCTORS.put(MAX_PASTE_LENGTH, MaxPasteLength::new);
        NON_PARAMETRIZED_METRICS_CONSTRUCTORS.put(PASTE_COUNTER, PasteCounter::new);
        NON_PARAMETRIZED_METRICS_CONSTRUCTORS.put(PASTE_LENGTH_COUNTER, PasteLengthCounter::new);
        NON_PARAMETRIZED_METRICS_CONSTRUCTORS.put(EDITOR_COUNTER, EditorCounter::new);
        NON_PARAMETRIZED_METRICS_CONSTRUCTORS.put(MAX_OPENED_EDITORS, MaxOpenedEditors::new);
        NON_PARAMETRIZED_METRICS_CONSTRUCTORS.put(COMMIT_COUNTER, CommitCounter::new);
        NON_PARAMETRIZED_METRICS_CONSTRUCTORS.put(MAX_OPENED_PROJECTS, MaxOpenedProjects::new);
        NON_PARAMETRIZED_METRICS_CONSTRUCTORS.put(PROJECT_OPENS_NUMBER, ProjectOpensNumber::new);
        NON_PARAMETRIZED_METRICS_CONSTRUCTORS.put(ALL_CHAR_COUNTER, AllCharCounter::new);

    }
}
