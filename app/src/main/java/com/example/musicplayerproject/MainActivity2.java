package com.example.musicplayerproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity2 extends AppCompatActivity {

    Button playbtn,nextbtn,previousbtn,shufflebtn,repeatbtn;
    TextView songtextview,starttxt,endtxt;
    ImageView songimageview;
    SeekBar seekBar;

    boolean shuffle=false;
    boolean singleloop=false;
    boolean normal=true;
    boolean singlemedia=false;
    int number=1;

    boolean shffleon=false;
    boolean repeaton=false;
    boolean repeatone=false;

    boolean animation=true;

    private Handler handler;
    private Runnable runnable;

    int position;
    ArrayList<File> arrayList;
    MediaPlayer currently;


    AnimationDrawable animationDrawable;

    String so;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        playbtn=findViewById(R.id.playbtn);
        shufflebtn=findViewById(R.id.shufflebtn);
        repeatbtn=findViewById(R.id.repeatbtn);

        songimageview=findViewById(R.id.image);
        songtextview=findViewById(R.id.text);
        seekBar=findViewById(R.id.seekbar);


        nextbtn=findViewById(R.id.nextbtn);
        previousbtn=findViewById(R.id.previousbtn);

        starttxt=findViewById(R.id.starttime);
        endtxt=findViewById(R.id.endtime);




        songtextview.setSelected(true);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        String ssong=bundle.getString("singlesong");
        arrayList=(ArrayList)bundle.getParcelableArrayList("alllist");
        position=bundle.getInt("position",0);

        currently =play(position);


        animate();



        handler=new Handler();
        runnable=new Runnable()
        {
            public void run()
            {

                    int cupo=currently.getCurrentPosition();
                    int cuduration=currently.getCurrentPosition();
                    String cudurationstring=Integer.toString(cuduration);
                    String intialtime=calculateTime(cudurationstring);
                    starttxt.setText(intialtime);
                    seekBar.setProgress(cupo);
                    handler.postDelayed(this,50);

            }
        };

                handler.postDelayed(runnable,0);

                //sets the listener for the repeat btn
                repeatbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!repeaton)
                        {
                                repeatbtn.setBackgroundResource(R.drawable.repeat_white);
                                shufflebtn.setBackgroundResource(R.drawable.shuffle);
                                repeaton=true;
                                number=2;
                                setnextplay();
                        }
//
                        else
                        {
                            repeatbtn.setBackgroundResource(R.drawable.repeat);
                            repeaton=false;
                            repeatone=false;
                            removenextplay();
                        }


                    }
                });


                //sets listener for the shufflebtn
                shufflebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!shffleon)
                        {
                            shufflebtn.setBackgroundResource(R.drawable.shuffle_white);
                            repeatbtn.setBackgroundResource(R.drawable.repeat);
                            shffleon=true;
                            shffle();
                        }
                        else
                        {
                            shufflebtn.setBackgroundResource(R.drawable.shuffle);
                            shffleon=false;
                            removeShffle();
                        }


                    }
                });

                //sets a listener for the playbtn
        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currently.isPlaying())
                {
                    playbtn.setBackgroundResource(R.drawable.play);
                    currently.pause();
                    animation=false;

                }
                else
                {
                    playbtn.setBackgroundResource(R.drawable.pause);
                    currently.start();
                    animation=true;
                }
                animate();

            }
        });

        //sets the listener for the next play
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(normal)
                endingplaynext();
                if(shuffle)
                    playRandom();
                if(singleloop)
                    playnext();
                if(singlemedia)
                    singlemedialoop();

            }
        });

        //sets listener for the previousbtn
        previousbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(normal)
                endingplayprevious();
                if(shuffle)
                    playRandom();
                if(singleloop)
                    playprevious();
                if(singlemedia)
                    singlemedialoop();

            }
        });

        //set a listener for the seekbar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress= seekBar.getProgress();
                currently.seekTo(progress);

            }
        });

    }



//check the permission for the animation
    private void animate() {
        if(animation)
            animation();
        else
            songimageview.setBackgroundResource(R.drawable.music_bg);
    }

    //this method creates the animation
    private void animation() {
        animationDrawable=new AnimationDrawable();
        songimageview.setBackgroundResource(R.drawable.music_animation);
        animationDrawable=(AnimationDrawable) songimageview.getBackground();
        animationDrawable.start();

    }

    //this method converts millisecons to minute and second
    @NonNull
    private String calculateTime(String endtime) {
        int time=Integer.parseInt(endtime);
        String timee = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(time),
                TimeUnit.MILLISECONDS.toSeconds(time) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(time))
        );

        return timee;

    }

    //set singleloop
    private  void setnextplay()
    {
        shuffle=false;
        normal=false;
        singleloop=true;
        singlemedia=false;
    }
    //removes singleloop and back to default
    private  void removenextplay()
    {
        shuffle=false;
        normal=true;
        singleloop=false;
        singlemedia=false;
    }

    //single media loop logic
    private void singlemedialoop()
    {
        currently.stop();
        currently=play(position);
    }

    //sets the shuffle mode on
    private void shffle()
    {

        shuffle=true;
        normal=false;
        singleloop=false;
        singlemedia=false;
    }

    //removes the shuffle mode
    private void removeShffle()
    {
        shuffle=false;
        normal=true;
        singleloop=false;
        singlemedia=false;
    }

    //logic for shuffle
    private void playRandom()
    {
        Random random=new Random();
        currently.stop();
        int randomPosition=random.nextInt(arrayList.size());
        currently=play(randomPosition);
    }

    //logic for default play next
    private void endingplaynext()
    {
        position+=1;
        if(position >= arrayList.size())
        {
           Toast toast= Toast.makeText(getApplicationContext(),"Media Player reach end!",Toast.LENGTH_LONG);
           toast.show();
           position=arrayList.size()-1;
           currently.stop();
        }
        else
        {
            currently.stop();
            currently=play(position);
        }
    }

    //logic for default play previous
    private void endingplayprevious()
    {
        position-=1;
        if(position < 0)
        {
            Toast toast=Toast.makeText(this,"Media Player reach end!",Toast.LENGTH_LONG);
            toast.show();
            position=0;
            currently.stop();
        }
        else
        {
            currently.stop();
            currently=play(position);
        }
    }

    //this method is logic for the playnext media
    private void playnext() {
        position+=1;
        if(position >= arrayList.size())
        {
            position=0;
        }
        currently.stop();
        currently=play(position);

    }

    //this is a logic for the playprevious media
    private void playprevious() {
        position-=1;
        if(position < 0)
        {
            position=arrayList.size()-1;
        }
        currently.stop();
        currently.reset();
        currently=play(position);

    }

    //this part makes the media player to start playing
    private MediaPlayer play(int position) {

        Uri uri=Uri.parse(arrayList.get(position).toString());
        so=arrayList.get(position).getName();
        songtextview.setText(so);

        MediaPlayer mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        seekBar.setMax(mediaPlayer.getDuration());
        String endtime=Integer.toString(mediaPlayer.getDuration());
        String en=calculateTime(endtime);
        endtxt.setText(en);
        mediaPlayer.start();

        if(mediaPlayer.isPlaying())
        {
            playbtn.setBackgroundResource(R.drawable.pause);
            animation=true;
            animate();
        }
        else
        {
            playbtn.setBackgroundResource(R.drawable.play);
        }

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
               if(normal)
                   endingplaynext();
               if(shuffle)
                   playRandom();
               if(singleloop)
                   playnext();
            }
        });



        return mediaPlayer;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        currently.stop();

    }
}
