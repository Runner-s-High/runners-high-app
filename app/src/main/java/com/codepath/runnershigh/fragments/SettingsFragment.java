package com.codepath.runnershigh.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codepath.runnershigh.MainActivity;
import com.codepath.runnershigh.R;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.parse.Parse.getApplicationContext;

//TODO: Associate profile picture with user and not the run itself

/*
This fragment holds the ability for the user to change their profile pic, as well as for them to
change units from miles to kilometers.
 */
public class SettingsFragment extends Fragment {
    public static final String TAG = "SettingsFragment";
    SettingsFragmentInterface settingsFragmentInterface;

    Uri imageuri;
    CircleImageView SettingsProfilePic;
    CircleImageView MenuBarProfilePic;
    public static ParseFile ProfilePicture=null;

    String UserName;
    TextView tvusername;
    RadioGroup rgUnits;
    SharedPreferences prefs;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnLogOut;
        Button btnChangeProfilePic;

        UserName=ParseUser.getCurrentUser().getUsername();

        //Initialize views
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnChangeProfilePic=view.findViewById(R.id.btnchangepic);
        MenuBarProfilePic= MainActivity.Menu_Profile_Pic;
        SettingsProfilePic=view.findViewById(R.id.SettingsProfileImage);
        tvusername=view.findViewById(R.id.tvUserName);
        rgUnits = view.findViewById(R.id.rgUnits);

        //Make sure that appropriate unit button checked, based on what is saved
        if(prefs.getInt("units", MainActivity.DISTANCE_MILES) == MainActivity.DISTANCE_KILOMETERS)
            rgUnits.check(R.id.rbKilometers);
        else
            rgUnits.check(R.id.rbMiles);

        tvusername.setText(UserName);

        //Load in user profile image
        if (MainActivity.UserImage == null)                               //static variables is key
           SettingsProfilePic.setImageResource(R.drawable.trophy);
        else
           Glide.with(getApplicationContext()).load(MainActivity.UserImage.getUrl()).into(SettingsProfilePic);

        if (MainActivity.uri_after_pic_change!=null)
            Glide.with(getContext()).load(MainActivity.uri_after_pic_change).into(SettingsProfilePic);

        btnLogOut.setOnClickListener(v -> {
            Log.i(TAG, "User clicked btnLogOut");
            settingsFragmentInterface.logOut();
        });

        btnChangeProfilePic.setOnClickListener(v -> {
            Intent photogallery=new Intent();
            photogallery.setType("image/*");
            photogallery.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(photogallery,"choose picture"),754);
        });

        rgUnits.setOnCheckedChangeListener((group, checkedId) -> {
            SharedPreferences prefs = getContext().getSharedPreferences("settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor prefsEditor = prefs.edit();

            //Save appropriate unit setting for next time
            switch(checkedId) {
                case R.id.rbMiles:
                    prefsEditor.putInt("units", MainActivity.DISTANCE_MILES);
                    break;
                case R.id.rbKilometers:
                    prefsEditor.putInt("units", MainActivity.DISTANCE_KILOMETERS);
                    break;
            }

            prefsEditor.apply();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==754 && resultCode==RESULT_OK){
            imageuri=data.getData();
            InputStream inputstream=null;

            Glide.with(getContext()).load(imageuri).into(SettingsProfilePic);
            Glide.with(getContext()).load(imageuri).into(MenuBarProfilePic);
            String filename=getFileName(imageuri);
            MainActivity.uri_after_pic_change=imageuri;             //persists newly chosen image while using app

            try {
                inputstream = getContext().getContentResolver().openInputStream(imageuri);
                byte buffer[] = new byte[inputstream.available()];
                inputstream.read(buffer);
                ProfilePicture = new ParseFile(filename, buffer);     //try making this static and see
                inputstream.close();
                //MainActivity.UserImage=ProfilePicture;     doesnt work like hoped-needed the imageuri (line 132)

            }
            catch (IOException e){
                e.printStackTrace();
            }

        }
    }

    //Attaching the interface
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof SettingsFragmentInterface){
            settingsFragmentInterface = (SettingsFragmentInterface) context;
        }

        else{
            throw new RuntimeException(context.toString()+
                    "must implement SettingsFragmentInterface");
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public interface SettingsFragmentInterface {
        void logOut();
        void openSettingsFragment();
    }
}