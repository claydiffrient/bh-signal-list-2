package edu.byui.signaldownloader;

import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Settings
 *
 * Window to appear as part of the Signal List Downloader 2
 * @author Clay Diffrient
 * @company IT Academic Portfolio
 */
public class Settings
   extends Stage
   implements Runnable
{
   /**
    * Holds the domain identifier.
    */
   TextField mIdentifier;

   /**
    * Holds the domain id
    */
   TextField mDomainId;

   /**
    * Holds the current listing
    */
   ChoiceBox mDomains;

   /**
    * Preferences object.
    */
   private Preferences mAvailDomains;

   /**
    * Holds domain list
    */
   DomainListing mDomainList;

   /**
    * Constructor
    * Creates the stage.
    */
   public Settings()
   {

      mAvailDomains = Preferences.userRoot().node("bh-signal-downloader");
      mDomainList = DomainListing.getInstance();

      //Create the Title HBox
      HBox title = new HBox();
      Label titleLabel = new Label("Configure Domains");
      titleLabel.setFont(new Font(30));
      title.getChildren().addAll(titleLabel);

      //Create the fields HBox
      HBox fields = new HBox(10);
      mIdentifier = new TextField();
      mDomainId = new TextField();
      mIdentifier.setPromptText("byui");
      mDomainId.setPromptText("14853");
      Button addDomain = new Button("Add Domain");
      addDomain.setOnAction(new EventHandler<ActionEvent>()
      {
          @Override
         public void handle(ActionEvent ae)
         {
            addDomain();
         }
      });
      Label identifier = new Label("Identifier:");
      Label id = new Label("Domain ID:");
      fields.getChildren().addAll(identifier, mIdentifier, id, mDomainId, addDomain);

      //Create the remove fields HBox
      HBox remove = new HBox(10);
      mDomains = new ChoiceBox();
      mDomains.setItems(mDomainList.getList());
      mDomains.setPrefWidth(250);
      Button removeDomain = new Button("Remove Domain");
      removeDomain.setOnAction(new EventHandler<ActionEvent>()
      {
          @Override
         public void handle(ActionEvent ae)
         {
            removeDomain();
         }
      });
      remove.getChildren().addAll(mDomains, removeDomain);

      //Create the buttons HBox
      HBox buttons = new HBox(10);
      Button exit = new Button("Close Settings");
      exit.setOnAction(new EventHandler<ActionEvent>()
      {
          @Override
         public void handle(ActionEvent ae)
         {
            close();
         }
      });
      buttons.getChildren().addAll(exit);

      //Create a grid to arrange it all.
      GridPane root = new GridPane();
      GridPane.setRowIndex(title, 0);
      GridPane.setRowIndex(fields, 1);
      GridPane.setRowIndex(remove, 2);
      GridPane.setRowIndex(buttons, 3);
      GridPane.setMargin(title, new Insets(10,10,10,10));
      GridPane.setMargin(fields, new Insets(10,10,10,10));
      GridPane.setMargin(remove, new Insets(5,0,10,100));
      GridPane.setMargin(buttons, new Insets(5,0,10,250));
      root.setVgap(10);
      root.getChildren().addAll(title, fields, remove, buttons);

      //Create the scene
      setScene(new Scene(root, 600, 200));
      setTitle("Settings");

   }

   /**
    * addDomain
    * Adds a domain to the preferences.
    */
   private void addDomain()
   {
      String domain = mIdentifier.getText();
      String id = mDomainId.getText();
      String value = domain + ":" + id;
      mAvailDomains.put(domain + id , value);
      mDomainList.addAll(value);
      try
      {
         mAvailDomains.flush();
      }
      catch (Exception e)
      {
         System.out.println("Error saving preferences" + e.getMessage());
      }
   }

   /**
    * removeDomain
    * Removes a domain from the preferences.
    */
   private void removeDomain()
   {
      String selected = (String) mDomains.getValue();
      String[] data = selected.split(":");
      mAvailDomains.remove(data[0] + data[1]);
      mDomainList.remove(selected);
   }

   /**
    * Run method to do everything
    */
    @Override
   public void run()
   {
      initModality(Modality.APPLICATION_MODAL);
      show();
   }
}