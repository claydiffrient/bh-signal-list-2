package edu.byui.signaldownloader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * SignalReport
 * An actively running reporting mechanism.
 */
public class SignalReport
   extends Thread
{
   /**
    * Holds domain
    */
   String mDomain;

   /**
    * Holds username
    */
   String mUsername;

   /**
    * Holds password
    */
   String mPassword;

   /**
    * Holds last signal
    */
   String mLastSignal;

   /**
    * Holds count
    */
   int mCount;

   /**
    * Holds running indicator
    */
   boolean mRunning;

   /**
    * Holds last Id
    */
   String mLastId;

   /**
    * Constructor
    */
   SignalReport(String pDomain, String pUsername, String pPassword,
                String pLastSignal)
   {
      setName(pDomain);
      mDomain = pDomain;
      mUsername = pUsername;
      mPassword = pPassword;
      mLastSignal = pLastSignal;
      mCount = 0;
      mRunning = true;
      mLastId = pLastSignal;
   }

   /**
    * terminate
    * Ends the process.
    */
   public void terminate()
   {
      mRunning = false;
   }

   /**
    * isRunning
    * Returns running status.
    */
   public boolean isRunning()
   {
      return mRunning;
   }

   /**
    * getSignalList
    * Actively performs the data retrieval.
    */
    private void getSignalList(String pLastId, String pDomain,
                                  String pUsername, String pPassword, int count)
    {
      String[] tempData = pDomain.split(":");
      String domainId = tempData[1];
      String fileName = "signals-" + tempData[0] + "-" + tempData[1];
      String signalId = pLastId;


      if (pLastId == null)
      {
         mRunning = false;
      }
      else {
         try {
            Pattern regexPattern = Pattern.compile("signalid=\"[0-9]+\"");
            Matcher match;
            String url = "http://dlap.agilix.com/Dlap.ashx";
            URL passwordXML = new URL(url);
            URLConnection urlc = passwordXML.openConnection();
            urlc.setRequestProperty("Content-Type","text/xml");
            urlc.setDoOutput(true);
            urlc.setDoInput(true);
            PrintWriter pw = new PrintWriter(urlc.getOutputStream());
            pw.write("<batch>");
            pw.write("<request cmd=\"login\" username=\""+pUsername+"\" password=\""+pPassword+"\" />");
            pw.write("<request cmd=\"getsignallist\" lastsignalid=\""+pLastId+"\" domainid=\""+domainId+"\" />");
            pw.write("</batch>");
            pw.close();
            try {
               BufferedReader in = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
               String inputLine;
               File ourFile = new File(fileName + "-" + mLastId + ".xml");
               BufferedWriter fOut = new BufferedWriter(new FileWriter(ourFile, true));
               while ((inputLine = in.readLine()) != null){
                  match = regexPattern.matcher(inputLine);
                  if (match.find())
                  {
                     signalId = match.group(0);
                     signalId = signalId.split("=")[1];
                     signalId = signalId.replaceAll("\"", "");
                  }
                  fOut.write(inputLine);
                  fOut.newLine();
                  //System.out.println(inputLine);
                  if (inputLine.contains("<signals />"))
                  {
                    signalId = null;
                    mRunning = false;
                  }
                  if (inputLine.contains("<response code=\"InvalidCredentials\"")) {
                     signalId = null;
                     mRunning = false;
                  }
                  if (inputLine.contains("<response code=\"AccessDenied\""))
                  {
                     signalId = null;
                     mRunning = false;
                  }
               }
                  if (count == 50)
                  {
                    try
                    {
                      mLastId = signalId;
                      System.out.println("50 DLAP calls.  File Closed and Renamed");
                      fOut.close();
                      count = 0;
                    }
                    catch (Exception e)
                    {
                      System.out.println("Problem!" + e.getMessage());
                    }

                  }
               System.out.println("Last Signal ID : " + signalId);
               System.out.println("DLAP Call Count: " + count);
               System.out.println(isRunning());
               if (count != 50){
                  fOut.close();
               }
               in.close();
               getSignalList(signalId, pDomain, pUsername, pPassword, ++count);
            }
            catch (Exception e)
            {
               System.out.println("Problem2! " + e.getMessage());
            }
         }
         catch (Exception e)
         {
            System.out.println("Problem 3! " + e.getMessage());
         }
      }
   }


   /**
    * Run
    * Used to make this report start.
    */
    @Override
   public void run()
   {
      while (mRunning == true)
      {
         getSignalList(mLastId, mDomain, mUsername, mPassword, mCount);
      }
   }
}