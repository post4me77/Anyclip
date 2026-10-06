package com.anyclip.review;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import javax.swing.*;
import java.awt.*;

final class ReviewDialog extends DialogWrapper {
    private final String result;
    ReviewDialog(Project project, String result) {
        super(project);
        this.result = result;
        setTitle("Заключение: качество тестов");
        setOKButtonText("Закрыть");
        init();
    }
    @Override protected JComponent createCenterPanel() {
        JTextArea text = new JTextArea(result, 28, 95);
        text.setEditable(false);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setCaretPosition(0);
        JScrollPane scroll = new JScrollPane(text);
        scroll.setPreferredSize(new Dimension(850, 580));
        return scroll;
    }
    @Override protected Action[] createActions() { return new Action[]{getOKAction()}; }
}
