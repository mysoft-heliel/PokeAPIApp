package br.com.opet.tds.pokeapiapp.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import br.com.opet.tds.pokeapiapp.Model.Pokemon;
import br.com.opet.tds.pokeapiapp.R;

public class MainActivity extends Activity {

    private static final String URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final String URL_SPRITE = "http://pokeapi.co/media/sprites/pokemon/";
    private RequestQueue queue;
    private Gson gson;

    private TextView textID;
    private TextView textName;
    private TextView textHeight;
    private TextView textWeight;
    private TextView textTypes;
    private EditText textBusca;
    private ImageView imvImagem;


    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textID = findViewById(R.id.textID);
        textBusca = findViewById(R.id.textBusca);
        textName = findViewById(R.id.textName);
        textHeight = findViewById(R.id.textHeight);
        textWeight = findViewById(R.id.textWeight);
        textTypes = findViewById(R.id.textTypes);
        progressBar = findViewById(R.id.progressConnection);
        imvImagem = findViewById(R.id.imvImagem);

        GsonBuilder builder = new GsonBuilder();
        gson = builder.create();
        queue = Volley.newRequestQueue(this);
        callPokemon();
    }

    private void callPokemon(){
        progressBar.setVisibility(ProgressBar.VISIBLE);
        String url_busca = URL+textName;
        StringRequest request = new StringRequest(Request.Method.GET,url_busca,onPokemonLoaded,onPokemonError);
        queue.add(request);
    }

    private final Response.Listener<String> onPokemonLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Pokemon pokemon = gson.fromJson(response,Pokemon.class);

            textID.setText(String.valueOf(pokemon.getID()));
            textName.setText(pokemon.getName());
            textHeight.setText(String.valueOf(pokemon.getHeight()));
            textWeight.setText(String.valueOf(pokemon.getWeight()));

            String stypes = "";
            for(Pokemon.Types types : pokemon.getTypes()){
                stypes += types.getType().getName() + " ";
            }

            textTypes.setText(stypes);

            Log.i("POKERESPONSE",response);
            progressBar.setVisibility(ProgressBar.GONE);
            String url_busca_imagem = URL_SPRITE+textName;
            Picasso.get().load(url_busca_imagem).into(imvImagem);

        }
    };

    private final Response.ErrorListener onPokemonError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("POKERESPONSE",error.toString());
            Toast.makeText(MainActivity.this, "Erro ao capturar os dados.", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(ProgressBar.GONE);
        }
    };

    public void BuscaPokemon(View view) {
        callPokemon();
    }
}
