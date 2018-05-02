	package ViewLogin;

	import java.util.ResourceBundle;

	import com.lynden.gmapsfx.GoogleMapView;
	import com.lynden.gmapsfx.javascript.object.DirectionsPane;
	import com.lynden.gmapsfx.javascript.object.GoogleMap;
	import com.lynden.gmapsfx.javascript.object.InfoWindow;
	import com.lynden.gmapsfx.javascript.object.InfoWindowOptions;
	import com.lynden.gmapsfx.service.directions.DirectionStatus;
	import com.lynden.gmapsfx.service.directions.DirectionsRenderer;
	import com.lynden.gmapsfx.service.directions.DirectionsRequest;
	import com.lynden.gmapsfx.service.directions.DirectionsResult;
	import com.lynden.gmapsfx.service.directions.DirectionsService;
	import com.lynden.gmapsfx.service.directions.DirectionsServiceCallback;
	import com.lynden.gmapsfx.service.directions.TravelModes;
	import com.lynden.gmapsfx.service.geocoding.GeocoderStatus;
	import com.lynden.gmapsfx.service.geocoding.GeocodingResult;
	import com.lynden.gmapsfx.service.geocoding.GeocodingService;

	import javafx.beans.property.SimpleStringProperty;
	import javafx.beans.property.StringProperty;
	import javafx.event.ActionEvent;
	import javafx.fxml.FXML;
	import javafx.fxml.Initializable;
	import javafx.scene.Node;
	import javafx.scene.Parent;
	import javafx.scene.Scene;
	import javafx.scene.control.Alert;
	import javafx.scene.control.TextField;
	import com.lynden.gmapsfx.MapComponentInitializedListener;
	import com.lynden.gmapsfx.javascript.object.LatLong;
	import com.lynden.gmapsfx.javascript.object.MapOptions;
	import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
	import com.lynden.gmapsfx.javascript.object.Marker;
	import com.lynden.gmapsfx.javascript.object.MarkerOptions;
	import java.io.IOException;
	import java.net.URL;
import java.text.DecimalFormat;

