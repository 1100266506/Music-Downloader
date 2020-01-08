import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.VBox;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class execute extends Application {

    public void start (Stage primaryStage) throws Exception {
        VBox mainPain = new VBox();
        Label header = new Label("Welcome to the Music Downloader v1!");
        Label instructions = new Label("Please type the songs you would like to download and hit enter");
        TextField songSearch = new TextField();
        Button songSearchEnter = new Button("Enter");
        Label starter = new Label("Click the below button to start the program");
        Button startButton = new Button("Execute");
        ListView<String> mySearches = new ListView<>();
        ObservableList<String> searches = FXCollections.observableArrayList();

        //setting up song_list.txt file and creating search file
        ArrayList<String> write = new ArrayList<>();
        WriteSearch writeSearchObj = new WriteSearch(songSearch, write, searches, mySearches);
        songSearchEnter.setOnAction(writeSearchObj);

        //executing python script
        Start startExecute = new Start(writeSearchObj.getSearchArray());
        startButton.setOnAction(startExecute);

        //putting together VBox
        mainPain.getChildren().addAll(header,instructions,songSearch,songSearchEnter, starter,
            startButton, mySearches);

        //final steps
        Scene myScene = new Scene(mainPain, 500, 200);
        primaryStage.setScene(myScene);
        primaryStage.setTitle("Music Downloader v1");
        primaryStage.show();
    }

    //button action
    //enters textfield into txt file
    class WriteSearch implements EventHandler<ActionEvent> {
        ArrayList<String> searchArray = new ArrayList<>();
        TextField searchQuery;
        ObservableList<String> searches;
        ListView<String> mySearches;

        public WriteSearch(TextField searchQuery, ArrayList<String> searchArray,
            ObservableList<String> searches, ListView<String> mySearches) {
            this.searchArray = searchArray;
            this.searchQuery = searchQuery;
            this.searches = searches;
            this.mySearches = mySearches;
        }

        public ArrayList<String> getSearchArray() {
            return this.searchArray;
        }

        @Override
        public void handle (ActionEvent e) {
            searchArray.add(searchQuery.getText());
            searches.add(searchQuery.getText());
            mySearches.setItems(searches);
            searchQuery.clear();
        }
    }

    //button action
    //method so that when the start button is clicked, this command runs
    class Start implements EventHandler<ActionEvent> {
        ArrayList<String> searchArray = new ArrayList<>();

        public Start (ArrayList<String> searchArray) {
            this.searchArray = searchArray;
        }

        public void handle(ActionEvent e) {
            try {
                System.out.println("Updating song_list.txt");
                File myFile = new File("song_list.txt");
                PrintWriter myPrintWriter = new PrintWriter(myFile);
                for (String search:searchArray) {
                    myPrintWriter.println(search);
                }
                System.out.println("Completed update of song_list.txt");
                myPrintWriter.close();
                System.out.println("Executing python script");
                Process p = Runtime.getRuntime().exec("python MusicDownload.py");
            }
            catch (Exception ex) {
                System.out.println("Error!");
            }
        }
    }

}
