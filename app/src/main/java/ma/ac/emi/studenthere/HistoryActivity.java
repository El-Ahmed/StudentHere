package ma.ac.emi.studenthere;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Construct the data source
        ArrayList<History> arrayOfHistories = new ArrayList<History>();
        arrayOfHistories.add(new History("Technologies du mobile","present", "12/31/2022", "Bah"));
        arrayOfHistories.add(new History("Réseaux NGN","absent", "12/30/2022", "Bah"));
        arrayOfHistories.add(new History("Base de données","present", "12/31/2022", "Bah"));
        arrayOfHistories.add(new History("Technologies du mobile","present", "12/31/2022", "Bah"));
        arrayOfHistories.add(new History("Technologies du mobile","present", "12/31/2022", "Bah"));
        arrayOfHistories.add(new History("Technologies du mobile","present", "12/31/2022", "Bah"));


        // Create the adapter to convert the array to views
        HistoryAdapter adapter = new HistoryAdapter(this, arrayOfHistories);

        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

    }
}