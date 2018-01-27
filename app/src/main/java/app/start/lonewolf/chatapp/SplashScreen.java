package app.start.lonewolf.chatapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    private TextView txt;
    private Thread mSplashThread;
    private FirebaseAuth auth;
    private SavedPreferences savedPreferences;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        auth = FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser = auth.getCurrentUser();
        savedPreferences = new SavedPreferences(this);
        progressDialog = new ProgressDialog(this);
        final SplashScreen sPlashScreen = this;
        //txt = (TextView)findViewById(R.id.txtSet);
        //txt.animate().rotation(180);

        // The thread to wait for splash screen events
        mSplashThread =  new Thread(){
            @Override
            public void run(){

                try {
                    synchronized (this) {
                        // Wait given period of time or exit on touch
                       // Toast.makeText(SplashScreen.this, "Worked-......", Toast.LENGTH_LONG).show();
                        wait(2000);
                    }
                } catch (InterruptedException ex) {
                }

                finish();

                // Run next activity
                Intent intent = new Intent();
                intent.setClass(sPlashScreen, MainActivity.class);
                startActivity(intent);
                finish();

           }
        };


        if(auth.getCurrentUser()==null){

                       savedPreferences.setIsAnnonymous("yes");

                       mSplashThread.start();
                       //Toast.makeText(SplashScreen.this, "Worked-......", Toast.LENGTH_LONG).show()

        }else
        {
            savedPreferences.setIsAnnonymous("no");
            mSplashThread.start();
            //Toast.makeText(SplashScreen.this, "poked-......", Toast.LENGTH_LONG).show();
        }

        Toast.makeText(sPlashScreen, savedPreferences.getIsAnnonymous(), Toast.LENGTH_SHORT).show();

    }

    /**
     * Processes splash screen touch events
     */
    @Override
    public boolean onTouchEvent(MotionEvent evt)
    {
        if(evt.getAction() == MotionEvent.ACTION_DOWN)
        {
            synchronized(mSplashThread){
                mSplashThread.notifyAll();
            }
        }
        return true;
    }


}
