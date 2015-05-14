package javcarroz.com.playtestabacus;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

import javcarroz.com.playtestabacus.data.Participant;

public class PlaytestAbacusApplication extends Application{

    public static ParseObject mProjectRef;

    @Override
    public void onCreate(){
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Participant.class);

        Parse.initialize(this, "mYKQL2PjBSDWh20OyYPEtJtXUVQVtdKL6E7lBv9X", "wOgQnZly16gO1oXAdtApv8uN34KAt8EqN460TWrw");
        //Parameters (AppId, ClientId);
    }

}