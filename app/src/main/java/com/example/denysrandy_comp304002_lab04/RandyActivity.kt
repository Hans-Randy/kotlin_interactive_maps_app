package com.example.denysrandy_comp304002_lab04

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.denysrandy_comp304002_lab04.data.NBADivision
import com.example.denysrandy_comp304002_lab04.data.NBATeam
import com.example.denysrandy_comp304002_lab04.ui.theme.DenysRandy_COMP304002_Lab04Theme
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * RandyActivity - Displays the selected NBA team's arena on Google Maps
 * Features: Real-time location tracking, custom markers, geofencing, and map interactions
 */
class RandyActivity : ComponentActivity() {
    
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geofencingClient: GeofencingClient
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Initialize location services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        geofencingClient = LocationServices.getGeofencingClient(this)
        
        // Get team and division from intent
        val team = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("team", NBATeam::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("team")
        }
        
        val division = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("division", NBADivision::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("division")
        }
        
        setContent {
            DenysRandy_COMP304002_Lab04Theme {
                if (team != null && division != null) {
                    MapScreen(
                        team = team,
                        division = division,
                        fusedLocationClient = fusedLocationClient,
                        geofencingClient = geofencingClient,
                        activity = this
                    )
                }
            }
        }
    }
}

@SuppressLint("MissingPermission", "DefaultLocale", "UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    team: NBATeam,
    division: NBADivision,
    fusedLocationClient: FusedLocationProviderClient,
    geofencingClient: GeofencingClient,
    activity: ComponentActivity
) {
    val context = LocalContext.current
    
    // State management
    var hasLocationPermission by remember { mutableStateOf(false) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    var geofenceAdded by remember { mutableStateOf(false) }
    var mapClickLocation by remember { mutableStateOf<LatLng?>(null) }
    var isMapLoaded by remember { mutableStateOf(false) }
    
    // Camera position centered on arena
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(team.arenaLocation, 14f)
    }
    
    // Map properties
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                isMyLocationEnabled = hasLocationPermission,
                mapType = MapType.NORMAL
            )
        )
    }
    
    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                zoomControlsEnabled = true,
                myLocationButtonEnabled = true,
                compassEnabled = true,
                mapToolbarEnabled = true
            )
        )
    }
    
    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasLocationPermission = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        
        if (hasLocationPermission) {
            startLocationTracking(
                fusedLocationClient = fusedLocationClient,
                onLocationUpdate = { location ->
                    currentLocation = location
                }
            )
        }
    }
    
    // Check permissions on launch
    LaunchedEffect(Unit) {
        hasLocationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        
        if (hasLocationPermission) {
            startLocationTracking(
                fusedLocationClient = fusedLocationClient,
                onLocationUpdate = { location ->
                    currentLocation = location
                }
            )
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = team.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = team.arenaName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { activity.finish() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Center on arena button
                FloatingActionButton(
                    onClick = {
                        cameraPositionState.move(
                            CameraUpdateFactory.newLatLngZoom(team.arenaLocation, 15f)
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = "Center on arena"
                    )
                }
                
                // Location tracking button
                if (!hasLocationPermission) {
                    FloatingActionButton(
                        onClick = {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "Enable location"
                        )
                    }
                } else {
                    FloatingActionButton(
                        onClick = {
                            currentLocation?.let { location ->
                                cameraPositionState.move(
                                    CameraUpdateFactory.newLatLngZoom(location, 15f)
                                )
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.MyLocation,
                            contentDescription = "My location",
                            tint = if (currentLocation != null) 
                                MaterialTheme.colorScheme.primary 
                            else 
                                MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Google Map
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = mapUiSettings,
                onMapLoaded = {
                    isMapLoaded = true
                },
                onMapClick = { latLng ->
                    mapClickLocation = latLng
                    Toast.makeText(
                        context,
                        "Clicked: ${latLng.latitude}, ${latLng.longitude}",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                onMapLongClick = { latLng ->
                    Toast.makeText(
                        context,
                        "Long pressed at: ${latLng.latitude}, ${latLng.longitude}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            ) {
                // Arena marker (custom red marker)
                Marker(
                    state = MarkerState(position = team.arenaLocation),
                    title = team.arenaName,
                    snippet = "${team.name} - ${team.city}",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
                
                // Current location marker (if available)
                currentLocation?.let { location ->
                    Marker(
                        state = MarkerState(position = location),
                        title = "You are here",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                    )
                }
                
                // Clicked location marker
                mapClickLocation?.let { location ->
                    Marker(
                        state = MarkerState(position = location),
                        title = "Clicked Location",
                        icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)
                    )
                }
                
                // Geofence circle around arena (500m radius)
                Circle(
                    center = team.arenaLocation,
                    radius = 500.0, // 500 meters
                    fillColor = Color(0x220000FF),
                    strokeColor = Color(0xFF0000FF),
                    strokeWidth = 2f
                )
            }
            
            // Loading indicator
            if (!isMapLoaded) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(48.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Loading map...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            
            // Info card at bottom
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = team.logoResId),
                                contentDescription = "${team.name} logo",
                                modifier = Modifier
                                    .size(45.dp)
                                    .padding(4.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = team.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = division.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    HorizontalDivider()
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    InfoRow(label = "Arena", value = team.arenaName)
                    InfoRow(label = "City", value = team.city)
                    InfoRow(label = "Founded", value = team.founded.toString())
                    InfoRow(
                        label = "Location",
                        value = "${team.arenaLocation.latitude}, ${team.arenaLocation.longitude}"
                    )
                    
                    if (currentLocation != null) {
                        val distance = calculateDistance(currentLocation!!, team.arenaLocation)
                        InfoRow(
                            label = "Distance",
                            value = String.format("%.2f km", distance)
                        )
                    }
                }
            }
        }
    }
    
    // Add geofence when permission is granted
    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission && !geofenceAdded) {
            addGeofence(
                geofencingClient = geofencingClient,
                team = team,
                context = activity
            )
            geofenceAdded = true
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

/**
 * Start tracking user's location in real-time
 */
@SuppressLint("MissingPermission")
fun startLocationTracking(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationUpdate: (LatLng) -> Unit
) {
    val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        10000L // 10 seconds
    ).apply {
        setMinUpdateIntervalMillis(5000L) // 5 seconds
    }.build()
    
    val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            locationResult.lastLocation?.let { location ->
                onLocationUpdate(LatLng(location.latitude, location.longitude))
            }
        }
    }
    
    fusedLocationClient.requestLocationUpdates(
        locationRequest,
        locationCallback,
        null
    )
}

/**
 * Add geofence around the arena
 */
@SuppressLint("MissingPermission")
fun addGeofence(
    geofencingClient: GeofencingClient,
    team: NBATeam,
    context: ComponentActivity
) {
    val geofence = Geofence.Builder()
        .setRequestId(team.id)
        .setCircularRegion(
            team.arenaLocation.latitude,
            team.arenaLocation.longitude,
            500f // 500 meters radius
        )
        .setExpirationDuration(Geofence.NEVER_EXPIRE)
        .setTransitionTypes(
            Geofence.GEOFENCE_TRANSITION_ENTER or
                    Geofence.GEOFENCE_TRANSITION_EXIT
        )
        .build()
    
    val geofencingRequest = GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
        .addGeofence(geofence)
        .build()
    
    val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
    val pendingIntent = PendingIntent.getBroadcast(
        context,
        0,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
    )
    
    geofencingClient.addGeofences(geofencingRequest, pendingIntent).run {
        addOnSuccessListener {
            Toast.makeText(
                context,
                "Geofence added for ${team.arenaName}",
                Toast.LENGTH_SHORT
            ).show()
        }
        addOnFailureListener {
            Toast.makeText(
                context,
                "Failed to add geofence: ${it.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}

/**
 * Calculate distance between two points in kilometers
 */
fun calculateDistance(start: LatLng, end: LatLng): Double {
    val earthRadius = 6371.0 // kilometers
    
    val dLat = Math.toRadians(end.latitude - start.latitude)
    val dLon = Math.toRadians(end.longitude - start.longitude)
    
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(start.latitude)) *
            cos(Math.toRadians(end.latitude)) *
            sin(dLon / 2) * sin(dLon / 2)
    
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    
    return earthRadius * c
}
