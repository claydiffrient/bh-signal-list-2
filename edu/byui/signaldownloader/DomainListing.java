import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

/**
 * DomainListing
 * @author Clay Diffrient
 */

public class DomainListing
{
   /**
    * Holds listing of domains
    */
   ObservableList<String> mListing;

   /**
    * Holds instance.
    */
   private static DomainListing cInstance = null;

   /**
    * Constructor
    */
   protected DomainListing()
   {
      mListing = FXCollections.observableArrayList();
   }

   /**
    * getInstance
    */
   public static DomainListing getInstance()
   {
      if (cInstance == null)
      {
         cInstance = new DomainListing();
      }
      return cInstance;
   }

   /**
    * Gets the list
    */
   public ObservableList<String> getList()
   {
      return mListing;
   }

   /**
    * Add to list
    */
   protected void addAll(String ... elements)
   {
      for (int i = 0; i < elements.length; i++)
      {
         mListing.addAll(elements[i]);
      }
   }

   protected void remove(String ... elements)
   {
      for (int i = 0; i < elements.length; i++)
      {
         mListing.removeAll(elements[i]);
      }
   }



}