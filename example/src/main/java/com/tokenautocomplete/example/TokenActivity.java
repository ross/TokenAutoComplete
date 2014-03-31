package com.tokenautocomplete.example;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

import java.util.List;

public class TokenActivity extends Activity implements TokenCompleteTextView.TokenListener {

    private static final class ContactsAdapter extends SimpleCursorAdapter {

        private static final String[] PROJECTION = new String[] {
                ContactsContract.CommonDataKinds.Phone._ID,
                ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY :
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
        };
        private static final String[] FROM = new String[] {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY :
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        private static final int[] TO = new int[] {android.R.id.text1, android.R.id.text2};
        private static final String ORDERING =
                ContactsContract.CommonDataKinds.Phone.TIMES_CONTACTED + " DESC, " +
                        (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY :
                                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME) +
                        " COLLATE LOCALIZED ASC";

        public ContactsAdapter(final Context context) {
            super(context, R.layout.person_layout, null, FROM, TO, 0);

            setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence charSequence) {
                    //noinspection ConstantConditions
                    Uri uri = charSequence == null || charSequence.length() == 0 ?
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI :
                            Uri.withAppendedPath(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
                                    charSequence.toString());
                    assert uri != null;
                    return context.getContentResolver().query(uri, PROJECTION, null, null,
                            ORDERING);
                }
            });
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            final View view = super.getView(position, convertView, parent);

            Person person = Person.get((Cursor)getItem(position));

            ((TextView)view.findViewById(R.id.name)).setText(person.getName());
            ((TextView)view.findViewById(R.id.email)).setText(person.getNumber());

            return view;
        }
    }

    private ContactsCompletionView completionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ContactsAdapter adapter = new ContactsAdapter(this);

        completionView = (ContactsCompletionView)findViewById(R.id.searchView);
        completionView.setAdapter(adapter);
        completionView.setTokenListener(this);

        Button removeButton = (Button)findViewById(R.id.removeButton);
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Object> objects = completionView.getObjects();
                if (objects.size() > 0) {
                    completionView.removeObject(objects.get(objects.size() - 1));
                }
            }
        });
    }

    private void updateTokenConfirmation() {
        StringBuilder sb = new StringBuilder("Current tokens:\n");
        for (Object token: completionView.getObjects()) {
            sb.append(token.toString());
            sb.append("\n");
        }

        ((TextView)findViewById(R.id.tokens)).setText(sb);
    }


    @Override
    public void onTokenAdded(Object token) {
        ((TextView)findViewById(R.id.lastEvent)).setText("Added: " + token);
        updateTokenConfirmation();
        System.out.println();
    }

    @Override
    public void onTokenRemoved(Object token) {
        ((TextView)findViewById(R.id.lastEvent)).setText("Removed: " + token);
        updateTokenConfirmation();
    }
}
