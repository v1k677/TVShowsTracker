package com.v1k6s.tvshows;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

import java.util.ArrayList;

public class TVShowsListFragment extends ListFragment {
    ShowAdapter adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new ShowAdapter(ShowStore.getAllShows(getActivity()));
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        viewDialog(position);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        registerForContextMenu(this.getListView());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = this.getActivity().getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case R.id.menu_view_or_edit_a_show:
                viewDialog(info.position);
                return true;
            case R.id.menu_delete_a_show:
                deleteDialog(info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void viewDialog(final int position) {
        final Show s = ShowStore.getShowByIndex(position);

        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_season_episode, null);

        final Button seasonButton = (Button) dialogView.findViewById(R.id.dialog_se_season_button);
        final Button episodeButton = (Button) dialogView.findViewById(R.id.dialog_se_episode_button);

        seasonButton.setText(String.valueOf(s.getSeason()));
        Button seasonButtonMinus = (Button) dialogView.findViewById(R.id.dialog_se_season_button_prev);
        seasonButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer val = Integer.parseInt(seasonButton.getText().toString());
                val = Math.max(val - 1, 0);
                seasonButton.setText(String.valueOf(val));
                episodeButton.setText("0");
            }
        });

        Button seasonButtonPlus = (Button) dialogView.findViewById(R.id.dialog_se_season_button_next);
        seasonButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer val = Integer.parseInt(seasonButton.getText().toString());
                seasonButton.setText(String.valueOf(val + 1));
                episodeButton.setText("0");
            }
        });

        episodeButton.setText(String.valueOf(s.getEpisode()));
        Button episodeButtonMinus = (Button) dialogView.findViewById(R.id.dialog_se_episode_button_prev);
        episodeButtonMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer val = Integer.parseInt(episodeButton.getText().toString());
                val = Math.max(val - 1, 0);
                episodeButton.setText(String.valueOf(val));
            }
        });

        Button episodeButtonPlus = (Button) dialogView.findViewById(R.id.dialog_se_episode_button_next);
        episodeButtonPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer val = Integer.parseInt(episodeButton.getText().toString());
                episodeButton.setText(String.valueOf(val + 1));
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(s.getName());
        builder.setView(dialogView);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Integer season = Integer.parseInt(seasonButton.getText().toString());
                Integer episode = Integer.parseInt(episodeButton.getText().toString());

                s.setSeason(season);
                s.setEpisode(episode);
                ShowStore.overRide(position, s);

                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity().getApplicationContext(), "Successfully changed.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
    }

    private void deleteDialog(final int position) {
        Show s = ShowStore.getShowByIndex(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete " + s.getName() + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                ShowStore.delete(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(getActivity().getApplicationContext(), "Successfully deleted.", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        builder.create().show();
    }

    private class ShowAdapter extends ArrayAdapter<Show> {

        public ShowAdapter(ArrayList<Show> s) {
            super(getActivity(), 0, s);
        }

        public void onListItemClick(ListView l, View v, int position, long id) {
            viewDialog(position);
        }
    }
}
