package com.example.musicplayerproject;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listView;
    String[] item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.songListView);
        runtimePermission();

    }

    public void runtimePermission()
    {
        Dexter.withContext(this).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        display();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> getSong(File file)
    {
        ArrayList<File> arrayList=new ArrayList<>();
        File[] file1=file.listFiles();
        for(File singles: file1)
        {
            if(singles.isDirectory() && !singles.isHidden())
            {
//                arrayList.addAll(getSong(singles));
//                getSong(singles);

            }
            else
            {
                if(singles.getName().endsWith(".mp3") || singles.getName().endsWith(".wav")|| singles.getName().endsWith(".m4a"))
                {
                    arrayList.add(singles);

                }
            }

        }
      return arrayList;
    }
     void display()
    {
        final ArrayList<File> arrayList=getSong(Environment.getExternalStorageDirectory());
        item=new String[arrayList.size()];
        for(int i=0; i < arrayList.size(); i++)
        {
            item[i]=arrayList.get(i).getName().toString().replace(".mp3","").replace(".wav","");

        }
        CustomAdapter customAdapter=new CustomAdapter();
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String singlesong=(String) listView.getItemAtPosition(i);
                startActivity(new Intent(getApplicationContext(),MainActivity2.class).putExtra("alllist",arrayList)
                        .putExtra("singlesong",singlesong)
                        .putExtra("position",i)
                );
            }
        });
    }

    class  CustomAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return item.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            View myview=getLayoutInflater().inflate(R.layout.list_item,null);
            TextView textView=myview.findViewById(R.id.textSongname);
            textView.setSelected(true);
            textView.setText(item[i]);
            return myview;
        }
    }
}