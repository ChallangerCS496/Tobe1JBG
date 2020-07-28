package com.example.myapplication.register;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatDialogFragment;
import com.example.myapplication.R;
import org.json.JSONException;
public class RegisterDialog extends AppCompatDialogFragment {
    private EditText Name, Nickname;
    private RegisterDialogListener listener;
    private Button registerButton;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.register_dialog, null);
        builder.setView(view)
                .setTitle("Register");
        Name = view.findViewById(R.id.enter_name);
        Nickname = view.findViewById(R.id.enter_nickname);
        registerButton = view.findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = Name.getText().toString();
                String nick = Nickname.getText().toString();
                try {
                    listener.register(name, nick);
                } catch (JSONException e) {
                    Log.e("RegisterDialog", Log.getStackTraceString(e));
                }
            }
        });
        return builder.create();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (RegisterDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement RegisterDialogListener!");
        }
    }
    public interface RegisterDialogListener {
        void register(String phoneNumber, String nickname) throws JSONException;
    }
}