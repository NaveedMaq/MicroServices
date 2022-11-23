package tech.thenaveed.moviecatalogservice.resources;

import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import tech.thenaveed.moviecatalogservice.models.CatalogItem;
import tech.thenaveed.moviecatalogservice.models.Movie;
import tech.thenaveed.moviecatalogservice.models.Rating;
import tech.thenaveed.moviecatalogservice.models.UserRating;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

       UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId, UserRating.class);

        return ratings.getUserRating().stream()
                .map(rating ->{
                    Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);

                    return new CatalogItem(movie.getMovieName(), "Desc", rating.getRating());
                })

                .collect(Collectors.toList());




    }
}

//                    Movie movie = webClientBuilder.build()
//                            .get()
//                            .uri("http://localhost:8082/movies/" + rating.getMovieId())
//                            .retrieve()
//                            .bodyToMono(Movie.class)
//                            .block();
