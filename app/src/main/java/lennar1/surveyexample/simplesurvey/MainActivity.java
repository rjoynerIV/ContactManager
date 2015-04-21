package lennar1.surveyexample.simplesurvey;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    private static final int EDIT = 0, DELETE = 1;

    /* Variables */
    EditText txtFName, txtLName, txtEmail, txtPhone, txtZip;
    List<Contact> Contacts = new ArrayList<Contact>();
    ListView contactListView;
    DatabaseHandler dbHandler;

    int longClickedItemIndex;
    ArrayAdapter<Contact> contactAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    /* Text */
        txtFName = (EditText) findViewById(R.id.txtFName);
        txtLName = (EditText) findViewById(R.id.txtLName);
        txtEmail = (EditText) findViewById(R.id.txtEmail);
        txtPhone = (EditText) findViewById(R.id.txtPhone);
        txtZip = (EditText) findViewById(R.id.txtZip);
    /* ListView */
        contactListView = (ListView) findViewById(R.id.listView);
    /* Database Handler */
        dbHandler = new DatabaseHandler(getApplicationContext());
    /*  */
        registerForContextMenu(contactListView);
        contactListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                longClickedItemIndex = position;

                return false;
            }});
    /* Tabhost */
        TabHost tabHost;
        tabHost= (TabHost) findViewById(R.id.tabHost);

        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("creator");
        tabSpec.setContent(R.id.Survey);
        tabSpec.setIndicator("Creator");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("host");
        tabSpec.setContent(R.id.Result);
        tabSpec.setIndicator("Host");
        tabHost.addTab(tabSpec);

    /* Buttons */
        final Button btnRegister = (Button) findViewById(R.id.btnSubmit);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact contact = new Contact(dbHandler.getContactCount(), String.valueOf(txtFName.getText()), String.valueOf(txtLName.getText()), String.valueOf(txtEmail.getText()), String.valueOf(txtPhone.getText()), String.valueOf(txtZip.getText()));
                if (!contactExists(contact)) {
                    dbHandler.createContact(contact);
                    Contacts.add(contact);
                    contactAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), String.valueOf(txtFName.getText()) + " Has Been Registered!", Toast.LENGTH_SHORT).show();
                    return;
                }
                    Toast.makeText(getApplicationContext(), String.valueOf(txtEmail.getText()) + " already exists. Please use a different Email.", Toast.LENGTH_SHORT).show();
            }});

    /* TextWatcher */
        txtFName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnRegister.setEnabled(String.valueOf(txtFName.getText()).trim().isEmpty()); }

            @Override
            public void afterTextChanged(Editable s) {}
        });

    /*  */
        if (dbHandler.getContactCount() != 0)
        Contacts.addAll(dbHandler.getAllContacts());
            populateList();
    }

    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderIcon(R.drawable.pencil_icon);
        menu.setHeaderTitle("Contact Options");
        menu.add(Menu.NONE, EDIT, Menu.NONE, "Edit Contact");
        menu.add(Menu.NONE, DELETE, Menu.NONE, "Delete Contact");
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case EDIT:
                // TODO: Implement editing a contact
                break;
            case DELETE:
                dbHandler.deleteContact(Contacts.get(longClickedItemIndex));
                Contacts.remove(longClickedItemIndex);
                contactAdapter.notifyDataSetChanged();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private boolean contactExists(Contact contact){
        String name = contact.gettxtEmail();
        int contactCount = Contacts.size();

        for (int i = 0; i < contactCount; i++) {
            if (name.compareToIgnoreCase(Contacts.get(i).gettxtEmail()) == 0)
                return true;
        }
        return false;
    }


    private void populateList() {
        contactAdapter = new ContactListAdapter();
        contactListView.setAdapter(contactAdapter);
    }

    /* Add Contact */
    private void addContact(String txtFName, String txtLName, String txtEmail, String txtPhone,String txtZip){

        Contacts.add(new Contact(0, txtFName, txtLName, txtEmail, txtPhone, txtZip));
    }

    private class ContactListAdapter extends ArrayAdapter<Contact> {
        public ContactListAdapter() {
            super(MainActivity.this, R.layout.listviewitem, Contacts);
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null)
                view = getLayoutInflater().inflate(R.layout.listviewitem, parent, false);

            Contact currentContact = Contacts.get(position);

            TextView Fname = (TextView) view.findViewById(R.id.txtFName);
            Fname.setText(currentContact.gettxtFName());

            TextView Lname = (TextView) view.findViewById(R.id.txtLName);
            Lname.setText(currentContact.gettxtLName());

            TextView Email = (TextView) view.findViewById(R.id.txtEmail);
            Email.setText(currentContact.gettxtEmail());

            TextView Phone = (TextView) view.findViewById(R.id.txtPhone);
            Phone.setText(currentContact.gettxtPhone());

            TextView Zip = (TextView) view.findViewById(R.id.txtZip);
            Zip.setText(currentContact.gettxtZip());

            return view;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
