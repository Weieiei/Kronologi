package appointmentscheduler.service.GoogleApiCalls;

import appointmentscheduler.exception.BusinessNotFoundException;
import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import sun.jvm.hotspot.debugger.Address;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class GoogleApi {


    @Value("${google.key}")
    private String googleApiKey;


    public Map<String, List<String>> getMoreBusinessInfo(String businessName, String address, int photoMaxWidth, int photoMaxHeight)throws JSONException, InterruptedException, ApiException, IOException {


        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(googleApiKey)
                .build();

        Map<String, List<String>> finalMap = new HashMap<>();

        String fullAddress = businessName.concat(",").concat(address);

        GeocodingResult[] results = GeocodingApi.geocode(context,fullAddress).await();
        GeocodingResult placeFound = null;

        for(GeocodingResult result : results){
            if(result.partialMatch){
                continue;
            }
            if(result.types.length == 1){
                if(result.types[0].toString().equals(AddressType.STREET_ADDRESS.toString())){
                    continue;
                }
            }
            else{
                placeFound = result;
                break;
            }
        }

        if(placeFound == null){
            throw new BusinessNotFoundException(String.format("Business with name %s was not found", businessName));
        }

        PlaceDetails detailsOfFound = PlacesApi.placeDetails(context, placeFound.placeId).await();

        List<String> foundImages = new ArrayList<>();
        for(Photo photo : detailsOfFound.photos){
            ImageResult googleImage = PlacesApi.photo(context,photo.photoReference).maxHeight(photoMaxHeight).maxWidth(photoMaxWidth).await();
            foundImages.add(Base64Utils.encodeToString(googleImage.imageData));
        }

        List<String> reviews = new ArrayList<>();
        for(PlaceDetails.Review review : detailsOfFound.reviews){
            reviews.add(review.text);
        }

        finalMap.put("pictures", foundImages);
        finalMap.put("review", reviews);
        finalMap.put("rating", Collections.singletonList(String.valueOf(detailsOfFound.rating)));

        return finalMap;
    }

}
