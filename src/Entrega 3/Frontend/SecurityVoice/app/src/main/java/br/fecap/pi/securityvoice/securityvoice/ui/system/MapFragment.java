package br.fecap.pi.securityvoice.securityvoice.ui.system;

import static com.mapbox.maps.plugin.animation.CameraAnimationsUtils.getCamera;
import static com.mapbox.maps.plugin.gestures.GesturesUtils.addOnMapClickListener;
import static com.mapbox.maps.plugin.gestures.GesturesUtils.getGestures;
import static com.mapbox.maps.plugin.locationcomponent.LocationComponentUtils.getLocationComponent;
import static com.mapbox.navigation.base.extensions.RouteOptionsExtensions.applyDefaultNavigationOptions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.Bearing;
import com.mapbox.api.directions.v5.models.RouteOptions;
import com.mapbox.bindgen.Expected;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.EdgeInsets;
import com.mapbox.maps.MapView;
import com.mapbox.maps.Style;
import com.mapbox.maps.extension.style.layers.properties.generated.TextAnchor;
import com.mapbox.maps.plugin.animation.MapAnimationOptions;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.OnMapClickListener;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentConstants;
import com.mapbox.maps.plugin.locationcomponent.LocationComponentPlugin;
import com.mapbox.maps.plugin.locationcomponent.generated.LocationComponentSettings;
import com.mapbox.navigation.base.options.NavigationOptions;
import com.mapbox.navigation.base.route.NavigationRoute;
import com.mapbox.navigation.base.route.NavigationRouterCallback;
import com.mapbox.navigation.base.route.RouterFailure;
import com.mapbox.navigation.base.route.RouterOrigin;
import com.mapbox.navigation.core.MapboxNavigation;
import com.mapbox.navigation.core.directions.session.RoutesObserver;
import com.mapbox.navigation.core.directions.session.RoutesUpdatedResult;
import com.mapbox.navigation.core.lifecycle.MapboxNavigationApp;
import com.mapbox.navigation.core.trip.session.LocationMatcherResult;
import com.mapbox.navigation.core.trip.session.LocationObserver;
import com.mapbox.navigation.ui.base.util.MapboxNavigationConsumer;
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider;
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi;
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView;
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions;
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineError;
import com.mapbox.navigation.ui.maps.route.line.model.RouteLineResources;
import com.mapbox.navigation.ui.maps.route.line.model.RouteSetValue;
import com.mapbox.search.autocomplete.PlaceAutocomplete;
import com.mapbox.search.autocomplete.PlaceAutocompleteSuggestion;
import com.mapbox.search.ui.adapter.autocomplete.PlaceAutocompleteUiAdapter;
import com.mapbox.search.ui.view.CommonSearchViewConfiguration;
import com.mapbox.search.ui.view.SearchResultsView;

