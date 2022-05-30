package ro.pub.cs.systems.pdsd.practicaltest02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText server_port_edit_text = null;
    private Button server_connect_button = null;

    private EditText client_address_edit_text = null;
    private EditText client_port_edit_text = null;
    private EditText word_edit_text = null;
    private Button client_connect_button = null;
    private TextView definition_text_view = null;

    private ServerThread serverThread = null;
    private ClientThread clientThread = null;

    private ButtonClickListener buttonClickListener = new ButtonClickListener();

    private class ButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            switch(view.getId())
            {
                case R.id.server_connect_button:
                {
                    String serverPort = server_port_edit_text.getText().toString();

                    if (serverPort == null || serverPort.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    serverThread = new ServerThread(Integer.parseInt(serverPort));

                    if (serverThread.getServerSocket() == null) {
                        Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                        return;
                    }

                    serverThread.start();

                    break;
                }

                case R.id.client_connect_button:
                {
                    String clientAddress = client_address_edit_text.getText().toString();
                    String clientPort = client_port_edit_text.getText().toString();

                    if (clientAddress == null || clientAddress.isEmpty()
                            || clientPort == null || clientPort.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (serverThread == null || !serverThread.isAlive()) {
                        Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String word = word_edit_text.getText().toString();
                    if (word == null || word.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Parameters from client (city / information type) should be filled", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    definition_text_view.setText(Constants.EMPTY_STRING);

                    clientThread = new ClientThread(
                            clientAddress, Integer.parseInt(clientPort), word, definition_text_view
                    );
                    clientThread.start();

                    break;
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        server_port_edit_text = (EditText) findViewById(R.id.server_port_edit_text);
        server_connect_button = (Button) findViewById(R.id.server_connect_button);

        client_address_edit_text = (EditText) findViewById(R.id.client_address_edit_text);
        client_port_edit_text = (EditText) findViewById(R.id.client_port_edit_text);
        word_edit_text = (EditText) findViewById(R.id.word_edit_text);
        client_connect_button = (Button) findViewById(R.id.client_connect_button);
        definition_text_view = (TextView) findViewById(R.id.definition_text_view);

        server_connect_button.setOnClickListener(buttonClickListener);
        client_connect_button.setOnClickListener(buttonClickListener);
    }
}