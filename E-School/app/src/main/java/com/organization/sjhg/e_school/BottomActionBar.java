package com.organization.sjhg.e_school;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.organization.sjhg.e_school.ClassSession.SessionLogs;
import com.organization.sjhg.e_school.ClassSession.SyncService;
import com.organization.sjhg.e_school.Helpers.StudentApplicationUserData;
import com.organization.sjhg.e_school.Remote.ExceptionHandler;
import com.organization.sjhg.e_school.Remote.RemoteCallHandler;
import com.organization.sjhg.e_school.Remote.RemoteCalls;
import com.organization.sjhg.e_school.Remote.RemoteHelper;
import com.organization.sjhg.e_school.Remote.ServerAddress;
import com.organization.sjhg.e_school.Structure.ActiveSessions;
import com.organization.sjhg.e_school.Structure.GlobalConstants;
import com.organization.sjhg.e_school.Structure.TeacherSession;
import com.organization.sjhg.e_school.Sync.FileManager;


import com.organization.sjhg.e_school.Sync.NotificationBar;

import com.organization.sjhg.e_school.Sync.SyncManager;
import com.organization.sjhg.e_school.TakeEvents.EventListingActivity;
import com.organization.sjhg.e_school.TakeNotes.AddSmallNotesActivity;
import com.organization.sjhg.e_school.TakeNotes.ChatHeadService;
/*import com.organization.sjhg.e_school.TakeNotes.NoteEditActivity;*/
import com.organization.sjhg.e_school.TakeNotes.NoteListingActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import static android.app.AlarmManager.ELAPSED_REALTIME;
import static android.os.SystemClock.elapsedRealtime;

public class BottomActionBar extends Fragment implements RemoteCallHandler {

    ImageView ConnectBtn;
    ToggleButton RaiseHandBtn;
   // Context context=getActivity();
    String sessionID;


    // Handler to handle the raise hand status change by teacher
    final Handler updateRaiseHandHandler = new Handler() {
        @Override
        public void handleMessage(Message raiseHandStatus) {
            if (raiseHandStatus.what == 0) {
                RaiseHandBtn.setBackground(getResources().getDrawable(R.drawable.raise_hand_text_disabled));
            } else {
                RaiseHandBtn.setBackground(getResources().getDrawable(R.drawable.raise_hand_text_raised));
            }
            super.handleMessage(raiseHandStatus);
        }
    };
    ImageView ChatBtn;
    // Handler to handle the session status change; Handler responsible of updating the UI
    final Handler updateSessionStatusHandler = new Handler() {
        @Override
        public void handleMessage(Message currentSessionStatus) {
            if (currentSessionStatus.what == 0) {
                // Change ConnectBtn image to disconnected.
                //ConnectBtn.setImageResource(0);
                ConnectBtn.setImageResource(R.drawable.sessiondisconnect);
                // Disable RaiseHandBtn and change image to disconnected.
                RaiseHandBtn.setEnabled(false);
               // RaiseHandBtn.setBackground(getResources().getDrawable(R.drawable.rectangle));
                RaiseHandBtn.setVisibility(View.INVISIBLE);
                // Disable ChatBtn and change image to disconnected.
                ChatBtn.setEnabled(false);
                //ChatBtn.setBackgroundResource(R.drawable.test_page);
                ChatBtn.setVisibility(View.INVISIBLE);

               // ChatBtn.setImageResource(0);
                //ChatBtn.setImageResource(R.drawable.rectangle);
                //ChatBtn.setVisibility(View.INVISIBLE);
            } else {
                // Change ConnectBtn image to connected.
                //ConnectBtn.setImageResource(0);
                ConnectBtn.setImageResource(R.drawable.connect_with_teacher_text_connected);
                // Enable RaiseHandBtn and change image to connected.
                RaiseHandBtn.setEnabled(true);
                RaiseHandBtn.setVisibility(View.VISIBLE);
                RaiseHandBtn.setBackground(getResources().getDrawable(R.drawable.raise_hand_text_disabled));
                // Enable ChatBtn and change image to connected.
                ChatBtn.setEnabled(true);
                ChatBtn.setVisibility(View.VISIBLE);
                //ChatBtn.setImageResource(0);
                ChatBtn.setImageResource(R.drawable.chat_text_disabled);
            }
            super.handleMessage(currentSessionStatus);
        }
    };
    ImageView WriteNotesBtn;
    ImageView SyncBtn;
    ImageView SettingsBtn;
    ImageView DiaryBtn;
    BroadcastReceiver batteryLevelReceiver;
    AlertDialog askQuestionDialog;

