 package ru.sergeykozhukhov.fileguide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.base_guide_frame_layout, FileListFragment.newInstance())
                    .add(R.id.detail_guide_frame_layout, FileListFragment.newInstance())
                    .commit();
        }


    }
}
