package util;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class LoadingUtil {

    public static void runWithLoading(AnchorPane anchorPane, Runnable backgroundTask, Runnable onSuccess) {
        Platform.runLater(() -> {
            anchorPane.setDisable(true);

            // Wrapper para centralizar automaticamente
            StackPane loadingWrapper = new StackPane();
            loadingWrapper.setPrefSize(anchorPane.getWidth(), anchorPane.getHeight());
            loadingWrapper.setStyle("-fx-background-color: rgba(255,255,255,0.6);"); // opcional: overlay transparente
            AnchorPane.setTopAnchor(loadingWrapper, 0.0);
            AnchorPane.setBottomAnchor(loadingWrapper, 0.0);
            AnchorPane.setLeftAnchor(loadingWrapper, 0.0);
            AnchorPane.setRightAnchor(loadingWrapper, 0.0);

            ProgressIndicator progress = new ProgressIndicator();
            loadingWrapper.getChildren().add(progress);
            loadingWrapper.setAlignment(Pos.CENTER);

            anchorPane.getChildren().add(loadingWrapper);

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    backgroundTask.run();
                    return null;
                }
            };

            task.setOnSucceeded(event -> {
                anchorPane.setDisable(false);
                anchorPane.getChildren().remove(loadingWrapper);
                if (onSuccess != null) onSuccess.run();
            });

            task.setOnFailed(event -> {
                anchorPane.setDisable(false);
                anchorPane.getChildren().remove(loadingWrapper);
                task.getException().printStackTrace();
            });

            new Thread(task).start();
        });
    }
}
