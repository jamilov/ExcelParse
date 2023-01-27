import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextArea;
import javafx.concurrent.Task;
import javafx.scene.control.ProgressBar;
import javafx.stage.WindowEvent;
import javax.swing.*;
import java.io.*;
import Project.Netjob;


public class Main extends Application {
    Thread t = new Thread();
    Double f;
    boolean exit = false;
    @Override
    public void start(final Stage primaryStage) {
        primaryStage.setTitle("Проверка ИНН");
        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        final FileChooser fileChooser = new FileChooser();
        try{
            configuringFileChooser(fileChooser);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(null,"Error!");
        }
        Text scenetitle = new Text("Проверка ИНН");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(scenetitle, 0, 0, 2, 1);

        Label userName = new Label("Путь к файлу:");
        grid.add(userName, 0, 1);

        final TextField textField = new TextField();
        final TextArea textArea = new TextArea();
        textArea.setMaxSize(150,200);

        grid.add(textField, 1, 1);
        grid.add(textArea,1,2);
        final Label label = new Label("Готово!");

        Button button = new Button("Обзор");
        grid.add(button, 2, 1);
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                    printLog(textField, file);
                }
            }
        });
        final ProgressBar bar = new ProgressBar();
        grid.add(bar,1,4);
        bar.setProgress(0);
        final Task task = new Task<Void>() {
            @Override public Void call() {
                exit = false;
                String filepath = textField.getText();
                String excel = Parser.parse(filepath);
                String[] inn = excel.split("\n");
                for (int i = 0; i < inn.length; i++) {
                    f = Double.valueOf(i)/Double.valueOf(inn.length);
                    if (exit) {
                        bar.setProgress(0);
                        textArea.appendText("Отменено!");
                        return null;
                    } else {
                        String query = "https://zachestnyibiznes.ru/search?query=" + inn[i];
                        if (Netjob.result(Netjob.newqueryget(query)).equals("1"))  {
                            if( !Netjob.ipcheck( Netjob.newqueryget( query ) ).equals("0") ){
                                bar.setProgress(f);
                                textArea.appendText(inn[i] + "\n");
                            }
                        }
                        else if(Netjob.result(Netjob.newqueryget(query)).equals("0")){
                        }
                        else{
                            bar.setProgress(f);
                            textArea.appendText(inn[i] + "\n");
                        }
                    }
                }
                bar.setProgress(1.0);
                if(!exit)
                    textArea.appendText("Завершено!");
                return null;
            }
        };

        Button button1 = new Button("Проверить");
        grid.add(button1, 1, 3);
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                t= new Thread(task);
                t.start();
            }
        });
        Button button2 = new Button("Отмена");
        grid.add(button2, 2, 3);
        button2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!exit)
                    exit = true;
            }
        });
        Scene scene = new Scene(grid, 500, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(!exit){
                    exit = true;
                    grid.getChildren().remove(6);
                }
            }
        });
    }

    private void configuringFileChooser(FileChooser fileChooser) throws Exception {
        fileChooser.setTitle("Выбор excel файла");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Файлы Microsoft Excel", "*.xls","*.xlsx") );
    }

    private void printLog(TextField textField, File file) {
        if (file == null) {
            return;
        }
        textField.appendText(file.getAbsolutePath() + "\n");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
