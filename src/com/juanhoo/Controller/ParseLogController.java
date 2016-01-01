package com.juanhoo.Controller;

/**
 * Created by a20023 on 11/10/2015.
 */
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ParseLogController extends VBox implements Initializable {
    @FXML
    private SplitPane splitpane;
    @FXML
    private TreeView<ParseTreeNode> parseTreeView;
    @FXML
    private WebView parseWebView;
    Parent root;

    public ParseLogController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("parseui.fxml"));
        //fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            root = (Parent) fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        parseTreeView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        parseTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<ParseTreeNode>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<ParseTreeNode>> observableValue, TreeItem<ParseTreeNode> oldValue, TreeItem<ParseTreeNode> newValue) {
                System.out.println("Select item: " + newValue.getValue());
                String path = System.getProperty("user.dir");
                path.replace("\\\\", "/");
                path += "/html"+"/"+newValue.getValue().resultHTMLPath;
                webEngine.load("file:///" + path);
            }
        });

    }

    public Parent GetRoot() {
        return root;
    }

    @FXML
    protected void switchView() {

        // System.out.println("Click item XXX");
    }

    TreeItem<ParseTreeNode> rootItem = null;

    public void AddTreeNode(ParseTreeNode node) {
        //Firstnode
        if (node.parentNodeName == null) {
            AddTreeNode(node, rootItem);
        } else {
            //
            boolean found = false;
            for (TreeItem<ParseTreeNode> firstLvlNode : rootItem.getChildren()) {
                if (firstLvlNode.getValue().nodeName.contentEquals(node.parentNodeName)) {
                    found = true;
                    AddTreeNode(node, firstLvlNode);
                    break;
                }
            }
            if (found == false){
                AddTreeNode(node, rootItem);
            }
        }
    }

    private void AddTreeNode(ParseTreeNode node, TreeItem<ParseTreeNode> parentTreeItem) {
        if (node == null || parentTreeItem == null)  {
            return;
        }
        TreeItem<ParseTreeNode> treeItem = new TreeItem<>(node);
        parentTreeItem.getChildren().add(treeItem);
        for (int i = 0; i < node.childArray.size(); i++) {
            AddTreeNode(node.childArray.get(i), treeItem);
        }
        node.childArray.clear();
    }




    public void InitTree(String rootName, String contentHtmlPath) {
        rootItem = new TreeItem<ParseTreeNode>(new ParseTreeNode(rootName, contentHtmlPath));
        rootItem.setExpanded(true);
        parseTreeView.setRoot(rootItem);
    }

    public void InitTree(ParseTreeNode rootNode) {
        rootItem = new TreeItem<ParseTreeNode>(rootNode);
        rootItem.setExpanded(true);
        parseTreeView.setRoot(rootItem);
    }

    private WebEngine webEngine = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        webEngine = parseWebView.getEngine();
        String path = System.getProperty("user.dir");
        path.replace("\\\\", "/");
        path += "/html/summary.html";
        webEngine.load("file:///" + path);
    }
}