package com.jk.rcp.main.activities.instructor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.jk.rcp.R;
import com.jk.rcp.main.data.model.course.Student;
import com.jk.rcp.main.data.model.user.Users;
import com.otaliastudios.autocomplete.RecyclerViewPresenter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class UserPresenter extends RecyclerViewPresenter<Users> {
    private static final String TAG = "UserPresenter";
    @SuppressWarnings("WeakerAccess")
    protected Adapter adapter;
    private List<Users> all;

    @SuppressWarnings("WeakerAccess")
    public UserPresenter(@NonNull Context context, List<Users> users) {
        super(context);
        this.all = users;
        Log.d("prueba", "contruscvtor");

    }

    public static boolean containsName(Collection<Student> c, String name) {
        for (Student o : c) {
            if (o != null && o.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    @Override
    protected PopupDimensions getPopupDimensions() {
        PopupDimensions dims = new PopupDimensions();
        dims.width = 600;
        dims.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        return dims;
    }

    @NonNull
    @Override
    protected RecyclerView.Adapter instantiateAdapter() {
        adapter = new Adapter();
        return adapter;
    }

    @Override
    protected void onQuery(@Nullable CharSequence query) {
        if (TextUtils.isEmpty(query)) {
            Log.d("prueba", "todo");
            adapter.setData(all);
        } else {
            query = query.toString().toLowerCase();
            List<Users> list = new ArrayList<>();
            for (Users u : all) {
                if (!containsName(CrearCursoActivity.getPersonas(), u.getName())) {
                    Log.d("ad", "adentro");
                    if (u.getName().toLowerCase().contains(query) ||
                            u.getName().toLowerCase().contains(query)) {
                        list.add(u);
                    }
                }
            }
            adapter.setData(list);
//            Log.e("UserPresenter", "found " + list.size() + " users for query " + query);
        }
        adapter.notifyDataSetChanged();
    }

    protected class Adapter extends RecyclerView.Adapter<Adapter.Holder> {

        private List<Users> data;

        @SuppressWarnings("WeakerAccess")
        protected void setData(@Nullable List<Users> data) {
            this.data = data;
        }

        @Override
        public int getItemCount() {
            return (isEmpty()) ? 1 : data.size();
        }

        @NonNull
        @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(getContext()).inflate(R.layout.user, parent, false));
        }

        private boolean isEmpty() {
            return data == null || data.isEmpty();
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onBindViewHolder(@NonNull Holder holder, int position) {
            if (isEmpty()) {
                holder.fullname.setText("Usuario no encontrado");
                holder.root.setOnClickListener(null);
                return;
            }
            final Users user = data.get(position);
            holder.fullname.setText(user.getName());
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchClick(user);
                }
            });
        }

        @SuppressWarnings("WeakerAccess")
        protected class Holder extends RecyclerView.ViewHolder {
            private View root;
            private TextView fullname;

            Holder(View itemView) {
                super(itemView);
                root = itemView;
                fullname = itemView.findViewById(R.id.fullname);
            }
        }
    }
}
