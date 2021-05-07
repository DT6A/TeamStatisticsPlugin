package ru.hse.plugin;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.ui.Messages;

public class MyAction extends AnAction {
    public void update(AnActionEvent e) {
        // perform action if and only if EDITOR != null
        boolean enabled = null != e.getData(CommonDataKeys.EDITOR);
        e.getPresentation().setEnabled(enabled);
    }

    public void actionPerformed(AnActionEvent e) {
        // if we're here then EDITOR != null
        Document doc = e.getRequiredData(CommonDataKeys.EDITOR).getDocument();
        if (doSomething(doc)) {
            System.out.println("Nice cock");
            Messages.showInfoMessage("Nice", "cock");
        }
    }

    private boolean doSomething(Document doc) {
        System.out.println(doc.getText());
        return doc.getText().contains("cock");
    }
}
