package ru.hse.plugin.metrics.commons.util;

import ru.hse.plugin.metrics.abstracts.Metric;
import ru.hse.plugin.metrics.backspaces.DeletedLengthCounter;
import ru.hse.plugin.metrics.backspaces.DeletionCounter;
import ru.hse.plugin.metrics.copypaste.*;
import ru.hse.plugin.metrics.editor.EditorCounter;
import ru.hse.plugin.metrics.editor.MaxOpenedEditors;
import ru.hse.plugin.metrics.git.CommitCounter;
import ru.hse.plugin.metrics.project.MaxOpenedProjects;
import ru.hse.plugin.metrics.project.ProjectOpensNumber;
import ru.hse.plugin.metrics.typed.AllCharCounter;
import ru.hse.plugin.metrics.typed.LineCounter;
import ru.hse.plugin.metrics.typed.TotalTypedCounter;

import java.util.Map;
import java.util.function.Supplier;

public class Names {
    private Names() { }

    public static final String EMPTY_USER_INFO = "EmptyUserInfo";

    public static final String WORD_COUNTER = "WordCounter";
    public static final String SUBSTRING_COUNTER = "SubstringCounter";
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
    public static final String DELETED_LENGTH_COUNTER = "DeletedLengthCounter";
    public static final String DELETION_COUNTER = "DeletionCounter";
    public static final String TOTAL_TYPED_COUNTER = "TotalTypedCounter";

    public static final Map<String, Supplier<Metric>> NON_PARAMETRIZED_METRICS_CONSTRUCTORS;

    static {

        NON_PARAMETRIZED_METRICS_CONSTRUCTORS = Map.ofEntries(
                Map.entry(COPY_COUNTER, CopyCounter::new),
                Map.entry(COPY_LENGTH_COUNTER, CopyLengthCounter::new),
                Map.entry(MAX_COPY_LENGTH, MaxCopyLength::new),
                Map.entry(MAX_PASTE_LENGTH, MaxPasteLength::new),
                Map.entry(PASTE_COUNTER, PasteCounter::new),
                Map.entry(PASTE_LENGTH_COUNTER, PasteLengthCounter::new),
                Map.entry(EDITOR_COUNTER, EditorCounter::new),
                Map.entry(MAX_OPENED_EDITORS, MaxOpenedEditors::new),
                Map.entry(COMMIT_COUNTER, CommitCounter::new),
                Map.entry(MAX_OPENED_PROJECTS, MaxOpenedProjects::new),
                Map.entry(PROJECT_OPENS_NUMBER, ProjectOpensNumber::new),
                Map.entry(ALL_CHAR_COUNTER, AllCharCounter::new),
                Map.entry(DELETED_LENGTH_COUNTER, DeletedLengthCounter::new),
                Map.entry(DELETION_COUNTER, DeletionCounter::new),
                Map.entry(LINE_COUNTER, LineCounter::new),
                Map.entry(TOTAL_TYPED_COUNTER, TotalTypedCounter::new)
        );

    }
}
