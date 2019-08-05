package com.example.travelmantics;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FirebaseUtil {
    public static FirebaseDatabase mFirebaseDatabase;
    public static DatabaseReference mDatabaseReference;
    public static FirebaseUtil mFirebaseUtil;
    public static FirebaseAuth mFirebaseAuth;
    public static FirebaseStorage mFirebaseStorage;
    public static StorageReference mStorageRef;
    public static FirebaseAuth.AuthStateListener mAuthListener;
    public static ArrayList <TravelDeal> mTravelDeals;
    public static ListActivity caller;
    public static  boolean isAdmin;

    private static final int RC_SIGN_IN = 123;

    private FirebaseUtil(){

    }
    public static void openFbReference(String ref, final ListActivity callerActivity){
        if (mFirebaseUtil==null){
            mFirebaseUtil = new FirebaseUtil();
            mFirebaseDatabase=FirebaseDatabase.getInstance();
            mFirebaseAuth=FirebaseAuth.getInstance();
            caller=callerActivity;
            mAuthListener=new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    if(firebaseAuth.getCurrentUser()==null) {
                        FirebaseUtil.signIn();
                    }else{
                        String userId = firebaseAuth.getUid();
                        checkAdmin(userId);
                    }
                    Toast.makeText(callerActivity.getBaseContext(),"Welcome", Toast.LENGTH_SHORT).show();
                }
            };
            connectStorage();
        }
        mTravelDeals= new ArrayList<TravelDeal>();
        mDatabaseReference =mFirebaseDatabase.getReference().child(ref);
    }

    private static void checkAdmin(String userId) {
        FirebaseUtil.isAdmin=false;
        DatabaseReference ref = mFirebaseDatabase.getReference().child("administrators").child(userId);
        ChildEventListener  childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseUtil.isAdmin=true;
                caller.showMenu();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addChildEventListener(childEventListener);
    }

    public static void signIn(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());


// Create and launch sign-in intent
        caller.startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);
    }

    public static void attachStateListener(){
        mFirebaseAuth.addAuthStateListener(mAuthListener);
    }
    public static void detachStateListener(){
        mFirebaseAuth.removeAuthStateListener(mAuthListener);
    }
    public static void connectStorage(){
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageRef=mFirebaseStorage.getReference().child("deals_pictures");
    }
}
