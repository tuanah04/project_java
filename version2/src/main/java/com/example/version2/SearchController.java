package com.example.version2;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.Word;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchController extends Controller implements Initializable {
    private ArrayList<Word> searchWordTemp = new ArrayList<>();
    private String current;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            getCurrentDic().insertFromDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getCurrentDic().insertFromFile(getCurrentDic().getBookMarkFile(), getCurrentDic().getBookMark());
        setLanguage();
        searchWordTemp.addAll(getCurrentDic().getWordList());
        setSearchListViewItem();
        setLanguage();
    }

    public void setSearchListViewItem() {
        searchList.clear();
        if (searchField.getText().equals("")) {
            searchWordTemp.clear();
            searchWordTemp.addAll(getCurrentDic().getWordList());
        }
        for (Word temp : searchWordTemp) {
            searchList.add(temp.getWord_target());
        }
        wordListView.setItems(searchList);
    }

    @FXML
    public void searchFieldAction() throws IOException {
        searchWordTemp.clear();
        searchList.clear();
        String word = searchField.getText();
        searchWordTemp = getCurrentDic().dictionarySearcher(word.toLowerCase(), getCurrentDic().getWordList());
        setSearchListViewItem();
    }

    @FXML
    public void showDefinition() {
        String spelling = wordListView.getSelectionModel().getSelectedItem();
        current = spelling;
        if (spelling == null) {
            return;
        }
        Word res = getCurrentDic().dictionaryLookup(spelling, getCurrentDic().getWordList());
        bookmark.setVisible(true);
        if (getCurrentDic().dictionaryLookup(spelling, getCurrentDic().getBookMark()) != null) {
            bookmark.getStyleClass().add("active");
        } else {
            bookmark.getStyleClass().removeAll("active");
        }
        definitionView.getEngine().loadContent(res.getWord_explain());
    }

    @FXML
    public void handleClickRemoveButton() {
        super.handleClickRemoveButton(current);
        searchList.clear();
        wordListView.getItems().clear();
        for (Word temp : dictionaryManagement.getWordList()) {
            searchList.add(temp.getWord_target());
        }
        wordListView.setItems(searchList);
    }

    public void initSearchListView() {
        wordListView.getItems().clear();
        setSearchListViewItem();
        setLanguage();
    }
}
