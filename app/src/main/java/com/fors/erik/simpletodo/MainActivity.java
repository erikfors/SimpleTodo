package com.fors.erik.simpletodo;

import android.content.Intent;
import android.os.FileUtils;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_ITEM_TEXT = "item_text";
    public static final String KEY_ITEM_POSITION = "item_position";
    public static final int EDIT_TEXT_CODE = 20;

    List<String> itemsList;

    Button btnAdd;
    EditText etxtItem;
    RecyclerView rvItems;
    ItemsAdapter ItemsAdapter;

    //handle the result of the edit activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK && requestCode == EDIT_TEXT_CODE){
            //Retrieve updated tes value
            String editText = data.getStringExtra(KEY_ITEM_TEXT);
            //extract position of edited item
            int position = data.getExtras().getInt(KEY_ITEM_POSITION);

            //update the model at the right position with new item text
            itemsList.set(position,editText);
            //notify the adapter
            ItemsAdapter.notifyItemChanged(position);
            //persist the chanes
            saveItems();
            Toast.makeText(getApplicationContext(),"Item updated successfully!", Toast.LENGTH_SHORT).show();
        }
        else {
            Log.w("MainActivity", "Unknown call to onActivityResult");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = findViewById(R.id.btnAdd);
        etxtItem = findViewById(R.id.etxtEditItem);
        rvItems = findViewById(R.id.rvItems);

   loadItems();

      ItemsAdapter.OnLongClickListener onLongClickListener =  new ItemsAdapter.OnLongClickListener(){

            @Override
            public void onItemLongClicked(int position) {
                //delete the item from the model
                itemsList.remove(position);
                //notify the adapter
                ItemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(),"Item was removed!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        };

      ItemsAdapter.OnClickListener onClickListener = new ItemsAdapter.OnClickListener() {
          @Override
          public void onItemClicked(int position) {
              Log.d("MainActivity","Single click at position "+ position);
              //create new activity
              Intent i = new Intent(MainActivity.this, EditActivity.class);
              //pass the data being edited
              i.putExtra(KEY_ITEM_TEXT,itemsList.get(position));
              i.putExtra(KEY_ITEM_POSITION,position);
              //display the activity
              startActivityForResult(i,EDIT_TEXT_CODE);
          }
      };

       ItemsAdapter = new ItemsAdapter(itemsList, onLongClickListener,onClickListener);
        rvItems.setAdapter(ItemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoString = etxtItem.getText().toString();
                //add item to list
                itemsList.add(todoString);
                //notify adapter
                ItemsAdapter.notifyItemInserted(itemsList.size() - 1);
                etxtItem.setText("");
                Toast.makeText(getApplicationContext(),"Item was added!", Toast.LENGTH_SHORT).show();
                saveItems();
            }
        });

    }

    private File getDataFile(){
        return new File(getFilesDir(), "data.txt");
    }

    //this function will load items by reading every line of the data
    private void loadItems(){
        try {

            itemsList = new ArrayList<>(org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        }
        catch (IOException e){
            Log.e("MainActivity", "Error reading items", e);
            itemsList = new ArrayList<>();
        }
    }
    // this function saves items by writing them into the data file
    private void saveItems(){
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), itemsList);
        }
        catch (IOException e){
            Log.e("MainActivity", "Error writing items", e);
        }
    }
}
