package com.example.alertapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private CheckBox musicCheckBox, carCheckBox, streetCheckBox, personCheckBox;
    private ImageView swipeImageView;
    private Button verInfoButton;
    private String name = "Nombre por defecto";
    private String subject = "Materia por defecto";
    private String institution = "Institución por defecto";
    private GestureDetectorCompat gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        musicCheckBox = findViewById(R.id.musicCheckBox);
        carCheckBox = findViewById(R.id.carCheckBox);
        streetCheckBox = findViewById(R.id.streetCheckBox);
        personCheckBox = findViewById(R.id.personCheckBox);
        swipeImageView = findViewById(R.id.swipeImageView);
        verInfoButton = findViewById(R.id.verInfoButton);

        verInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoDialog();
            }
        });

        // Implementar el gesto de swipe para salir de la aplicación en el swipeImageView
        gestureDetector = new GestureDetectorCompat(this, new SwipeGestureListener());
        swipeImageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
    }

    // Método para mostrar una imagen según las categorías seleccionadas
    public void showImage(View view) {
        boolean musicChecked = musicCheckBox.isChecked();
        boolean carChecked = carCheckBox.isChecked();
        boolean streetChecked = streetCheckBox.isChecked();
        boolean personChecked = personCheckBox.isChecked();

        if (musicChecked && carChecked) {
            swipeImageView.setImageResource(R.drawable.car_with_music);
        } else if (personChecked && carChecked) {
            swipeImageView.setImageResource(R.drawable.car_with_person);
        } else if (musicChecked && streetChecked) {
            swipeImageView.setImageResource(R.drawable.music_on_street);
        } else if (personChecked && streetChecked) {
            swipeImageView.setImageResource(R.drawable.person_on_street);
        } else if (personChecked && musicChecked) {
            swipeImageView.setImageResource(R.drawable.person_with_music);
        } else if (carChecked && streetChecked) {
            swipeImageView.setImageResource(R.drawable.car_on_street);
        } else {
            // Si no se cumple ninguna de las condiciones, muestra un mensaje de error.
            swipeImageView.setImageResource(0); // Limpiar la imagen
            showErrorDialog("Selecciona al menos dos categorías.");
        }
    }

    // Método para mostrar el cuadro de diálogo de información
    private void showInfoView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Información");

        View view = getLayoutInflater().inflate(R.layout.info_view, null);
        final TextView nameTextView = view.findViewById(R.id.nameTextView);
        final TextView subjectTextView = view.findViewById(R.id.subjectTextView);
        final TextView institutionTextView = view.findViewById(R.id.institutionTextView);

        nameTextView.setText("Nombre: " + name);
        subjectTextView.setText("Materia: " + subject);
        institutionTextView.setText("Institución: " + institution);

        builder.setView(view);

        builder.setPositiveButton("Cerrar", null);

        builder.show();
    }

    // Método para mostrar el cuadro de diálogo de información
    private void showInfoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Información")
                .setMessage("Nombre: " + name + "\nMateria: " + subject + "\nInstitución: " + institution)
                .setPositiveButton("Editar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showEditDialog();
                    }
                })
                .setNegativeButton("Ver Info", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showInfoView(); // Mostrar la información en la vista
                    }
                })
                .show();
    }

    // Método para mostrar el cuadro de diálogo de edición
    private void showEditDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Información");

        View view = getLayoutInflater().inflate(R.layout.edit_dialog, null);
        final EditText nameEditText = view.findViewById(R.id.nameEditText);
        final EditText subjectEditText = view.findViewById(R.id.subjectEditText);
        final EditText institutionEditText = view.findViewById(R.id.institutionEditText);

        nameEditText.setText(name);
        subjectEditText.setText(subject);
        institutionEditText.setText(institution);

        builder.setView(view);

        builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name = nameEditText.getText().toString();
                subject = subjectEditText.getText().toString();
                institution = institutionEditText.getText().toString();
                showInfoDialog(); // Actualiza la información mostrada
            }
        });

        builder.setNegativeButton("Cancelar", null);

        builder.show();
    }

    // Método para mostrar un cuadro de diálogo de error
    private void showErrorDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    // Clase interna para detectar el gesto de swipe
    private class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getY() > e2.getY()) {
                showExitDialog();
            }
            return true;
        }
    }

    // Método para mostrar un cuadro de diálogo de confirmación de salida
    private void showExitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Salir de la aplicación?");
        builder.setMessage("¿Estás seguro de que quieres salir de la aplicación?");
        builder.setPositiveButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish(); // Cierra la aplicación
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Cierra el cuadro de diálogo
            }
        });
        builder.show();
    }
}