import javafx.stage.Stage;
	import javafx.fxml.FXMLLoader;


	public class BookstoreMapController implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback
	{
		
		@FXML
	    private GoogleMapView mapView;
	    
	    private GoogleMap map;
	    
	    private DirectionsRenderer renderer;
	    
	    protected GoogleMapView mapComponent;

	    
	    private GeocodingService geocodingService;
	    private StringProperty address = new SimpleStringProperty();


	    protected DirectionsService directionsService;
	    protected DirectionsPane directionsPane;

	    protected StringProperty from = new SimpleStringProperty();
	    protected StringProperty to = new SimpleStringProperty();
	    protected DirectionsRenderer directionsRenderer = null;
	    
	    private String username;

	    @FXML
	    protected TextField fromTextField;

	    @FXML
	    protected TextField toTextField;
	    
		@Override
	    public void initialize(URL url, ResourceBundle rb) {
	        mapView.addMapInializedListener(this);
	        address.bind(fromTextField.textProperty());
	        from.bindBidirectional(fromTextField.textProperty());
	        to.bindBidirectional(toTextField.textProperty());
	    }
		
		@FXML
		private void toTextFieldAction(ActionEvent event) {
			System.out.println(toTextField.getText());
			DirectionsRequest request = new DirectionsRequest(from.get(), to.get(), TravelModes.DRIVING);
			directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
			directionsService.getRoute(request, this, directionsRenderer);
			
		}

		@FXML
		private void clearDirections(ActionEvent event) {
			directionsRenderer.clearDirections();
		}
		
		@FXML
		private void closeButtonAction(ActionEvent event) throws IOException {
			FXMLLoader bookStoreLoader = new FXMLLoader(getClass().getResource("Bookstore.fxml"));
			Parent bookStore = (Parent) bookStoreLoader.load();
			Scene registrationScene = new Scene(bookStore);
			Stage window = (Stage) ((Node) (event.getSource())).getScene().getWindow();

			BookstoreController newUsername = bookStoreLoader.getController();
			newUsername.sendData(username);

			window.setScene(registrationScene);
			window.show();
		}

	    @Override
	    public void mapInitialized() {
	        geocodingService = new GeocodingService();
	        MapOptions mapOptions = new MapOptions();
	        
	        mapOptions.center(new LatLong(33.7525192,-84.3928201))
	                .mapType(MapTypeIdEnum.ROADMAP)
	                .overviewMapControl(false)
	                .panControl(false)
	                .rotateControl(false)
	                .scaleControl(false)
	                .streetViewControl(false)
	                .zoomControl(true)
	                .zoom(12);

	        map = mapView.createMap(mapOptions);
	        directionsService = new DirectionsService();
	        directionsPane = mapView.getDirec();
	        
	    }
	    @FXML
	    public void addressTextFieldAction(ActionEvent event) {
	        geocodingService.geocode(address.get(), (GeocodingResult[] results, GeocoderStatus status) -> {
	            
	            LatLong latLongTo = null;
	            LatLong latLongFrom = new LatLong(33.7525192,-84.3928201);
	            
	            if( status == GeocoderStatus.ZERO_RESULTS) {
	                Alert alert = new Alert(Alert.AlertType.ERROR, "No matching address found");
	                alert.show();
	                return;
	            } else if( results.length > 1 ) {
	                Alert alert = new Alert(Alert.AlertType.WARNING, "Multiple results found, showing the first one.");
	                alert.show();
	                latLongTo = new LatLong(results[0].getGeometry().getLocation().getLatitude(), results[0].getGeometry().getLocation().getLongitude());
	            } else {
	                latLongTo = new LatLong(results[0].getGeometry().getLocation().getLatitude(), results[0].getGeometry().getLocation().getLongitude());
	            }
//	            map.addMarker(new Marker(MarkerOptions));
	            MarkerOptions markerOptions = new MarkerOptions();
	            DecimalFormat decimalFormat = new DecimalFormat("#.0");
	            double distance = latLongFrom.distanceFrom(latLongTo);
	            String d = decimalFormat.format(distance /1609.34);
	            		
	            markerOptions.position(latLongTo);
	            Marker customer = new Marker(markerOptions);
	            map.addMarker(customer);
	            map.setCenter(latLongTo);
	            
	            InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
	            infoWindowOptions.content("<h3>You are " + d + " miles from the bookstore </h3>" );
	
	            InfoWindow customerWindow = new InfoWindow(infoWindowOptions);
	            customerWindow.open(map, customer);
	            

	        });
	    }
	    
	    public void sendData(String username) {
			this.username = username;
		}

		@Override
		public void directionsReceived(DirectionsResult results, DirectionStatus status) {
			if(status.equals(DirectionStatus.OK)){
	            mapComponent.getMap().showDirectionsPane();
	            System.out.println("OK");
	            
	            DirectionsResult e = results;
	            System.out.println("SIZE ROUTES: " + e.getRoutes().size() + "\n" + "ORIGIN: " + e.getRoutes().get(0).getLegs().get(0).getStartLocation());
	            //gs.reverseGeocode(e.getRoutes().get(0).getLegs().get(0).getStartLocation().getLatitude(), e.getRoutes().get(0).getLegs().get(0).getStartLocation().getLongitude(), this);
	            System.out.println("LEGS SIZE: " + e.getRoutes().get(0).getLegs().size());
	            System.out.println("WAYPOINTS " +e.getGeocodedWaypoints().size());
	            /*double d = 0;
	            for(DirectionsLeg g : e.getRoutes().get(0).getLegs()){
	                d += g.getDistance().getValue();
	                System.out.println("DISTANCE " + g.getDistance().getValue());
	            }*/
	            try{
	                System.out.println("Distancia total = " + e.getRoutes().get(0).getLegs().get(0).getDistance().getText());
	            } catch(Exception ex){
	                System.out.println("ERRO: " + ex.getMessage());
	            }
	            System.out.println("LEG(0)");
	            System.out.println(e.getRoutes().get(0).getLegs().get(0).getSteps().size());
	            
	            System.out.println(renderer.toString());
	        }
	    }
}
