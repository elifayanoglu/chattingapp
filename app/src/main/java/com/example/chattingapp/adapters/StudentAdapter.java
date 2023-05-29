package com.example.chattingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattingapp.R;
import com.example.chattingapp.models.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    private List<User> studentList;
    private OnRequestClickListener requestClickListener;
    private FirebaseFirestore firestore;

    public StudentAdapter(List<User> studentList) {
        this.studentList = studentList;
        firestore = FirebaseFirestore.getInstance();
    }

    public void setOnRequestClickListener(OnRequestClickListener listener) {
        this.requestClickListener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ViewHolder'ı oluşturma işlemleri
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_user, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        // Verileri ViewHolder'a bağlama işlemleri
        User student = studentList.get(position);
        holder.bind(student);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public interface OnRequestClickListener {
        void onRequestClick(User student);
    }

    public class StudentViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private Button requestButton;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            requestButton = itemView.findViewById(R.id.requestButton);

            requestButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && requestClickListener != null) {
                        User student = studentList.get(position);
                        requestClickListener.onRequestClick(student);
                    }
                }
            });
        }

        public void bind(User student) {
            nameTextView.setText(student.getName());
        }
    }
}

