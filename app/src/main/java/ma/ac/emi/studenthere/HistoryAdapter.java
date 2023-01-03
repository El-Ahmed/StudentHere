package ma.ac.emi.studenthere;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class HistoryAdapter extends ArrayAdapter<History> {

    public HistoryAdapter(@NonNull Context context, ArrayList<History> histories) {
        super(context, 0, histories);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        History history = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.history_item, parent, false);
        }
        // Lookup view for data population
        TextView courseName = (TextView) convertView.findViewById(R.id.courseName);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView presence = (TextView) convertView.findViewById(R.id.presence);
        TextView teacherName = (TextView) convertView.findViewById(R.id.teacherName);

        // Populate the data into the template view using the data object
        courseName.setText(history.getCourseName());
        date.setText(history.getDate().toLocaleString());
        presence.setText(history.getPresence()?"Present":"Absent");
        teacherName.setText(history.getTeacherName());

        // Return the completed view to render on screen
        return convertView;
    }
}

