package info.androidhive.volleyexamples;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.volleyexamples.app.AppController;
import info.androidhive.volleyexamples.obj.Contact;
import info.androidhive.volleyexamples.obj.Phone;
import info.androidhive.volleyexamples.volley.utils.Const;


public class ContactsRequestActivity extends Activity {


    private String TAG = JsonRequestActivity.class.getSimpleName();
    private ProgressDialog pDialog;

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";


    // URL to get contacts JSON
    private static String url = "http://api.androidhive.info/contacts/";

    // JSON Node names
    private static final String TAG_CONTACTS = "contacts";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_ADDRESS = "address";
    private static final String TAG_GENDER = "gender";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_PHONE_MOBILE = "mobile";
    private static final String TAG_PHONE_HOME = "home";
    private static final String TAG_PHONE_OFFICE = "office";

    // contacts JSONArray
    JSONArray contacts = null;

    // Hashmap for ListView
   // ArrayList<HashMap<String, String>> contactList;
    List<Contact>contactsList;

    RecyclerView mContactList;

    LinearLayoutManager llm;

    ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_request);
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);

        mContactList = (RecyclerView)findViewById(R.id.rvContactList);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mContactList.setLayoutManager(llm);


        contactsList = new ArrayList<Contact>();
        makeJsonObjReq();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts_request, menu);
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

    private void showProgressDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideProgressDialog() {
        if (pDialog.isShowing())
            pDialog.hide();
    }

    /**
     * Making json object request
     * */
    private void makeJsonObjReq() {
        showProgressDialog();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                Const.URL_CONTACTS, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        VolleyLog.d(response.toString(), null);
                        //msgResponse.setText(response.toString());

                        JSONObject jsonObj = response;
                        try {
                            // Getting JSON Array node
                            contacts = jsonObj.getJSONArray(TAG_CONTACTS);

                            // looping through All Contacts
                            for (int i = 0; i < contacts.length(); i++) {
                                JSONObject c = contacts.getJSONObject(i);
                                Contact contact = new Contact();
                                contact.setId(c.getString(TAG_ID));
                                contact.setName(c.getString(TAG_NAME));
                                contact.setEmail(c.getString(TAG_EMAIL));
                                contact.setAddress(c.getString(TAG_ADDRESS));
                                contact.setGender(c.getString(TAG_GENDER));

                                // Phone node is JSON Object
                                JSONObject phone = c.getJSONObject(TAG_PHONE);
                                Phone phone1 = new Phone();
                                phone1.setMobile(phone.getString(TAG_PHONE_MOBILE));
                                phone1.setHome(phone.getString(TAG_PHONE_HOME));
                                phone1.setOffice(phone.getString(TAG_PHONE_OFFICE));

                                contact.setPhone(phone1);

                                // tmp hashmap for single contact
                                //HashMap<String, String> contact = new HashMap<String, String>();

                                // adding each child node to HashMap key => value
                                /*contact.put(TAG_ID, id);
                                contact.put(TAG_NAME, name);
                                contact.put(TAG_EMAIL, email);
                                contact.put(TAG_PHONE_MOBILE, mobile);*/
                                //Contact contact = new Contact();

                                // adding contact to contact list

                                contactsList.add(contact);

                            }
                            contactAdapter = new ContactAdapter(contactsList);
                            mContactList.setAdapter(contactAdapter);
                            pDialog.hide();
                        } catch (JSONException e) {
                            e.printStackTrace();

                            hideProgressDialog();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hideProgressDialog();
            }
        }) {

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json");
                return headers;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq,
                tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

    public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ButtonViewHolder> {

        private List<Contact> pinList;

        public ContactAdapter(List<Contact> pinList) {
            this.pinList = pinList;
        }

        @Override
        public int getItemCount() {
            return pinList.size();
        }

        @Override
        public void onBindViewHolder(final ButtonViewHolder pinViewHolder,int i) {
            final Contact cd = pinList.get(i);
            //SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());

            //Log.d(PREFS_NAME, "onBindView: " + i);

            //pinViewHolder.vCustomButton.setText(cd.get_name());

            pinViewHolder.vNameText.setText(cd.getName());
            pinViewHolder.vIdText.setText(cd.getId());
            pinViewHolder.vAddressText.setText(cd.getAddress());
            pinViewHolder.vMobilePhoneText.setText(cd.getPhone().getMobile());
            pinViewHolder.vHomePhoneText.setText(cd.getPhone().getHome());
            pinViewHolder.vOfficeText.setText(cd.getPhone().getOffice());



        }

        @Override
        public ButtonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            View itemView;

            itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.card_contact, viewGroup, false);



            return new ButtonViewHolder(itemView);
        }

        public class ButtonViewHolder extends RecyclerView.ViewHolder {

            protected TextView vNameText;
            protected TextView vIdText;
            protected TextView vAddressText;
            protected TextView vMobilePhoneText;
            protected TextView vHomePhoneText;
            protected TextView vOfficeText;


            public ButtonViewHolder(View v) {
                super(v);
                vNameText = (TextView)v.findViewById(R.id.nameText);
                vIdText = (TextView)v.findViewById(R.id.idText);
                vAddressText = (TextView)v.findViewById(R.id.addressText);
                vMobilePhoneText = (TextView)v.findViewById(R.id.mobilePhoneText);
                vHomePhoneText = (TextView)v.findViewById(R.id.homePhoneText);
                vOfficeText = (TextView)v.findViewById(R.id.officePhoneText);




            }
        }

    }


}
