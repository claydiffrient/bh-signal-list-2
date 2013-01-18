package edu.byui.signaldownloader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * ThreadsWindow
 */
public class ThreadsWindow
   extends Stage
   implements Runnable
{
   /**
    * Holds List View
    */
   ListView<String> mRunningList;

   /**
    * Holds the backing for Lists.
    */
   ObservableList<String> mListOfNames;

   /**
    * Holds the active running list of Signal Reports
    */
   List<SignalReport> mReportsRunning;

   /**
    * Timer for the reaping.
    */
   ScheduledExecutorService mReapTimer;

   /**
    * Default constructor.  Sets up the UI.
    */
   ThreadsWindow()
   {

      mReapTimer = Executors.newSingleThreadScheduledExecutor();
      Reaper reaper = new Reaper();
      mReapTimer.scheduleAtFixedRate(reaper, 1, 1, TimeUnit.SECONDS);

      mReportsRunning = new ArrayList<SignalReport>();
      //Create title
      HBox title = new HBox();
      Label titleLabel = new Label("Reports Actively Running");
      titleLabel.setFont(new Font(26));
      title.getChildren().addAll(titleLabel);

      //Create ListView
      mRunningList = new ListView<String>();
      mListOfNames = FXCollections.observableArrayList();

      mRunningList.setPrefWidth(200);
      mRunningList.setPrefHeight(200);
      mRunningList.setItems(mListOfNames);

      //Create Buttons
      HBox buttons = new HBox(10);
      Button exit = new Button("Close");
      exit.setOnAction(new EventHandler<ActionEvent>()
      {
          @Override
         public void handle(ActionEvent ae)
         {
            close();
         }
      });
/*      Button reap = new Button("Refresh List");
      reap.setOnAction(new EventHandler<ActionEvent>()
      {
         public void handle(ActionEvent ae)
         {
            new Reaper().start();
         }
      });*/
      buttons.getChildren().addAll(exit);
      BorderPane main = new BorderPane();
      main.setTop(title);
      main.setCenter(mRunningList);
      main.setBottom(buttons);

      setTitle("Running Threads");
      setScene(new Scene(main, 400, 350));
   }

   /**
    * startReport
    * Starts a report running for the given information.
    * @param domain the domain string.  (e.g. byui:14853)
    * @param username the username (e.g. byui/marsh3)
    * @param password the password to use
    * @param lastsignal the last signal to use.
    */
   public void startReport(String pDomain, String pUsername, String pPassword,
                           String pLastSignal)
   {
      if (!mListOfNames.contains(pDomain))
      {
         mListOfNames.addAll(pDomain);
         SignalReport report = new SignalReport(pDomain, pUsername, pPassword, pLastSignal);
         mReportsRunning.add(report);
         report.start();
      }
   }

   /**
    * Run method to make it start.
    */
    @Override
   public void run()
   {
      show();
   }

   /**
    * Inner class to handle finishing of tasks.
    */
   private class Reaper
      extends Thread
   {
       @Override
      public void run()
      {
         try
         {
            for (SignalReport t : mReportsRunning)
            {
               String tName = t.getName();
               if (mListOfNames.contains(tName))
               {
                  if (!t.isRunning())
                  {
                     mListOfNames.removeAll(tName);
                     mReportsRunning.remove(t);
                  }
               }
            }
         }
         catch(Exception e)
         {
         }
      }
   }
}
