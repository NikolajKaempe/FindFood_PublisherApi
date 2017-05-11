package firebaseFiles;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.*;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Kaempe on 01-05-2017.
 */
public class FireBaseDatabase
{
    private static FirebaseDatabase database;
    private static DatabaseReference ref;
    private static HashMap<String, User> userInfo;

    public FireBaseDatabase(){
        try {
            // During development
            //FileInputStream serviceAccount = new FileInputStream("src//main/java/firebaseFiles/recipes-2c54d-firebase-adminsdk-2lkaa-d26faaad1d.json");

            FileInputStream serviceAccount = new FileInputStream("files/recipes-2c54d-firebase-adminsdk-2lkaa-d26faaad1d.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                    .setDatabaseUrl("https://recipes-2c54d.firebaseio.com/")
                    .build();
            FirebaseApp.initializeApp(options);
        }catch (Exception e){
            System.out.println("NO firebase Auth :/ ");
        }

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("users");
        this.observeUserInfo();
    }

    public static void observeUserInfo()
    {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // ...
                userInfo = new HashMap<>();
                Iterable<DataSnapshot> snapChildren = dataSnapshot.getChildren();
                for (DataSnapshot child : snapChildren)
                {
                    User user = child.getValue(User.class);
                    userInfo.put(child.getKey(), user);
                    System.out.println(user.getPublisherName() + " " + user.getRole());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static User getUserInfo(String uid)
    {
        return userInfo.get(uid);
    }
}
