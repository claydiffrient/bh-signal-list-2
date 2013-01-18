package edu.byui.signaldownloader;

import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;


/**
 * SignalDownloader
 * 
 *
 * A java class which performs the function of starting an instance
 * of the Signal List Downloader 2 application.  It also constructs
 * the GUI portions.
 *
 * @author Clay Diffrient
 * @company IT Academic Portfolio
 */
public class SignalDownloader
   extends Application
{

   /**
    * Holds the username field.
    */
   TextField mUserName;

   /**
    * Holds the password field
    */
   PasswordField mPassword;

   /**
    * Holds the last signal field
    */
   TextField mLastSignal;

   /**
    * Holds the domain selection.
    */
   ChoiceBox mDomain;

   /**
    * Preferences object.
    */
   private Preferences mAvailDomains;

   /**
    * Holds list of domains
    */
   DomainListing mDomainList;

   /**
    * Holds settings window.
    */
   Settings mSettingsWindow;

   /**
    * Holds Threads window.
    */
   ThreadsWindow mThreadsWindow;

   /**
    * start
    *
    * Contains the initialization parameters to start the application.
    *
    * @param primaryStage the stage on which to start.
    */
    @Override
   public void start(Stage primaryStage)
   {
      mAvailDomains = Preferences.userRoot().node("bh-signal-downloader");
      mDomainList = DomainListing.getInstance();
      String[] keys = null;
      try
      {
         keys = mAvailDomains.keys();
      }
      catch (Exception e)
      {
         System.out.println("Error getting preferences" + e.getMessage());
      }
      for (int i = 0; i < keys.length; i++)
      {
         String result = mAvailDomains.get(keys[i], "Nothing");
         mDomainList.addAll(result);
      }
      BorderPane rootPanel = new BorderPane();
      HBox title = generateTitleBox();
      BorderPane.setAlignment(title, Pos.CENTER);
      BorderPane.setMargin(title, new Insets(12,12,12,12));
      rootPanel.setTop(title);
      GridPane inputElements = generateInputElements();
      BorderPane.setMargin(inputElements, new Insets(0,10,0,30));
      rootPanel.setCenter(inputElements);
      HBox buttons = generateButtonBox();
      BorderPane.setMargin(buttons, new Insets(10,10,10,125));
      rootPanel.setBottom(buttons);
      primaryStage.setScene(new Scene(rootPanel, 400, 250));
      primaryStage.setTitle("Signal List Downloader 2");
      primaryStage.show();
   }

   /**
    * generateInputElements
    *
    * Used to create the input elements for the input.
    * @return GridPane
    */
   private GridPane generateInputElements()
   {
      GridPane returnPane = new GridPane();
      returnPane.setPadding(new Insets(20,20,10,10));
      returnPane.setVgap(5);
      returnPane.setHgap(5);
      mUserName = new TextField();
      mUserName.setPromptText("BYUI/marsh3");
      mUserName.setPrefColumnCount(15);
      Label user = new Label("Username:");
      mPassword = new PasswordField();
      mPassword.setPrefColumnCount(15);
      Label pass = new Label("Password:");
      Label signal = new Label("Last Signal:");
      mLastSignal = new TextField();
      mLastSignal.setPromptText("0");
      mLastSignal.setPrefColumnCount(15);
      Label domain = new Label("Domain:");
      mDomain = new ChoiceBox();
      mDomain.setItems(mDomainList.getList());
      mDomain.setPrefWidth(200);
      GridPane.setConstraints(user, 0,0);
      GridPane.setConstraints(mUserName, 1, 0);
      GridPane.setConstraints(pass, 0, 1);
      GridPane.setConstraints(mPassword, 1, 1);
      GridPane.setConstraints(signal, 0, 2);
      GridPane.setConstraints(mLastSignal, 1, 2);
      GridPane.setConstraints(domain, 0, 3);
      GridPane.setConstraints(mDomain, 1, 3);
      returnPane.getChildren().add(pass);
      returnPane.getChildren().add(mPassword);
      returnPane.getChildren().add(user);
      returnPane.getChildren().add(mUserName);
      returnPane.getChildren().add(signal);
      returnPane.getChildren().add(mLastSignal);
      returnPane.getChildren().add(mDomain);
      returnPane.getChildren().add(domain);
      return returnPane;
   }

   /**
    * generateButtonBox
    *
    * Creates the button portion
    * @return HBox
    */
   private HBox generateButtonBox()
   {
      HBox buttonBox = new HBox(10);
      Button quitButton = new Button("Exit");
      quitButton.setOnAction(new EventHandler<ActionEvent>()
      {
          @Override
         public void handle(ActionEvent ae)
         {
            System.exit(0);
         }
      });
      Button getSignals = new Button("Get Signals");
      getSignals.setOnAction(new EventHandler<ActionEvent>(){
          @Override
         public void handle(ActionEvent ae)
         {
            getSignals();
         }
      });
      getSignals.setDefaultButton(true);
      buttonBox.getChildren().addAll(quitButton, getSignals);
      return buttonBox;
   }

   /**
    * generateTitleBox
    *
    * Creates the heading portion of the application.
    * @return HBox
    */
   private HBox generateTitleBox()
   {
      HBox titleBox = new HBox(10);
      Text title = new Text("Signal List Downloader 2");
      title.setFont(new Font(26));
      title.setTextAlignment(TextAlignment.CENTER);
      Image settingsImage = new Image(getClass().getResourceAsStream("/edu/byui/signaldownloader/resources/gear.png"));
      Button settingsButton = new Button();
      settingsButton.setGraphic(new ImageView(settingsImage));
      settingsButton.setPrefSize(10, 10);
      settingsButton.setTooltip(new Tooltip("Settings"));
      //Add action listener for the settings button.
      settingsButton.setOnAction(new EventHandler<ActionEvent>()
      {
          @Override
        public void handle(ActionEvent ae)
        {
           if (mSettingsWindow == null)
           {
              mSettingsWindow = new Settings();
              mSettingsWindow.run();
           }
           else
           {
              mSettingsWindow.show();
           }

        }
      });

      titleBox.getChildren().addAll(title, settingsButton);
      return titleBox;
   }

   /**
    * getSignals
    */
   private void getSignals()
   {
      if (mThreadsWindow == null)
      {
         mThreadsWindow = new ThreadsWindow();
         mThreadsWindow.run();
      }
      else
      {
         mThreadsWindow.show();
      }
      String selected = (String) mDomain.getValue();
      String username = mUserName.getText();
      String password = mPassword.getText();
      String lastSignal = mLastSignal.getText();
      //Set defaults
      if (username.isEmpty())
      {
         username = "byui/marsh3";
      }
      if (lastSignal.isEmpty())
      {
         lastSignal = "0";
      }
      mThreadsWindow.startReport(selected,  username,  password, lastSignal);
   }

   /**
    * main
    *
    * Defined, but should never be called as this is a JavaFX application
    */
   public static void main(String[] args)
   {
      //Backup in case it's called
      launch(args);
   }
}