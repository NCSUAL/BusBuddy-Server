package Java2Project.controller;
import Java2Project.domain.Facilities;
import Java2Project.dto.request.FacilitiesRequest;
import Java2Project.dto.response.FacilitiesResponse;
import Java2Project.service.BusStopFacilitiesService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/facilities")
public class BusStopFacilitiesController {

    private final BusStopFacilitiesService facilitiesService;

    public BusStopFacilitiesController(BusStopFacilitiesService facilitiesService) {
        this.facilitiesService = facilitiesService;
    }

    @PostMapping("/{busStopId}")
    public ResponseEntity<FacilitiesResponse> addFacilities(@PathVariable("busStopId") String busStopId, @Valid @RequestBody FacilitiesRequest facilitiesRequest){
        return ResponseEntity.ok().body(FacilitiesResponse.of(facilitiesService.addFacilities(busStopId, facilitiesRequest)));
    }

    @GetMapping("/{busStopId}")
    public ResponseEntity<? extends FacilitiesResponse> inquiryFacilities(@PathVariable("busStopId") String busStopId){
        return ResponseEntity.ok().body(facilitiesService.inquiryFacilities(busStopId) == null ? null : FacilitiesResponse.of(facilitiesService.inquiryFacilities(busStopId)));
    }
}
