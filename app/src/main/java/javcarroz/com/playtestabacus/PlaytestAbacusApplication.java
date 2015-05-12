package javcarroz.com.playtestabacus;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

public class PlaytestAbacusApplication extends Application{


    @Override
    public void onCreate(){
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "mYKQL2PjBSDWh20OyYPEtJtXUVQVtdKL6E7lBv9X", "wOgQnZly16gO1oXAdtApv8uN34KAt8EqN460TWrw");
        //Parameters (AppId, ClientId);
    }

}