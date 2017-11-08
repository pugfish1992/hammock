package com.pugfish1992.hammock.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.pugfish1992.hammock.R;
import com.pugfish1992.hammock.model.Work;
import com.pugfish1992.hammock.ui.adapter.ProjectAdapter;
import com.pugfish1992.hammock.ui.adapter.WorkAdapterBase;
import com.pugfish1992.hammock.ui.widget.NewWorkDialog;

public class MainActivity extends AppCompatActivity
        implements
        NewWorkDialog.OnCreateNewWorkListener,
        WorkAdapterBase.ItemCardClickListener {

    private RecyclerView mProjectList;
    private ProjectAdapter mProjectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFabClick();
            }
        });

        mProjectAdapter = new ProjectAdapter(Work.getRootWorks(), this, this);
        mProjectList = (RecyclerView) findViewById(R.id.recycler_view);
        mProjectList.setLayoutManager(new LinearLayoutManager(this));
        mProjectList.setAdapter(mProjectAdapter);
    }

    private void onFabClick() {
        NewWorkDialog dialog = new NewWorkDialog();
        dialog.show(getSupportFragmentManager(), null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * INTERFACE IMPL -> NewWorkDialog.OnCreateNewWorkListener
     * ---------- */

    @Override
    public void onNewWorkCreated(@NonNull Work work) {
        mProjectAdapter.add(work);
    }

    /**
     * INTERFACE IMPL -> WorkAdapterBase.ItemCardClickListener
     * ---------- */

    @Override
    public void onItemCardClick(int position) {
        Work project = mProjectAdapter.getAt(position);
        Bundle extras = WorkDetailsActivity.makeExtras(project);
        Intent intent = new Intent(this, WorkDetailsActivity.class);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
