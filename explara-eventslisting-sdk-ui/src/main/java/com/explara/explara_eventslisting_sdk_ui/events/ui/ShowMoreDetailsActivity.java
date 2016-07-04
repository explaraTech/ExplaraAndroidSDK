package com.explara.explara_eventslisting_sdk_ui.events.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import com.explara.explara_eventslisting_sdk_ui.R;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ShowMoreDetailsActivity extends AppCompatActivity {

    private String longDescription = "";
    private String eventName = "";
    private HtmlTextView textView;
    private static final String LONG_DESCRIPTION = "description";
    private static final String EVENT_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_details);

        try {

            Bundle bundle = getIntent().getExtras();
            longDescription = bundle.getString(LONG_DESCRIPTION);
            eventName = bundle.getString(EVENT_NAME);

        } catch (NullPointerException e) {
        }

        textView = (HtmlTextView) findViewById(R.id.text_description_long);
        textView.setHtmlFromString(longDescription, new HtmlTextView.LocalImageGetter());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextAppearance(this, R.style.textViewBoldFontStyle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_red);
        getSupportActionBar().setTitle("" + Html.fromHtml(eventName));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_content_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
