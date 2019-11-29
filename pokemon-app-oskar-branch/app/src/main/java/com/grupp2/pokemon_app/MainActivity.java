package com.grupp2.pokemon_app;

import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.grupp2.pokemon_app.models.Pokemon;
import com.grupp2.pokemon_app.models.PokemonRequest;
import com.grupp2.pokemon_app.pokeapi.PokeapiService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "POKEDEX";

    private Retrofit retrofit;

    private RecyclerView recyclerView;
    private ListaPokemonAdapter listaPokemonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Hej

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        listaPokemonAdapter = new ListaPokemonAdapter(this);
        recyclerView.setAdapter(listaPokemonAdapter);
        recyclerView.setHasFixedSize(true);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(layoutManager);

        retrofit = new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        obtenerDatos();

    }

    private void obtenerDatos() {
        PokeapiService service = retrofit.create(PokeapiService.class);
        final Call<PokemonRequest> pokemonRequestCall = service.obtenerListaPokemon();

        pokemonRequestCall.enqueue(new Callback<PokemonRequest>() {
            @Override
            public void onResponse(Call<PokemonRequest> call, Response<PokemonRequest> response) {
                if (response.isSuccessful()){

                    PokemonRequest pokemonRequest = response.body();
                    ArrayList<Pokemon> listapokemon = pokemonRequest.getResults();

                    listaPokemonAdapter.adicionarListaPokemon(listapokemon);

                } else {

                    Log.e(TAG, "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<PokemonRequest> call, Throwable t) {

                Log.e(TAG, "onFailure: " + t.getMessage());

            }
        });
    }
}
