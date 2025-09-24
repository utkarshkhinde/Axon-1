package com.example.axon1;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private LinearLayout membersContainer;
    private LayoutInflater inflater;
    private Random random;
    private List<String> membersList; // Track names

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Init
        membersContainer = findViewById(R.id.membersContainer);
        inflater = LayoutInflater.from(this);
        random = new Random();
        membersList = new ArrayList<>();

        // Add Member button
        ImageView addMemberBtn = findViewById(R.id.addMemberButton);
        addMemberBtn.setOnClickListener(v -> showAddMemberDialog());

        // Leave Group button
        ImageView leaveGroupBtn = findViewById(R.id.leaveGroupButton);
        leaveGroupBtn.setOnClickListener(v -> showRemoveMemberDialog());
    }

    // Show input dialog to enter a member name
    private void showAddMemberDialog() {
        EditText input = new EditText(this);
        input.setHint("Enter member name");

        new AlertDialog.Builder(this)
                .setTitle("Add Member")
                .setView(input)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (!name.isEmpty()) {
                        addMember(name, false);
                        membersList.add(name);
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Add member dynamically
    private void addMember(String name, boolean isAdmin) {
        View itemView = inflater.inflate(R.layout.item_group_member, membersContainer, false);

        TextView memberName = itemView.findViewById(R.id.memberName);
        View avatar = itemView.findViewById(R.id.memberAvatar);
        TextView adminBadge = itemView.findViewById(R.id.adminBadge);

        memberName.setText(name);

        // Random avatar color
        int[] colors = {
                Color.parseColor("#F9A825"), // Yellow
                Color.parseColor("#7986CB"), // Blue
                Color.parseColor("#BDBDBD"), // Gray
                Color.parseColor("#E57373"), // Red
                Color.parseColor("#81C784")  // Green
        };
        int color = colors[random.nextInt(colors.length)];
        GradientDrawable bg = (GradientDrawable) avatar.getBackground();
        bg.setColor(color);

        if (isAdmin) {
            adminBadge.setVisibility(View.VISIBLE);
        }

        // Remove member on long press
        itemView.setOnLongClickListener(v -> {
            membersContainer.removeView(itemView);
            membersList.remove(name);
            Toast.makeText(this, name + " removed", Toast.LENGTH_SHORT).show();
            return true;
        });

        membersContainer.addView(itemView);
    }

    // Show dialog with members to remove
    private void showRemoveMemberDialog() {
        if (membersList.isEmpty()) {
            Toast.makeText(this, "No members to remove", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] membersArray = membersList.toArray(new String[0]);

        new AlertDialog.Builder(this)
                .setTitle("Remove Member")
                .setItems(membersArray, (dialog, which) -> {
                    String selectedName = membersArray[which];
                    removeMemberByName(selectedName);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Remove member by name
    private void removeMemberByName(String name) {
        for (int i = 0; i < membersContainer.getChildCount(); i++) {
            View memberView = membersContainer.getChildAt(i);
            TextView memberName = memberView.findViewById(R.id.memberName);

            if (memberName.getText().toString().equals(name)) {
                membersContainer.removeView(memberView);
                membersList.remove(name);
                Toast.makeText(this, name + " removed", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
