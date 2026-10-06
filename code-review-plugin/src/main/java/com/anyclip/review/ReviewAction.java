package com.anyclip.review;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.progress.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import java.net.http.HttpTimeoutException;
import java.util.concurrent.*;

public final class ReviewAction extends AnAction {
    @Override public ActionUpdateThread getActionUpdateThread() { return ActionUpdateThread.BGT; }
    @Override public void update(AnActionEvent event) {
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        event.getPresentation().setEnabledAndVisible(event.getProject() != null && editor != null
            && editor.getSelectionModel().hasSelection());
    }
    @Override public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        Editor editor = event.getData(CommonDataKeys.EDITOR);
        if (project == null || editor == null) return;
        String code = editor.getSelectionModel().getSelectedText();
        if (code == null || code.isBlank()) return;
        if (code.length() > 30000) {
            Messages.showInfoMessage(project, "Выделите не более 30 000 символов.", "Проверка кода");
            return;
        }
        RequestDialog dialog = new RequestDialog(project, code.length());
        if (!dialog.showAndGet()) return;
        String key = dialog.apiKey();
        String model = dialog.model();
        new Task.Backgroundable(project, "Проверка качества тестов", true) {
            private String result;
            @Override public void run(ProgressIndicator indicator) {
                indicator.setIndeterminate(true);
                // Cancellation interrupts the HTTP request; no request runs on the UI thread.
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Future<String> request = executor.submit(() -> new CloudReviewClient().review(code, model, key));
                try {
                    while (true) {
                        indicator.checkCanceled();
                        try { result = request.get(200, TimeUnit.MILLISECONDS); break; }
                        catch (TimeoutException waiting) { /* poll cancellation */ }
                    }
                } catch (InterruptedException interrupted) {
                    Thread.currentThread().interrupt();
                    throw new ProcessCanceledException();
                } catch (ExecutionException failed) {
                    Throwable cause = failed.getCause();
                    if (cause instanceof HttpTimeoutException) throw new RuntimeException("Сервис не ответил за отведенное время.");
                    if (cause instanceof java.io.IOException) throw new RuntimeException(cause.getMessage());
                    throw new RuntimeException("Не удалось получить заключение от модели.");
                } finally {
                    request.cancel(true);
                    executor.shutdownNow();
                }
            }
            @Override public void onSuccess() {
                if (!project.isDisposed()) new ReviewDialog(project, result).show();
            }
            @Override public void onThrowable(Throwable error) {
                if (!project.isDisposed()) Messages.showErrorDialog(project, error.getMessage(), "Ошибка проверки кода");
            }
        }.queue();
    }
}
