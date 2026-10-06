package com.anyclip.review;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import javax.swing.*;
import java.awt.*;

final class RequestDialog extends DialogWrapper {
    private final int length;
    private final JPasswordField key = new JPasswordField(35);
    private final JTextField model = new JTextField("mistral-small-latest", 35);
    RequestDialog(Project project, int length) {
        super(project);
        this.length = length;
        String env = System.getenv("MISTRAL_API_KEY");
        if (env != null) key.setText(env);
        setTitle("Проверка выделенного кода — Mistral");
        setOKButtonText("Проверить");
        init();
    }
    @Override protected JComponent createCenterPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1, 0, 8));
        panel.add(new JLabel("API-ключ Mistral (не сохраняется):"));
        panel.add(key);
        panel.add(new JLabel("Модель:"));
        panel.add(model);
        panel.add(new JLabel("В api.mistral.ai будет отправлено " + length + " символов выделенного кода."));
        return panel;
    }
    @Override protected ValidationInfo doValidate() {
        if (key.getPassword().length == 0) return new ValidationInfo("Введите API-ключ", key);
        if (model.getText().trim().isEmpty()) return new ValidationInfo("Введите название модели", model);
        return null;
    }
    String apiKey() { return new String(key.getPassword()).trim(); }
    String model() { return model.getText().trim(); }
}
