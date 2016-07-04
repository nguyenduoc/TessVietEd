package vietedcom.tessvieted;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import vietedcom.tessvieted.cameras.CameraFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment fragment = getSupportFragmentManager().
                findFragmentById(R.id.content_layout);
        if (fragment == null) {
            fragment = new HomeFragment();
            getSupportFragmentManager().
                    beginTransaction().
                    replace(R.id.content_layout, fragment).
                    commit();
        }
    }
}
