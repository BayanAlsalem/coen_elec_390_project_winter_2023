package com.example.coen_elec_390_project_winter_2023.Controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.coen_elec_390_project_winter_2023.Models.Doctor;
import com.example.coen_elec_390_project_winter_2023.Models.Patient;
import com.example.coen_elec_390_project_winter_2023.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class FirebaseHelper {
    private static final String DB_USERS_COLLECTION = "users";

    public interface voidCallbackInterface {
        void onSuccess();

        void onFail(Exception e);
    }

    public interface getUserCallbackInterface {
        void onSuccess(User user);

        void onFail(Exception e);
    }

    public FirebaseHelper() {
        //Empty constructor
    }

    public static FirebaseFirestore db() {
        return FirebaseFirestore.getInstance();
    }

    public static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }


    public void login(String email, String password, voidCallbackInterface callback) {
        auth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            callback.onSuccess();
                        } else {
                            callback.onFail(task.getException());
                        }
                    }
                });
    }

    public void logout() {
        auth().signOut();
    }

    public boolean isLoggedIn() {
        return auth().getCurrentUser() != null;
    }

    public void getCurrentUser(getUserCallbackInterface callback) {
        if (auth().getCurrentUser() == null) {
            callback.onFail(new Exception("User is not logged in."));
            return;
        }

        String uid = auth().getCurrentUser().getUid();
        db().collection(DB_USERS_COLLECTION).document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if(task.getResult().getData() != null){
                                switch(task.getResult().getData().get("type").toString()){
                                    case "PATIENT":
                                        callback.onSuccess(task.getResult().toObject(Patient.class));
                                        break;
                                    case "DOCTOR":
                                        callback.onSuccess(task.getResult().toObject(Doctor.class));
                                        break;
                                    default:
                                        callback.onFail(new Exception("User type is invalid."));
                                }
                            } else {
                                callback.onFail(new Exception("User data does not exists."));
                            }
                        } else {
                            callback.onFail(task.getException());
                        }
                    }
                });

    }

    public void createUser(User user, voidCallbackInterface callback) {
        auth().createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //After adding user to authentication, use the generated UID to add the whole user obj to db.
                            String uid = Objects.requireNonNull(task.getResult().getUser()).getUid();
                            user.setUID(uid);
                            db().collection(DB_USERS_COLLECTION).document(uid)
                                    .set(user)
                                    .addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task2) {
                                            if (task2.isSuccessful()) {
                                                Log.d("API", "DocumentSnapshot added with ID: " + uid);
                                                callback.onSuccess();
                                            } else {
                                                //Couldn't add user in database
                                                callback.onFail(task2.getException());
                                            }
                                        }
                                    });
                        } else {
                            System.out.println(task.getException().getMessage());
                            //Couldn't create auth for user
                            callback.onFail(task.getException());
                        }
                    }
                });
    }
}
