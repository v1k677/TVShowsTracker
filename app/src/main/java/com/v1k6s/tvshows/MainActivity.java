package com.v1k6s.tvshows;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            ShowStore.store(this);
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.add_a_show:
                addShowDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addShowDialog() {
        final View dialogView = getLayoutInflater().inflate(R.layout.dialog_new_show, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String name = ((EditText) dialogView.findViewById(R.id.dialog_add_show_name)).getText().toString();
                String season = ((EditText) dialogView.findViewById(R.id.dialog_add_season)).getText().toString();
                String episode = ((EditText) dialogView.findViewById(R.id.dialog_add_episode)).getText().toString();

                if (name.isEmpty() || season.isEmpty() || episode.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Error: Could NOT add!", Toast.LENGTH_SHORT).show();
                } else {
                    ShowStore.add(new Show(name, Integer.parseInt(season), Integer.parseInt(episode)));
                    Toast.makeText(getApplicationContext(), "Successfully added.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
    }
}