import br.fecap.pi.securityvoice.criptography.Criptography;
import br.fecap.pi.securityvoice.entities.SystemAtributes;
import br.fecap.pi.securityvoice.entities.Travel;
import br.fecap.pi.securityvoice.resources.SpeechRecognizerClass;
import br.fecap.pi.securityvoice.resources.SpeechRecognizerTypeExecution;
import br.fecap.pi.securityvoice.R;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import br.fecap.pi.securityvoice.securityvoice.MainActivity;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.jvm.functions.Function1;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class MapFragment extends Fragment{


    private MapView mapView; // o mapa
    private MaterialButton setRoute; // botão que vai setar a rota
    private FloatingActionButton focusLocationBtn; // botão que vai centralizar na localização do usuário
    private final NavigationLocationProvider navigationLocationProvider = new NavigationLocationProvider(); // O provedor da localização do usuário

    private MapboxRouteLineView routeLineView; // responsável por desenhar a rota no mapa
    private MapboxRouteLineApi routeLineApi; // Processa a rota e manda para o LineView

    private SearchResultsView searchResultsView; // vai exibir as sugestões de locais
    private PlaceAutocompleteUiAdapter placeAutocompleteUiAdapter; // Quem vai gerenciar a pesquisa do usuário
    private TextInputEditText searchET; // o campo de texto
    private boolean ignoreNextQueryUpdate = false; // usado para evitar atualizações duplicadas na busca
    private MapboxNavigation mapboxNavigation; // gerencia a navegação do Mapbox
    boolean focusLocation = true; // Indica se a câmera do mapa deve seguir o usuário

    private final String locationPermission = Manifest.permission.ACCESS_FINE_LOCATION;


    private ConstraintLayout waitingDriver;
    private ConstraintLayout travelAccepted;

    public boolean startAcceptingTravel = false;

    public Handler handler;

    public static boolean checkFragmentActive = false;
    private View mapFragment;
    // Vai ver as mudanças no mapa e atualiza a posição da localização
    private final LocationObserver locationObserver = new LocationObserver() {
        @Override
        public void onNewRawLocation(@NonNull Location location) {

        }

        @Override
        public void onNewLocationMatcherResult(@NonNull LocationMatcherResult locationMatcherResult) {
            Location location = locationMatcherResult.getEnhancedLocation();
            navigationLocationProvider.changePosition(location, locationMatcherResult.getKeyPoints(), null, null);
            if (focusLocation) {
                updateCamera(Point.fromLngLat(location.getLongitude(), location.getLatitude()), (double) location.getBearing());
            }
        }
    };

    // Vai gerenciar as rotas e exibi-las na tela
    private final RoutesObserver routesObserver = new RoutesObserver() {
        @Override
        public void onRoutesChanged(@NonNull RoutesUpdatedResult routesUpdatedResult) {
            routeLineApi.setNavigationRoutes(routesUpdatedResult.getNavigationRoutes(), new MapboxNavigationConsumer<>() {
                @Override
                public void accept(Expected<RouteLineError, RouteSetValue> routeLineErrorRouteSetValueExpected) {
                    mapView.getMapboxMap().getStyle(style -> {
                        // Vai pegar o estilo do mapa e verificar se é valido
                        if (style.isValid()) {
                            // Vai desenhar a rota
                            // routeLineErrorRouteSetValueExpected -> o objeto que usado para desenhar a rota ou mandar um erro
                            routeLineView.renderRouteDrawData(style, routeLineErrorRouteSetValueExpected);
                        }
                    });
                }
            });
        }
    };


    // Vai atualizar a câmera para um certo ponto no mapa
    private void updateCamera(Point point, Double bearing) {
        MapAnimationOptions animationOptions = new MapAnimationOptions.Builder().duration(1500L).build();
        CameraOptions cameraOptions = new CameraOptions.Builder().center(point).zoom(20.0).bearing(bearing).pitch(45.0)
                .padding(new EdgeInsets(1000.0, 0.0, 0.0, 0.0)).build();

        getCamera(mapView).easeTo(cameraOptions, animationOptions);
    }

    // Vai detectar quando o usuário mover o mapa e tirar o foco
    private final OnMoveListener onMoveListener = new OnMoveListener() {
        @Override
        public void onMoveBegin(@NonNull MoveGestureDetector moveGestureDetector) {
            focusLocation = false;
            getGestures(mapView).removeOnMoveListener(this);
            focusLocationBtn.show();
        }

        @Override
        public boolean onMove(@NonNull MoveGestureDetector moveGestureDetector) {
            return false;
        }

        @Override
        public void onMoveEnd(@NonNull MoveGestureDetector moveGestureDetector) {

        }
    };

    // ActivityResultLauncher para solicitar permissões
    private ActivityResultLauncher<String> permissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    Toast.makeText(requireContext(), "Permissão concedida! ", Toast.LENGTH_SHORT).show();
                    startLocationServices();
                } else {
                    // Mostrar mensagem ao usuário
                    Toast.makeText(requireContext(), "Permissão não concedida!", Toast.LENGTH_SHORT).show();
                }
            }
    );

    // Método para verificar e solicitar permissões
    private void checkAndRequestPermissions () {
        // Verificar permissão de notificação para Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        // Verificar permissão de localização
        if (ContextCompat.checkSelfPermission(requireContext(), locationPermission) == PackageManager.PERMISSION_GRANTED) {
            startLocationServices();
        } else {
            // Solicitar apenas a permissão FINE_LOCATION (já inclui COARSE)
            permissionLauncher.launch(locationPermission);
        }
    }
    ;

    // Método para iniciar os serviços de localização
    private void startLocationServices () {
        try {
            if (ContextCompat.checkSelfPermission(requireContext(), locationPermission) == PackageManager.PERMISSION_GRANTED) {
                // Verificar se o GPS está ativado
                LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
                boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (isGpsEnabled) {
                    mapboxNavigation.startTripSession();
                } else {
                    // Pedir para usuário ativar o GPS
                    showGpsSettingsDialog();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Erro ao iniciar serviços de localização", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para mostrar diálogo pedindo para ativar GPS
    private void showGpsSettingsDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("GPS desativado")
                .setMessage("Por favor, ative o GPS para continuar")
                .setPositiveButton("Configurações", (dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapFragment = view;
        TextView dica = view.findViewById(R.id.dica);

        handler = new Handler();

        if (!SystemAtributes.user.getEmergencyCode().equals("NaN") && SystemAtributes.user.getEmergencyCode() != null) {
            SpeechRecognizerClass.startListening(getActivity(), SpeechRecognizerTypeExecution.LISTENING);
            dica.setText("Diga:" + SystemAtributes.user.getEmergencyCode().toString());
        } else {
            dica.setText("Dica: Configure sua Fala de Emergência nas configurações de segurança de SecurityVoice.");
        }

        if(startAcceptingTravel){
            acceptingTravel();
        }

        mapView = view.findViewById(R.id.mapView);
        focusLocationBtn = view.findViewById(R.id.focusLocation);
        setRoute = view.findViewById(R.id.setRoute);

        waitingDriver = view.findViewById(R.id.waitingDriver);
        travelAccepted = view.findViewById(R.id.travelAccepted);
        // Configura e estilizar a linha de rota
        MapboxRouteLineOptions options = new MapboxRouteLineOptions.Builder(requireContext())
                .withRouteLineResources(new RouteLineResources.Builder().build())
                .withRouteLineBelowLayerId(LocationComponentConstants.LOCATION_INDICATOR_LAYER).build();
        routeLineView = new MapboxRouteLineView(options);
        routeLineApi = new MapboxRouteLineApi(options);

        // Configurar o token de acesso do Mapbox
        NavigationOptions navigationOptions = new NavigationOptions.Builder(requireContext()).accessToken(getString(R.string.mapbox_access_token)).build();
        MapboxNavigationApp.setup(navigationOptions);

        // Vai definir quem vai cuidar das rotas e da localização
        mapboxNavigation = new MapboxNavigation(navigationOptions);
        mapboxNavigation.registerRoutesObserver(routesObserver);
        mapboxNavigation.registerLocationObserver(locationObserver);

        PlaceAutocomplete placeAutocomplete = PlaceAutocomplete.create(getString(R.string.mapbox_access_token));
        searchET = view.findViewById(R.id.searchET);

        searchResultsView = view.findViewById(R.id.search_results_view);
        searchResultsView.initialize(new SearchResultsView.Configuration(new CommonSearchViewConfiguration()));
        placeAutocompleteUiAdapter = new PlaceAutocompleteUiAdapter(searchResultsView, placeAutocomplete, LocationEngineProvider.getBestLocationEngine(requireContext()));

        focusLocationBtn.hide(); // Esconde o botão por padrão

        checkAndRequestPermissions();


        setRoute.setOnClickListener(new View.OnClickListener() {
            // Caso o usuário clique sem ter selecionado nenhum ponto no mapa
            @Override
            public void onClick(View view) {
                Toast.makeText(requireContext(), "Busque uma localização", Toast.LENGTH_SHORT).show();
            }
        });

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (ignoreNextQueryUpdate) {
                    ignoreNextQueryUpdate = false;
                } else {
                    placeAutocompleteUiAdapter.search(charSequence.toString(), new Continuation<Unit>() {
                        @NonNull
                        @Override
                        public CoroutineContext getContext() {
                            return EmptyCoroutineContext.INSTANCE;
                        }

                        @Override
                        public void resumeWith(@NonNull Object o) {
                            requireActivity().runOnUiThread(() -> {
                                searchResultsView.setVisibility(View.VISIBLE);
                                setRoute.setVisibility(View.VISIBLE);
                            });
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        LocationComponentPlugin locationComponentPlugin = getLocationComponent(mapView);
        getGestures(mapView).addOnMoveListener(onMoveListener);

        mapView.getMapboxMap().loadStyleUri(Style.DARK, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                mapView.getMapboxMap().setCamera(new CameraOptions.Builder().zoom(20.0).build()); // configura a câmera

                locationComponentPlugin.setEnabled(true); // habilita o localizador do mapa
                locationComponentPlugin.setLocationProvider(navigationLocationProvider); // vincula o localizador ao componente que fornece a localizaçãdo do usuário durante a viagem
                getGestures(mapView).addOnMoveListener(onMoveListener); // adicinar quem vai cuidar da movimentaçao do mapa
                locationComponentPlugin.updateSettings(new Function1<LocationComponentSettings, Unit>() {
                    @Override
                    public Unit invoke(LocationComponentSettings locationComponentSettings) {
                        locationComponentSettings.setEnabled(true);
                        locationComponentSettings.setPulsingEnabled(true);
                        return null;
                    }
                });
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.location_pin); // imagem do marcador
                AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(mapView); // permiti adicionar marcadores no mapa
                PointAnnotationManager pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, mapView); // gerencia a criação e remoção de marcadores no mapa
                addOnMapClickListener(mapView.getMapboxMap(), new OnMapClickListener() {
                    // Executa quando o usuario clicar na tela
                    @Override
                    public boolean onMapClick(@NonNull Point point) {

                        searchResultsView.setVisibility(View.GONE);
                        return true;
                    }
                });
                focusLocationBtn.setOnClickListener(new View.OnClickListener() {
                    // Botão de focar no usuário
                    @Override
                    public void onClick(View view) {
                        focusLocation = true; // Foca na posição atual do usuário
                        getGestures(mapView).addOnMoveListener(onMoveListener); // habilita o movimento do mapa
                        focusLocationBtn.hide(); // esconde o botão
                    }
                });


                placeAutocompleteUiAdapter.addSearchListener(new PlaceAutocompleteUiAdapter.SearchListener() {
                    @Override
                    public void onSuggestionsShown(@NonNull List<PlaceAutocompleteSuggestion> list) {

                    }

                    @Override
                    // Quando a localização for selecionada
                    public void onSuggestionSelected(@NonNull PlaceAutocompleteSuggestion placeAutocompleteSuggestion) {
                        ignoreNextQueryUpdate = true;
                        focusLocation = false; // desabilita o foco no usuário

                        searchET.setText(placeAutocompleteSuggestion.getName()); // Vai adicionar a sugestão selecionada ao campo de texto
                        searchResultsView.setVisibility(View.GONE); // Oculta o campo de sugestão

                        // Mesmo processo do OnMapClick()
                        pointAnnotationManager.deleteAll();
                        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions().withTextAnchor(TextAnchor.CENTER).withIconImage(bitmap)
                                .withPoint(placeAutocompleteSuggestion.getCoordinate());
                        pointAnnotationManager.create(pointAnnotationOptions);


                        updateCamera(placeAutocompleteSuggestion.getCoordinate(), 0.0); // Move a câmera até o ponto e o ângulo ao norte

                        setRoute.setOnClickListener(new View.OnClickListener() {
                            // Ao clicar no ponto ele usa a posição fornecida para fazer a rota
                            @Override
                            public void onClick(View view) {
                                fetchRoute(placeAutocompleteSuggestion.getCoordinate());
                                setRoute.setVisibility(View.GONE);

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }

                                Integer id = 0;
                                Integer driverId = 0;
                                Integer passengerId = SystemAtributes.user.getId();

                                Double distance = Math.random()*200.0; //Colocar aqui a quilometragem

                                String destination = placeAutocompleteSuggestion.getName();
                                String origin = "FECAP - Fundação Escola de Comércio Álv...";

                                Double custo = distance * 2.0;
                                String cust = "R$ " + String.format("%.2f",custo);
                                String date = new Date().toString();

                                double time = (distance / 30.0) * 60.0 * 60.0;
                                String duration = getDuration(time);

                                String driverName = "NaN";
                                String passengerName = SystemAtributes.user.getName();
                                String state = "WAITING";


                                SystemAtributes.travel = new Travel(id,driverId,passengerId,destination, origin,date,cust,duration,driverName, passengerName, state);

                                Travel travelCrypt = Criptography.travelCriptography(SystemAtributes.travel);

                                Call<Travel> call = SystemAtributes.apiService.requestingTravel(travelCrypt);

                                call.enqueue(new Callback<Travel>() {
                                    @Override
                                    public void onResponse(Call<Travel> call, Response<Travel> response) {
                                        if(response.isSuccessful()){
                                            Toast.makeText(getActivity().getApplicationContext(), "Viagem solicitada com sucesso!", Toast.LENGTH_LONG).show();
                                            SystemAtributes.travel = Criptography.travelDecrypt(response.body());

                                            waitingDriver.setVisibility(View.VISIBLE);


                                            Button cancelButton = mapFragment.findViewById(R.id.cancelTravel);

                                            cancelButton.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    cancelTravel();
                                                }
                                            });

                                            Runnable runnable = new Runnable(){

                                                @Override
                                                public void run() {
                                                   waitingDriver();

                                                   handler.postDelayed(this, 20000);
                                                }
                                            };

                                            handler.post(runnable);

                                        }else{
                                            Toast.makeText(getActivity().getApplicationContext(), "Falha ao solicitar viagem!", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Travel> call, Throwable throwable) {
                                        Toast.makeText(getActivity().getApplicationContext(), "Servidor não responde!", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onPopulateQueryClick(@NonNull PlaceAutocompleteSuggestion placeAutocompleteSuggestion) {

                    }

                    @Override
                    public void onError(@NonNull Exception e) {

                    }
                });

            }
        });

        return view;
    }


    @SuppressLint("MissingPermission")
    // Vai setar as rotar de um ponto ao outro
    private void fetchRoute(Point point) {
        LocationEngine locationEngine = LocationEngineProvider.getBestLocationEngine(requireContext());
        locationEngine.getLastLocation(new LocationEngineCallback<LocationEngineResult>() {
            @Override
            public void onSuccess(LocationEngineResult result) {
                Location location = result.getLastLocation();
                setRoute.setEnabled(false);
                setRoute.setText("Buscando rotas...");
                RouteOptions.Builder builder = RouteOptions.builder();
                Point origin = Point.fromLngLat(Objects.requireNonNull(location).getLongitude(), location.getLatitude());
                builder.coordinatesList(Arrays.asList(origin, point));
                builder.alternatives(false);
                builder.profile(DirectionsCriteria.PROFILE_DRIVING);
                builder.bearingsList(Arrays.asList(Bearing.builder().angle(location.getBearing()).degrees(45.0).build(), null));
                applyDefaultNavigationOptions(builder);

                mapboxNavigation.requestRoutes(builder.build(), new NavigationRouterCallback() {
                    @Override
                    public void onRoutesReady(@NonNull List<NavigationRoute> list, @NonNull RouterOrigin routerOrigin) {
                        mapboxNavigation.setNavigationRoutes(list);
                        focusLocationBtn.performClick();
                        setRoute.setEnabled(true);
                        setRoute.setText("Definir rotas");
                    }

                    @Override
                    public void onFailure(@NonNull List<RouterFailure> list, @NonNull RouteOptions routeOptions) {
                        setRoute.setEnabled(true);
                        setRoute.setText("Definir rotas");
                        Toast.makeText(requireContext(), "Route request failed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCanceled(@NonNull RouteOptions routeOptions, @NonNull RouterOrigin routerOrigin) {

                    }
                });
            }

            @Override
            public void onFailure(@NonNull Exception exception) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (routeLineView != null) {
            routeLineView.cancel();
            routeLineView = null;
        }
        if (routeLineApi != null) {
            routeLineApi.cancel();
            routeLineApi = null;
        }

        if (mapboxNavigation != null) {
            mapboxNavigation.unregisterRoutesObserver(routesObserver);
            mapboxNavigation.unregisterLocationObserver(locationObserver);
            mapboxNavigation.onDestroy();
        }
    }

    public String getDuration(Double time){
        String duration;

        int h = 0;
        int min = 0;
        int sec = 0;
        while(time > 0.0) {
            for (int m = 0; m < 60 && time > 0.0; m++) {
                min = m;
                for (int s = 0; s < 60 && time > 0.0; s++) {
                    sec = s;
                    time -= 1;
                }
            }
            h++;

        }
        duration = (h - 1) + ":" + min + ":" + sec;
        return duration;
    }

    public void cancelTravel(){

        SystemAtributes.travel.setPassengerName("NaN");
        SystemAtributes.travel.setDriverName("NaN");

        Travel travelCrypt = Criptography.travelCriptography(SystemAtributes.travel);
        Call<Travel> call = SystemAtributes.apiService.cancelTravel(travelCrypt);

        call.enqueue(new Callback<Travel>() {
            @Override
            public void onResponse(Call<Travel> call, Response<Travel> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Viagem cancelada!", Toast.LENGTH_LONG).show();
                    handler.removeCallbacksAndMessages(null);

                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.nav_host_fragment_activity_main, new TravelFragment());
                    transaction.commit();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "Erro ao cancelar viagem!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Travel> call, Throwable throwable) {
                Toast.makeText(getActivity().getApplicationContext(), "Servidor não responde!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void waitingDriver(){
        if(checkFragmentActive) {
            Toast.makeText(getActivity().getApplicationContext(), "Nenhum motorista aceitou a corrida até agora!", Toast.LENGTH_LONG).show();
            waitingDriverWindowLoad();
        }
        System.out.println(SystemAtributes.travel);

        SystemAtributes.travel.setDriverName("NaN");
        SystemAtributes.travel.setPassengerName("NaN");

        Travel travelCrypt = Criptography.travelCriptography(SystemAtributes.travel);

        Call<Travel> call = SystemAtributes.apiService.waitingDriver(travelCrypt);

        call.enqueue(new Callback<Travel>() {
            @Override
            public void onResponse(Call<Travel> call, Response<Travel> response) {
                if(response.isSuccessful()) {
                    SystemAtributes.travel = Criptography.travelDecrypt(response.body());
                    if(!SystemAtributes.travel.getStatus().equals("WAITING")) {
                        handler.removeCallbacksAndMessages(null);

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.nav_host_fragment_activity_main, MainActivity.mapFragment);

                        transaction.commit();

                        Runnable runnable = new Runnable() {
                            @Override
                            public void run() {
                                waitingTravelFinish();

                                handler.postDelayed(this, 20000);
                            }
                        };

                        handler.post(runnable);

                        travelAccepted();

                    }
                }else{
                    if(checkFragmentActive)
                        Toast.makeText(getActivity().getApplicationContext(), "Falha ao buscar corrida no sistema!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Travel> call, Throwable throwable) {
                if(checkFragmentActive)
                    Toast.makeText(getActivity().getApplicationContext(), "Servidor não responde!", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void travelAccepted (){
        waitingDriver.setVisibility(View.INVISIBLE);
        travelAccepted.setVisibility(View.VISIBLE);
        TextView destination = mapFragment.findViewById(R.id.destinationTravelAccepted);
        TextView origin = mapFragment.findViewById(R.id.originTravelAccepted);
        TextView date = mapFragment.findViewById(R.id.dateTravelAccepted);
        TextView cust = mapFragment.findViewById(R.id.custTravelAccpeted);
        TextView duration = mapFragment.findViewById(R.id.durationTravelAccepted);
        TextView driverName = mapFragment.findViewById(R.id.driverNameTravelAccepted);
        TextView x = mapFragment.findViewById(R.id.x);

        destination.setText(SystemAtributes.travel.getDestination());
        origin.setText(SystemAtributes.travel.getOrigin());
        date.setText(SystemAtributes.travel.getDate());
        cust.setText(SystemAtributes.travel.getCust());
        duration.setText(SystemAtributes.travel.getDuration());
        driverName.setText(SystemAtributes.travel.getDriverName());

        x.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                travelAccepted.setVisibility(View.INVISIBLE);

            }
        });
    }

    public void waitingTravelFinish(){
        if(checkFragmentActive) {
            Toast.makeText(getActivity().getApplicationContext(), "Aguardando término da viagem!", Toast.LENGTH_LONG).show();
            travelAcceptedWindowLoad();
        }
        SystemAtributes.travel.setPassengerName("NaN");

        Travel travelCrypt = Criptography.travelCriptography(SystemAtributes.travel);

        Call<Travel> call = SystemAtributes.apiService.waitingDriver(travelCrypt);

        call.enqueue(new Callback<Travel>() {
            @Override
            public void onResponse(Call<Travel> call, Response<Travel> response) {
                if(response.isSuccessful()){
                    SystemAtributes.travel = Criptography.travelDecrypt(response.body());
                    if(!SystemAtributes.travel.getStatus().equals("IN PROGRESS")){
                        Toast.makeText(getActivity().getApplicationContext(), "Você chegou ao seu destino!", Toast.LENGTH_LONG).show();
                        SystemAtributes.travel = null;

                        handler.removeCallbacksAndMessages(null);
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                        transaction.replace(R.id.nav_host_fragment_activity_main, new TravelFragment());
                        transaction.commit();
                    }
                }else{
                    if(checkFragmentActive)
                        Toast.makeText(getActivity().getApplicationContext(), "Falha ao aguardar término da viagem!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Travel> call, Throwable throwable) {
                if(checkFragmentActive)
                    Toast.makeText(getActivity().getApplicationContext(), "Servidor não responde!", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void acceptingTravel(){
        ConstraintLayout acceptingTravel = mapFragment.findViewById(R.id.acceptingTravel);
        acceptingTravel.setVisibility(View.VISIBLE);


        Button travelFinish = mapFragment.findViewById(R.id.travelFinish);

        travelFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SystemAtributes.travel.setPassengerName("NaN");
                SystemAtributes.travel.setDriverName("NaN");

                Travel travelCrypt = Criptography.travelCriptography(SystemAtributes.travel);

                Call<Travel> call = SystemAtributes.apiService.travelFinish(travelCrypt);

                call.enqueue(new Callback<Travel>() {
                    @Override
                    public void onResponse(Call<Travel> call, Response<Travel> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(getActivity().getApplicationContext(), "Viagem finalizada com sucesso!", Toast.LENGTH_LONG).show();
                            SystemAtributes.travel = null;

                            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction(); //Instânciando um objeto de transação de fragments

                            transaction.replace(R.id.nav_host_fragment_activity_main,new TravelFragment()); //Definindo quando layout receberá o fragmento e sobrescrevendo o layout atual
                            transaction.commit();//Efetuando alterações da transação

                        }else{
                            Toast.makeText(getActivity().getApplicationContext(), "Não foi possível finalizar a corrida!", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Travel> call, Throwable throwable) {
                        Toast.makeText(getActivity().getApplicationContext(), "Servidor não responde!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void waitingDriverWindowLoad(){
        waitingDriver.setVisibility(View.VISIBLE);
    }

    public void travelAcceptedWindowLoad(){
        travelAccepted();
    }

    public void acceptingTravelWindowLoad(){
        travelAccepted();
    }
}