
package com.example.assignment3;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.assignment3.R;




interface AddMemoDialogListener{
    public void applyText(String memo);

}


public class AddMemoDialog extends AppCompatDialogFragment {
    private EditText memo;
    private AddMemoDialogListener listener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try { listener = (AddMemoDialogListener) context;
        }
        catch (ClassCastException e)
        { throw new ClassCastException(context.toString()+ "implement DialogListener");
        }
    }

    @Override

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.addmemo, null);

        builder.setView(view)
                .setTitle("New Note")
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setPositiveButton("SAVE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                String addMemo = memo.getText().toString() ;

                listener.applyText(addMemo);
            }
        });

        memo = view.findViewById(R.id.customDialogAdd);

        return builder.create();
    }
}
