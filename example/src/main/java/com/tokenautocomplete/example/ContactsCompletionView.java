package com.tokenautocomplete.example;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

/**
 * Sample token completion view for basic contact info
 *
 * Created on 9/12/13.
 * @author mgod
 */
public class ContactsCompletionView extends TokenCompleteTextView {

    public ContactsCompletionView(Context context) {
        super(context);
        init();
    }

    public ContactsCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContactsCompletionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        allowDuplicates(false);
        setTokenClickStyle(TokenClickStyle.Select);
    }

    @Override
    protected View getViewForObject(final Object object) {
        Person p = (Person)object;

        LayoutInflater l = (LayoutInflater)getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout)l.inflate(R.layout.contact_token,
                (ViewGroup)ContactsCompletionView.this.getParent(), false);
        ((TextView)view.findViewById(R.id.name)).setText(p.getNumber());

        return view;
    }

    @Override
    protected Object defaultObject(final String completionText) {
        return Person.get(completionText);
    }

    @Override
    protected TokenImageSpan buildSpanForObject(final Object obj) {
        final Person person;
        if (obj instanceof Person) {
            // it's a defaultObject
            person = (Person)obj;
        } else {
            // it's a Cursor
            final Cursor cursor = (Cursor)obj;
            person = Person.get(cursor);
        }
        return super.buildSpanForObject(person);
    }
}