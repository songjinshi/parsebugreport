package com.juanhoo.Controller;/**
 * Created by a20023 on 11/10/2015.
 */

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class ParseBugreportApp extends Application {

    public static String fileName ;

    public static void main(String[] args) {

        if (args.length == 0) {
            System.out.println("Please input file name");
            return;
        }
        fileName = args[0];
        File inputFile = new File(fileName);
        if (!inputFile.exists() || inputFile.isDirectory()) {
            System.out.println("File "+fileName+ " doesn't exist!");
            return;
        }
        launch(args);

    }

    ParseLogController controller = null;

/*    ParseLogController GetUIController() {
        return controller;
    }*/


    @Override
    public void start(Stage primaryStage) {
        //FileChooser fileChooser = new FileChooser();
        //fileChooser.setTitle("Open bugreport File");
       // File file = fileChooser.showOpenDialog(primaryStage);
        File file = new File("bugreport.txt");

        controller = new ParseLogController();
        primaryStage.setTitle(file.getName());
        primaryStage.setScene(new Scene(controller.GetRoot()));
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.show();
        ParsefileHandle fh = new ParsefileHandle(file);
        fh.SetUIController(controller);
        fh.ParseFile();
    }


}