    public BottomActionBar() {
        // Required empty public constructor
    }
   public void hideSoftKeyboard(Activity activity)  {
       try {
           InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
           inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
       }catch (Exception e)
       {
          // Log.e("error",""+e.toString());
           e.printStackTrace();
       }
    }
    public  void startSync() {

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getActivity(), SyncService.class);
        intent.setAction(SyncService.SYNC);

        PendingIntent pi = SplashActivity.getSyncPendingIntent(intent,getActivity());

        alarmManager.set(ELAPSED_REALTIME, elapsedRealtime() + 1000l, pi);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View fragmentBaseView = inflater.inflate(R.layout.fragment_bottom_action_bar, container, false);

        fragmentBaseView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v!=null&& (!(v instanceof EditText)))
                {
                    hideSoftKeyboard(getActivity());
                    return false;
                }
                return true;
            }
        });

        ConnectBtn = (ImageView) fragmentBaseView.findViewById(R.id.connectwithteacher);
        ConnectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                ConnectBtn.setEnabled(false);
                // If student is connected to session then disconnect on click and return.
                if (StudentApplicationUserData.getInstance(getActivity()).isConnectedToSession()) {
                    SessionLogs.LogEntry(getActivity(), this.getClass().getName(), Level.INFO,
                            "Calling for: Disconnect Class Session");
                    StudentApplicationUserData.getInstance(getActivity()).disconnectSession();
                    ConnectBtn.setEnabled(true);

                    return;
                }



                // Make the remote call asynchronously; HandleRemoteCall function will be called once the call completes
                // Make sure to enable the Connect button in the remote call handler
                SessionLogs.LogEntry(getActivity(), this.getClass().getName(), Level.INFO,
                        "Calling for: Display Active Sessions");
                new RemoteHelper(getActivity()).getActiveSessions(BottomActionBar.this, RemoteCalls.GET_ACTIVE_SESSIONS);
            }
        });

        RaiseHandBtn = (ToggleButton) fragmentBaseView.findViewById(R.id.raisehand);
        RaiseHandBtn.setEnabled(false);
        RaiseHandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionLogs.LogEntry(getActivity(), this.getClass().getName(), Level.INFO,
                        "Calling for: Get Raise Hand Status");
                boolean raiseHandValue = StudentApplicationUserData.getInstance(getActivity()).isHandRaised();
                StudentApplicationUserData.getInstance(getActivity()).updateRaiseHandStatus(!raiseHandValue);

            }
        });

        ChatBtn = (ImageView) fragmentBaseView.findViewById(R.id.chat);
        ChatBtn.setEnabled(false);
        ChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionLogs.LogEntry(getActivity(), this.getClass().getName(), Level.INFO,
                        "Calling for: Ask Question");
                askQuestionDialog = AskQuestion(getActivity());
                askQuestionDialog.show();
            }
        });
       // final NoteEditActivity  notesFragment = new NoteEditActivity();
     /*  final View addnote= getActivity().findViewById(R.id.addnotebutton);
       */

        WriteNotesBtn = (ImageView) fragmentBaseView.findViewById(R.id.writenotes);
        WriteNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //commented code is to add note small app and for fragament intraction between two fragmnets which is currently not in used
               /* View vi=(View) getActivity().findViewById(R.id.notesEdit);
                if(vi.getVisibility()==View.VISIBLE)
                {
                    Fragment fragment=getFragmentManager().findFragmentById(R.id.notesfragment);
                    getFragmentManager().beginTransaction().detach(fragment).commit();
                    vi.setVisibility(View.INVISIBLE);
                }
                else {
                    getFragmentManager().beginTransaction().replace(R.id.notesfragment, new NoteEditActivity()).commit();
                    vi.setVisibility(View.VISIBLE);
                }*/
                //create notes chat head here




                View vi=(View) getActivity().findViewById(R.id.notesfragment);
                View addnote= getActivity().findViewById(R.id.addnotebutton);
                addnote.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getActivity(), AddSmallNotesActivity.class);
                        startActivity(intent);
                    }
                });



                if(vi.getVisibility()==View.VISIBLE)
                {
                    Fragment fragment=getFragmentManager().findFragmentById(R.id.notesfragment);
                    getFragmentManager().beginTransaction().detach(fragment).commit();
                    vi.setVisibility(View.INVISIBLE);

                   addnote.setVisibility(View.INVISIBLE);
                }
                else {
                    getFragmentManager().beginTransaction().replace(R.id.notesfragment, new NoteListingActivity()).commit();
                    vi.setVisibility(View.VISIBLE);

                   addnote.setVisibility(View.VISIBLE);
                }
               /* View vi=(View) getActivity().findViewById(R.id.notesEdit);
                //View viSidebar=(View)getActivity().findViewById(R.id.notesSidebar);

                if(vi.getVisibility()==View.VISIBLE)
                {
                    //Fragment fragment=getFragmentManager().findFragmentById(R.id.notesfragment);
                    //getFragmentManager().beginTransaction().remove(fragment).commit();
                    vi.setVisibility(View.INVISIBLE);

                  //  viSidebar.setVisibility(View.INVISIBLE);
                }
                else {
                    Fragment f=getFragmentManager().findFragmentById(R.id.notesfragment);
                    if(f==null) {
                        //if(Fragment f = mFragmentManager.findFragmentById(R.id.notesfragment);)
                        //if(getFragmentManager().findFr)
                        getFragmentManager().beginTransaction()
                                .add(R.id.notesfragment, notesFragment)
                                .commit();
                    }
                }
                    // NoteListingActivity nla=new NoteListingActivity(context);

                    vi.setVisibility(View.VISIBLE);
                   // viSidebar.setVisibility(View.VISIBLE);*/



            }
        });

        SyncBtn = (ImageView) fragmentBaseView.findViewById(R.id.sync);
        SyncBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (ServerAddress.isConnectedToInternet(getActivity())) {
                    Intent mainToFileManager = new Intent(getActivity(), FileManager.class);
                    startActivity(mainToFileManager);
                } else {
                    ExceptionHandler.thrownExceptions(
                            getActivity(),
                            getResources().getString(R.string.network_error),
                            getResources().getString(R.string.error_message_internet));
                    }

            }
        });
        DiaryBtn=(ImageView)fragmentBaseView.findViewById(R.id.diaryentry);
        DiaryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainToNoteListing = new Intent(getActivity(), EventListingActivity.class);
                startActivity(mainToNoteListing);
            }
        });





        SettingsBtn = (ImageView) fragmentBaseView.findViewById(R.id.settings);
        SettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new RemoteHelper(getActivity()).getSubscriptionSubjects(BottomActionBar.this,
                        RemoteCalls.GET_SUBSCRIPTION_SUBJECT_LIST);
                    Intent setting = new Intent(getActivity(), SettingsFragment.class);
                    startActivity(setting);
            }
        });

        getBatteryPercentage();

        return fragmentBaseView;
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Register the UI handlers to the UserData class so that it can control the session UI
        StudentApplicationUserData.getInstance(getActivity()).register(updateRaiseHandHandler, updateSessionStatusHandler);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().unregisterReceiver(batteryLevelReceiver);
        // Unregister the UI handlers
        StudentApplicationUserData.getInstance(getActivity()).unregister();
    }



    private AlertDialog BuildSessionListDialog(final Context ctx, final String[] options, final String[] sessionIDs)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper( ctx, android.R.style.Theme_Holo_Light_Dialog));

        // Set the dialog title
        builder.setTitle("Choose the class you are in...")
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(options, -1,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                            }
                        })
                .setCancelable(false)
                        // Set the action buttons
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                sessionID = sessionIDs[((AlertDialog) dialog).getListView().getCheckedItemPosition()];
                                SessionLogs.LogEntry(getActivity(), this.getClass().getName(), Level.INFO,
                                        "Calling for: Connect Student Session: " + sessionID);
                                StudentApplicationUserData.getInstance(ctx).updateSessionStatus(true, sessionID);
                                String op = options[((AlertDialog) dialog).getListView().getCheckedItemPosition()];
                                Toast.makeText(getActivity().getApplicationContext(), "Connected to "+op , Toast.LENGTH_LONG).show();
                                dialog.cancel();
                                ConnectBtn.setEnabled(true);
                                startSync();
                            }
                        }

                )
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                               // StudentApplicationUserData.getInstance(getActivity()).disconnectSession();
                                //ConnectBtn.setEnabled(true);
                                dialog.cancel();
                                ConnectBtn.setEnabled(true);

                            }
                        }

                );

        return builder.create();
    }

    private AlertDialog AskQuestion(final Context ctx) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper( ctx, android.R.style.Theme_Holo_Light_Dialog));

        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setView(inflater.inflate(R.layout.dialog_askquestion, null));

        // Set the dialog title
        builder.setTitle("Ask a question")

                // Set the action buttons
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        EditText askQuestion = (EditText) ((AlertDialog) dialog).findViewById(R.id.question);
                        SessionLogs.LogEntry(getActivity(), this.getClass().getName(), Level.INFO,
                                "Ask Question Status: Sending question to teacher: " + askQuestion.getText().toString());
                        new RemoteHelper(ctx).sendStudentQuestion(BottomActionBar.this, RemoteCalls.SEND_STUDENT_QUESTION, askQuestion.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        return builder.create();
    }

    private void DisplayActiveSessions(Context ctx, List<TeacherSession> activeSessions) {
        // If student is not connected to session and session list is empty then show alert box and return.
        if (activeSessions == null || activeSessions.size() <= 0) {
            SessionLogs.LogEntry(getActivity(), this.getClass().getName(), Level.INFO,
                    "Active Sessions: No Active Sessions");
            AlertDialog.Builder noSession = new AlertDialog.Builder(new ContextThemeWrapper( getActivity(), android.R.style.Theme_Holo_Light_Dialog));
            noSession.setMessage(R.string.no_session_available_message);
            noSession.setCancelable(false);
            noSession.setNegativeButton("OK", null).create().show();
            ConnectBtn.setEnabled(true);
            return;
        }

        // If student is not connected to session and active sessions are present then show session list alert box.
        SessionLogs.LogEntry(getActivity(), this.getClass().getName(), Level.INFO,
                "Active Sessions: Displaying Active Sessions: " + activeSessions.toString());
        int noOfItems = activeSessions.size();
        String[] SessionDetailsToDisplay = new String[noOfItems];
        String[] SessionIDs = new String[noOfItems];
        int iterator = 0;

        for (TeacherSession session : activeSessions) {
            SessionDetailsToDisplay[iterator] = session.classDisplayString + " " +
                    session.subjectName + " by " + session.teacherName;
            SessionIDs[iterator] = session.sessionId;
            iterator++;
        }
        AlertDialog dialog = BuildSessionListDialog(ctx, SessionDetailsToDisplay, SessionIDs);
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
    }

    // This registers to listen to Battery status change; and updates the server on battery status change.
    private void getBatteryPercentage() {
        batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level = -1;
                if (currentLevel >= 0 && scale > 0) {
                    level = (currentLevel * 100) / scale;
                }
//                SessionLogs.LogEntry(this.getClass().getName(), Level.INFO,
//                        "Battery Status: Setting Battery Status: " + level);
                StudentApplicationUserData.getInstance(getActivity()).setBatteryStatus(level);
                // new RemoteHelper(ctx).SendBatteryStatus(BottomActionBar.this, RemoteCalls.SEND_BATTERY_STATUS, level);
            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        getActivity().registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

    public void HandleRemoteCall(boolean isSuccessful, RemoteCalls callFor, JSONObject response, Exception exception) {
        switch (callFor) {
            case GET_ACTIVE_SESSIONS:

                if (!isSuccessful) {
                    Toast.makeText(getActivity(),
                            R.string.network_error,
                            Toast.LENGTH_SHORT)
                            .show();
                    SessionLogs.LogEntry(getActivity(), this.getClass().getName(), Level.SEVERE,
                            "Remote Call failed: " + callFor.toString() + " " + exception.getMessage());
                }
                try {
                    DisplayActiveSessions(getActivity(), ActiveSessions.getSessionList(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),
                            R.string.basic_error,
                            Toast.LENGTH_LONG)
                            .show();
                    SessionLogs.LogEntry(getActivity(), this.getClass().getName(), Level.SEVERE,
                            "Session Connection: Error Connecting to the session");
                }
                ConnectBtn.setEnabled(true);

                break;
            case SEND_STUDENT_QUESTION:
                if (!isSuccessful) {
                    SessionLogs.LogEntry(getActivity(), this.getClass().getName(), Level.SEVERE,
                            "Remote Call failed: " + callFor.toString() + " " + exception.getMessage());
                    Toast.makeText(
                            getActivity(),
                            R.string.basic_error,
                            Toast.LENGTH_LONG)
                            .show();
                } else {
                    SessionLogs.LogEntry(getActivity(), this.getClass().getName(), Level.INFO,
                            "Ask Question Status: Question sent to teacher");
                    askQuestionDialog.cancel();
                }
                return;
            case GET_SUBSCRIPTION_SUBJECT_LIST:
                SettingsFragment.UpdateSubscriptionList(response);
                break;
            default:
                SessionLogs.LogEntry(getActivity(), this.getClass().getName(), Level.SEVERE,
                        "Remote Call failed: invalid remote call." );
                break;
        }
    }

}