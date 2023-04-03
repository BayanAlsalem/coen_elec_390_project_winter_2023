package com.example.coen_elec_390_project_winter_2023.Controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.coen_elec_390_project_winter_2023.Models.Doctor;
import com.example.coen_elec_390_project_winter_2023.Models.Patient;
import com.example.coen_elec_390_project_winter_2023.Models.Reading;
import com.example.coen_elec_390_project_winter_2023.Models.User;
import com.example.coen_elec_390_project_winter_2023.Models.userOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FirebaseHelper {
    private static final String DB_USERS_COLLECTION = "users";
    private static final String DB_READINGS_COLLECTION= "readings";

    public interface voidCallbackInterface {
        void onSuccess();

        void onFail(Exception e);
    }

    public interface getUserCallbackInterface {
        void onSuccess(User user);

        void onFail(Exception e);
    }

    public interface getUsersCallbackInterface {
        void onSuccess(List<User> users);

        void onFail(Exception e);
    }

    public interface getReadingsListCallbackInterface {
        void onSuccess(List<Reading> readingsList);

        void onFail(Exception e);
    }

    public interface getReadingsCallbackInterface {
        void onSuccess(List<Integer> readings);

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

    public String getCurrentUserId(){
        return isLoggedIn() ? auth().getCurrentUser().getUid() : null;
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
        auth().createUserWithEmailAndPassword(user.getAge(), user.getExperience())
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

    public void loadAllPatients(getUsersCallbackInterface callback){
        List<User> users = new ArrayList<>();
        db().collection(DB_USERS_COLLECTION)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User temp = document.toObject(Patient.class);

                                if(temp.getUserType() == userOptions.userType.PATIENT){
                                    users.add(temp);
                                }
                            }
                            callback.onSuccess(users);
                        } else {
                            callback.onFail(task.getException());
                        }
                    }
                });
    }
    public void updateDoctorPatientsList(List<String> patientsList, voidCallbackInterface callback) {
        String uid = getCurrentUserId();
        db().collection(DB_USERS_COLLECTION).document(uid)
                .update("patientsList", patientsList)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("API", "Doctor ID: " + uid + " updated patients to " + patientsList);
                            callback.onSuccess();
                        } else {
                            //Couldn't update doctor
                            callback.onFail(task.getException());
                        }
                    }
                });
    }

    public void updateDoctorInfo(String fullName, String hospitalName, String age, String experience, voidCallbackInterface callback) {
        String uid = getCurrentUserId();
        Map<String, Object> m = new HashMap<>();
        m.put("name", fullName);
        m.put("hospital", hospitalName);
        m.put("age", age);
        m.put("experience", experience);
        db().collection(DB_USERS_COLLECTION).document(uid)
                .update(m)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            //Couldn't update doctor
                            callback.onFail(task.getException());
                        }
                    }
                });
    }

    public void updatePatientInfo(String fullName, String age, String city, String country, String doctorName, voidCallbackInterface callback) {
        String uid = getCurrentUserId();
        Map<String, Object> m = new HashMap<>();
        m.put("name", fullName);
        m.put("age", age);
        m.put("city", city);
        m.put("country", country);
        m.put("doctorName", doctorName);

        db().collection(DB_USERS_COLLECTION).document(uid)
                .update(m)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            //Couldn't update doctor
                            callback.onFail(task.getException());
                        }
                    }
                });
    }

    public void createReading(List<Integer> flexedList,List<Integer> relaxedList, voidCallbackInterface callback, String userID){

        Log.d("Readings","Creating Reading");
        getCurrentUser(new getUserCallbackInterface() {
            @Override
            public void onSuccess(User user) {
                Log.d("Readings","Creating Reading: Get current user succeeded");
                if (user.getUserType() == userOptions.userType.PATIENT){
                    if(user.getUid().equals(userID)){
                        Log.d("Readings","Creating Reading: User confirmed");
                        Reading reading= new Reading(userID,flexedList,relaxedList,new Timestamp(System.currentTimeMillis()));
                        db().collection(DB_USERS_COLLECTION).document(user.getUid()).collection(DB_READINGS_COLLECTION).add(reading).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("Readings", "DocumentSnapshot written with ID: " + documentReference.getId());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("Readings", "Error adding document", e);
                                    }
                                });
                        System.out.println("TEST SUCCESSSSS a bit!!!!!");
                    }

                } else if (user.getUserType() == userOptions.userType.DOCTOR){
                    //To Do: we need to pass user object to either dashboards (eg: putExtra)

                }
            }
            @Override
            public void onFail(Exception e) {

            }
        });
    }

    public List<Reading> getReadings(String userID, getReadingsListCallbackInterface callback){
        List<Reading> readingsList= new ArrayList<>();
        getCurrentUser(new getUserCallbackInterface() {
            public void onSuccess(User user) {
                if (user.getUserType() == userOptions.userType.PATIENT) {
                    {
                        if (user.getUid().equals(userID)) {
                            db().collection(DB_USERS_COLLECTION).document(user.getUid()).collection(DB_READINGS_COLLECTION).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    //Log.d("Readings", document.getId() + " => " + document.getData());
                                                    Reading temp = document.toObject(Reading.class);
                                                    //Log.d("Readings","TEMP VARIABLE"+temp.toString());
                                                    readingsList.add(temp);
                                                }
                                                callback.onSuccess(readingsList);
                                            } else {
                                                Log.d("Readings", "Error getting documents: ", task.getException());
                                                callback.onFail(task.getException());
                                            }
                                        }
                                    });
                        }
                    }
                }
                else
                {
                    Log.d("Readings","NOT A PATIENT");
                }
            }
            @Override
            public void onFail(Exception e) {
                Log.d("Readings","GETING READINGS FAILED");
            }
        });
//        Log.d("Readings","ReadingsList: "+readingsList.toString());
        return readingsList;
    }

    public List<Reading> getReadingsNotCurrentUser(String userID, getReadingsListCallbackInterface callback){
        List<Reading> readingsList= new ArrayList<>();
        getCurrentUser(new getUserCallbackInterface() {
            public void onSuccess(User user) {
                            db().collection(DB_USERS_COLLECTION).document(userID).collection(DB_READINGS_COLLECTION).get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    //Log.d("Readings", document.getId() + " => " + document.getData());
                                                    Reading temp = document.toObject(Reading.class);
                                                    //Log.d("Readings","TEMP VARIABLE"+temp.toString());
                                                    readingsList.add(temp);
                                                }
                                                callback.onSuccess(readingsList);
                                            } else {
                                                Log.d("Readings", "Error getting documents: ", task.getException());
                                                callback.onFail(task.getException());
                                            }
                                        }
                                    });
            }
            @Override
            public void onFail(Exception e) {
                Log.d("Readings","GETING READINGS FAILED");
            }
        });
//        Log.d("Readings","ReadingsList: "+readingsList.toString());
        return readingsList;
    }


}
